/*
 * Aug 22, 2007
 * Proyect axioma
 * Paquete com.princetonsa.action.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.HashMap;

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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.AnulacionCargosFarmaciaForm;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.AnulacionCargosFarmacia;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class AnulacionCargosFarmaciaAction extends Action
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(AnulacionCargosFarmaciaAction.class);
	
	/**
	 * Indices del mapa de solicitudes.
	 */
	private static String[] indices={"numerosolicitud_","tiposolicitud_","nomtiposolicitud_","consecutivosolicitud_","fechahorasolicitud_","codigoccsolicitado_","nomccsolicitado_","nomcentroatencion_","nomviaingreso_"};
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof AnulacionCargosFarmaciaForm)
			{

				con = UtilidadBD.abrirConexion();	

				AnulacionCargosFarmaciaForm forma =(AnulacionCargosFarmaciaForm)form;
				AnulacionCargosFarmacia mundo=new AnulacionCargosFarmacia();
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//validar que el paciente este cargado en sesion.
				if(paciente.getCodigoPersona()<1)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.paciente.noCargado", true);
				}

				String estado=forma.getEstado();

				logger.info("El estado es: "+estado);

				if(estado.equals("empezar"))
				{
					forma.reset();


					forma.setIngresos(UtilidadesHistoriaClinica.consultarIngresosValidos(con,paciente.getCodigoPersona(),usuario.getCodigoCentroAtencion()));
					if(Utilidades.convertirAEntero(forma.getIngresos().get("numRegistros")+"",false)==1)
					{
						forma.setIndiceIngresoSeleccionado(0);
						return this.accionCargarInformacionGeneral(con,forma,mundo,paciente,usuario,mapping,request);
					}
					else if (Utilidades.convertirAEntero(forma.getIngresos().get("numRegistros")+"",false)>1)
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listadoIngresos");
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no tiene Ingresos/Cuentas validas.", "Paciente no tiene Ingresos/Cuentas validas", false);
					}
				}
				else if(estado.equals("cargarIngreso"))
				{
					return this.accionCargarInformacionGeneral(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("cargarSolicitudes"))
				{
					return this.accionCargarSolicitudes(con,forma,mundo,mapping);	
				}
				else if(estado.equals("ordenarSolicitudes"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("detalleSolicitud"))
				{
					return this.accionDetalleSolicitud(con,forma,mundo,mapping);
				}
				else if (estado.equals("guardar"))
				{
					return this.accionGuardarAnulacion(con,forma,mundo,usuario,mapping);
				}
				else if (estado.equals("guardar2"))
				{
					return this.accionGuardarAnulacion(con,forma,mundo,usuario,mapping);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de AnulacionCargosFarmaciaForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarAnulacion(Connection con, AnulacionCargosFarmaciaForm forma, AnulacionCargosFarmacia mundo, UsuarioBasico usuario, ActionMapping mapping) 
	{
		boolean exitoso=mundo.guardarAnulacion(con,forma.getResponsables().get(forma.getIndiceResponsableSeleccionado()).getSubCuenta(),forma.getSolicitudes("numerosolicitud_"+forma.getIndiceSolSeleccionado())+"",forma.getMotivoAnulacion(),forma.getDetalleSolicitudes(),usuario.getLoginUsuario(),forma.getSolicitudes("codigoccsolicitado_"+forma.getIndiceSolSeleccionado())+"",usuario.getCodigoInstitucionInt(),forma.getEstado(),forma.getSolicitudesOrdenes());
		if(exitoso)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Proceso resalizado Exitosamente."));
			forma.setEsResumen(true);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Proceso No Realizado."));
			forma.setEsResumen(false);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleSolicitud(Connection con, AnulacionCargosFarmaciaForm forma, AnulacionCargosFarmacia mundo, ActionMapping mapping) 
	{
		forma.setEsResumen(false);
		forma.setMostrarMensaje(new ResultadoBoolean(false));
		forma.setMotivoAnulacion("");

		forma.setDetalleSolicitudes(mundo.consultarDetalleSolicitudes(con,forma.getSolicitudes("numerosolicitud_"+forma.getIndiceSolSeleccionado())+"",forma.getResponsables().get(forma.getIndiceResponsableSeleccionado()).getSubCuenta(),
				forma.getSolicitudesOrdenes()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(AnulacionCargosFarmaciaForm forma) 
	{
		int numReg=Integer.parseInt(forma.getSolicitudes("numRegistros")+"");
		forma.setSolicitudes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getSolicitudes(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSolicitudes("numRegistros",numReg+"");
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarSolicitudes(Connection con, AnulacionCargosFarmaciaForm forma, AnulacionCargosFarmacia mundo, ActionMapping mapping) 
	{
		if(forma.getIndiceResponsableSeleccionado()>ConstantesBD.codigoNuncaValido)
		{
			forma.setSolicitudes(mundo.consultarSolicitudes(con,forma.getResponsables().get(forma.getIndiceResponsableSeleccionado()).getSubCuenta()));
			forma.setSolicitudesOrdenes(mundo.consultarDetalleSolicitudesOrdenes(con,forma.getResponsables().get(forma.getIndiceResponsableSeleccionado()).getSubCuenta()));
			
			
			
			
		}
		else
		{
			HashMap<String, Object> mapa=new HashMap<String, Object>();
			mapa.put("numRegistros","0");
			forma.setSolicitudes(mapa);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario 
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarInformacionGeneral(Connection con, AnulacionCargosFarmaciaForm forma, AnulacionCargosFarmacia mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		int codigoPaciente=paciente.getCodigoPersona();
		forma.setCodigoIngreso(Utilidades.convertirAEntero(forma.getIngresos().get("idingreso_"+forma.getIndiceIngresoSeleccionado())+"",false));
			
			//********************SE CARGA EL PACIENTE*******************************************
			ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
			try 
			{
				paciente.clean();
				paciente.cargar(con, codigoPaciente);
				
				//solo si el ingreso se encuentra en estado abierto, se puede cargar la informacion del ingreso en la sesion.
				if((forma.getIngresos().get("estadoingreso_"+forma.getIndiceIngresoSeleccionado())+"").trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
				
			} 
			catch (Exception e) 
			{
				logger.info("Error cargando el paciente.: "+e);
			}
			observable.addObserver(paciente);
			request.getSession().removeAttribute("pacienteActivo");
			request.getSession().setAttribute("pacienteActivo",paciente);
			UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
			//***********************************************************************************
		
		///cargar los responsables del ingreso.
		forma.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con,forma.getCodigoIngreso(),true,new String[0],false, "" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Via de ingreso*/));
		if(forma.getResponsables().size()==1)
		{
			forma.setIndiceResponsableSeleccionado(0);
			return this.accionCargarSolicitudes(con, forma, mundo, mapping);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

}
