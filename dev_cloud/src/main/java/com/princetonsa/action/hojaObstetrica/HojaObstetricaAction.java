/*
 * Creado en Jun 14, 2005
 */
package com.princetonsa.action.hojaObstetrica;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
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

import util.ConstantesBD;
import util.InfoDatos;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.hojaObstetrica.HojaObstetricaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.Embarazo;
import com.princetonsa.mundo.antecedentes.HijoBasico;
import com.princetonsa.mundo.hojaObstetrica.HojaObstetrica;
import com.princetonsa.pdf.HojaObstetricaPdf;
import com.princetonsa.pdf.ListaEmbarazadasPdf;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class HojaObstetricaAction extends Action
{
	/**
	 * //-Para cargar el ultimo embarazo
	 */
	HojaObstetricaForm frmAux = new HojaObstetricaForm();  
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(HojaObstetricaAction.class);
	
	/**
	 * Manejo de los estados para la impresion
	 */
	private String estadoAnterior;
	
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

			if(form instanceof HojaObstetricaForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				HojaObstetricaForm hojaObstetricaForm =(HojaObstetricaForm)form;

				HttpSession session=request.getSession();
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				String estado=hojaObstetricaForm.getEstado();

				logger.warn("Estado==>"+estado);

				if(estado == null)
				{
					hojaObstetricaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de registro hoja obstétrica (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				if(estado.equals("detalleEmbarazo"))
				{
					estadoAnterior=hojaObstetricaForm.getEstado();
				}

				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

				int institucion =  medico.getCodigoInstitucionInt();

				if(estado.equals("insercionDatosHijo"))
				{
					cerrarConexion(con);
					return null;
				}else
					if (!estado.equals("listarEmbarazos") && !estado.equals("busqueda") && !estado.equals("resultadosBusqueda") && !estado.equals("imprimirListado") && !estado.equals("ordenar") && !estado.equals("cargarPaciente"))				
						if( medico == null )
						{
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
						}
						else

							if (!estado.equals("listarEmbarazos") && !estado.equals("busqueda") && !estado.equals("resultadosBusqueda") && !estado.equals("imprimirListado") && !estado.equals("ordenar") && !estado.equals("cargarPaciente"))												
								if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") ) //Valida que el  paciente esté cargado
								{
									return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
								}

				if(!UtilidadValidacion.esProfesionalSalud(usuario))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud", "errors.usuario.noAutorizado", true);
				}				

				if ( estado.equals("empezar"))
				{
					//Valida que el paciente sea del sexo femenino
					if( paciente.getCodigoSexo()==1 )					
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no es del sexo femenino", "errors.noEsSexoFemenino", true);

					//Validar que el usuario no se autoatienda
					ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
					if(respuesta.isTrue())
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);


					logger.info("entro a accion empezar");
					return this.accionEmpezar(hojaObstetricaForm,mapping, con, paciente, institucion);
				}
				else if(estado.equals("salir"))
				{
					return this.accionSalir(hojaObstetricaForm, mapping, con, paciente.getCodigoPersona(), medico.getLoginUsuario(), medico.getCodigoInstitucionInt(), medico.getNombreRegistroMedico(),medico.getInformacionGeneralPersonalSalud(), session, true);
				}
				if (estado.equals("listarEmbarazos"))
				{					
					logger.info("entro a accion Listar Embarazos");
					return this.accionListarEmbarazos(hojaObstetricaForm,mapping, con);
				}
				if (estado.equals("busqueda"))
				{
					logger.info("entro a accion Listar Embarazos");
					return this.accionBusquedaEmbarazos(hojaObstetricaForm,mapping, con);					
				}
				if (estado.equals("resultadosBusqueda"))
				{
					return this.accionResultadoBusquedaAvanzada(hojaObstetricaForm,mapping, con);					
				}		
				if (estado.equals("imprimirListado"))
				{
					//-Para imprimir el resultado de la busqueda abanzada
					return this.accionImprimirBusqueda(hojaObstetricaForm,mapping, con,request, usuario);
				}	
				if (estado.equals("ordenar"))
				{
					//-Para Ordenar el listado de mujeres embarazadas
					return this.accionOrdenar(hojaObstetricaForm,mapping, con);
				}	
				if (estado.equals("insertado"))
				{
					//Esta funcion es para que se generen los logs de  la hoja obstétrica
					accionModificar(con, hojaObstetricaForm, usuario, paciente);
					return this.accionSalir(hojaObstetricaForm, mapping, con, paciente.getCodigoPersona(), medico.getLoginUsuario(), medico.getCodigoInstitucionInt(), medico.getNombreRegistroMedico(),medico.getInformacionGeneralPersonalSalud(), session, false);
				}					
				if (estado.equals("detalleEmbarazo"))
				{

					System.out.print("\n\n\n Estado en detalle embarazo CosuHistorico    \n\n" + hojaObstetricaForm.getConsuHistoricos());		

					if ( hojaObstetricaForm.getConsuHistoricos() == false ) //-Estaba en 
					{
						//-Colocar en modo de consulta 
						hojaObstetricaForm.setConsuHistoricos(true);

						System.out.print("\n\n\n ( PASO A HISTORICOS ) Informacion de la Original hojaObstetricaForm "+ hojaObstetricaForm.getFpp() + "\n\n\n");

						frmAux  =(HojaObstetricaForm)hojaObstetricaForm.clone();

					}
					else  //-Para Modificar Solo 
					{							
						//--Funcion para mostrar toda la infomacion de un embarazo de la paciente 
						return this.accionDetalleEmbarazo(con, hojaObstetricaForm, usuario,mapping, paciente, institucion);						
					} 

					//--Funcion para mostrar toda la imfomacion de un embarazo de la paciente 
					return this.accionDetalleEmbarazo(con, hojaObstetricaForm, usuario,mapping, paciente, institucion);
				}

				if (estado.equals("volverEmbarazoActual"))  //-Para actualizar los datos con la informacion de un embarazo acutal despues de mirar los historicos
				{
					// System.out.print("\n\n\n Volver  embarazo Actual        " + "\n\n");
					//--Funcion para mostrar toda la imfomacion de un embarazo de la paciente 
					return this.accionVolverEmbarazoActual(con, hojaObstetricaForm, mapping, paciente, institucion);
				}

				//-Para cargar una paciente desde los listados de consulta de embarazadas
				if (estado.equals("cargarPaciente"))
				{
					//-Carga el paciente
					TipoNumeroId idPaci = new TipoNumeroId();
					idPaci.setNumeroId(hojaObstetricaForm.getId());
					idPaci.setTipoId(hojaObstetricaForm.getTipoId());

					paciente.cargar(con, idPaci);

					paciente.cargarPaciente2(con, paciente.getCodigoPersona(), medico.getCodigoInstitucion(), medico.getCodigoCentroAtencion()+"");

					// Código necesario para registrar este paciente como Observer
					Observable observable = (Observable) this.servlet.getServletContext().getAttribute("observable");
					if (observable != null) 
					{
						paciente.setObservable(observable);
						// Si ya lo habíamos añadido, la siguiente línea no hace nada
						observable.addObserver(paciente);
					}

					request.getSession().setAttribute("pacienteActivo", paciente);
					response.sendRedirect(request.getContextPath()+"/ingresarHojaObstetrica/hojaObstetrica.do?estado=empezar&cabezote=");

					cerrarConexion(con);
					return null;				
				}

				//--Funcion para imprimir la hoja obstetrica
				if (estado.equals("imprimirHoja"))
				{
					return this.imprimirHoja(hojaObstetricaForm,mapping, con,request, usuario, paciente);
				}
				cerrarConexion(con);
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
	 * Funcion para imprimir la Hoja Obstetrica 
	 * @param con
	 * @param hojaObstetricaForm
	 * @param mapping
	 * @param paciente
	 * @return
	 * @throws SQLException
	 */
	 private ActionForward imprimirHoja(HojaObstetricaForm hojaObstetricaForm, ActionMapping mapping, Connection con, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) throws SQLException 
	 {
	
		String nombreArchivo;
	    Random r=new Random();
	    nombreArchivo="/Hoja Obstetrica" + r.nextInt()  +".pdf";
		
	    HojaObstetricaPdf.pdfHojaObstetrica(con, ValoresPorDefecto.getFilePath() + nombreArchivo,hojaObstetricaForm, usuario, paciente);	
		
	    UtilidadBD.cerrarConexion(con);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Hoja Obstétrica");
		logger.info("estadoAnterior "+estadoAnterior);
		hojaObstetricaForm.setEstado(estadoAnterior);
		return mapping.findForward("principal");
	}
	/**
	 * @param con
	 * @param hojaObstetricaForm
	 * @param usuario
	 * @param mapping
	 * @param paciente
	 */
/*	private void accionModificar(Connection con, HojaObstetricaForm hojaObstetricaForm, UsuarioBasico usuario, ActionMapping mapping, PersonaBasica paciente)
	{
		HojaObstetrica mundoHojaObstetrica = new HojaObstetrica();
		
		
		System.out.print("\n\n\n Valor Obser ->" +hojaObstetricaForm.getObservacionesGrales() + "<--  \n\n\n");
		System.out.print("\n\n\n Valor Obser Nueva->" +hojaObstetricaForm.getObservacionesGralesNueva() + "<--  \n\n\n");
		
		
		//-Verificar si cambio LA OBSERVACION 
		//if (!hojaObstetricaForm.getObservacionesGrales().trim().equals(hojaObstetricaForm.getObservacionesGrales().trim() + hojaObstetricaForm.getObservacionesGralesNueva().trim()))
		
		//-Verificar que solo se Halla insertado las Observaciones Generales por primera vez .... 
		if (!hojaObstetricaForm.getObservacionesGrales().trim().equals(hojaObstetricaForm.getObservacionesGrales().trim())
			&&  hojaObstetricaForm.getObservacionesGralesNueva().trim().equals(""))
		{	 
		  System.out.print("\n\n\n REALIZO EL CAMBIO EN OBSERVACIONES GENERALES \n\n\n");
		  // mundoHojaObstetrica.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(hojaObstetricaForm.getObservacionesGrales(),hojaObstetricaForm.getObservacionesGralesNueva(),usuario, true));
		  // Cambio el del Form Para no alterar el llenarMundo		  		  
		  hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(hojaObstetricaForm.getObservacionesGrales(),hojaObstetricaForm.getObservacionesGralesNueva(),usuario, true));
		  
			 try {
				  //-Ejecutar la actualizacion 
				  mundoHojaObstetrica.actualizarEmbarazo(con, hojaObstetricaForm.getEmbarazo(), paciente.getCodigoPersona(), hojaObstetricaForm.getObservacionesGrales() );
				} catch (SQLException e) 
				{
				   System.out.print("Error Actualizando Historico de Hoja Obstetrica ( ACTION )" + e.toString());
				}
		 }   
	}*/
	
	/**
	 * Funcion para cargar los datos  
	 * @param con
	 * @param hojaObstetricaForm
	 * @param usuario
	 * @param mapping
	 * @param paciente
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionVolverEmbarazoActual(Connection con, HojaObstetricaForm hojaObstetricaForm, ActionMapping mapping, PersonaBasica paciente, int institucion) throws SQLException
	{
		HojaObstetrica mundoHojaObstetrica = new HojaObstetrica();
		System.out.print("\n\n\n Consultar Historicos  false    \n\n");		
		//Limpiamos lo que venga del form
		hojaObstetricaForm.reset();
		//Bandera para saber si esta consultando embarazos anteriores 
		hojaObstetricaForm.setConsuHistoricos(false);	
        System.out.print("\n\n\n (Dentro Funcion) Informacion de la copia "+ frmAux.getFpp() + "\n\n\n");
		
        logger.info("FRM AUX FIN EMBARAZO->"+frmAux.isFinalizacionEmbarazo());
        
        if(mundoHojaObstetrica.consultarEmbarazo(con, -1, paciente.getCodigoPersona()))
		{
			llenarForm(con, paciente.getCodigoPersona(), hojaObstetricaForm, mundoHojaObstetrica, institucion);
			
			//-------Se consulta la información de la sección plan de manejo
			consultarSeccionPlanManejo(con, mundoHojaObstetrica, hojaObstetricaForm, paciente.getCodigoPersona(), institucion);
			
			hojaObstetricaForm.setEstado("insertado");
			estadoAnterior=hojaObstetricaForm.getEstado();
			cerrarConexion(con);
			return mapping.findForward("principal");
		}
		else
		{
        //----------------------si esta guardando datos-----------------------//
		try {
			//-Pasar los valores del embarazo actual para mostrarlos en la pagina
			PropertyUtils.copyProperties(hojaObstetricaForm,frmAux);
		} catch (IllegalAccessException e) {
			System.out.print("Error Copiando el FORM");
		} catch (InvocationTargetException e) {
			System.out.print("Error Copiando el FORM");
		} catch (NoSuchMethodException e) {
			System.out.print("Error Copiando el FORM");
		}
		
		System.out.print("\n\n\n (Dentor Func) Informacion de la Original copia "+ hojaObstetricaForm.getFpp() + "\n\n\n");
		
		//-Cambiarle el estado al Form para que se puedan modificar datos
		hojaObstetricaForm.setEstado("empezar");
		}
		
		hojaObstetricaForm.setMotivoFinalizacion(Utilidades.obtenerUltimoTipoParto(con, paciente.getCodigoPersona()).getCodigo());
		
		cerrarConexion(con);
		logger.info("estado hojaObstetricaForm " +hojaObstetricaForm.getEstado());
		return mapping.findForward("principal");		
		
	}

	/**
	 * @param con
	 * @param hojaObstetricaForm
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionDetalleEmbarazo(Connection con, HojaObstetricaForm hojaObstetricaForm, UsuarioBasico usuario, ActionMapping mapping, PersonaBasica paciente, int institucion ) throws SQLException
	{	
		HojaObstetrica hojaObstetricaMundo = new HojaObstetrica();
		String Aux="";
		int numAct =0;
		 
        
		//-Enviar el numero de embarazo y el codigo de la paciente (retorna la informacion de la hoja obstetrica de la ultima modificacion)
		hojaObstetricaMundo.consultarEmbarazo(con, hojaObstetricaForm.getEmbarazo(), paciente.getCodigoPersona() );
		
		
		
		//-Verificar Que Hubo Cambio 
		//if (hojaObstetricaForm.getObservacionesGrales().trim().equals(hojaObstetricaMundo.getObservacionesGrales().trim() + hojaObstetricaForm.getObservacionesGralesNueva().trim()))
		if (  (hojaObstetricaMundo.getObservacionesGrales().trim().equals("") && !hojaObstetricaForm.getObservacionesGrales().trim().equals(""))
			  || (!hojaObstetricaForm.getObservacionesGralesNueva().trim().equals("") ) )
		{ 
			//-Primera Modificacion
			if (  hojaObstetricaMundo.getObservacionesGrales().trim().equals("") && !hojaObstetricaForm.getObservacionesGrales().trim().equals("") )
			{
				hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(null,hojaObstetricaForm.getObservacionesGrales(),usuario, false));	
			}
			else
			{
				//-Siguientes modificación
				hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(hojaObstetricaForm.getObservacionesGrales(),hojaObstetricaForm.getObservacionesGralesNueva(),usuario, true)); 
			}
			
			Aux =hojaObstetricaForm.getObservacionesGrales();
			   
			try {
				//-Ejecutar la actualizacion 
				numAct = hojaObstetricaMundo.actualizarEmbarazo(con, hojaObstetricaForm.getEmbarazo(), paciente.getCodigoPersona(), hojaObstetricaForm.getObservacionesGrales() ); 
				if ( numAct == 0)
				{
					System.out.print("\n\n NO ACTUALIZO REGISTROS EN LOS HISTORICOS DE EMBARAZOS DE HOJA OBSTETRICA ....\n\n");
				}
				else
				{
					System.out.print("\n\n ACTUALIZO REGISTROS EN LOS HISTORICOS DE EMBARAZOS DE HOJA OBSTETRICA !!!! \n\n");		 	  	
				}
			}
			catch (SQLException e) 
			{
			   System.out.print("Error Actualizando Historico de Hoja Obstetrica ( ACTION )" + e.toString());
			}
		}

		llenarForm(con, paciente.getCodigoPersona(), hojaObstetricaForm, hojaObstetricaMundo, institucion);
		
		//Se consulta la información de plan de manejo 
		consultarSeccionPlanManejo(con, hojaObstetricaMundo, hojaObstetricaForm, paciente.getCodigoPersona(), institucion);
		
		//hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(null,hojaObstetricaForm.getObservacionesGrales(),usuario, false));

 	    if ( numAct != 0)
   		   hojaObstetricaForm.setObservacionesGrales(Aux);

		cerrarConexion(con);
		return mapping.findForward("principal");		
	}

	private ActionForward accionOrdenar(HojaObstetricaForm hojaObstetricaForm, ActionMapping mapping, Connection con) throws SQLException
		{
			try
			{
				hojaObstetricaForm.setListado(Listado.ordenarColumna(new ArrayList(hojaObstetricaForm.getListado()),hojaObstetricaForm.getUltimaPropiedad(),hojaObstetricaForm.getPropiedad()));							
				
				//-Actualizar la ultima propiedad
				hojaObstetricaForm.setUltimaPropiedad(hojaObstetricaForm.getPropiedad()); 
				
				
				//hojaObstetricaForm.setEstado("resultadosBusqueda");
				this.cerrarConexion(con);
				
				if (hojaObstetricaForm.getPagina().equals("consultarEmbarazos"))
				{
				  return mapping.findForward("listarEmbarazos");
				}			  
				else 
				{
				  return mapping.findForward("resultadosBusqueda");  //--Retornar a la pagina principal
				}  
			}
			catch(Exception e)
			{
				logger.warn("Error en el Ordenar  (Action)");
				//hojaObstetricaForm.setEstado("listarEmbarazos");
				this.cerrarConexion(con);
				hojaObstetricaForm.reset();

				if (hojaObstetricaForm.getPagina().equals("consultarEmbarazos"))
				{
				  return mapping.findForward("listarEmbarazos");
				}			  
				else 
				{
				  return mapping.findForward("resultadosBusqueda");  //--Retornar a la pagina principal
				}  
			}
					
		}	//-Fin de Rutina Ordenar	
	
	private void accionModificar(Connection con, HojaObstetricaForm hojaObstetricaForm, UsuarioBasico usuario, 	PersonaBasica paciente)
			{
		        boolean hayCambio = false;
	        
				String logAnt="\n\n ==== INFORMACION ORIGINAL HOJA OBSTÉTRICA ===== ";
				//-Agregar la identificacion del paciente
				logAnt = logAnt + "\n\n PACIENTE [ " + paciente.getCodigoTipoIdentificacionPersona() + " "+ paciente.getNumeroIdentificacionPersona() + "  " + paciente.getNombrePersona() + " ]";
				
				String logDes="\n ==== INFORMACION NUEVA HOJA OBSTÉTRICA ===== ";	   		
				HojaObstetrica mundoHojaObstetrica = new HojaObstetrica();		
				mundoHojaObstetrica.cargarDatosLog (con,hojaObstetricaForm.getCodigoHojaObstetricaAnt());
				
				
				//-Saber si algunos de los campos (FUR - FPP - FPP por Ultra - Edad Parto - Finalizacion Embarazo ) cambio
				
				if ( hojaObstetricaForm.getFur()!=null && mundoHojaObstetrica.getFur()!=null && !mundoHojaObstetrica.getFur().trim().equals(hojaObstetricaForm.getFur().trim()))   //--Verificar cambios en la FUR
				{
					logAnt = logAnt +  "\n* FUR ["+mundoHojaObstetrica.getFur() +"] ";		
					logDes = logDes +  "\n* FUR ["+hojaObstetricaForm.getFur() +"] ";
					hayCambio = true; 
				}							

				if (hojaObstetricaForm.getFpp()!=null && mundoHojaObstetrica.getFpp()!=null && !mundoHojaObstetrica.getFpp().trim().equals(hojaObstetricaForm.getFpp().trim()) )   //--Verificar cambios en la FPP
				{
					logAnt = logAnt +  "\n* FPP ["+mundoHojaObstetrica.getFpp() +"] ";		
					logDes = logDes +  "\n* FPP ["+hojaObstetricaForm.getFpp() +"] ";
					hayCambio = true; 
				}							
				if (hojaObstetricaForm.getFppUltrasonido()!=null && mundoHojaObstetrica.getFppUltrasonido()!=null &&  !mundoHojaObstetrica.getFppUltrasonido().trim().equals(hojaObstetricaForm.getFppUltrasonido().trim()) )   //--Verificar cambios en la FPP Ultrasonido
				{
					logAnt = logAnt +  "\n* FPP UltraSonido ["+mundoHojaObstetrica.getFppUltrasonido() +"] ";		
					logDes = logDes +  "\n* FPP UltraSonido ["+hojaObstetricaForm.getFppUltrasonido() +"] ";
					hayCambio = true; 
				}
				if ( mundoHojaObstetrica.getEdadGestacional() !=  hojaObstetricaForm.getEdadGestacional() )   //--Verificar cambios en la edad gestacional
				{
					logAnt = logAnt +  "\n* EDAD GESTACIONAL ["+mundoHojaObstetrica.getEdadGestacional() +"] ";		
					logDes = logDes +  "\n* EDAD GESTACIONAL ["+hojaObstetricaForm.getEdadGestacional() +"] ";
					hayCambio = true; 
				}
				
				if ( mundoHojaObstetrica.getEdadParto() !=  hojaObstetricaForm.getEdadParto() )   //--Verificar cambios en la edad parto
				{
					logAnt = logAnt +  "\n* EDAD PARTO ["+mundoHojaObstetrica.getEdadParto() +"] ";		
					logDes = logDes +  "\n* EDAD PARTO ["+hojaObstetricaForm.getEdadParto() +"] ";
					hayCambio = true; 
				}	
							
				if ( mundoHojaObstetrica.isFinalizacionEmbarazo() !=  hojaObstetricaForm.isFinalizacionEmbarazo() )   //--Verificar cambios en la Finalización del embarazo
				{
					if ( mundoHojaObstetrica.isFinalizacionEmbarazo() )
					{
					 logAnt = logAnt +  "\n* FINALIZACION EMBARAZO [SI] ";		
					 logDes = logDes +  "\n* FINALIZACION EMBARAZO [NO] ";
					}
					else
					{
					  logAnt = logAnt +  "\n* FINALIZACION EMBARAZO [NO] ";		
					  logDes = logDes +  "\n* FINALIZACION EMBARAZO [SI] ";					
					}	
					
					hayCambio = true; 
				}							
				if (hayCambio)
				{					
					logAnt = logAnt + "\n*" + logDes;
					LogsAxioma.enviarLog(ConstantesBD.logHojaObstetricaCodigo, logAnt , ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
				}
					
				//-Verificar que hubo cambios  ( Hizo el primer cambio ) en observaciones generales 
			/*	if ( !hojaObstetricaForm.getObservacionesGralesNueva().trim().equals("") )
				{  
					//-Modificaciones Siguientes al primero 
					hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(null,hojaObstetricaForm.getObservacionesGrales(),usuario, false));
				}	
			*/
				
				//----Si Hubo Modificación
				if (!hojaObstetricaForm.getObservacionesGralesNueva().trim().equals(""))
				{
					hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(hojaObstetricaForm.getObservacionesGrales(),hojaObstetricaForm.getObservacionesGralesNueva(),usuario, false));
				}
	}

	
	
	/**
	 * Funcion para imprimir los resultados de la busqueda avanzada
	 * @param hojaObstetricaForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionImprimirBusqueda(HojaObstetricaForm hojaObstetricaForm, ActionMapping mapping, Connection con,HttpServletRequest request,UsuarioBasico usuario) throws SQLException 
	{
		
		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Pacientes Embarazadas" + r.nextInt()  +".pdf";
   
        ListaEmbarazadasPdf.pdfListaEmbarazadas(ValoresPorDefecto.getFilePath() + nombreArchivo,hojaObstetricaForm, usuario, request);	

        UtilidadBD.cerrarConexion(con);
        request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Pacientes Embarazadas");
	    
	    return mapping.findForward("abrirPdf");	           
	}

	/**
	 * @param hojaObstetricaForm para tener los datos del pagina 
	 * @param mapping 
	 * @param con
	 * @return  
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaEmbarazos(HojaObstetricaForm hojaObstetricaForm, ActionMapping mapping, Connection con) throws SQLException
	{
		try 
		{
			//Limpiamos lo que venga del form
			hojaObstetricaForm.reset();
//			hojaObstetricaForm.setEstado("busquedaAbanzada");
			
			this.cerrarConexion(con);
			return mapping.findForward("busqueda");
		}
		catch(SQLException e)
		{
		  logger.warn("Error consultando Hoja Obstetrica (Action)");
		  hojaObstetricaForm.setEstado("busqueda");	 
  		  this.cerrarConexion(con);
		}
		
			
		 hojaObstetricaForm.reset();
		 return mapping.findForward("busqueda");			
	}

	private ActionForward accionResultadoBusquedaAvanzada(HojaObstetricaForm hojaObstetricaForm, ActionMapping mapping, Connection con) throws SQLException 
	{
		HojaObstetrica mundoHojaObtetrica = new HojaObstetrica();
		mundoHojaObtetrica.reset();
		
		enviarItemsSeleccionadosBusqueda(hojaObstetricaForm, mundoHojaObtetrica);  //-Cargar en el mundo los datos por los cuales va a consultar
		
		//-Ejecuta la consulta
		hojaObstetricaForm.setListado(mundoHojaObtetrica.resultadoBusquedaAvanzada(con));
		
		this.cerrarConexion(con);
		return mapping.findForward("resultadosBusqueda");		
	}

	//-Funcion para cargar en el mundo los items de la busqueda avanzada de mujeres embarazadas
	private void enviarItemsSeleccionadosBusqueda(HojaObstetricaForm hojaObtetricaForm, 
			HojaObstetrica mundoHojaObtetrica)
		{
			//-Enviar los elemento seleccionados
			mundoHojaObtetrica.setApellido(hojaObtetricaForm.getApellido());
			mundoHojaObtetrica.setNombre(hojaObtetricaForm.getNombre());
			mundoHojaObtetrica.setId(hojaObtetricaForm.getId());
			mundoHojaObtetrica.setEdad(hojaObtetricaForm.getEdad());
			mundoHojaObtetrica.setFpp(hojaObtetricaForm.getFpp());
			mundoHojaObtetrica.setEdadGestacional(hojaObtetricaForm.getEdadGestacional());
			mundoHojaObtetrica.setNombreMedico(hojaObtetricaForm.getNombreMedico());
		}

	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param hojaObstetricaForm HojaObstetricaForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param request
	 * @return ActionForward a la página principal "hojaObstetrica.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar	(HojaObstetricaForm hojaObstetricaForm, 
											ActionMapping mapping, 
											Connection con, PersonaBasica paciente, int institucion) throws SQLException
	{		
		
		HojaObstetrica mundoHojaObstetrica=new HojaObstetrica();
		
		//-Cambio Montoya
		hojaObstetricaForm.reset();	
		hojaObstetricaForm.setTiposUltrasonido(mundoHojaObstetrica.consultarTiposUltrasonido(con, institucion));
		
		
		//---------------
	
		if(mundoHojaObstetrica.consultarEmbarazo(con, -1, paciente.getCodigoPersona()))
		{
			llenarForm(con, paciente.getCodigoPersona(), hojaObstetricaForm, mundoHojaObstetrica, institucion);
			
			//---Se consulta la información de la sección plan de manejo
			consultarSeccionPlanManejo(con, mundoHojaObstetrica, hojaObstetricaForm, paciente.getCodigoPersona(), institucion);
			
			hojaObstetricaForm.setEstado("insertado");
			estadoAnterior=hojaObstetricaForm.getEstado();
			cerrarConexion(con);
			return mapping.findForward("principal");
		}
		else
		{		
			//---Se consulta la información de la sección plan de manejo
			consultarSeccionPlanManejo(con, mundoHojaObstetrica, hojaObstetricaForm, paciente.getCodigoPersona(), institucion);
			
			//Asignamos a la motivo finalización el tipo trabajo parto que hay en la tabla ant_gineco_embarazo
			hojaObstetricaForm.setMotivoFinalizacion(Utilidades.obtenerUltimoTipoParto(con, paciente.getCodigoPersona()).getCodigo());
			hojaObstetricaForm.setEstado("empezar");
			cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}

	/**
	 * Este método es para listar todas las Mujeres que tiene hoja obstetrica activa 
	 * (En estado de Embarazo)
	 * 
	 * 
	 * @param hojaObstetricaForm HojaObstetricaForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "hojaObstetrica.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarEmbarazos	(HojaObstetricaForm hojaObstetricaForm, 
												 ActionMapping mapping, 
												 Connection con) throws SQLException
	{			
		hojaObstetricaForm.reset();  ///-reset
		HojaObstetrica mundoHojaObstetrica = new HojaObstetrica();
		mundoHojaObstetrica.reset();
			
		hojaObstetricaForm.setListado(mundoHojaObstetrica.cargarListado(con));	
		
		this.cerrarConexion(con);
		return mapping.findForward("listarEmbarazos");		
	}
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto hojaObstetrica
	 * en el objeto mundo
	 * 
	 * @param hojaObstetricaForm HojaObstetricaForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param datosMedicoEspecialidades 
	 * @param session
	 * 
	 * @return ActionForward "hojaObstetrica.do?estado=resumen"
	 * @throws SQLException
	*/
	private ActionForward accionSalir( HojaObstetricaForm hojaObstetricaForm,
									   ActionMapping mapping,  Connection con, int codigoPaciente, 
									   String login, int institucion, String datosMedico, String datosMedicoEspecialidades, HttpSession session, boolean agregaObservacion) throws SQLException
	{
		
		int codigoHojaObstetrica=0;
		int codigoResumenGest=0;
		int codigoHistoricoExamenLab=0;
		int codigoDetalleExamenLab=0;
		int codigoDetalleOtroExamen=0;
		int codigoHistoricoUltrasonido=0;
		int j=0;
		
		UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				
		HojaObstetrica mundoHojaObstetrica=new HojaObstetrica();
	
		//----Si Hubo Modificación
		if (!hojaObstetricaForm.getObservacionesGralesNueva().trim().equals("") && agregaObservacion )
		{
			hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(hojaObstetricaForm.getObservacionesGrales(),hojaObstetricaForm.getObservacionesGralesNueva(),usuario, false));
		}

		//-Verificar que hubo cambios  ( Hizo el primer cambio ) en observaciones generales 
		/*if ( !hojaObstetricaForm.getObservacionesGrales().trim().equals("") && hojaObstetricaForm.getObservacionesGralesNueva().trim().equals("") && agregaObservacion)
		{  
			//-Primera Vez que se modifico 
			hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(null,hojaObstetricaForm.getObservacionesGrales(),usuario, false));
		}*/
	    
		//-Siguientess modificacion
        //-hojaObstetricaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(hojaObstetricaForm.getObservacionesGrales(),hojaObstetricaForm.getObservacionesGralesNueva(),usuario, true)); 			
					
		llenarMundo(hojaObstetricaForm, mundoHojaObstetrica);
		logger.info("entro a accion salir-->"+codigoPaciente);
		
		mundoHojaObstetrica.ingresarEmbarazoTotal(con, mundoHojaObstetrica.getFur(), mundoHojaObstetrica.getFpp(), mundoHojaObstetrica.getEdadGestacional()+"", usuario, codigoPaciente,false, mundoHojaObstetrica.getDuracion(), mundoHojaObstetrica.getTiempoRupturaMemebranas(), mundoHojaObstetrica.getLegrado());
						
		//Inserta la hoja obstétrica
		codigoHojaObstetrica=mundoHojaObstetrica.insertarHojaObstetrica(con, codigoPaciente, login, institucion, datosMedico);
		
		/*Se modifica en el detalle de embarazos gineco-obstétricos los campos fechaTerminación
		 trabajoParto y otroTrabajoParto*/
		//mundoHojaObstetrica.modificarAntecedenteGinecoEmbarazo(con, codigoPaciente);
		
		//Se obtiene el codigo de la hoja obstétrica anterior para generar los logs
		hojaObstetricaForm.setCodigoHojaObstetricaAnt(codigoHojaObstetrica);
		
		//Verificar que no venga null los codigo del resumen gestacional, que pasa cuando no está
		//parametrizada la sección
		if(hojaObstetricaForm.getMapa("codigos") != null)
		{
			boolean resp=verificarInsertaronDatosResumen(hojaObstetricaForm);
			logger.info("----------------------"+resp+"-------------------------------\n\n");
			
			if(resp || (hojaObstetricaForm.getEdadGestacionalResumen()!=hojaObstetricaForm.getEdadGestResumenTemp()))
			{
				//Inserta el resumenGestacional
				codigoResumenGest=mundoHojaObstetrica.insertarResumenGestacional(con, codigoHojaObstetrica, datosMedico);
				//CodigoResumenGest es igual a 1 cuando no se realizó ninguna inserción en el resumen_gestacional
				if(codigoResumenGest != 0)
				{
					logger.info("ENTRO A INSERTAR RESUMEN GESTACIONAL \n\n");
					//Insertar detalle resumen gestacional
					Vector codigos=(Vector)hojaObstetricaForm.getMapa("codigos");
					for(int i=0; i<codigos.size();i++)
					{
						//logger.info("Codigo "+codigos.elementAt(i));
						int tipoResumen=Integer.parseInt(codigos.elementAt(i)+"");
						String valorTipoResumen=(String)hojaObstetricaForm.getMapa("resumenGestacional_"+tipoResumen);
						if(valorTipoResumen!=null && !valorTipoResumen.trim().equals(""))
						{
							//logger.info("Valor  "+valorTipoResumen);
							mundoHojaObstetrica.insertarDetalleResumenGestacional(con, codigoResumenGest, tipoResumen, valorTipoResumen);
						}
					}
				}
			}
		}

		
		//Verificar que no venga null los codigo de los exámenes de laboratorio, que pasa cuando no está
		//parametrizada la sección	
		logger.info("\n\nPASO POR AQUI=> "+hojaObstetricaForm.getMapa("codigosExamen")+"\n\n");
		if (hojaObstetricaForm.getMapa("codigosExamen") !=null )
		{
			//Se verifica si insertaron datos en los exámenes de laboratorio
			boolean res=verificarInsertaronDatosExamen(hojaObstetricaForm);
			logger.info("\n\nPASO POR AQUI 2=> res:"+res+",comparacion: "+(hojaObstetricaForm.getEdadGestacionalExamen()!=hojaObstetricaForm.getEdadGestExamenTemp())+" \n\n");
			if (res || (hojaObstetricaForm.getEdadGestacionalExamen()!=hojaObstetricaForm.getEdadGestExamenTemp()))
			{
				codigoHistoricoExamenLab=mundoHojaObstetrica.insertarHistoricoExamenLab(con, codigoHojaObstetrica ,datosMedico);
						
				//Inserta el detalle de los exames de laboratorio
				Vector codigosExamen=(Vector)hojaObstetricaForm.getMapa("codigosExamen");
				for(int i=0; i<codigosExamen.size();i++)
				{
					//logger.info("Codigo "+codigosExamen.elementAt(i));
					int tipoExamen=Integer.parseInt(codigosExamen.elementAt(i)+"");
					logger.info("TIPO EXAMEN=> "+tipoExamen);
					String resultadoExamenLab=(String)hojaObstetricaForm.getMapa("resultadoExamenLab_"+tipoExamen);
					String observacionExamenLab=(String)hojaObstetricaForm.getMapa("observacionExamenLab_"+tipoExamen);
												
					if((resultadoExamenLab!=null && !resultadoExamenLab.trim().equals("")) || (observacionExamenLab!=null && !observacionExamenLab.trim().equals("")))
						{
							codigoDetalleExamenLab=mundoHojaObstetrica.insertarDetalleExamenLab(con, codigoHistoricoExamenLab, tipoExamen, resultadoExamenLab, observacionExamenLab);
							//Insertar los documentos adjuntos
							logger.info("num docs->"+(String)hojaObstetricaForm.getMapa("numDocAdj_"+tipoExamen));
							int numDocsAdj=Integer.parseInt((String)hojaObstetricaForm.getMapa("numDocAdj_"+tipoExamen));
							for(j=0; j<numDocsAdj; j++)
							{
								String checkAdj=(String)hojaObstetricaForm.getMapa("checkbox_"+tipoExamen+"_"+j);													
								String docRealAdj=(String)hojaObstetricaForm.getMapa("nombreRealAdj_"+tipoExamen+"_"+j);
								String docGeneradolAdj=(String)hojaObstetricaForm.getMapa("nombreGenAdj_"+tipoExamen+"_"+j);
								logger.info(checkAdj+"**"+docRealAdj+"**"+docGeneradolAdj);
								if(checkAdj!=null)
									mundoHojaObstetrica.insertarAdjuntoExamenLab(con, codigoDetalleExamenLab, docRealAdj, docGeneradolAdj);
							}
						}//if
				}//for
				
				//Insercion de datos de otros exámenes de laboratorio
				String codigosOtrosExam=hojaObstetricaForm.getMapa("codigosOtros")+"";
				logger.info("codigos otros->"+codigosOtrosExam+"\n\n");
				if (codigosOtrosExam!=null   && !codigosOtrosExam.equals(""))
				{
					String[] codOtrosExamenes=codigosOtrosExam.split("-");
					logger.info("Cantidad de otros examenes->"+codOtrosExamenes.length+"\n\n");
					int codigoUltimo=Integer.parseInt(hojaObstetricaForm.getMapa("codigoUltimoExamen")+"");
					for (int z=1; z<=codOtrosExamenes.length; z++)
					{
						String descripcionOtro=(String)hojaObstetricaForm.getMapa("descripcionOtro_"+z);
						String resultadoOtroExamenLab=(String)hojaObstetricaForm.getMapa("resultadoOtroExamen_"+z);
						String observacionOtroExamenLab=(String)hojaObstetricaForm.getMapa("observOtroExamen_"+z);
						codigoUltimo+=1;
						if ( (resultadoOtroExamenLab!=null && !resultadoOtroExamenLab.trim().equals("")) || (observacionOtroExamenLab!=null && !observacionOtroExamenLab.trim().equals("")))
						{
							codigoDetalleOtroExamen=mundoHojaObstetrica.insertarDetalleOtroExamenLab(con, codigoHistoricoExamenLab, descripcionOtro, resultadoOtroExamenLab, observacionOtroExamenLab);
							
							//Insertar los documentos adjuntos de otros exámes de laboratorio
							logger.info("codigo Ultimo->"+codigoUltimo+"\n");
							if ((String)hojaObstetricaForm.getMapa("numDocAdjOtro_"+codigoUltimo) != null)
								{
								int numDocsAdjOtro=Integer.parseInt((String)hojaObstetricaForm.getMapa("numDocAdjOtro_"+codigoUltimo));						
								logger.info("numero adjuntos->"+numDocsAdjOtro+"\n");
								for (int y=0; y<numDocsAdjOtro; y++)
								{
									String checkAdjOtro=(String)hojaObstetricaForm.getMapa("checkboxOtro_"+codigoUltimo+"_"+y);													
									String docRealAdjOtro=(String)hojaObstetricaForm.getMapa("nombreRealAdjOtro_"+codigoUltimo+"_"+y);
									String docGeneradolAdjOtro=(String)hojaObstetricaForm.getMapa("nombreGenAdjOtro_"+codigoUltimo+"_"+y);
									logger.info(checkAdjOtro+"**"+docRealAdjOtro+"**"+docGeneradolAdjOtro);
									if (checkAdjOtro != null)
									{
										mundoHojaObstetrica.insertarAdjuntoOtroExamenLab(con, codigoDetalleOtroExamen, docRealAdjOtro, docGeneradolAdjOtro);
									}
								}//for
							}
						}//if						
					}//for
				}//if
			}//if
		}//if codigosExamen != null	
		
		//Verificar que no venga null los codigo de ultrasonidos, que pasa cuando no está
		//parametrizada la sección	
		if (hojaObstetricaForm.getMapa("codigosUltra") != null)
		{
			boolean resp2=verificarInsertaronDatosUltrasonido(hojaObstetricaForm);
			logger.info("----------------------"+resp2+"--ULTRASONIDOS-----------------------------\n\n");
			if(resp2)
			{
				//Inserta el histórico de ultrasonidos
				codigoHistoricoUltrasonido=mundoHojaObstetrica.insertarHistoricoUltrasonido(con, codigoHojaObstetrica, datosMedico);
				//CodigoHistoricoUltrasonido es igual a 1 cuando no se realizó ninguna inserción en el resumen_gestacional
				if(codigoHistoricoUltrasonido != 0)
				{
					//Insertar detalle ultrasonido
					Vector codigos=(Vector)hojaObstetricaForm.getMapa("codigosUltra");
					for(int i=0; i<codigos.size();i++)
					{
						int tipoUltrasonido=Integer.parseInt(codigos.elementAt(i)+"");
						String valorTipoUltrasonido=(String)hojaObstetricaForm.getMapa("valorUltrasonido_"+tipoUltrasonido);
						if(valorTipoUltrasonido!=null && !valorTipoUltrasonido.trim().equals(""))
						{
							mundoHojaObstetrica.insertarDetalleUltrasonido(con, codigoHistoricoUltrasonido, tipoUltrasonido, valorTipoUltrasonido);
						}
					}
					//Insertar los documentos adjuntos a ultrasonidos
					logger.info("num docs->"+(String)hojaObstetricaForm.getMapa("numDocAdjUltrasonidos"));
					int numDocsAdjUltrasonido=Utilidades.convertirAEntero(hojaObstetricaForm.getMapa("numDocAdjUltrasonidos")+"");
					for(j=0; j<numDocsAdjUltrasonido; j++)
					{
						String checkAdjUltra=(String)hojaObstetricaForm.getMapa("checkboxUltrasonidos_"+j);													
						String docRealAdjUltra=(String)hojaObstetricaForm.getMapa("nombreRealAdjUltrasonidos_"+j);
						String docGeneradolAdjUltra=(String)hojaObstetricaForm.getMapa("nombreGenAdjUltrasonidos_"+j);
						logger.info(checkAdjUltra+"**"+docRealAdjUltra+"**"+docGeneradolAdjUltra);
						
						if(checkAdjUltra!=null)
							mundoHojaObstetrica.insertarAdjuntoUltrasonido(con, codigoHistoricoUltrasonido, docRealAdjUltra, docGeneradolAdjUltra);
					}
				}
			}
		}
		
		//---------------------Insercion de los datos de Plan de Manejo ------------------------------//
		mundoHojaObstetrica.insertarPlanManejo(con, codigoHojaObstetrica, datosMedicoEspecialidades, hojaObstetricaForm.getMapaTiposPlanManejo());
		
					
		if(hojaObstetricaForm.isFinalizacionEmbarazo())
		{
			String numHijosStr=(String)hojaObstetricaForm.getMapa("numHijosDetEmb");
			logger.info("--> "+numHijosStr);
			int numHijos=0;
			if(numHijosStr!=null && !numHijosStr.equals(""))
			{
				numHijos=Integer.parseInt(numHijosStr);
			}
			for(int i=0; i<numHijos; i++)
			{
				HijoBasico hijo=new HijoBasico();
				hijo.setNumeroHijoEmbarazo(i+1);
				hijo.setVivo(UtilidadTexto.getBoolean(hojaObstetricaForm.getMapa("vivo_"+i)+""));
				hijo.setOtraFormaNacimientoVaginal((String)hojaObstetricaForm.getMapa("otroTipoParto_"+i));
				ArrayList partosVaginales=new ArrayList();
				String tipoTrabajoPartoStr=(String)hojaObstetricaForm.getMapa("tipoTrabajoParto_"+i);
				int tipoTrabajoParto=-1;
				if(tipoTrabajoPartoStr!=null && !tipoTrabajoPartoStr.equals(""))
				{
					tipoTrabajoParto=Integer.parseInt(tipoTrabajoPartoStr);
				}
				hijo.setOtroTipoParto(tipoTrabajoPartoStr);
				hijo.setCesarea(tipoTrabajoParto==ConstantesBD.codigoTipoPartoCesarea);
				hijo.setAborto(tipoTrabajoParto==ConstantesBD.codigoTipoPartoMortinato);
				String sexoTempo=(String)hojaObstetricaForm.getMapa("sexo_"+i);
				if(sexoTempo!=null && !sexoTempo.equals(""))
				{
					hijo.setSexo(Integer.parseInt(sexoTempo));
				}
				else
				{
					hijo.setSexo(0);
				}
				hijo.setPeso((String)hojaObstetricaForm.getMapa("peso_"+i));
				hijo.setLugar((String)hojaObstetricaForm.getMapa("lugar_"+i));
				String campoOtroParto=(String)hojaObstetricaForm.getMapa("campoOtroParto");
				hijo.setOtroTipoParto(campoOtroParto);
				logger.info("tipo trabajo parto->"+tipoTrabajoParto+"   codigo tipo parto->"+ConstantesBD.codigoTipoPartoVaginal+"\n\n");
				boolean entro=false;
				if(tipoTrabajoParto==ConstantesBD.codigoTipoPartoVaginal)
				{
					int[] tiposPartoVaginal=Utilidades.consultarTiposPartoVaginal(con, "V");
					for(int y=0; y<tiposPartoVaginal.length;y++)
					{										
						if(hojaObstetricaForm.getMapa("tipoPartoVaginal_"+i+"_"+tiposPartoVaginal[y])!=null)
						{
							InfoDatos datos=new InfoDatos();
							datos.setCodigo(tiposPartoVaginal[y]);
							partosVaginales.add(datos);
							entro=true;
						}
						if(y==tiposPartoVaginal.length-1 && hojaObstetricaForm.getMapa("tipoPartoVaginal_"+i+"_"+tiposPartoVaginal[y])==null && !entro)
						{
							InfoDatos datos=new InfoDatos();
							datos.setCodigo(-2);
							partosVaginales.add(datos);
						}
					}
				}
				hijo.setFormasNacimientoVaginal(partosVaginales);
				hijo.insertar(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo());
			}
			Vector lista=new Vector();
			for(int i=1; i<5; i++)
			{
				String compli=(String)hojaObstetricaForm.getMapa("complicacion_"+i);
				if(compli!=null)
				{
					lista.add(new Integer(i));
				}
			}
			int[] complicaciones=new int[lista.size()];
			for(int i=0; i<lista.size();i++)
			{
				complicaciones[i]=((Integer)lista.elementAt(i)).intValue();
			}
			String tempoNumComplicacionesOtras=(String)hojaObstetricaForm.getMapa("numeroComplicacionesOtras");
			int numComplicacionesOtras=0;
			if(tempoNumComplicacionesOtras!=null && !tempoNumComplicacionesOtras.equals(""))
			{
				numComplicacionesOtras=Integer.parseInt(tempoNumComplicacionesOtras);
			}
			lista=new Vector();
			for(int i=0;i<numComplicacionesOtras;i++)
			{
				lista.add((String)hojaObstetricaForm.getMapa("otraComplicacion_"+i));
			}
			Embarazo.ingresarComplicaciones(con, hojaObstetricaForm.getNumeroEmbarazo(), codigoPaciente, complicaciones, lista);
			hojaObstetricaForm.reset();
			hojaObstetricaForm.setEstado("empezar");
		}
		else
		{
			hojaObstetricaForm.setMapaCompleto(new HashMap());
			hojaObstetricaForm.setMapaPlanManejo(new HashMap());
			hojaObstetricaForm.setMapaPlanManejo("numRegistros", "0");
			hojaObstetricaForm.setExamenesLaboratorioXEmbarazo(mundoHojaObstetrica.consultarHistoricoExamenesLab(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo()));
			/*hojaObstetricaForm.setHistoricoExamenesLaboratorio(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 1));
			hojaObstetricaForm.setHistoricoExamenesParam(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 2));
			hojaObstetricaForm.setHistoricoNuevosExamenes(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 3));*/
			hojaObstetricaForm.setEstado("insertado");
			estadoAnterior=hojaObstetricaForm.getEstado();
		}
		
		hojaObstetricaForm.setTiposUltrasonido(mundoHojaObstetrica.consultarTiposUltrasonido(con, institucion));
		hojaObstetricaForm.setHistoricoResumenGestacional(mundoHojaObstetrica.consultarResumenGestacional(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false));
		hojaObstetricaForm.setHistoricoExamenesLaboratorio(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 1));
		hojaObstetricaForm.setHistoricoExamenesParam(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 2));
		hojaObstetricaForm.setHistoricoNuevosExamenes(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 3));
		hojaObstetricaForm.setHistoricoUltrasonido(mundoHojaObstetrica.consultarHistoricoUltrasonidos(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false));
		hojaObstetricaForm.setFurTemp(hojaObstetricaForm.getFur());
		//se blanquean las observaciones
		hojaObstetricaForm.setObservacionesGralesNueva("");
		
		//Se consulta la información de plan de manejo 
		consultarSeccionPlanManejo(con, mundoHojaObstetrica, hojaObstetricaForm, codigoPaciente, institucion);
		
		//Se resetea el mapa que traía los datos
		hojaObstetricaForm.setMapaPlanManejo(new HashMap());
		hojaObstetricaForm.setMapaPlanManejo("numRegistros","0");
		
		this.cerrarConexion(con);									
				
		return mapping.findForward("principal");	
	}
	
	
	/**
	 * Método que llena el mundo, con los datos del form
	 * @param hojaObstetricaForm
	 * @param mundoHojaObstetrica
	 */
	private void llenarMundo(HojaObstetricaForm hojaObstetricaForm, HojaObstetrica mundoHojaObstetrica)
	{
		mundoHojaObstetrica.setNumeroEmbarazo(hojaObstetricaForm.getNumeroEmbarazo());
		mundoHojaObstetrica.setFechaOrden(hojaObstetricaForm.getFechaOrden());
		mundoHojaObstetrica.setFur(hojaObstetricaForm.getFur());
		mundoHojaObstetrica.setFurTemp(hojaObstetricaForm.getFurTemp());
		mundoHojaObstetrica.setFpp(hojaObstetricaForm.getFpp());
		mundoHojaObstetrica.setFechaUltrasonido(hojaObstetricaForm.getFechaUltrasonido());
		mundoHojaObstetrica.setFppUltrasonido(hojaObstetricaForm.getFppUltrasonido());
		mundoHojaObstetrica.setEdadGestacional(hojaObstetricaForm.getEdadGestacional());
		mundoHojaObstetrica.setEdadParto(hojaObstetricaForm.getEdadParto());
		mundoHojaObstetrica.setEdadGestacional(hojaObstetricaForm.getEdadGestacional());
		mundoHojaObstetrica.setFinalizacionEmbarazo(hojaObstetricaForm.isFinalizacionEmbarazo());
		mundoHojaObstetrica.setFechaTerminacion(hojaObstetricaForm.getFechaTerminacion());
		mundoHojaObstetrica.setFinalizacionEmbarazo(hojaObstetricaForm.isFinalizacionEmbarazo());
		
		mundoHojaObstetrica.setMotivoFinalizacion(hojaObstetricaForm.getMotivoFinalizacion());
		mundoHojaObstetrica.setNombreMotivoFinalizacion(hojaObstetricaForm.getNombreMotivoFinalizacion());
		
		
		mundoHojaObstetrica.setOtroMotivoFinalizacion(hojaObstetricaForm.getOtroMotivoFinalizacion());
		mundoHojaObstetrica.setEgFinalizar(hojaObstetricaForm.getEgFinalizar());
		mundoHojaObstetrica.setMotivoFinalizacion(hojaObstetricaForm.getMotivoFinalizacion());
		mundoHojaObstetrica.setObservacionesGrales(hojaObstetricaForm.getObservacionesGrales());
		mundoHojaObstetrica.setEdadGestacionalResumen(hojaObstetricaForm.getEdadGestacionalResumen());
		mundoHojaObstetrica.setFechaGestacional(hojaObstetricaForm.getFechaGestacional());
		mundoHojaObstetrica.setHoraGestacional(hojaObstetricaForm.getHoraGestacional());
		mundoHojaObstetrica.setEdadGestacionalExamen(hojaObstetricaForm.getEdadGestacionalExamen());
        mundoHojaObstetrica.setObservacionesGrales(hojaObstetricaForm.getObservacionesGrales());
        
        mundoHojaObstetrica.setDuracion((String)hojaObstetricaForm.getMapa("duracion"));
        mundoHojaObstetrica.setTiempoRupturaMemebranas((String)hojaObstetricaForm.getMapa("ruptura"));
        mundoHojaObstetrica.setLegrado((String)hojaObstetricaForm.getMapa("legrado"));
        mundoHojaObstetrica.setFechaExamen(hojaObstetricaForm.getFechaExamen());
        mundoHojaObstetrica.setHoraExamen(hojaObstetricaForm.getHoraExamen());
        mundoHojaObstetrica.setUltrasonidoFecha(hojaObstetricaForm.getUltrasonidoFecha());
        mundoHojaObstetrica.setUltrasonidoHora(hojaObstetricaForm.getUltrasonidoHora());
        
        //-------------Plan de Manejo -------------------
        mundoHojaObstetrica.setConfiable(hojaObstetricaForm.getConfiable());
        mundoHojaObstetrica.setVigenteAntitetanica(hojaObstetricaForm.getVigenteAntitetanica());
        mundoHojaObstetrica.setDosisAntitetanica(hojaObstetricaForm.getDosisAntitetanica());
        mundoHojaObstetrica.setMesesGestacionAntitetanica(hojaObstetricaForm.getMesesGestacionAntitetanica());
        mundoHojaObstetrica.setAntirubeola(hojaObstetricaForm.getAntirubeola());
        mundoHojaObstetrica.setPeso(hojaObstetricaForm.getPeso());
        mundoHojaObstetrica.setTalla(hojaObstetricaForm.getTalla());
        mundoHojaObstetrica.setEmbarazoDeseado(hojaObstetricaForm.getEmbarazoDeseado());
        mundoHojaObstetrica.setEmbarazoPlaneado(hojaObstetricaForm.getEmbarazoPlaneado());
        
        //------Se pasa al mundo el mapa que contiene los datos de plan de manejo--------//
        mundoHojaObstetrica.setMapaPlanManejo(hojaObstetricaForm.getMapaPlanManejo());
	}
	
	
	/**
	 * Método que llena el Form, con los datos del mundo
	 * @param hojaObstetricaForm
	 * @param mundoHojaObstetrica
	 */
	private void llenarForm(Connection con, int codigoPaciente, HojaObstetricaForm hojaObstetricaForm, HojaObstetrica mundoHojaObstetrica, int institucion)
	{
		hojaObstetricaForm.setNumeroEmbarazo(mundoHojaObstetrica.getNumeroEmbarazo());
		hojaObstetricaForm.setFechaOrden(mundoHojaObstetrica.getFechaOrden());
		hojaObstetricaForm.setFur(mundoHojaObstetrica.getFur());
		hojaObstetricaForm.setFurTemp(mundoHojaObstetrica.getFur());
		hojaObstetricaForm.setFpp(mundoHojaObstetrica.getFpp());
		hojaObstetricaForm.setFechaUltrasonido(mundoHojaObstetrica.getFechaUltrasonido());
		hojaObstetricaForm.setFppUltrasonido(mundoHojaObstetrica.getFppUltrasonido());
		hojaObstetricaForm.setEdadGestacional(mundoHojaObstetrica.getEdadGestacional());
		hojaObstetricaForm.setEdadParto(mundoHojaObstetrica.getEdadParto());
		hojaObstetricaForm.setFinalizacionEmbarazo(mundoHojaObstetrica.isFinalizacionEmbarazo());
		hojaObstetricaForm.setFechaTerminacion(mundoHojaObstetrica.getFechaTerminacion());
		hojaObstetricaForm.setFinalizacionEmbarazo(mundoHojaObstetrica.isFinalizacionEmbarazo());
		hojaObstetricaForm.setMotivoFinalizacion(mundoHojaObstetrica.getMotivoFinalizacion());
		hojaObstetricaForm.setNombreMotivoFinalizacion(mundoHojaObstetrica.getNombreMotivoFinalizacion());
		
		hojaObstetricaForm.setOtroMotivoFinalizacion(mundoHojaObstetrica.getOtroMotivoFinalizacion());
		hojaObstetricaForm.setEgFinalizar(mundoHojaObstetrica.getEgFinalizar());
		hojaObstetricaForm.setMotivoFinalizacion(mundoHojaObstetrica.getMotivoFinalizacion());
		hojaObstetricaForm.setObservacionesGrales(mundoHojaObstetrica.getObservacionesGrales());
		hojaObstetricaForm.setEdadGestacionalResumen(mundoHojaObstetrica.getEdadGestacionalResumen());
		hojaObstetricaForm.setFechaGestacional(mundoHojaObstetrica.getFechaGestacional());
		hojaObstetricaForm.setHoraGestacional(mundoHojaObstetrica.getHoraGestacional());
		hojaObstetricaForm.setEdadGestacionalExamen(mundoHojaObstetrica.getEdadGestacionalExamen());
		hojaObstetricaForm.setUltrasonidoFecha(mundoHojaObstetrica.getUltrasonidoFecha());
		hojaObstetricaForm.setUltrasonidoHora(mundoHojaObstetrica.getUltrasonidoHora());
		hojaObstetricaForm.setExamenesLaboratorioXEmbarazo(mundoHojaObstetrica.consultarHistoricoExamenesLab(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo()));
		hojaObstetricaForm.setHistoricoExamenesLaboratorio(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 1));
		hojaObstetricaForm.setHistoricoExamenesParam(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 2));
		hojaObstetricaForm.setHistoricoNuevosExamenes(mundoHojaObstetrica.consultarExamenesLaboratorio(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false, 3));
		hojaObstetricaForm.setFechaExamen(mundoHojaObstetrica.getFechaExamen());
		hojaObstetricaForm.setHoraExamen(mundoHojaObstetrica.getHoraExamen());
		hojaObstetricaForm.setHistoricoResumenGestacional(mundoHojaObstetrica.consultarResumenGestacional(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false));
		
		//-Cargar Informnacion Ultrasonidos  	
		hojaObstetricaForm.setTiposUltrasonido(mundoHojaObstetrica.consultarTiposUltrasonido(con,institucion));
		//-Cargar Informnacion Historico Ultrasonidos  	
		hojaObstetricaForm.setHistoricoUltrasonido(mundoHojaObstetrica.consultarHistoricoUltrasonidos(con, codigoPaciente, hojaObstetricaForm.getNumeroEmbarazo(), false));
		
		//--------Plan de Manejo -----------------//
		hojaObstetricaForm.setConfiable(mundoHojaObstetrica.getConfiable());
		hojaObstetricaForm.setVigenteAntitetanica(mundoHojaObstetrica.getVigenteAntitetanica());
		hojaObstetricaForm.setDosisAntitetanica(mundoHojaObstetrica.getDosisAntitetanica());
		hojaObstetricaForm.setMesesGestacionAntitetanica(mundoHojaObstetrica.getMesesGestacionAntitetanica());
		hojaObstetricaForm.setAntirubeola(mundoHojaObstetrica.getAntirubeola());
		
		hojaObstetricaForm.setPeso(mundoHojaObstetrica.getPeso());
		hojaObstetricaForm.setTalla(mundoHojaObstetrica.getTalla());
		hojaObstetricaForm.setEmbarazoDeseado(mundoHojaObstetrica.getEmbarazoDeseado());
		hojaObstetricaForm.setEmbarazoPlaneado(mundoHojaObstetrica.getEmbarazoPlaneado());
	}
	
	/**
	 * Método para verificar si insertaron algún dato en el resumen gestacional
	 * @param hojaObstetricaForm
	 * @return true si hay datos sino retorna false
	 */
	private boolean verificarInsertaronDatosResumen(HojaObstetricaForm hojaObstetricaForm)
	{
		boolean resp=false;
		String tempUterina="";
		Vector codigos=(Vector)hojaObstetricaForm.getMapa("codigos");
		tempUterina=hojaObstetricaForm.getAlturaUterinaTemp()+"";
		if(hojaObstetricaForm.getAlturaUterinaTemp()==0)
			{
				tempUterina="";
			}
			
		for(int i=0; i<codigos.size();i++)
		{
			int tipoResumen=Integer.parseInt(codigos.elementAt(i)+"");
			String valorTipoResumen=(String)hojaObstetricaForm.getMapa("resumenGestacional_"+tipoResumen);
			//logger.info("tipoResumen->"+tipoResumen+"   VALOR TIPO RESUMEN->"+valorTipoResumen + "   ALTURA UTERINA TEMP->"+tempUterina+"\n\n");
			if(tipoResumen==ConstantesBD.codigoAlturaUterina && valorTipoResumen!=null && !valorTipoResumen.equals(tempUterina))
			{
				resp=true;
				break;
			}
			
			if(UtilidadCadena.noEsVacio(valorTipoResumen) && tipoResumen!=ConstantesBD.codigoAlturaUterina)
			{
				resp=true;
				break;
			}
		}
		return resp;
	}

	
	/**
	 * Método para verificar si insertaron algún dato en los exámenes de laboratorio
	 * @param hojaObstetricaForm
	 * @return true si hay datos sino retorna false
	 */
	private boolean verificarInsertaronDatosExamen(HojaObstetricaForm hojaObstetricaForm)
	{
		boolean resp=false;
		Vector codigosExamen=(Vector)hojaObstetricaForm.getMapa("codigosExamen");
		for(int i=0; i<codigosExamen.size();i++)
		{
			int tipoExamen=Integer.parseInt(codigosExamen.elementAt(i)+"");
			String resultadoExamenLab=(String)hojaObstetricaForm.getMapa("resultadoExamenLab_"+tipoExamen);
			String observacionExamenLab=(String)hojaObstetricaForm.getMapa("observacionExamenLab_"+tipoExamen);
									
			if(UtilidadCadena.noEsVacio(resultadoExamenLab) || UtilidadCadena.noEsVacio(observacionExamenLab))
				{
				resp=true;
				return resp;
				}								
		}//for
		
		//Se verifica si insertaron datos en otros exámenes de laboratorio
		String codigosOtrosExam=hojaObstetricaForm.getMapa("codigosOtros")+"";
		if (codigosOtrosExam!=null   && !codigosOtrosExam.equals(""))
		{
			String[] codOtrosExamenes=codigosOtrosExam.split("-");
			for (int z=1; z<=codOtrosExamenes.length; z++)
			{
				String resultadoOtroExamenLab=(String)hojaObstetricaForm.getMapa("resultadoOtroExamen_"+z);
				String observacionOtroExamenLab=(String)hojaObstetricaForm.getMapa("observOtroExamen_"+z);
				if (UtilidadCadena.noEsVacio(resultadoOtroExamenLab) || UtilidadCadena.noEsVacio(observacionOtroExamenLab))
				{
					resp=true;
					return resp;
				}
			}//for
		}//if
		
		
		return resp;
	}	
	
	/**
	 * Método para verificar si insertaron algún dato en ultrasonidos
	 * @param hojaObstetricaForm
	 * @return true si hay datos sino retorna false
	 */
	private boolean verificarInsertaronDatosUltrasonido(HojaObstetricaForm hojaObstetricaForm)
	{
		boolean resp=false;
		String tempEdadGestUltra="";
		String tempEdadGestEcoUltra="";
		tempEdadGestUltra=hojaObstetricaForm.getEdadGestUltrasonidoTemp()+"";
		
		if(hojaObstetricaForm.getEdadGestUltrasonidoTemp()==0)
		{
			tempEdadGestUltra="";
		}
		
		tempEdadGestEcoUltra=hojaObstetricaForm.getEdadGestEcoUltraTemp()+"";
		
		if(hojaObstetricaForm.getEdadGestEcoUltraTemp()==0)
		{
			tempEdadGestEcoUltra="";
		}
	
		Vector codigosUltrasonido=(Vector)hojaObstetricaForm.getMapa("codigosUltra");
		for(int i=0; i<codigosUltrasonido.size();i++)
		{
			int tipoUltrasonido=Integer.parseInt(codigosUltrasonido.elementAt(i)+"");
			String valorUltrasonido=(String)hojaObstetricaForm.getMapa("valorUltrasonido_"+tipoUltrasonido);
			if(valorUltrasonido==null)
			{
				valorUltrasonido="";
			}
			if(valorUltrasonido.equals("0"))
			{
				valorUltrasonido="";			
			}
			
			//logger.info("TIPO ULTRASONIDO->"+tipoUltrasonido+"   VALOR ULTRASONIDO->"+valorUltrasonido+ "   EDAD GEST ULTRA TEMP->"+tempEdadGestUltra+"EDAD ECO ULTRA TEMP->"+tempEdadGestEcoUltra+"\n\n");

			if(tipoUltrasonido==ConstantesBD.codigoPostularEdadGestacionalUltrasonido && valorUltrasonido!=null && !valorUltrasonido.equals(tempEdadGestUltra))
			{
				//logger.info("PRIMER IF");
				resp=true;
				break;
			}
			if(tipoUltrasonido==ConstantesBD.codigoPostularEdadGestEcoUltrasonido && valorUltrasonido!=null && !valorUltrasonido.equals(tempEdadGestEcoUltra))
			{
				//logger.info("SEGUNDO IF");
				resp=true;
				break;
			}
			if(UtilidadCadena.noEsVacio(valorUltrasonido) && tipoUltrasonido!=ConstantesBD.codigoPostularEdadGestacionalUltrasonido && tipoUltrasonido!=ConstantesBD.codigoPostularEdadGestEcoUltrasonido)
			{
				//logger.info("TERCER IF");
				resp=true;
				break;
			}
			if( hojaObstetricaForm.getMapa("numDocAdjUltrasonidos")!=null && Utilidades.convertirAEntero(hojaObstetricaForm.getMapa("numDocAdjUltrasonidos")+"")>0)
			{
				//logger.info("CUARTO IF");
				resp=true;
				break;
			}
		}//for
		return resp;
	}	
	
	/**
	 * Método que consulta la información de la sección Plan de Manejo, los tipos parametrizados
	 * y su histórico respectivo para el embarazo actual
	 * @return 
	 */
	public void consultarSeccionPlanManejo (Connection con, HojaObstetrica mundoHojaObstetrica, HojaObstetricaForm hojaObstetricaForm, int codigoPaciente, int institucion)
	{
		//Se consultan los tipos de Plan manejo parametrizados por institución y los otros planes de manejo ingresados para el embarazo y paciente
		hojaObstetricaForm.setMapaTiposPlanManejo(mundoHojaObstetrica.consultarTiposPlanManejoInstitucion(con, institucion, codigoPaciente));
		
		int numReg=UtilidadCadena.vInt(hojaObstetricaForm.getMapaTiposPlanManejo("numRegistros")+"");
		
		if(numReg > 0)
		  {
		  //Se consulta el historico de Plan Manejo tanto parametrizado como otros
		  hojaObstetricaForm.setMapaHistoPlanManejo(mundoHojaObstetrica.consultaHistoricoPlanManejo (con, codigoPaciente));
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
