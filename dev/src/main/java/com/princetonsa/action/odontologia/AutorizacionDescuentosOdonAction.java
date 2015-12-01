package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

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

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.InfoDctoOdontologicoPresupuesto;
import util.odontologia.InfoDefinirSolucitudDsctOdon;

import com.princetonsa.actionform.odontologia.AutorizacionDescuentosOdonForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoConcurrenciaPresupuesto;
import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoTotalesContratarInclusion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.odontologia.AutorizacionDescuentosOdon;
import com.princetonsa.mundo.odontologia.DescuentoOdontologico;
import com.princetonsa.mundo.odontologia.MotivosDescuentos;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;
import com.servinte.axioma.orm.PresupuestoDctoOdon;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio;

/**
 * 
 * @author axioma
 *
 */
public class AutorizacionDescuentosOdonAction extends Action
{
	/**
	 * Atributo para el manejo del log catalina out
	 */
	private Logger logger = Logger.getLogger(AutorizacionDescuentosOdonAction.class);
	
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
		 	
		 	{
		Connection con=null;
		try{
			if(form instanceof AutorizacionDescuentosOdonForm)
			{
				AutorizacionDescuentosOdonForm forma = (AutorizacionDescuentosOdonForm)form;	
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				ActionErrors errores = new ActionErrors();

				//VALIDAMOS SI ES PROGRAMAS O SERVICIOS 
				//PROGRAMAS -->SI
				//SERVICIOS -->NO
				forma.setUtilizaProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
				//


				logger.info("\n\nEL ESTADO DE Autorizacion Descuentos Odontologicos=====>"+forma.getEstado());

				//Este primer estado sirve para cargar el primer menu
				if (forma.getEstado().equals("empezar"))
				{
					forma.reset();
					return mapping.findForward("menu");
				}
				//Este estado sirve para cargar el menu de Autorizar/Consultar Descuentos Odontológicos
				else if (forma.getEstado().equals("irMenuAutConDescuentosOdon"))
				{
					return mapping.findForward("menuAutCon");
				}
				//Este estado sirve para cargar el menu de Definir Descuentos Odontologicos
				else if (forma.getEstado().equals("definirDescuentoOdon"))
				{
					return accionCargarDefinicionDescuento(forma, usuario , mapping, paciente);
				}

				else if (forma.getEstado().equals("cargarDetallePresupuesto"))
				{
					return accionCargarDetallePresupuesto(forma,usuario,mapping,request,paciente);
				}
				//Estado para registrar el detalle de autorización dcto odontologico
				else if (forma.getEstado().equals("detalleAutDescuento"))
				{
					//encapsulamos la informacion respectiva
					forma.setTipoDescuento(""); //LIMPIAR TIPO DESCUENTO

					forma.setInfoDefinir(forma.getListaInforDefinir().get(forma.getPosPresupuesto()));

					//Cambiado
					//Carvajal 
					//forma.setTieneNivelAutorizacion(AutorizacionDescuentosOdon.tieneNivelAutorizacion(usuario.getLoginUsuario(),forma.getInfoDefinir().getConsecutivoPresupuesto()));

					//forma.setTieneNivelAutorizacion(true);

					con= UtilidadBD.abrirConexion();
					DtoConcurrenciaPresupuesto dtoConcurrencia= PresupuestoOdontologico.estaEnProcesoPresupuesto(con,forma.getInfoDefinir().getCuenta() , "", false);
					if(dtoConcurrencia.existeConcurrencia())
					{
						logger.info("******************************************************************************");
						logger.info("TIENE SECCION DE PRESPUESTO ABIERTA");
						logger.info("******************************************************************************");
						errores.add("", new ActionMessage("errors.notEspecific", "No se Permite Definir Solicitudes de Autorizacion de Descuento Mientras se este Utilizando el Presupuesto "));
						saveErrors(request, errores);
						try {
							UtilidadBD.cerrarConexion(con);
						}
						catch(SQLException e)
						{

						}
						return mapping.findForward("definirDescuento");
					}
					try {
						UtilidadBD.cerrarConexion(con);
					}
					catch(SQLException e)
					{

					}
					return mapping.findForward("detalleAutDescuento");
				}
				else if(forma.getEstado().equals("tipoDescuento"))
				{
					return accionCargarTipoDescuento(mapping, forma, usuario, errores, request, paciente);
				}

				else if(forma.getEstado().equals("cargarMotivos"))
				{

					DtoMotivoDescuento dtoMotivo=new DtoMotivoDescuento();

					if(forma.getDtoMotivosDescuento().getTipo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))
					{
						dtoMotivo.setTipo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
					}
					else
					{
						dtoMotivo.setTipo(forma.getDtoMotivosDescuento().getTipo());
					}	

					dtoMotivo.setIndicativo(forma.getTipoDescuento()); // CAMBIO TAREA 154194

					forma.setListaMotivos(MotivosDescuentos.cargar(dtoMotivo));
					forma.setEstado(forma.getEstadoAnterior());
					forma.setEstadoAnterior("");

					return mapping.findForward("detalleAutDescuento");
				}
				//Esado para cargar la opción por paciente por paciente
				else if(forma.getEstado().equals("porPaciente"))
				{	
					return accionCargarOpcionPorPaciente(forma,usuario,mapping,request,paciente,errores);
				}
				else if(forma.getEstado().equals("modificarAutorizacion"))
				{
					return this.accionModificarDescuentoOdon(forma, usuario, errores, request, mapping);
				}

				else if (forma.getEstado().equals("registroAutorizacionCtdoAnulado"))
				{
					this.accionConsultarHistoricoSolAutorizacionDcto(forma,usuario,mapping,request,paciente);
					return mapping.findForward("registroAutorizacionCtdoAnulado");
				}

				else if (forma.getEstado().equals("registroAutorizacionAutor"))
				{
					return accionRegistroAutorizacionAutorizada(forma, usuario, mapping, request, paciente);
				}

				else if(forma.getEstado().equals("cargarMotivosAutor"))
				{
					DtoMotivoDescuento dtoMotivo=new DtoMotivoDescuento();

					if(forma.getDtoMotivosDescuento().getTipo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))
					{
						dtoMotivo.setTipo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
					}
					else
					{
						dtoMotivo.setTipo(forma.getDtoMotivosDescuento().getTipo());
					}

					if(forma.getListaInforDefinir().size()>0 && forma.getPosPresupuesto() > ConstantesBD.codigoNuncaValido){

						InfoDefinirSolucitudDsctOdon solicitud = forma.getListaInforDefinir().get(forma.getPosPresupuesto());

						dtoMotivo.setIndicativo(solicitud.getAcronimoIndicativoMotivo());
					}

					forma.setListaMotivos(MotivosDescuentos.cargar(dtoMotivo));				 
					return mapping.findForward("registroAutorizacionAutor");
				}
				else if(forma.getEstado().equals("porRango"))
				{
					forma.reset();
					forma.setPorPacienteORango(ConstantesIntegridadDominio.acronimoRangoTiempo);
					this.cargarCentroAtencion(forma, usuario);
					return mapping.findForward("porRango");
				}
				else if(forma.getEstado().equals("consultarPorRango"))
				{
					return consultarPorRango(forma, usuario, mapping,errores, request,paciente);
				}
				else if (forma.getEstado().equals("accionModificarDescuentoOdonAutor"))
				{
					accionModificarDescuentoOdonAten(forma, usuario, errores, request);
					return mapping.findForward("registroAutorizacionAutor");
				}
				else if(forma.getEstado().equals("accionOrdenarAuto"))
				{
					this.accionOrdenarAvanzadaAuto(mapping, forma, usuario);
					return  mapping.findForward("definirDescuento"); 
				}
				else if(forma.getEstado().equals("accionOrdenarAutoPorRango"))
				{
					this.accionOrdenarAvanzadaPorRango(mapping, forma, usuario);
					return mapping.findForward("detalleConsultaPorRango");
				}
				else if(forma.getEstado().equals("accionOrdenarAutoPorPaciente"))
				{
					this.accionOrdenarAvanzadaPorRango(mapping, forma, usuario);
					return mapping.findForward("porPaciente");
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
	 * @param errores 
	 * @param request 
	 * @return
	 */
	private ActionForward accionModificarDescuentoOdonAten(
			AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request) 
	{
		int diasFechaPresupuesto = Integer.parseInt(ConstantesBD.codigoNuncaValidoLong+"");
		String fechaSolicitud = "";
		String horaSolicitud = "";
		String fechaIncrementada = "";
		boolean esVigente = false;
		BigDecimal tmpCodigoPresupuesto = BigDecimal.ZERO;
		
		logger.info("******************************************************************************");
		logger.info("--------------------------- CONSTRUIR EL OBJETO DESCUENTO-------------------- ");
		logger.info("******************************************************************************");
	
		if(forma.getDtoMotivosDescuento().getCodigoPk() <= 0.0){
			
			errores.add("", new ActionMessage("errors.required","La selección del motivo"));
			saveErrors(request, errores);
			
			
			return null;
		}
		
		DtoPresupuestoOdontologicoDescuento dtoNew = new DtoPresupuestoOdontologicoDescuento();
		
		dtoNew.setUsuarioFechaModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dtoNew.setCodigo(new BigDecimal(forma.getDtoMotivosDescuento().getCodigoSolicitudDcto()));
		dtoNew.setEstado(forma.getDtoMotivosDescuento().getTipo());
		dtoNew.setMotivo(forma.getDtoMotivosDescuento().getCodigoPk());
		dtoNew.setObservaciones(forma.getDtoMotivosDescuento().getDescripcion()); // pilas con esto
		
		InfoDefinirSolucitudDsctOdon solicitudDescuento = null;

		InfoDefinirSolucitudDsctOdon solicitudDescuentoConsulta = new InfoDefinirSolucitudDsctOdon();
		
		solicitudDescuentoConsulta.setCodigoPkPresupuestoDctoOdon(forma.getListaInforDefinir().get(forma.getPosPresupuesto()).getCodigoPkPresupuestoDctoOdon());
		solicitudDescuentoConsulta.setOrdenamiento("desc");
		
		ArrayList<InfoDefinirSolucitudDsctOdon> solicitudesDescuento = AutorizacionDescuentosOdon.cargarDefinicionSolicitudesDescuento(solicitudDescuentoConsulta);
		
		/*
		 * Se carga nuevamente para tener la información registrada actualmente
		 */
		for (InfoDefinirSolucitudDsctOdon solicitud : solicitudesDescuento) {
			
			solicitudDescuento = solicitud;
		}
		
		if(solicitudDescuento !=null ){
			
			/*
			 * Si los estados son diferentes quiere decir que se esta cambiando el estado de 
			 * la solicitud de descuento de Autorizada a No autorizada o viceversa. o Al estado Anulada,
			 * Una vez este se encuentre en este estado no se permite modificar nuevamente la solicitud
			 * 
			 */
			if(!solicitudDescuento.getAcronimoEstadoSolicitud().equals(forma.getDtoMotivosDescuento().getTipo())){
				
				IAutorizacionPresuDctoOdonServicio autorizacionPresuDctoOdonServicio = PresupuestoFabricaServicio.crearAutorizacionPresuDctoOdonServicio();
				
				/*
				 * Si se tiene un registro de detalle de autorización y si el estado previo de la solicitud
				 * es Autorizado, se debe eliminar el detalle
				 */
				if(solicitudDescuento.getCodigoPkAutorizacionPresuDctoOdon() > 0 && 
				   solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)){
				
					UtilidadTransaccion.getTransaccion().begin();
					
					boolean resultado = autorizacionPresuDctoOdonServicio.eliminarAutorizacionPresuDctoOdon(solicitudDescuento.getCodigoPkAutorizacionPresuDctoOdon());

					if(!resultado){
						
						UtilidadTransaccion.getTransaccion().rollback();
						
						errores.add("", new ActionMessage("errors.notEspecific", "No se pudo actualizar correctamente la solicitud de descuento"));
						
						saveErrors(request, errores);
						
						return null;
					
					}else{
						
						UtilidadTransaccion.getTransaccion().commit();
					}

				}else if (forma.getDtoMotivosDescuento().getTipo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)){
					
					/*
					 * Si se va a autorizar. se debe registrar el detalle de autorización de la solicitud
					 */
					
					long codigoDescuento = ConstantesBD.codigoNuncaValidoLong;
					
					if(solicitudDescuento.getAcronimoIndicativoMotivo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto)
							&& solicitudDescuento.getCodigoPkDetDescuentoOdon()!=null){
						
						codigoDescuento = solicitudDescuento.getCodigoPkDetDescuentoOdon().longValue();
		
					}else if(solicitudDescuento.getAcronimoIndicativoMotivo().equals(ConstantesIntegridadDominio.acronimoPorAtencion)
							&& solicitudDescuento.getCodigoPkDetDescuentoOdonAten()!=null){
					
						codigoDescuento = solicitudDescuento.getCodigoPkDetDescuentoOdonAten().longValue();
					}
					
					if(codigoDescuento > 0){
						
						long codigoAutorizacionPresuDctoOdon = registrarDetalleAutorizacionSolicitudDescuento(solicitudDescuento.getCodigoPkPresupuestoDctoOdon().longValue(), codigoDescuento, solicitudDescuento.getAcronimoIndicativoMotivo());

						if(codigoAutorizacionPresuDctoOdon <= 0){
							
							errores.add("", new ActionMessage("errors.notEspecific", "No se pudo modificar correctamente la Solicitud de Descuento"));
							saveErrors(request, errores);
							return null;
							
						}else{
							
							dtoNew.setCodigoAutorizacionPresuDctoOdon(codigoAutorizacionPresuDctoOdon);
						}
					}
				}
			}
			
			/*
			 * SI ES VALOR O PRESUPUESTO VALIDE LOS DIAS DE VIGENCIA DEL PORCENTAJE CON RESPECTO A LA FECHA DE SOLICITUD
			 */
			/*if(solicitudDescuento.getAcronimoIndicativoMotivo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))		
			{
				
				
				tmpCodigoPresupuesto=solicitudDescuento.getCodigoPkPresupuestoDctoOdon();
				logger.info("CODIGO PK SOLICITUD DESCUENTO -->> "+tmpCodigoPresupuesto);

				diasFechaPresupuesto = AutorizacionDescuentosOdon.vigenciaDiasPresupuesto(tmpCodigoPresupuesto);

				if(diasFechaPresupuesto != ConstantesBD.codigoNuncaValidoLong){
					//dd/mm/yyyy
					fechaSolicitud = solicitudDescuento.getFechaHora();
					UtilidadFecha.getFechaActualTipoBD();
					UtilidadFecha.getFechaActual();
					UtilidadFecha.getHoraActual();

					fechaIncrementada = UtilidadFecha.incrementarDiasAFecha(fechaSolicitud.substring(0, 10), diasFechaPresupuesto, false);
					fechaSolicitud.substring(13);
					//Fecha inicial = fechaSolicitud
					//Fecha final = fechaIncrementada
					//Fecha evaluar = fecha del sistema
					esVigente = UtilidadFecha.betweenFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), fechaSolicitud.substring(0, 10), fechaSolicitud.substring(13), fechaIncrementada, UtilidadFecha.getHoraActual());
					logger.info("ES VIGENTE  -->> "+esVigente);
				}
			}*/

			/*
			 * Modifica la tabla presupuesto_dcto_odon con el nuevo estado
			 */
			if(!AutorizacionDescuentosOdon.modificarPresupuestoDescuento(dtoNew))
			{
				logger.info("ERROR EN LA MODIFICACION ");
			}
		}
		
		return null;
	}

	
	/**
	 * Método que permite registrar el detalle de la autorización de la solicitud de descuento
	 * con la información sobre la fecha de autorización y los dias de vigencia entre otros.
	 * @param codigoDescuento 
	 * 
	 * @return codigo del registro del detalle de autorización
	 */
	private long registrarDetalleAutorizacionSolicitudDescuento (long codigoPresupuestoDctoOdon, long codigoDescuento, String tipoDescuento){
	
		UtilidadTransaccion.getTransaccion().begin();
		
		AutorizacionPresuDctoOdon autorizacionPresuDctoOdon = new AutorizacionPresuDctoOdon();
		
		PresupuestoDctoOdon presupuestoDctoOdon = new PresupuestoDctoOdon();
		presupuestoDctoOdon.setCodigoPk(codigoPresupuestoDctoOdon);
		
		autorizacionPresuDctoOdon.setPresupuestoDctoOdon(presupuestoDctoOdon);
		
		autorizacionPresuDctoOdon.setFechaAutorizacion(UtilidadFecha.getFechaActualTipoBD());
		autorizacionPresuDctoOdon.setHora(UtilidadFecha.getHoraActual());
		//autorizacionPresuDctoOdon.setDiasVigencia(5);
	
		IAutorizacionPresuDctoOdonServicio autorizacionPresuDctoOdonServicio = PresupuestoFabricaServicio.crearAutorizacionPresuDctoOdonServicio();
		
		autorizacionPresuDctoOdonServicio.guardarAutorizacionPresuDctoOdon(autorizacionPresuDctoOdon, codigoDescuento, tipoDescuento);
		
		if(autorizacionPresuDctoOdon.getCodigoPk() > 0){
			
			UtilidadTransaccion.getTransaccion().commit();
			
			return autorizacionPresuDctoOdon.getCodigoPk();
		}else{

			UtilidadTransaccion.getTransaccion().rollback();
			return ConstantesBD.codigoNuncaValidoLong;
		}
	}

	/**
	*
	* @param forma
	* @param usuario
	* @param mapping
	* @param request
	* @param paciente
	* @return
	*/
	private ActionForward accionRegistroAutorizacionAutorizada(AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente)
	{
		//Pongo dentro del Dto los elementos que hay
		int pos=forma.getPosPresupuesto();
		
		if (forma.getListaInforDefinir().size()>0)
		{
			//Consulto el nivel de Autorizacion
			forma.setTieneNivelAutorizacion(AutorizacionDescuentosOdon.tieneNivelAutorizacion(usuario.getLoginUsuario(),forma.getListaInforDefinir().get(pos).getCodigoPkDetDescuentoOdon(),ConstantesIntegridadDominio.acronimoActividadTipoUsuarioAutoDescuentos));
			
			//Si no tiene nivel de autorizacion se consulta el historico para mostrar
			if (!forma.isTieneNivelAutorizacion())
				this.accionConsultarHistoricoSolAutorizacionDcto(forma, usuario, mapping, request, paciente);
			
			forma.getDtoMotivosDescuento().setTipo(forma.getListaInforDefinir().get(pos).getAcronimoTipoMotivo());
			
			if (forma.getListaInforDefinir().get(pos).getCodigoMotivoDescuento().doubleValue()>0)
				forma.getDtoMotivosDescuento().setCodigoPk(forma.getListaInforDefinir().get(pos).getCodigoMotivoDescuento().doubleValue());
			
			forma.getDtoMotivosDescuento().setDescripcion(forma.getListaInforDefinir().get(pos).getObservaciones());
			forma.getDtoMotivosDescuento().setCodigoSolicitudDcto(forma.getListaInforDefinir().get(pos).getCodigoSolicitud().doubleValue());
			//forma.getDtoMotivosDescuento().setEstadoActualSolicitud(forma.getListaInforDefinir().get(pos).getAcronimoEstadoSolicitud());
			
			//Cargo un DTO auxiliar para hacer la busqueda
			DtoMotivoDescuento dtoMotivo=new DtoMotivoDescuento();
			
			if(forma.getListaInforDefinir().get(pos).getAcronimoTipoMotivo().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado))
			{
				dtoMotivo.setTipo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			}
			else
			{
				dtoMotivo.setTipo(forma.getListaInforDefinir().get(pos).getAcronimoTipoMotivo());
			}	
			
			dtoMotivo.setIndicativo(forma.getListaInforDefinir().get(pos).getAcronimoIndicativoMotivo());
			
			//Cargo los elementos correspondientes a los motivos   
			forma.setListaMotivos(MotivosDescuentos.cargar(dtoMotivo));
		}
		return mapping.findForward("registroAutorizacionAutor");
	}
	

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward consultarPorRango(AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario,ActionMapping mapping, ActionErrors errores,HttpServletRequest request, PersonaBasica paciente) throws IPSException {
		
		if(!validarConsultaRango(forma, errores))
		{
			saveErrors(request, errores);
			return mapping.findForward("porRango");
		}
		else
		{
			InfoDefinirSolucitudDsctOdon info = new  InfoDefinirSolucitudDsctOdon();
			logger.info("---------------CHECK PRESUPUESTO ------"+forma.getInfoDefinir().isSoliGeneraPresupuesto()+"<---");
			logger.info("---------------CHECK PRESUPUESTO ------"+forma.getInfoDefinir().getEstadoPresupuestos()+"<---");
			logger.info("---------------CHECK INCLUSIONES ------"+forma.getInfoDefinir().isSoliGeneraInclusiones()+"<---");
			logger.info("---------------CHECK INCLUSIONES ------"+forma.getInfoDefinir().getEstadoInclusion()+"<---");
			info.setTipoBusqueda(forma.getTipoDescuento());
			info.setFechaInicial(forma.getInfoDefinir().getFechaInicialBD());
			info.setFechaFinal(forma.getInfoDefinir().getFechaFinalBD());
			info.setCentroAtencion(new InfoDatosInt(forma.getConsecutivoCentroAtencion()));
			info.setEstado(forma.getInfoDefinir().getEstado());
			info.setSoliGeneraPresupuesto(forma.getInfoDefinir().isSoliGeneraPresupuesto());
			info.setSoliGeneraInclusiones(forma.getInfoDefinir().isSoliGeneraInclusiones());
			info.setEstadoPresupuestos(forma.getInfoDefinir().getEstadoPresupuestos());
			info.setEstadoInclusion(forma.getInfoDefinir().getEstadoInclusion());
			
			InfoDefinirSolucitudDsctOdon dto  = new InfoDefinirSolucitudDsctOdon();
			dto.getCentroAtencion().setCodigo(usuario.getCodigoCentroAtencion());

			//Adiciono el codigo del paciente
			dto.setCodigoPaciente(paciente.getCodigoPersona());
			
			//Obtengo el listado de presupuestos
			forma.setListaInforDefinir(AutorizacionDescuentosOdon.cargarDefinicionSolicitudesDescuento(dto));
			
			//Despues de obtener el listado, postulo el valro total del presupuesto para cada uno de los elementos del listado
			int numRegistros=forma.getListaInforDefinir().size();
			for (int i=0;i<numRegistros;i++)
			{
				InfoDefinirSolucitudDsctOdon dtoDescuento=forma.getListaInforDefinir().get(i);
				BigDecimal codigoPresupuesto=dtoDescuento.getCodigoPkPresupuesto();
				if(dtoDescuento.isInclusion())
				{
					IPresupuestoExclusionesInclusionesServicio inclusion=PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
					DtoTotalesContratarInclusion totales=inclusion.calcularTotalInclusion(dtoDescuento.getCodigoPkEncabezadoInclusion(), paciente.getCodigoIngreso());
					dtoDescuento.setValorPresupuesto( totales.getTotalInclusionesParaDescuento() );
					dtoDescuento.setValorPresupuestoSinDctosPromociones(totales.getTotalInclusiones());
				}
				else
				{
					dtoDescuento.setValorPresupuesto( PresupuestoOdontologico.obtenerTotalPresupuestoParaDescuento(codigoPresupuesto) );
				}
			}

		}
		return mapping.findForward("detalleConsultaPorRango");	
	}

	
	
	
	/**
	 * 
	 * @param forma
	 * @param errores
	 * @return
	 */
	private boolean validarConsultaRango(AutorizacionDescuentosOdonForm forma , ActionErrors errores){
		boolean retornar= true;
		
		if(forma.getConsecutivoCentroAtencion()<=ConstantesBD.codigoNuncaValido)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "El Centro de Atencion es Requerido "));
			retornar= false;
		}
		if(forma.getTipoDescuento().isEmpty())
		{
			errores.add("", new ActionMessage("errors.notEspecific", "El Indicativo Descuento Odontologico es Requerido "));
			retornar= false;
		}
		
		if( !UtilidadTexto.isEmpty(forma.getInfoDefinir().getFechaInicial()) &&  !UtilidadTexto.isEmpty(forma.getInfoDefinir().getFechaFinal()) )
		{
			if( UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getInfoDefinir().getFechaInicial(), forma.getInfoDefinir().getFechaFinal()) )
			{
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), forma.getInfoDefinir().getFechaFinal()) )
				{
					errores.add("", new ActionMessage("errors.notEspecific", "La Fecha Final debe ser menor que la del Sistema"));
					retornar= false;
				}
			}
			else
			{
				errores.add("", new ActionMessage("errors.notEspecific", "La Fecha Inicial Mayor que Fecha Final"));
				retornar= false;
			}
				
		}
		else
		{
			if(UtilidadTexto.isEmpty(forma.getInfoDefinir().getFechaInicial())){
					errores.add("", new ActionMessage("errors.notEspecific", "La Fecha Inicial es Requerido "));
					retornar= false;
			}
			else  if(UtilidadTexto.isEmpty(forma.getInfoDefinir().getFechaFinal())){
					errores.add("", new ActionMessage("errors.notEspecific", "El Fecha Final es Requerido "));
					retornar= false;
			}
		}
	
		return retornar;
	}

	/**
	*
	* @param forma
	* @param usuario
	* @param mapping
	* @param request
	* @param paciente
	* @return
	*/
	private void accionConsultarHistoricoSolAutorizacionDcto(AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente)
	{
		BigDecimal tmpCodigoPresupuesto=forma.getListaInforDefinir().get(forma.getPosPresupuesto()).getCodigoSolicitud();
		forma.setListadoLogDescuentos(AutorizacionDescuentosOdon.consultarHistoricoSolAutorizacionDcto(tmpCodigoPresupuesto));
	}
	
	/**
	 *
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarDescuentoOdon(
			AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request, ActionMapping mapping) {
		
		BigDecimal  valorPresupuesto;
		
		/*
		 * Codigo del descuento asociado, depende si se realiza por Valor presupuesto o por Atención
		 */
		BigDecimal codigoDescuento = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		
		logger.info("******************************************************************************");
		logger.info("--------------------------- CONSTRUIR EL OBJETO DESCUENTO-------------------- ");
		logger.info("******************************************************************************");
		
		if (!this.validarActualizacionDescuento(forma, usuario, errores) )
		{
			saveErrors(request, errores);
			return mapping.findForward("detalleAutDescuento");	
		}
		
		DtoPresupuestoOdontologicoDescuento dtoNew = new DtoPresupuestoOdontologicoDescuento();
		
		dtoNew.setUsuarioFechaModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dtoNew.setCodigo(forma.getInfoDefinir().getCodigoPkPresupuestoDctoOdon());
		
		//NIVEL DE AUTORIZACION
		if(forma.getTieneNivelAutorizacion())
		{
			dtoNew.setEstado(forma.getDtoMotivosDescuento().getTipo());
			dtoNew.setMotivo(forma.getDtoMotivosDescuento().getCodigoPk());
			dtoNew.setObservaciones(forma.getTmpObservacionPresupuestoDctoOdon());
		}
		
		//Tarea 154303
		if(!forma.getTieneNivelAutorizacion())
		{
			dtoNew.setEstado(ConstantesIntegridadDominio.acronimoPendientePorAutorizar);
		}
		
	
		if (forma.getTipoDescuento().equals( ConstantesIntegridadDominio.acronimoPorValorPresupuesto) )
		{
			dtoNew.setDetalleDescuentoOdontologico(forma.getInfoDctoOdonPresupuesto().getDetalleDctoOdon().doubleValue());
		
			codigoDescuento = forma.getInfoDctoOdonPresupuesto().getDetalleDctoOdon();
			
			dtoNew.setPorcentajeDcto(forma.getInfoDctoOdonPresupuesto().getPorcentajeDcto());
			valorPresupuesto= new BigDecimal ( forma.getInfoDefinir().getValorPresupuesto().doubleValue()*forma.getInfoDctoOdonPresupuesto().getPorcentajeDcto()/100);
			dtoNew.setValorDescuento(valorPresupuesto);
			
		}else if (forma.getTipoDescuento().equals( ConstantesIntegridadDominio.acronimoPorAtencion) )
		{

			dtoNew.setDetalleDescuentoOdontologicoAtencion( forma.getListaDctAtencion().get(forma.getPostDescuentosOdon()).getConsecutivo()); 
			
			codigoDescuento = new BigDecimal(forma.getListaDctAtencion().get(forma.getPostDescuentosOdon()).getConsecutivo());
			
			dtoNew.setPorcentajeDcto(forma.getListaDctAtencion().get(forma.getPostDescuentosOdon()).getPorcentajeDescuento());
			valorPresupuesto = new BigDecimal (forma.getInfoDefinir().getValorPresupuesto().doubleValue() * dtoNew.getPorcentajeDcto()/100);     
			dtoNew.setValorDescuento(valorPresupuesto);
			
		}
		
		/*
		 * Solo en el caso que se vaya a autorizar una solicitud de descuento se genera un registro de detalle de autorización
		 */
		if(dtoNew.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado) && forma.getInfoDefinir().getCodigoPkAutorizacionPresuDctoOdon() <= 0){
			
			long codigoAutorizacionPresuDctoOdon = registrarDetalleAutorizacionSolicitudDescuento(forma.getInfoDefinir().getCodigoPkPresupuestoDctoOdon().longValue() , codigoDescuento.longValue(), forma.getTipoDescuento());
			
			if(codigoAutorizacionPresuDctoOdon > 0){
				
				dtoNew.setCodigoAutorizacionPresuDctoOdon(codigoAutorizacionPresuDctoOdon);
			
			}else{
				
				errores.add("", new ActionMessage("errors.notEspecific", "No se pudo modificar correctamente la Solicitud de Descuento"));
				saveErrors(request, errores);
				return mapping.findForward("detalleAutDescuento");
			}
		}
		
		Connection con= UtilidadBD.abrirConexion();
		DtoConcurrenciaPresupuesto dtoConcurrencia= PresupuestoOdontologico.estaEnProcesoPresupuesto(con,forma.getInfoDefinir().getCuenta() , "", false);
		
		if(dtoConcurrencia.existeConcurrencia())
		{
			errores.add("", new ActionMessage("errors.notEspecific", "No se Permite Definir Solicitudes de Autorizacion de Descuento Mientras se este Utilizando el Presupuesto "));
			saveErrors(request, errores);
			try {
			UtilidadBD.cerrarConexion(con);
			}
			catch(SQLException e)
			{
				
			}
			return mapping.findForward("definirDescuento");
		}
		
		
		if(!AutorizacionDescuentosOdon.modificarPresupuestoDescuento(dtoNew))
		{
			errores.add("", new ActionMessage("errors.notEspecific", " 	NO SE PUEDE REALIZAR LA MODIFICACION "));
			saveErrors(request, errores);
		}
		else
		{
		   return  resumenDescuento( forma ,  mapping);
		}
		return mapping.findForward("detalleAutDescuento");	
	}
	

	
	/**
	 * 	RESUMEN
	 * @param forma
	 */
	private ActionForward resumenDescuento( AutorizacionDescuentosOdonForm forma , ActionMapping mapping){
		DtoPresupuestoOdontologicoDescuento dto = new DtoPresupuestoOdontologicoDescuento();
		dto.setCodigo(forma.getInfoDefinir().getCodigoPkPresupuestoDctoOdon());
		dto.setInclusion(forma.getInfoDefinir().isInclusion());
		ArrayList<String> lista= new ArrayList<String>();
		
		forma.setDtoPresupuestoDescuentoOdo(PresupuestoOdontologico.cargarPresupuestoDescuentos(dto, lista /* Lista de estado  */).get(0));
		return mapping.findForward("resumenAutDescuento");
	} 
	
	
	
	/**
	 * 	VAlIDAR LA AUTORIZACION LA ENTRADA DE LOS DATOS DE AUTORIZACION DE DESCUENTO ODONTOLOGICO
	 * @param forma
	 * @param usuario
	 * @param errores
	 * @param request
	 * @return
	 */
	private boolean  validarActualizacionDescuento(AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario, ActionErrors errores)
	{
		boolean retorna= true;
		
		if(forma.getTieneNivelAutorizacion())
		{
			if( UtilidadTexto.isEmpty(forma.getDtoMotivosDescuento().getTipo() ))
			{
				errores.add("", new ActionMessage("errors.notEspecific", "El tipo es Requerido "));
				retorna= false;
			}
			if( forma.getDtoMotivosDescuento().getCodigoPk()<=0)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "El Motivo es Requerido "));
				retorna= false;
			}
		}
		if(!retorna)
		{
			forma.setEstado(forma.getEstadoAnterior()); 
			forma.setEstadoAnterior("");
		}
		return retorna;
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionCargarOpcionPorPaciente(AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente,ActionErrors errores) throws IPSException 
	{
		forma.setPorPacienteORango(ConstantesIntegridadDominio.acronimoPaciente);

		if (paciente.getCodigoPersona()<1)
		{
			errores.add("",	new ActionMessage("errors.paciente.noCargado",""));
			saveErrors(request, errores);
			return mapping.findForward("menuAutCon");
		}
		else
		{
			InfoDefinirSolucitudDsctOdon dto  = new InfoDefinirSolucitudDsctOdon();
			dto.getCentroAtencion().setCodigo(usuario.getCodigoCentroAtencion());

			//Adiciono el codigo del paciente
			dto.setCodigoPaciente(paciente.getCodigoPersona());
			
			//Obtengo el listado de presupuestos
			forma.setListaInforDefinir(AutorizacionDescuentosOdon.cargarDefinicionSolicitudesDescuento(dto));
			
			//Despues de obtener el listado, postulo el valro total del presupuesto para cada uno de los elementos del listado
			int numRegistros=forma.getListaInforDefinir().size();
			for (int i=0;i<numRegistros;i++)
			{
				InfoDefinirSolucitudDsctOdon dtoDescuento=forma.getListaInforDefinir().get(i);
				BigDecimal codigoPresupuesto=dtoDescuento.getCodigoPkPresupuesto();
				if(dtoDescuento.isInclusion())
				{
					IPresupuestoExclusionesInclusionesServicio inclusion=PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
					DtoTotalesContratarInclusion totales=inclusion.calcularTotalInclusion(dtoDescuento.getCodigoPkEncabezadoInclusion(), paciente.getCodigoIngreso());
					dtoDescuento.setValorPresupuesto( totales.getTotalInclusionesParaDescuento() );
					dtoDescuento.setValorPresupuestoSinDctosPromociones(totales.getTotalInclusiones());
				}
				else
				{
					dtoDescuento.setValorPresupuesto( PresupuestoOdontologico.obtenerTotalPresupuestoParaDescuento(codigoPresupuesto) );
				}
			}
			
			return mapping.findForward("porPaciente");
		}
	}
	

	/**
	 * ACCION CARGAR TIPO DESCUENTO POR PRESUPUESTO O POR CENTRO ATENCION
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarTipoDescuento(ActionMapping mapping,
													AutorizacionDescuentosOdonForm forma, 
													UsuarioBasico usuario, 
													ActionErrors errores, 
													HttpServletRequest request, 
													PersonaBasica paciente) throws IPSException 
	{
		
		logger.info("\n\n\n\n\n\n\n");
		logger.info("**************************************************************************************************************");
		logger.info("------------------------------------  CARGAR DESCUENTOS  -----------------------------------------------------");
		logger.info("**************************************************************************************************************");

		if(forma.getInfoDefinir().isInclusion())
		{
			IPresupuestoExclusionesInclusionesServicio inclusion=PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();

			DtoTotalesContratarInclusion totales=inclusion.calcularTotalInclusion(forma.getInfoDefinir().getCodigoPkEncabezadoInclusion(), paciente.getCodigoIngreso());
			
			//forma.getInfoDefinir().setValorPresupuesto( totales.getTotalInclusiones() );
		}
		else{
			forma.getInfoDefinir().setValorPresupuesto( PresupuestoOdontologico.obtenerTotalPresupuestoParaDescuento(forma.getInfoDefinir().getCodigoPkPresupuesto()));
		}
		forma.setInfoDctoOdonPresupuesto(CargosOdon.obtenerDescuentoOdon( usuario.getCodigoCentroAtencion(), UtilidadFecha.getFechaActual(), forma.getInfoDefinir().getValorPresupuesto()));	
		
		logger.info("	-----------VALOR PRESUPUESTO--->	"+forma.getInfoDefinir().getValorPresupuesto()+"<---");
		logger.info("\n\n\n\n");
 
		
		/*
		 * VALIDACIONES GENERALES POR PRESUPUESTO O POR ATENCION
		 */
		
		if(forma.getTipoDescuento().equals( ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		{
			/*
			 * VALIDACION AUTORIZACION
			 */
			forma.setTieneNivelAutorizacion(AutorizacionDescuentosOdon.tieneNivelAutorizacion(usuario.getLoginUsuario(),forma.getInfoDefinir().getConsecutivoPresupuesto(),ConstantesIntegridadDominio.acronimoActividadTipoUsuarioAutoDescuentos));
			
			/*
			 * VALIACION DE PORCENTAJE 
			 */
			if(forma.getInfoDctoOdonPresupuesto().getPorcentajeDcto()<=0)
			{
				//forma.getInfoDefinir().setValorPresupuesto(BigDecimal.ZERO);
				forma.setInfoDctoOdonPresupuesto(new InfoDctoOdontologicoPresupuesto());
				errores.add("", new ActionMessage("errors.notEspecific", "No existe descuento 'Por Valor' vigente, Por favor Verifique,  Proceso Cancelado"));
				saveErrors(request, errores);
				
			}
			/*
			 * 
			 */
			forma.setValorTmp("");
			logger.info("POR PRESUPUETO");
			//DESCUENTO POR PRESUPUESTO 
			//CARGAR EL PORCENTAJE DE DESCUENTO, VALOR DE DESCUENTO
			logger.info("CARGANDO POR PRESUPUESTO");
			forma.setEstado("porPresupuesto");
		}
		else if (forma.getTipoDescuento().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
		{
			
			/*
			 *VALIDACION DE AUTORIZACION  
			 */
			forma.setTieneNivelAutorizacion(AutorizacionDescuentosOdon.tieneNivelAutorizacionCentroAtencion(usuario.getLoginUsuario(),forma.getInfoDefinir().getConsecutivoPresupuesto(),usuario.getCodigoInstitucionInt(),ConstantesIntegridadDominio.acronimoActividadTipoUsuarioAutoDescuentos));
			
			// DESCUENTO POR ATENCION 
			logger.info("POR ATENCION ");
			DtoDescuentoOdontologicoAtencion dtoAten = new DtoDescuentoOdontologicoAtencion();
			dtoAten.setCentroAtencion(new InfoDatosInt( usuario.getCodigoCentroAtencion()));
			/*
			 * CARGAR DESCUENTO POR CENTRO ATENCION
			 */
			forma.setListaDctAtencion(DescuentoOdontologico.cargarAtencion(dtoAten)) ;
			
			/*
			 * VALIACION DE PORCENTAJE 
			 */
			if(forma.getListaDctAtencion().size()<=0)
			{
				forma.getInfoDefinir().setValorPresupuesto(BigDecimal.ZERO);
				forma.setInfoDctoOdonPresupuesto(new InfoDctoOdontologicoPresupuesto());
				errores.add("", new ActionMessage("errors.notEspecific", "No Existe Descuento por Atencion vigente, Verifique, Proceso Cancelado"));
				saveErrors(request, errores);
				
			}
			forma.setEstado("porAtencion");
			
			
		}
		
		forma.getDtoMotivosDescuento().setTipo("");
		forma.getDtoMotivosDescuento().setCodigoPk(0);
		
		logger.info("\n\n\n\n");
		return mapping.findForward("detalleAutDescuento");
	}


	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarDefinicionDescuento(
			AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario, ActionMapping mapping,
			PersonaBasica paciente) throws IPSException {
		InfoDefinirSolucitudDsctOdon dto  = new InfoDefinirSolucitudDsctOdon();
		dto.setEstadoAutorizacionDescuento(ConstantesIntegridadDominio.acronimoXDefinir) ;
		dto.getCentroAtencion().setCodigo(usuario.getCodigoCentroAtencion());
		forma.setListaInforDefinir(AutorizacionDescuentosOdon.cargarDefinicionSolicitudesDescuento(dto));
		int tamanoLista=forma.getListaInforDefinir().size();
		for(int i=0; i<tamanoLista; i++)
		{
			InfoDefinirSolucitudDsctOdon dtoDescuento=forma.getListaInforDefinir().get(i);
			if(dtoDescuento.isInclusion())
			{
				IPresupuestoExclusionesInclusionesServicio inclusion=PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
				
				DtoTotalesContratarInclusion totales=inclusion.calcularTotalInclusion(dtoDescuento.getCodigoPkEncabezadoInclusion(), paciente.getCodigoIngreso());
				
				dtoDescuento.setValorPresupuesto( totales.getTotalInclusionesParaDescuento() );
				dtoDescuento.setValorPresupuestoSinDctosPromociones(totales.getTotalInclusiones());
			}
			else{
				dtoDescuento.setValorPresupuesto(PresupuestoOdontologico.obtenerTotalPresupuestoParaDescuento(forma.getListaInforDefinir().get(i).getCodigoPkPresupuesto()));
				dtoDescuento.setValorPresupuestoSinDctosPromociones(PresupuestoOdontologico.obtenerTotalPresupuestoSinDctoOdon(forma.getListaInforDefinir().get(i).getCodigoPkPresupuesto()));
			}
		}
		
		return mapping.findForward("definirDescuento");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionCargarDetallePresupuesto(AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) 
	{
		int posPresupuesto = forma.getPosPresupuesto();
		long tmpCodigoPresupuesto = 0;
		
		InfoDefinirSolucitudDsctOdon dtoDescuento=forma.getListaInforDefinir().get(posPresupuesto);
		logger.info("ES INCLUSION -----------------> "+dtoDescuento.isInclusion());
				
		if(dtoDescuento.isInclusion())
		{
			tmpCodigoPresupuesto=forma.getListaInforDefinir().get(forma.getPosPresupuesto()).getCodigoPkEncabezadoInclusion();
			//tmpCodigoPresupuesto=forma.getListaInforDefinir().get(forma.getPosPresupuesto()).getConsecutivoPresupuesto().longValue();
			
		}
		else
		{
			tmpCodigoPresupuesto=forma.getListaInforDefinir().get(forma.getPosPresupuesto()).getCodigoPkPresupuesto().longValue();	
		}

		if(dtoDescuento.isInclusion())
		{
			logger.info("ES INCLUSION");
			
			if(forma.getUtilizaProgramas().equals(ConstantesBD.acronimoSi))
			{
				forma.setListadoProgramasPresupuesto(AutorizacionDescuentosOdon.consultarDetalleProgramasInclusion(tmpCodigoPresupuesto, usuario.getCodigoInstitucionInt()));
				forma.setListadoServiciosPresupuesto(AutorizacionDescuentosOdon.consultarDetalleServiciosInclusion(tmpCodigoPresupuesto, usuario.getCodigoInstitucionInt()));
			}
			//Si no, solo se cargan los servicios
			else
			{
				forma.setListadoServiciosPresupuesto(AutorizacionDescuentosOdon.consultarDetalleServiciosInclusion(tmpCodigoPresupuesto, usuario.getCodigoInstitucionInt()));
			}
			
		}
		else
		{
			if(forma.getUtilizaProgramas().equals(ConstantesBD.acronimoSi))
			{
				forma.setListadoProgramasPresupuesto(AutorizacionDescuentosOdon.consultarDetalleProgramasPresupuesto(tmpCodigoPresupuesto, usuario.getCodigoInstitucionInt()));
				forma.setListadoServiciosPresupuesto(AutorizacionDescuentosOdon.consultarDetalleServiciosPresupuesto(tmpCodigoPresupuesto, usuario.getCodigoInstitucionInt()));
			}
			//Si no, solo se cargan los servicios
			else
			{
				forma.setListadoServiciosPresupuesto(AutorizacionDescuentosOdon.consultarDetalleServiciosPresupuesto(tmpCodigoPresupuesto, usuario.getCodigoInstitucionInt()));
			}
		}

		return mapping.findForward("detallePresupuesto");
	}
	
	
	
	
	/**
	 * CARGAR LOS CENTROS DE ATENCION
	 * @param forma
	 * @param usuario
	 */
	private void cargarCentroAtencion(AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario)
	{
		DtoCentrosAtencion dto = new DtoCentrosAtencion();
		dto.setCodInstitucion(usuario.getCodigoInstitucionInt());
		dto.setActivo(true);
		
		forma.setListaCentros(UtilidadesManejoPaciente.obtenerCentrosAtencion(dto));
		
		DtoCentrosAtencion centroAtencionTodos = new DtoCentrosAtencion();
		centroAtencionTodos.setConsecutivo(0);
		centroAtencionTodos.setDescripcion("Todos");
		forma.getListaCentros().add(centroAtencionTodos);
		
		DtoCentrosAtencion centroAtencionSeleccione = new DtoCentrosAtencion();
		centroAtencionSeleccione.setConsecutivo(ConstantesBD.codigoNuncaValido);
		centroAtencionSeleccione.setDescripcion("Seleccione");
		forma.getListaCentros().add(centroAtencionSeleccione);
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarAvanzadaAuto(ActionMapping mapping,
			AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario) {
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		boolean ordenamiento= false;
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaInforDefinir() ,sortG);
		return mapping.findForward("paginaPrincipalConsulta");
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 */
	private void accionOrdenarAvanzadaPorRango(ActionMapping mapping,
			AutorizacionDescuentosOdonForm forma, UsuarioBasico usuario) {
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		boolean ordenamiento= false;
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			ordenamiento = true;
		}
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaInforDefinir() ,sortG);
	}

	
	
}