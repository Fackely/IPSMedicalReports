/*
 * @(#)AdmisionesHospitalariasMAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action;

import java.sql.Connection;
import java.util.ArrayList;

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
import util.RespuestaValidacion;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AdmisionesHospitalariasForm;
import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * Action para el manejo de la funcionalidad de modificar admisión hospitalaria
 *
 * @version 1.0, Mar 4, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * adiciones
 * @author <a href="mailto:sgomez@PrincetonSA.com">Sebasti&acute;n G&oacute;mez</a>,
 */
public class AdmisionesHospitalariasMAction  extends Action
{

	/**
	 * Para hacer los logs de la aplicación
	 */
	protected Logger logger = Logger.getLogger(AdmisionesHospitalariasMAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
								 						ActionForm form,
								 						HttpServletRequest request,
								 						HttpServletResponse response) throws Exception
	{
		Connection con = null;
		
			try {
			if (response==null); //Para evitar que salga el warning
			
			if(form instanceof AdmisionesHospitalariasForm)
			{
				AdmisionesHospitalariasForm admisionesH = (AdmisionesHospitalariasForm)form;
	
				//Abro una conexión vacia, de esta manera si se presenta alguna excepcion pueda cerrarla
				//si no la transacción queda abierta
				
				//Vamos a inicializar todo lo que se necesite
				HttpSession session=request.getSession();
	
	
				//String tipoBD = session.getServletContext().getInitParameter("TIPOBD");
				String tipoBD=System.getProperty("TIPOBD");//mas seguro este metodo.
					
				String accion=admisionesH.getAccion();
	
				con=UtilidadBD.abrirConexion();
	
				UsuarioBasico usuario = new UsuarioBasico();
					
				if( request.getAttribute("TIPOBD") != null ) // Indica que se está en pruebas actualmente
				{
					usuario.init(tipoBD);
					usuario.cargarUsuarioBasico(con, "os-lopez");				
				}
				else
				{
					usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				}
				
				//**************OBJETOS QUE SE USARAN*****************************************************
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				Cuenta cuenta = new Cuenta();
				cuenta.cargarCuenta(con,paciente.getCodigoCuenta()+"");
				//*****************************************************************************************
				logger.warn("[AdmisionesHospitalariasMAction] estado=>"+accion);
				if (admisionesH.getAccion()==null||admisionesH.getAccion().equals(""))
				{
					admisionesH.reset();
				    return ComunAction.accionSalirCasoError(mapping,request, con, logger, "El estado (Acción no tenía ningún valor)" , "errors.estadoInvalido", true);
				}
				
				if (admisionesH.getAccion().equals("cargar"))
				{
					
					
					//Codigo que verifica que la cuenta activa es de admision
					if(cuenta.getCodigoViaIngreso()!=null&&paciente.getCodigoAdmision()>0)
					{
						if(cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+"")||
							cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoUrgencias+""))	
							//Señala que la cuent activa es una admisión
							admisionesH.setIndicador("1");
						else
							//Señala que la cuenta activa no es una admisión
							admisionesH.setIndicador("0");
							
					}
					else
					{
							
						//Es posible que el indicador se encuentre nulo, lo que indica que no hay admisiones actuales 
						if(admisionesH.getIndicador()!=null)
						{
							//Si el indicador es 2 entonces se está verificando
							//una admisión inactiva sin cuenta activa
							if(admisionesH.getIndicador().equals("2"))
								admisionesH.setIndicador("1");
							//de lo contrario se consulta una cuenta activa que no es admision
							else
								admisionesH.setIndicador("0");
						}
						else
							admisionesH.setIndicador("0");
						
							
					} 
					//**************************************************************************
								// Si entra a este if es o un error o porque estamos en una busqueda
								String numeroId=admisionesH.getNumeroId();
								String tipoId=admisionesH.getTipoId();
								String idBusqueda=admisionesH.getIdBusqueda();
								
								
								if (idBusqueda!=null&&!idBusqueda.equals(""))
								{
									//En este caso debo revisar si hay una admision abierta con ese codigo y
									//que se pueda abrir en esta institución (Utilidad validacion) y si se puede 
									//cargarla y llevarla a la pagina de modificacion
									
									if (UtilidadValidacion.existeAdmisionHospitalariaAbiertaCodigo (con, tipoBD, idBusqueda, usuario.getCodigoInstitucion()))
									{
										//Si entro por aca es porque la admision que me dieron si es valida
										AdmisionHospitalaria admisionHospitalaria=new AdmisionHospitalaria();
										admisionHospitalaria.init(tipoBD);
										admisionHospitalaria.cargar(con, new TipoNumeroId(null, idBusqueda));
										admisionHospitalaria.setDatosCamaActual(admisionHospitalaria.consultarDatosCamaActual(con, paciente.getCodigoPersona()));
										llenarBean(admisionesH, admisionHospitalaria);
										UtilidadBD.cerrarConexion(con);
										
										// forward a pagina de modificar
										return mapping.findForward("paginaPrincipal");
				
									}
									else 
									{
										//Si entro aca es porque no existe admision abierta, pero hay otras
										//Luego tengo que cargar la última
										RespuestaValidacion resp1=UtilidadValidacion.existeAdmisionHospitalariaCodigo(con, tipoBD, idBusqueda, usuario.getCodigoInstitucion());
										if (resp1.puedoSeguir)
										{
											AdmisionHospitalaria admisionHospitalaria=new AdmisionHospitalaria();
											admisionHospitalaria.init(tipoBD);
											admisionHospitalaria.cargar(con, new TipoNumeroId(null, resp1.textoRespuesta));
											admisionHospitalaria.setDatosCamaActual(new ArrayList());
											llenarBean(admisionesH, admisionHospitalaria);
											UtilidadBD.cerrarConexion(con);
											return mapping.findForward("paginaPrincipal");
										}
										else
										{
										    return ComunAction.accionSalirCasoError(mapping,request, con, logger, "El codigo de la admision suministrado no es valido", "El codigo de la admision suministrado no es valido", false);
										}
									}
							
								}
								else if (tipoId!=null&&!tipoId.equals("")&&numeroId!=null&&!numeroId.equals(""))
								{
									RespuestaValidacion resp0=UtilidadValidacion.existeAdmisionHospitalariaAbiertaPaciente(con,tipoBD,tipoId, numeroId, usuario.getCodigoInstitucion());
									if (resp0.puedoSeguir)
									{
										AdmisionHospitalaria admisionHospitalaria=new AdmisionHospitalaria();
										admisionHospitalaria.init(tipoBD);
										admisionHospitalaria.cargar(con, new TipoNumeroId(null, resp0.textoRespuesta));
										admisionHospitalaria.setDatosCamaActual(new ArrayList());
										llenarBean(admisionesH, admisionHospitalaria);
										UtilidadBD.cerrarConexion(con);
										
										return mapping.findForward("paginaPrincipal");
										
									}
									else 
									{
										//Aca tenemos que revisar si existen mas admisiones
										//y tomar la última o mostrar un error
										RespuestaValidacion resp1=UtilidadValidacion.existeAdmisionHospitalariaPaciente(con,tipoBD,tipoId, numeroId, usuario.getCodigoInstitucion());
										if (resp1.puedoSeguir)
										{
											//Si puedo seguir en resp1 obtengo el codigo de la admision que debo cargar
											AdmisionHospitalaria admisionHospitalaria=new AdmisionHospitalaria();
											admisionHospitalaria.init(tipoBD);
											admisionHospitalaria.cargar(con, new TipoNumeroId(null, resp1.textoRespuesta));
											admisionHospitalaria.setDatosCamaActual(new ArrayList());
											llenarBean(admisionesH, admisionHospitalaria);
											UtilidadBD.cerrarConexion(con);
											
											return mapping.findForward("paginaPrincipal");
										}
										else
										{
											admisionesH.reset();
										    return ComunAction.accionSalirCasoError(mapping,request, con, logger, "El paciente No tiene admisión", "errors.paciente.noAdmision", true);
										}
									}
											
								}
								else
								{
									admisionesH.reset();
									return ComunAction.accionSalirCasoError(mapping,request, con, logger, "No llegó tipo id en modificar admisiones hospitalarias" , "errors.accesoInvalido", true);
								}
						
				}
				else 
				if( accion.equals("buscarTexto") || accion.equals("buscarCodigo") )
				{
					request.setAttribute("accion", accion);
					
					request.setAttribute("criterioBusquedaDiagnostico", admisionesH.getCriterioBusquedaDiagnostico());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("busquedaDiagnostico");
				}
				else 
				if(accion.equals("volverPaginaPrincipal"))
				{
					//Si estamos en este punto, debemos poner la variable de visualización
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else 
				if(accion.equals("mostrarDatosAdmisionHospitalaria"))
				{
				}
				else
				if( accion.equals("actualizar") )
				{
					admisionesH.reset();
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipalAxioma");
				}
				else
				if(accion.equals("modificar") )
				{	
					try 
					{										
						if( tipoBD == null && request.getAttribute("TIPOBD") != null )  // Es una prueba
						{
							tipoBD = (String)request.getAttribute("TIPOBD");
						}
								
						// Aca se debe crear un nuevo objeto con la admision y hacer el update a la base de datos.
						String[] diagnosticoCodigos = admisionesH.getDiagnostico().split("-");
						AdmisionHospitalaria admision = new AdmisionHospitalaria( admisionesH.getOrigen(), diagnosticoCodigos[0], diagnosticoCodigos[1], admisionesH.getCausaExterna()/*, admisionesH.getNumeroAutorizacion() */);
						llenarBeanModificables(admisionesH , admision);
						TipoNumeroId idAdmision = new TipoNumeroId((String)null,  (new Integer(admisionesH.getCodigoAdmisionHospitalaria())).toString());
						admision.init(tipoBD);	
						if ( admision.modificar(con, idAdmision) == 0 )
						{
							admisionesH.reset();
							return ComunAction.accionSalirCasoError(mapping,request, con, logger, "No se pudo modificar correctamente la admision", "No se pudo modificar correctamente la admision", false);
						}
						else 
						{
							admisionesH.reset();
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("finalModificacion");
						}
					}
	
					catch (Exception e)
					{
						//Entramos aca si no se pudo realizar el ingreso en cuyo caso mostramos un error
						admisionesH.reset();
						return ComunAction.accionSalirCasoError(mapping,request, con, logger, "Los datos ingresados no son compatibles con el sistema de almacenamiento, por favor utilize la interfaz gráfica" , "errors.accesoInvalido", true);
					}		
				}
				
				else
				{
				    return ComunAction.accionSalirCasoError(mapping,request, con, logger, "El estado dado por el usuario es inválido", "errors.estadoInvalido", true);
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
	 * Carga el form con la información contenida en el objeto de admisiòn
	 * hospitalaria,
	 * @param 	ActionForm, admisionesH. Bean donde se van a cargar los datos 
	 * @param 	AdmisionHospitalaria, admisionHospitalaria. Objeto con la
	 * información pertinente de la admisión hospitalaria
	 */
	public void llenarBean(AdmisionesHospitalariasForm admisionesH , AdmisionHospitalaria admisionHospitalaria)
	{
		admisionesH.setIdCuenta(admisionHospitalaria.getIdCuenta()+"");
		admisionesH.setCodigoAdmisionHospitalaria(admisionHospitalaria.getCodigoAdmisionHospitalaria());
		admisionesH.setFecha(admisionHospitalaria.getFecha());
		admisionesH.setHora(admisionHospitalaria.getHora().substring(0, 5));
		admisionesH.setTipoUsuarioCama(admisionHospitalaria.getNombreTipoUsuarioCama());
		admisionesH.setDescripcionCama(admisionHospitalaria.getDescripcionCama());
		admisionesH.setCodigoCompuestoOrigen(admisionHospitalaria.getCodigoOrigen() + "-" + admisionHospitalaria.getOrigen());
		admisionesH.setCodigoCompuestoMedico(admisionHospitalaria.getMedico().getTipoIdentificacionPersona()+"-"+admisionHospitalaria.getMedico().getNumeroIdentificacionPersona() + "-" + admisionHospitalaria.getMedico().getNombrePersona());
		admisionesH.setCodigoCompuestoCausaExterna(admisionHospitalaria.getCodigoCausaExterna() + "-" + admisionHospitalaria.getCausaExterna());
		admisionesH.setCodigoCompuestoDiagnostico(admisionHospitalaria.getCodigoDiagnostico()+"-"+admisionHospitalaria.getCodigoCIEDiagnostico() + "-" + admisionHospitalaria.getNombreDiagnostico());
		admisionesH.setNombreCama(admisionHospitalaria.getNombreCama());
		admisionesH.setCcostoCama(admisionHospitalaria.getNombreCentroCostoCama());
		admisionesH.setEstadoCama(admisionHospitalaria.getNombreEstado());
		//admisionesH.setNumeroAutorizacion(admisionHospitalaria.getNumeroAutorizacion());
		admisionesH.setLoginUsuario(admisionHospitalaria.getLoginUsuario());
		
		admisionesH.setHabitacionInicial(admisionHospitalaria.getHabitacion());
		admisionesH.setDatosCamaActual(admisionHospitalaria.getDatosCamaActual());
	}

	/**
	 * Carga en el form de admisiones hospitalaria los datos de los campos
	 * modificables
	 * @param 	ActionForm, admisionesH. Bean donde se van a cargar los datos
	 * @param		AdmisionHospitalaria, admisionHospitalaria. Objeto con la
	 * información pertinente de la admisión hospitalaria
	 */
	public void llenarBeanModificables(AdmisionesHospitalariasForm admisionesH , AdmisionHospitalaria admisionHospitalaria)
	{
		admisionesH.setCodigoCompuestoOrigen(admisionHospitalaria.getCodigoOrigen() + "-" + admisionHospitalaria.getOrigen());
		admisionesH.setCodigoCompuestoCausaExterna(admisionHospitalaria.getCodigoCausaExterna() + "-" + admisionHospitalaria.getCausaExterna());
		admisionesH.setCodigoCompuestoDiagnostico(admisionHospitalaria.getCodigoDiagnostico()+"-"+admisionHospitalaria.getCodigoCIEDiagnostico() + "-" + admisionHospitalaria.getNombreDiagnostico());
		/*
		if( admisionHospitalaria.getNumeroAutorizacion().equals("") ) 
			admisionHospitalaria.setNumeroAutorizacion(" ");
		else
			admisionesH.setNumeroAutorizacion(admisionHospitalaria.getNumeroAutorizacion());
		*/
	}

}