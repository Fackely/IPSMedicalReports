/*
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.historiaClinica.InformacionRecienNacidosForm;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.InformacionRecienNacidos;
import com.sysmedica.util.UtilidadFichas;


/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class InformacionRecienNacidosAction extends Action
{

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(InformacionRecienNacidosAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;

		try {
			if(form instanceof InformacionRecienNacidosForm)
			{
				InformacionRecienNacidosForm forma = (InformacionRecienNacidosForm) form;
				InformacionRecienNacidos mundo= new InformacionRecienNacidos();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente= Utilidades.getPersonaBasicaSesion(request.getSession());
				String estado = forma.getEstado();
				if(!UtilidadValidacion.esProfesionalSalud(usuario))
				{
					request.setAttribute("codigoDescripcionError", "errors.noProfesionalSalud");
					return mapping.findForward("paginaError");
				}

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}


				logger.warn("estado -->"+estado);

				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto. getMaxPageItems(usuario.getCodigoInstitucionInt())));
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de InformacionRecienNacidosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezarRango"))
				{
					forma.reset();
					forma.setProfesionalesInstitucion(Utilidades.obtenerMedicosInstitucion(con,usuario.getCodigoInstitucion()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaRango");
				}
				else if(estado.equals("consultarRango"))
				{
					this.consultarSolicitudesRango(con,forma,mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaListado");
				}
				else if(estado.equals("consultarPaciente"))
				{
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto. getMaxPageItems(usuario.getCodigoInstitucionInt())));
					forma.setProfesionalesInstitucion(Utilidades.obtenerMedicosInstitucion(con,usuario.getCodigoInstitucion()));
					ActionForward forward= validacionAccesoPaciente(paciente, request, mapping);
					if(forward!=null)
					{
						UtilidadBD.closeConnection(con);
						return forward;
					}
					this.consultarSolicitudesPacinte(con,forma,mundo,paciente);
					if(Integer.parseInt(forma.getSolicitudes("numRegistros")+"")>0)
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaListado");
					}
					else
					{
						UtilidadBD.cerrarConexion(con);
						logger.warn("Paciente SIN SOLICITUDES");			
						request.setAttribute("codigoDescripcionError", "error.informacionRecienNacidos.pacienteSinSolicitudes");
						return mapping.findForward("paginaError");
					}
				}
				else if(estado.equals("volverListadoAdmisiones"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaListado");
				}
				else if(estado.equals("cargarDetalleAdmision"))
				{
					forma.setOcultarEncabezado(false);
					forma.setHijos(mundo.consultarHijosCirugia(con,forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+""));

					UtilidadSesion.notificarCambiosObserver(Integer.parseInt(forma.getSolicitudes("codigopaciente_"+forma.getItemSeleccionado())+""), getServlet().getServletContext());
					paciente.setCodigoPersona((Integer.parseInt(forma.getSolicitudes("codigopaciente_"+forma.getItemSeleccionado())+"")));
					paciente.cargar(con,(Integer.parseInt(forma.getSolicitudes("codigopaciente_"+forma.getItemSeleccionado())+"")));
					paciente.cargarPaciente(con, (Integer.parseInt(forma.getSolicitudes("codigopaciente_"+forma.getItemSeleccionado())+"")),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					setObservable(paciente);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("listadoDetalleHijos");
				}
				//Estado exclusivo para informacion del parto-------------------------------------------
				else if(estado.equals("cargarDetalleCirugia"))
				{
					return accionCargarDetalleCirugia(con,forma,mundo,mapping,request,usuario);

				}
				else if(estado.equals("nuevoHijo"))
				{
					//EliminarDichas Inactivas 
					UtilidadFichas.eliminarFichasInactivas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona());

					forma.setFinalizada(ConstantesBD.acronimoNo);

					forma.setModificacion(false);
					forma.resetIndicadoresSecciones();
					forma.setHijoSeleccionado("");
					forma.resetCamposHijos();
					mundo.consultarParametrizacion(con,usuario.getCodigoInstitucion());
					copiarInfoMundoToForm(forma, mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalleHijo");
				}
				else if(estado.equals("cargarHijo"))
				{
					forma.setFinalizada(ConstantesBD.acronimoNo);

					//EliminarDichas Inactivas 
					UtilidadFichas.eliminarFichasInactivas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona());

					forma.resetIndicadoresSecciones();
					this.accionCargarHijo(con,forma,mundo,usuario.getCodigoInstitucion());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalleHijo");
				}
				else if(estado.equals("volverListadoHijos"))
				{
					forma.setHijos(mundo.consultarHijosCirugia(con,forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+""));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("listadoDetalleHijos");
				}
				else if(estado.equals("guardar"))
				{

					if(forma.isModificacion())
					{
						this.accionModificarInformacion(con,forma,mundo, paciente, usuario);
					}
					else
					{
						forma.setUsuarioProceso(usuario.getLoginUsuario());
						forma.setFechaProceso(UtilidadFecha.getFechaActual());
						forma.setHoraProceso(UtilidadFecha.getHoraActual());
						//this.accionGuardarInformacion(con,forma,mundo);

						this.accionGuardarInformacion(con,forma,mundo, paciente, usuario);
					}
					//UtilidadBD.cerrarConexion(con);
					//return mapping.findForward("detalleHijo");


					forma.setHijos(mundo.consultarHijosCirugia(con,forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+""));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("listadoDetalleHijos");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de InformacionRecienNacidosForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Método implementado para ingresar/modificar información de recién nacidos desde la funcionalidad
	 * de información del parto
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param mapping
	 * @param request
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionCargarDetalleCirugia(Connection con, InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		forma.setSolicitudes(new HashMap());
		
		forma.setOcultarEncabezado(true);
		forma.setItemSeleccionado("0");
		
		forma.setProfesionalesInstitucion(Utilidades.obtenerMedicosInstitucion(con,usuario.getCodigoInstitucion()));
		
		logger.info("idCuenta => "+request.getParameter("idCuenta"));
		forma.setSolicitudes("cuenta_0", request.getParameter("idCuenta"));
		forma.setSolicitudes("tipoidmadre_0", request.getParameter("tipoIdMadre"));
		forma.setSolicitudes("numeroidmadre_0", request.getParameter("numeroIdMadre"));
		forma.setSolicitudes("nombremadre_0", request.getParameter("nombreMadre"));
		forma.setSolicitudes("codigocirugia_0", forma.getCodigoCirugia());
		forma.setSolicitudes("fechaadmision_0",request.getParameter("fechaAdmision"));
		forma.setSolicitudes("solicitud_0",request.getParameter("solicitud"));
		forma.setSolicitudes("admision_0",request.getParameter("admision"));
		forma.setSolicitudes("numerosolicitud_0",request.getParameter("numeroSolicitud"));
		forma.setHijos(mundo.consultarHijosCirugia(con,forma.getCodigoCirugia()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoDetalleHijos");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo

	 */
	private void accionGuardarInformacion(Connection con, InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo, PersonaBasica paciente, UsuarioBasico usuario)
	{
		this.copiarInfoFormToMundo(forma, mundo);
		mundo.insertarInformacionGeneral(con,paciente,usuario);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 */
	private void accionModificarInformacion(Connection con, InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo,  PersonaBasica paciente, UsuarioBasico usuario)
	{
		this.copiarInfoFormToMundo(forma,mundo);
		mundo.actualizarInformacionRecienNacido(con,paciente,usuario);
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void copiarInfoFormToMundo(InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo)
	{
		mundo.setFechaNacimiento(forma.getFechaNacimiento());
		mundo.setHoraNacimiento(forma.getHoraNacimiento());
		mundo.setSexo(forma.getSexo());
		mundo.setVivo(forma.getVivo());
		mundo.setDiagRN(forma.getDiagRN());
		mundo.setCieDiagRN(forma.getCieDiagRN());
		mundo.setNombreDiagRN(forma.getNombreDiagRN());
		mundo.setFechaMuerte(forma.getFechaMuerte());
		mundo.setHoraMuerte(forma.getHoraMuerte());
		mundo.setDiagMuerte(forma.getDiagMuerte());
		mundo.setCieDiagMuerte(forma.getCieDiagMuerte());
		mundo.setNombreDiagMuerte(forma.getNombreDiagMuerte());
		mundo.setMomentoMuerte(forma.getMomentoMuerte());
		mundo.setFalleceSalaParto(forma.getFalleceSalaParto());
		mundo.setPesoEdadGestacion(forma.getPesoEdadGestacion());
		mundo.setVitaminaK(forma.getVitaminaK());
		mundo.setProfilaxisOftalmico(forma.getProfilaxisOftalmico());
		mundo.setHemoclasificacion(forma.getHemoclasificacion());
		mundo.setSensibilizado(forma.getSensibilizado());
		mundo.setDefectosCongenitos(forma.getDefectosCongenitos());
		mundo.setDiagDefCong(forma.getDiagDefCong());
		mundo.setCieDiagDefCong(forma.getCieDiagDefCong());
		mundo.setNombreDiagDefCong(forma.getNombreDiagDefCong());
		mundo.setFechaEgreso(forma.getFechaEgreso());
		mundo.setHoraEgreso(forma.getHoraEgreso());
		mundo.setCondicionEgreso(forma.getCondicionEgreso());
		mundo.setLactancia(forma.getLactancia());
		mundo.setPesoEgreso(forma.getPesoEgreso());
		mundo.setNuip(forma.getNuip());
		mundo.setVacunaPolio(forma.getVacunaPolio());
		mundo.setVacunaBCG(forma.getVacunaBCG());
		mundo.setVacunaHepatitisB(forma.getVacunaHepatitisB());
		mundo.setSano(forma.getSano());
		mundo.setConductaSeguir(forma.getConductaSeguir());
		mundo.setCodigoProfesionalAtendio(forma.getCodigoProfesionalAtendio());
		mundo.setUsuarioProceso(forma.getUsuarioProceso());
		mundo.setFechaProceso(forma.getFechaProceso());
		mundo.setHoraProceso(forma.getHoraProceso());
		mundo.setCodigo(forma.getCodigo());
		mundo.setCodigoCirugia(forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())+"");
		mundo.setDiagEgreso((HashMap)forma.getDiagEgreso().clone());
		mundo.setReanimacion((HashMap)forma.getReanimacion().clone());
		mundo.setTamizacionNeonatal((HashMap)forma.getTamizacionNeonatal().clone());
		mundo.setSecAdaptacionNeonatalInmediata((HashMap)forma.getSecAdaptacionNeonatalInmediata().clone());
		mundo.setSecExamenesFisicos((HashMap)forma.getSecExamenesFisicos().clone());
		mundo.setSecDiagnosticoRecienNacido((HashMap)forma.getSecDiagnosticoRecienNacido().clone());
		mundo.setSecSano((HashMap)forma.getSecSano().clone());
		mundo.setSecApgar((HashMap)forma.getSecApgar().clone());
		mundo.setConductaSeguir_ani(forma.getConductaSeguir_ani());
		mundo.setEdadGestacionExamen(forma.getEdadGestacionExamen());
		
		mundo.setNumeroCertificadoNacimiento(forma.getNumeroCertificadoNacimiento());
		
		mundo.setObservacionesEgreso(forma.getObservacionesEgreso());
		
		mundo.setFicha(forma.getFicha());
		mundo.setFichaMte(forma.getFichaMte());
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		
		mundo.setFinalizada(forma.getFinalizada());
		
		mundo.setCodigoEnfermedad(forma.getCodigoEnfermedad());
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param institucion 
	 */
	private void accionCargarHijo(Connection con, InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo, String institucion)
	{
		forma.setModificacion(true);
		mundo.consultarHijo(con,forma.getHijos("consecutivo_"+forma.getHijoSeleccionado())+"",institucion);
		this.copiarInfoMundoToForm(forma,mundo);
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void copiarInfoMundoToForm(InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo)
	{
		forma.setFechaNacimiento(mundo.getFechaNacimiento());
		forma.setHoraNacimiento(mundo.getHoraNacimiento());
		forma.setSexo(mundo.getSexo());
		forma.setVivo(mundo.getVivo());
		forma.setDiagRN(mundo.getDiagRN());
		forma.setCieDiagRN(mundo.getCieDiagRN());
		forma.setNombreDiagRN(mundo.getNombreDiagRN());
		forma.setFechaMuerte(mundo.getFechaMuerte());
		forma.setHoraMuerte(mundo.getHoraMuerte());
		forma.setDiagMuerte(mundo.getDiagMuerte());
		forma.setCieDiagMuerte(mundo.getCieDiagMuerte());
		forma.setNombreDiagMuerte(mundo.getNombreDiagMuerte());
		forma.setMomentoMuerte(mundo.getMomentoMuerte());
		forma.setFalleceSalaParto(mundo.getFalleceSalaParto());
		forma.setPesoEdadGestacion(mundo.getPesoEdadGestacion());
		forma.setVitaminaK(mundo.getVitaminaK());
		forma.setProfilaxisOftalmico(mundo.getProfilaxisOftalmico());
		forma.setHemoclasificacion(mundo.getHemoclasificacion());
		forma.setSensibilizado(mundo.getSensibilizado());
		forma.setDefectosCongenitos(mundo.getDefectosCongenitos());
		forma.setDiagDefCong(mundo.getDiagDefCong());
		forma.setCieDiagDefCong(mundo.getCieDiagDefCong());
		forma.setNombreDiagDefCong(mundo.getNombreDiagDefCong());
		forma.setFechaEgreso(mundo.getFechaEgreso());
		forma.setHoraEgreso(mundo.getHoraEgreso());
		forma.setCondicionEgreso(mundo.getCondicionEgreso());
		forma.setLactancia(mundo.getLactancia());
		forma.setPesoEgreso(mundo.getPesoEgreso());
		forma.setNuip(mundo.getNuip());
		forma.setVacunaPolio(mundo.getVacunaPolio());
		forma.setVacunaBCG(mundo.getVacunaBCG());
		forma.setVacunaHepatitisB(mundo.getVacunaHepatitisB());
		forma.setSano(mundo.getSano());
		forma.setConductaSeguir(mundo.getConductaSeguir());
		forma.setCodigoProfesionalAtendio(mundo.getCodigoProfesionalAtendio());
		forma.setUsuarioProceso(mundo.getUsuarioProceso());
		forma.setFechaProceso(mundo.getFechaProceso());
		forma.setHoraProceso(mundo.getHoraProceso());
		forma.setCodigo(mundo.getCodigo());
		
		forma.setNumeroCertificadoNacimiento(mundo.getNumeroCertificadoNacimiento());
		
		forma.setDiagEgreso((HashMap)mundo.getDiagEgreso().clone());
		
		forma.setReanimacion((HashMap)mundo.getReanimacion().clone());
		forma.setTamizacionNeonatal((HashMap)mundo.getTamizacionNeonatal().clone());
		forma.setSecAdaptacionNeonatalInmediata((HashMap)mundo.getSecAdaptacionNeonatalInmediata().clone());
		forma.setSecExamenesFisicos((HashMap)mundo.getSecExamenesFisicos().clone());
		forma.setSecDiagnosticoRecienNacido((HashMap)mundo.getSecDiagnosticoRecienNacido().clone());
		forma.setSecSano((HashMap)mundo.getSecSano().clone());
		forma.setSecApgar((HashMap)mundo.getSecApgar().clone());
		
		forma.setConductaSeguir_ani(mundo.getConductaSeguir_ani());
		
		forma.setEdadGestacionExamen(mundo.getEdadGestacionExamen());
		
		forma.setObservacionesEgreso(mundo.getObservacionesEgreso());
		
		//new ini
		forma.setUniCodyCieDiagRN(forma.getDiagRN() + ConstantesBD.separadorSplit + forma.getCieDiagRN() + ConstantesBD.separadorSplit + forma.getNombreDiagRN() );
		forma.setUniCodyCieDiagDefCon(forma.getDiagDefCong() + ConstantesBD.separadorSplit + forma.getCieDiagDefCong() + ConstantesBD.separadorSplit + forma.getNombreDiagDefCong());
		//new fin
		
		
		forma.setDiagRN(mundo.getDiagRN());
		forma.setCieDiagRN(mundo.getCieDiagRN());
		forma.setNombreDiagRN(mundo.getNombreDiagRN());		
		//new fin
		
		forma.setFinalizada(mundo.getFinalizada());
		
		forma.setCodigoEnfermedad(mundo.getCodigoEnfermedad());
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 */
	private void consultarSolicitudesPacinte(Connection con, InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo, PersonaBasica paciente)
	{
		HashMap vo=new HashMap();
		vo.put("codigoPaciente", paciente.getCodigoPersona()+"");
		forma.setSolicitudes(mundo.consultarSolicitudes(con,vo));
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void consultarSolicitudesRango(Connection con, InformacionRecienNacidosForm forma, InformacionRecienNacidos mundo)
	{
		HashMap vo=new HashMap();
		vo.put("fechaInicial", forma.getFechaInicial());
		vo.put("fechaFinal", forma.getFechaFinal());
		forma.setSolicitudes(mundo.consultarSolicitudes(con,vo));
	}
	
	
	/**
	 * 
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward validacionAccesoPaciente(PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		else
		{
			if(paciente.getCodigoSexo()!=ConstantesBD.codigoSexoFemenino)
			{
				logger.warn("Paciente no Femenino");			
				request.setAttribute("codigoDescripcionError", "errors.paciente.noEsFemenino");
				return mapping.findForward("paginaError");
			}
		}
		return null;
	}
	
	/**
	* Método para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
}
