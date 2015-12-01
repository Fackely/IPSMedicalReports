/**
 * 
 */
package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.actionform.odontologia.ConfirmacionCambioServicioForm;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoProcesoCentralizadoConfirmacionCambioServicios;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosAnterioresCita;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosNuevosCita;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.odontologia.ProcesoCentralizadoConfirmacionCambioServicios;
import com.princetonsa.mundo.odontologia.SolicitudCambioServicioMundo;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author armando
 *
 */
public class ConfirmacionCambioServicioAction extends Action 
{

	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(ConfirmacionCambioServicioAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Connection con=null;
		try{
			if(form instanceof ConfirmacionCambioServicioForm)
			{
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica persona = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
				ConfirmacionCambioServicioForm forma=(ConfirmacionCambioServicioForm)form;

				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				if(forma.getEstado().equals("empezar"))
				{
					acciomEmpezar(forma,persona,usuario);
					return mapping.findForward("principal");
				}
				if(forma.getEstado().equals("empezarAnulacion"))
				{
					acciomEmpezar(forma,persona,usuario);
					forma.setAnulacion(true);
					return mapping.findForward("principal");
				}
				if(forma.getEstado().equals("confirmar"))
				{
					forma.setResumen(false);
					if(persona==null || persona.getCodigoPersona()<=0)
					{
						persona.setCodigoPersona(forma.getDtoSolicitud().getCodigoPaciente());
						try {
							con=UtilidadBD.abrirConexion();
							persona.cargar(con, persona.getCodigoPersona());
							persona.cargarPaciente2(con, persona.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
							UtilidadBD.closeConnection(con);
						} catch (SQLException e) 
						{
							logger.error("ERROR CARGANDO EL PACIENTE",e);
						}
					}
					forma.setDtoConfirmacion(ProcesoCentralizadoConfirmacionCambioServicios.generarConfirmacionCambioServiciosOdontologicos(forma.getDtoSolicitud(),persona,usuario, false));
					forma.setResumen(true);
					String forward=forma.getDtoConfirmacion().getForward();
					if(forward.equals("principal"))
					{
						forward="recargarPagina";
					}

					if(forma.getDtoConfirmacion().getErrores().size()>0)
					{
						ActionErrors errores = new ActionErrors();
						for(String error:forma.getDtoConfirmacion().getErrores())
						{
							errores.add("", new ActionMessage("errors.notEspecific",error));
						}
						saveErrors(request, errores);
					}
					return mapping.findForward(forward);
				}
				else if(forma.getEstado().equals("cofirmarAnulacion"))
				{
					forma.setResumen(false);
					if(anularSolicitud(forma,usuario))
					{
						acciomEmpezar(forma,persona,usuario);
						forma.setAnulacion(true);
						forma.setMensaje(new ResultadoBoolean(true,"Se anulo solicitud de cambio de servicio. Proceso Exitoso"));
						return mapping.findForward("principal");
					}
					else
					{
						forma.setMensaje(new ResultadoBoolean(true,"No se pudo anular la solicitud. Por favor verifique"));
					}
					return mapping.findForward("principal");
				}
				else if(forma.getEstado().equals("recargar"))
				{
					return mapping.findForward("principal");
				}
				else if(forma.getEstado().equals("procesoExitoso"))
				{
					forma.setMensaje(new ResultadoBoolean(true, "Proceso Exitoso."));
					return mapping.findForward("principal");
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
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private boolean anularSolicitud(ConfirmacionCambioServicioForm forma,UsuarioBasico usuario) 
	{
		forma.getDtoSolicitud().setUsuarioAnulacion(usuario.getLoginUsuario());
		forma.getDtoSolicitud().setFechaAnulacion(UtilidadFecha.getFechaActual());
		forma.getDtoSolicitud().setHoraAnulacion(UtilidadFecha.getHoraActual());
		forma.getDtoSolicitud().setMotivoAnulacion(forma.getMotivoAnulacion());
		forma.getDtoSolicitud().setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
		return (SolicitudCambioServicioMundo.anularSolicitud(forma.getDtoSolicitud()));
	}

	/**
	 * 
	 * @param forma
	 * @param persona 
	 * @param usuario 
	 */
	private void acciomEmpezar(ConfirmacionCambioServicioForm forma, PersonaBasica persona, UsuarioBasico usuario) throws IPSException 
	{
		forma.reset();
		forma.setDtoSolicitud(SolicitudCambioServicioMundo.cargarSolicitudCambioServicio(forma.getCodigoSolitud(),usuario.getCodigoInstitucionInt()));
		if(persona==null || persona.getCodigoPersona()<=0)
		{
			persona.setCodigoPersona(forma.getDtoSolicitud().getCodigoPaciente());
			try {
				Connection con=UtilidadBD.abrirConexion();
				persona.cargar(con, persona.getCodigoPersona());
				persona.cargarPaciente2(con, persona.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
				UtilidadBD.closeConnection(con);
			} catch (SQLException e) 
			{
				logger.error("ERROR CARGANDO EL PACIENTE",e);
			}
		}
		calcularTarifas(forma,persona);
		DtoProcesoCentralizadoConfirmacionCambioServicios proceso=new DtoProcesoCentralizadoConfirmacionCambioServicios();
		ProcesoCentralizadoConfirmacionCambioServicios.calcularTarifaServiciosCita(proceso,forma.getDtoSolicitud(),persona,usuario);
		double totalServiciosNuevos=proceso.getValorTotalServicios();
		double totalServiciosAnteriores=proceso.getValorTotalServiciosAnteriores();
		
		forma.getDtoSolicitud().setTotalTrifaProgServNuevos(totalServiciosNuevos);
		forma.getDtoSolicitud().setTotalTrifaProgServAnteriores(totalServiciosAnteriores);
	}

	/**
	 * 
	 * @param forma
	 * @param paciente 
	 */
	private void calcularTarifas(ConfirmacionCambioServicioForm forma, PersonaBasica paciente) throws IPSException 
	{
		{
			Connection con=UtilidadBD.abrirConexion();
			
			ArrayList<DtoSubCuentas> listaResponsables=UtilidadesHistoriaClinica.obtenerResponsablesIngreso(
								con,
								paciente.getCodigoIngreso(),
								true, // Traer todos los responsables (Facturados y no facturasdos)
								new String[0], // Exluir responsables
								false, // Solamente PYP
								"", // Sub cuenta
								paciente.getCodigoUltimaViaIngreso());
			
			/*
			 * Este arraylist se envia a la vista para mostrar las tarifas de los sevicios
			 */
			double valorTotalServiciosPaciente=0;
			double valorTotalServiciosConvenio=0;
			ArrayList<DtoServicioOdontologico> listadoServicios=new ArrayList<DtoServicioOdontologico>();
			for(DtoProgramasServiciosAnterioresCita temporal:forma.getDtoSolicitud().getProgServAnteriores())
			{
				listadoServicios.add(temporal.getDtoServicio());
			}
			for(DtoProgramasServiciosNuevosCita temporal:forma.getDtoSolicitud().getProgServNuevos())
			{
				listadoServicios.add(temporal.getServicio());
			}
			for(int i=0;i<listadoServicios.size();i++)
			{
				DtoServicioOdontologico servicio=listadoServicios.get(i);
				
					InfoTarifaServicioPresupuesto tarifa=new InfoTarifaServicioPresupuesto();
					InfoResponsableCobertura infoResponsableCobertura=new InfoResponsableCobertura();
					for(int w=0; w<listaResponsables.size(); w++)
					{
						DtoSubCuentas subCuenta= listaResponsables.get(w);
						if(servicio.getCodigoPresuOdoProgSer()>0)
						{
							CargosOdon.obtenerInfoPresupuestoContratadoProgSer(con,servicio.getCodigoPresuOdoProgSer(), infoResponsableCobertura, tarifa, UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(forma.getCodigoInstitucion())), servicio.getCodigoServicio(), new BigDecimal(servicio.getCodigoPlanTratamiento()));
				
							if(!tarifa.getError().equals(""))
							{
								tarifa.setError(tarifa.getError()+" para el servicio "+servicio.getDescripcionServicio());
							}
							
						}
						else
						{
							logger.info("Obtener info Tarifa Unitaria Por Servicio ");
							infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), servicio.getCodigoServicio(), forma.getCodigoInstitucion(), false, subCuenta.getSubCuenta());
							int convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
							tarifa=CargosOdon.obtenerTarifaUnitariaXServicio(servicio.getCodigoServicio(), convenio, subCuenta.getContrato(), "", forma.getCodigoInstitucion(), new BigDecimal(paciente.getCodigoCuenta()), false /*en este punto no existe programa, por esa razon la cobertura se hace a nivel del servicio*/, forma.getDtoSolicitud().getCita().getCodigoCentroCosto());
						}
						
						if(tarifa.getError().equals(""))
						{
							// Como no hay error encontro cobertura y tarifa
							servicio.setInfoTarifa(tarifa);
							servicio.setResponsableServicio(infoResponsableCobertura.getDtoSubCuenta());
							servicio.setPacientePagaAtencion(Contrato.pacientePagaAtencion(infoResponsableCobertura.getDtoSubCuenta().getContrato()));
							
							// no debo validar nada m�s
							w=listaResponsables.size();
							break;
						}
					}
					if(tarifa==null || !tarifa.getError().equals(""))
					{
						logger.error("error tomando la tarifa");
					}
				
			}
			
			UtilidadBD.closeConnection(con);
		}
		
	}
}
