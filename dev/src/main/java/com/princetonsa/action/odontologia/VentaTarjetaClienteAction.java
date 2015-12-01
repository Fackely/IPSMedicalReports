package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Errores;
import util.Errores.Tipo;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.clonacion.UtilidadClonacion;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.InfoTarifaServicioPresupuesto;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.actionform.odontologia.VentaTarjetaClienteForm;
import com.princetonsa.actionform.odontologia.VentaTarjetaClienteIdCampos;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoParentesco;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.princetonsa.dto.tesoreria.DtoBonoSerialValor;
import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.dto.tesoreria.DtoDetallePagosBonos;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.princetonsa.dto.tesoreria.DtoInformacionFormaPago;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturasVarias.GenModFacturasVarias;
import com.princetonsa.mundo.odontologia.TarjetaCliente;
import com.princetonsa.mundo.tesoreria.EntidadesFinancieras;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.odontologia.facturacion.convenio.ConveniosFabricaMundo;
import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.convenio.ConveniosFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturasvarias.FacturasVariasFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.ventaTarjeta.VentaTarjetaServicioFabrica;
import com.servinte.axioma.servicio.impl.administracion.CentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ITipoTarjetaClienteServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IContratoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.administracion.IEmisionTarjetaClienteServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IBeneficiarioServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IVentaTarjetaClienteServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IIngresosEgresosCajaServicioServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITarjetaFinancieraServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITurnoDeCajaServicio;


/**
 * Control de la venta de la tarjeta cliente
 * @author Edgar Carvajal Ruiz, Juan David Ramírez
 * @version 2.0
 * @since 27 Agosto 2010
 */
public class VentaTarjetaClienteAction extends Action  {
	
	
	/**
	 * Encargado de la ejecución del flujo
	 */
	public ActionForward execute(	ActionMapping mapping,
		 							ActionForm form,
		 							HttpServletRequest request,
		 							HttpServletResponse response) throws Exception{
		
		if(form instanceof VentaTarjetaClienteForm)
		{
			VentaTarjetaClienteForm forma = (VentaTarjetaClienteForm)form;	
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			Log4JManager.info(forma.getEstado());
			
			if(forma.getEstado().equals("asignarPropiedad")){
				/*
				 * Se asigna una propiedad a la forma sin cambiar nada en la presentación
				 */
				UtilidadTransaccion.getTransaccion().commit();
				return null;
			}
			if(forma.getEstado().equals("empezar")){
				/*
				 * Método que carga los datos Básicos 
				 */
				forma.reset();
				UtilidadTransaccion.getTransaccion().commit();
				return accionCargarDatosBasicos(mapping, forma, usuario, request);
			}
			
			else if(forma.getEstado().equals("cargarTipoVenta"))
			{
				/*
				 * Métodos para cargar el tipo de venta
				 */
				forma.resetSinLimpiarTipoVenta();// solamente voy a limpiar por el momento
				accionCargarDatosBasicos(mapping, forma, usuario, request);
				forma.setMsgInformacionComprador(null);
				cargarDatosParaClaseVenta(forma);
				UtilidadTransaccion.getTransaccion().commit();
				return mapping.findForward("paginaPrincipal");
			}
			else if(forma.getEstado().equals("seleccionarTipoId")){
				// Se recarga la sección para validar si el campo
				// número id se debe validar alfanumérico o numérico
				forma.setMsgInformacionComprador(null);
				forma.getDtoCompradorTarjeta().setNumeroIdentificacion("");
				limpiarDatosCompradorTarjeta(forma, false);
				UtilidadTransaccion.getTransaccion().commit();
				return mapping.findForward("seccionInformacionComprador");
			}
			else if(forma.getEstado().equals("cargarComprador")){
				/*
				 * Cargar los datos del comprador en caso de que existan
				 */
				this.accionCargarComprador(forma, usuario);
				UtilidadTransaccion.getTransaccion().commit();
				return mapping.findForward("seccionInformacionComprador");
			}
			else if(forma.getEstado().equals("seleccionarTipoTarjeta")){
				this.accionSeleccionarTipoTarjeta(forma, usuario);

				UtilidadTransaccion.getTransaccion().commit();
				return mapping.findForward("seccionInformacionTipoTarjeta");
			}
			else if(forma.getEstado().equals("seleccionarConvenio")){
				forma.limpiarMensajesInformacionTipoTarjeta();

				this.accionBuscarContratosConvenio(forma, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt());

				this.accionPonerFocoSeleccionConvenio(forma);

				return mapping.findForward("seccionInformacionTipoTarjeta");
			}
			else if(forma.getEstado().equals("seleccionarContrato"))
			{
				forma.limpiarMensajesInformacionTipoTarjeta();
				this.obtenerTarifaServicio(forma, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt());
				return mapping.findForward("seccionInformacionTipoTarjeta");
			}
			else if(forma.getEstado().equals("guardar")){
				ActionErrors errores=new ActionErrors();
				ArrayList<Errores> listaErrores=null;
				
				if(forma.isTurnoAbierto()){
				
					/*
					 * Validar las formas de pago existentes para el recibo
					 */
					errores.add(this.validacionFormasPagos(forma));
				}
				
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					return mapping.findForward("paginaPrincipal"); 
				}
				
				forma.getDtoFacturaVaria().setNombreCentroAtencion(usuario.getCentroAtencion());
				
				if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
				{
					listaErrores=this.accionGuardarPersonal(forma, usuario);
				}
				if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
				{
					listaErrores=this.accionGuardarFamiliar(forma, usuario);
				}
				if(listaErrores!=null)
				{
					int i=0;
					MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
					for(Errores error:listaErrores)
					{
						errores.add("serial_"+i, new ActionMessage("errors.notEspecific", mensajes.getMessage(error.getRecurso())));
						i++;
					}
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
					}
				}
				UtilidadTransaccion.getTransaccion().commit();
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					forma.setEsResumen(false);
					return mapping.findForward("paginaPrincipal");
				}
				forma.setEsResumen(true);
			 	return mapping.findForward("resumen");
			}
			else if(forma.getEstado().equals("cargarBeneficiarios"))
			{
				return mapping.findForward("seccionBeneficiarios");
			}
			else if(forma.getEstado().equals("cambiarSerial"))
			{
				this.limpiarNroTarjetasBeneficiarios(forma);
				return mapping.findForward("seccionBeneficiarios");
			}
			else if(forma.getEstado().equals("cambiarNroTarjeta"))
			{
				this.limpiarSerialesBeneficiarios(forma);
				return mapping.findForward("seccionBeneficiarios");
			}
			else if(forma.getEstado().equals("cambiarFormaPago"))
			{
				int indiceFormaPago=forma.getDtoVentas().getIndiceFormaPago();
				DtoInformacionFormaPago informacionFormaPago=forma.getDtoVentas().getListaInformacionFormasPago().get(indiceFormaPago);
				for(DtoFormaPago formaPago:forma.getListaFormasPago())
				{
					if(informacionFormaPago.getFormaPago().getConsecutivo()==formaPago.getConsecutivo())
					{
						informacionFormaPago.setFormaPago((DtoFormaPago)UtilidadClonacion.clonar(formaPago));
					}
				}
				if(informacionFormaPago.getFormaPago().getTipoDetalle().getCodigo()==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
				{
					if(forma.getDtoVentas().getIndiceFormaPago()==forma.getDtoVentas().getListaInformacionFormasPago().size()-1)
					{
						forma.getDtoVentas().getListaInformacionFormasPago().add(new DtoInformacionFormaPago());
					}
				}
				return mapping.findForward("seccionFormasPago");
			}
			else if(forma.getEstado().equals("guardarCheque"))
			{
				ActionErrors errores=this.accionGuardarCheque(forma);
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					return mapping.findForward("paginaCheques");
				}
				return mapping.findForward("cerrarPopup");
			}
			else if(forma.getEstado().equals("guardarTarjeta"))
			{
				ActionErrors errores=this.accionGuardarTarjeta(forma);
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					return mapping.findForward("paginaTarjetas");
				}
				return mapping.findForward("cerrarPopup");
			}
			else if(forma.getEstado().equals("guardarFormaPago"))
			{
				if(forma.getDtoVentas().getIndiceFormaPago()==forma.getDtoVentas().getListaInformacionFormasPago().size()-1)
				{
					forma.getDtoVentas().getListaInformacionFormasPago().add(new DtoInformacionFormaPago());
				}
				return mapping.findForward("seccionFormasPago");
			}
			else if(forma.getEstado().equals("eliminarFormaPago"))
			{
				int indice=forma.getDtoVentas().getIndiceFormaPago();
				forma.getDtoVentas().getListaInformacionFormasPago().remove(indice);
				return mapping.findForward("seccionFormasPago");
			}
			else if(forma.getEstado().equals("recargarTotalFormaPago"))
			{
				return mapping.findForward("seccionTotalFormasPago");
			}
			else if(forma.getEstado().equals("recargarFormaPago"))
			{
				return mapping.findForward("seccionFormasPago");
			}
			else if(forma.getEstado().equals("ingresoCantidadBonos"))
			{
				DtoDetallePagosBonos detalleBonos=forma.getDtoVentas().getListaInformacionFormasPago().get(forma.getDtoVentas().getIndiceFormaPago()).getDetalleBonos();
				detalleBonos.setSerialesBonos(new ArrayList<DtoBonoSerialValor>());
				for(int i=0; i<detalleBonos.getCantidadBonos(); i++)
				{
					detalleBonos.getSerialesBonos().add(new DtoBonoSerialValor());
				}
				return mapping.findForward("paginaBonos");
			}
			else if(forma.getEstado().equals("guardarBonos"))
			{
				ActionErrors errores=this.accionGuardarBonos(forma);
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					return mapping.findForward("paginaBonos");
				}
				return mapping.findForward("cerrarPopup");
			}
			else if(forma.getEstado().equals("imprimirFacturaVaria"))
			{
				this.accionImprimirFacturaVaria(request, forma, usuario);
		        return mapping.findForward("impresion");
			}
			else if(forma.getEstado().equals("imprimirReciboCaja"))
			{
				this.accionImprimirReciboCaja(request, forma, usuario);
    			return mapping.findForward("impresion");
			}
			else if(forma.getEstado().equals("cargarDatosBeneficiario")){
				this.accionBuscarBeneficiario(forma);
				return mapping.findForward("seccionBeneficiarios");
			}
			else if(forma.getEstado().equals("")){
				//return ComunAction.
			}
		}
		
		return null;
		
	}

	/**
	 * Busca la información de un beneficiario en la BD para ser postulada
	 * y que el usuario no la tenga que digitar
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @param mapping Mapeo de los JSP
	 * @return true en caso de encontrar la persona en el sistema, false de lo contrario
	 */
	private boolean accionBuscarBeneficiario(VentaTarjetaClienteForm forma)
	{
		/*
		 * 1. Se toma el índice
		 */
		int indice=forma.getIndexBeneficiario();
		
		/*
		 * 2. Se toman los datos del beneficiario (se necesita Tipo y Número Identificación)
		 */
		DtoBeneficiarioCliente beneficiario=forma.getDtoVentas().getListaBeneficiarios().get(indice);
		
		DtoPersonas dtoPersona=
			com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.
			crearPersonaServicio().
			buscarPersona(beneficiario.getDtoPersonas());
		
		if(dtoPersona!=null && dtoPersona.getCodigo()>0)
		{
			beneficiario.setDtoPersonas(dtoPersona);
			beneficiario.setPermitirModificar(false);
			beneficiario.getDtoPersonas().setTipoPersona(ConstantesBD.tipoPersonaGeneral);
			return true;
		}
		
		beneficiario.setPermitirModificar(true);
		beneficiario.getDtoPersonas().setPrimerNombre("");		
		beneficiario.getDtoPersonas().setPrimerApellido("");		
		beneficiario.getDtoPersonas().setSegundoNombre("");		
		beneficiario.getDtoPersonas().setSegundoApellido("");
		beneficiario.setParentesco(0);
		
		return false;
		
	}

	/**
	 * Método para imprimir el recibo de caja
	 * @param request
	 * @param forma
	 * @param usuario
	 */
	private void accionImprimirReciboCaja(HttpServletRequest request, VentaTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		//Cambio por anexo 959
		//Formato de impresion del recibo de caja formatos POS y carta
		
//		String nombreRptDesign="";
//		
//		Vector<String> v;
//		String newPathReport = "";
//		
//		String formatoImpresion=ValoresPorDefecto.getTamanioImpresionRC(usuario.getCodigoInstitucionInt());
//		if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCPOS))
//			nombreRptDesign="ReciboCajaPOS.rptdesign";
//		else if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCCarta))
//			nombreRptDesign="ReciboCajaCarta.rptdesign";
//		
//		// ***************** INFORMACIÓN DEL CABEZOTE
//		DesignEngineApi comp;
//		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "tesoreria/", nombreRptDesign);
//
//		// Logo
//		if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCCarta))
//			comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
//
//		// Nombre Institución, titulo 
//		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");
//
//			
//		v = new Vector<String>();
//		
//		String digitoVerificacion = !UtilidadTexto.isEmpty(ins.getDigitoVerificacion()) ? " - " + ins.getDigitoVerificacion() : "";
//	
//		v.add(ins.getRazonSocial()+ " NIT " + ins.getNit() + digitoVerificacion);
//		
//		//v.add("\nRECIBO DE CAJA  ");
//		
//		comp.insertLabelInGridOfMasterPage(0, 1, v);
//
//		// Fecha hora de proceso y usuario
//		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "+ UtilidadFecha.getFechaActual() + " Hora:"+ UtilidadFecha.getHoraActual());
//		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario:"+ usuario.getLoginUsuario());
//		// ****************** FIN INFORMACIÓN DEL CABEZOTE"
//		
//		// ***************** NUEVAS CONSULTAS DEL REPORTE
//		String newquery = "";
//		
//		
//		//Obtengo la neuva consulta de conceptos rc
//		comp.obtenerComponentesDataSet("conceptosRC");
//		newquery=ConsultasBirt.impresionConceptosReciboCaja(Utilidades.getUsuarioBasicoSesion(request.getSession()).getCodigoInstitucionInt(),forma.getDtoVentas().getNumeroReciboCaja()+"");
//		comp.modificarQueryDataSet(newquery);
//		
//		//Obtengo la nueva consulta de pagos RC
//		comp.obtenerComponentesDataSet("pagosRC");
//		newquery=ConsultasBirt.impresionTotalesReciboCaja(Utilidades.getUsuarioBasicoSesion(request.getSession()).getCodigoInstitucionInt(),forma.getDtoVentas().getNumeroReciboCaja()+"");
//		comp.modificarQueryDataSet(newquery);
//  
//		comp.lowerAliasDataSet(); 			
//		newPathReport = comp.saveReport1(false);
//		comp.updateJDBCParameters(newPathReport);
//		
//		
//		//Consulto el total para luego convertirlo en letras
//		ConsultaRecibosCaja mundoConsultaRecibos=new ConsultaRecibosCaja();
//		HashMap<String, Object> mapaTotalPagos=mundoConsultaRecibos.consultarTotalesPagos(Utilidades.getUsuarioBasicoSesion(request.getSession()).getCodigoInstitucionInt(),forma.getDtoVentas().getNumeroReciboCaja()+"");
//		double totalPagos=0;
//		
//		for(int i=0;i<Utilidades.convertirAEntero(mapaTotalPagos.get(("numRegistros"))+"");i++)
//			totalPagos+=Utilidades.convertirADouble(mapaTotalPagos.get("valor_"+i)+"");
//		
//		DtoTiposMoneda tipoMoneda=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuario.getCodigoInstitucionInt());
//		String monedaLetras=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(totalPagos+"" ).replaceAll(",", ""),tipoMoneda.getDescripcion(),"centavos MCTE");
//		
//		//Envio los elementos que se necesitan mostrar por parametro
//		newPathReport += 	"&recibocaja="+forma.getDtoVentas().getNumeroReciboCaja()+
//							"&fechaemision="+UtilidadFecha.getFechaActual()+
//							"&horaemision="+UtilidadFecha.getHoraActual()+
//							"&caja="+usuario.getDescripcionCaja()+
//							"&estado="+(Utilidades.obtenerEstadoReciboCaja(forma.getDtoVentas().getNumeroReciboCaja()+"",usuario.getCodigoInstitucionInt())).split(ConstantesBD.separadorSplit)[1]+
//							"&recibidode="+Utilidades.reciboCajaRecibidoDe(usuario.getCodigoInstitucionInt(),forma.getDtoVentas().getNumeroReciboCaja()+"")+
//							"&tipoid="+forma.getDtoCompradorTarjeta().getTipoIdentificacion()+
//							"&nroid="+forma.getDtoCompradorTarjeta().getNumeroIdentificacion()+
//							"&observaciones="+forma.getDtoVentas().getObservaciones()+
//							"&usuariogenero="+usuario.getNombreUsuario()+
//							"&totalletras="+monedaLetras;
		
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String numReciboCaja = forma.getDtoVentas().getNumeroReciboCaja()+"";
		String identificacionPaciente = forma.getDtoCompradorTarjeta().getNumeroIdentificacion();
		String tipoIdentificacion = forma.getDtoCompradorTarjeta().getTipoIdentificacion();
		String observacionesImprimir = forma.getDtoVentas().getObservaciones();
		
		IRecibosCajaServicio recibosCajaServicio  = TesoreriaFabricaServicio.crearRecibosCajaServicio();

		HashMap<String, String> parametros = new HashMap<String, String>();
		
		parametros.put("numReciboCaja", Utilidades.obtenerCodigoReciboCaja(numReciboCaja,usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroAtencion()));
		parametros.put("consecutivorc", numReciboCaja);
		parametros.put("identificacionPaciente", identificacionPaciente);
		parametros.put("tipoIdentificacion", tipoIdentificacion);
		parametros.put("observacionesImprimir", observacionesImprimir);
		parametros.put("usuarioElabora", usuario.getNombreUsuario());
		parametros.put("funcionalidadOrigen", "VentaTarjetaCliente");
		
		String newPathReport = recibosCajaServicio.imprimirReciboCaja(usuario, institucion, parametros);
		
		if (!newPathReport.equals(""))
		{
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}
	}

	/**
	 * Acción para imprimir la factura varia
	 * @param request
	 * @param forma
	 * @param usuario
	 */
	private void accionImprimirFacturaVaria(HttpServletRequest request, VentaTarjetaClienteForm forma, UsuarioBasico usuario) {
		
//		Connection con=UtilidadBD.abrirConexion();
//		String nombreRptDesign = "ConsultaImpresionFacturasVariasPOS.rptdesign";
//		
//		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
//		
//		//Informacion del Cabezote
//		DesignEngineApi comp;
//		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
//		
//		int cantidadFilas=3;
//
//		Vector v = new Vector();
//		if(forma.getDtoFacturaVaria().getEstadoFactura().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
//		{
//			v.add(forma.getDtoFacturaVaria().getEstadoFactura());
//			cantidadFilas=4;
//		}
//		String tipoId=institucion.getTipoIdentificacion();
//		if(institucion.getTipoIdentificacion().equals("NI"))
//		{
//			tipoId=institucion.getDescripcionTipoIdentificacion();
//		}
//		v.add(institucion.getRazonSocial());
//		v.add(tipoId+" "+institucion.getNit());
//		v.add("Sucursal: "+forma.getDtoFacturaVaria().getNombreCentroAtencion());
//
//		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, cantidadFilas, "titulo");
//		
//		comp.insertLabelInGridOfMasterPage(0, 1, v);
//
//		
//		boolean manejaMultiInstitucion= UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
//		int codigoInstitucion=0;
//		
//		if(manejaMultiInstitucion){
//			codigoInstitucion= Utilidades.convertirAEntero(institucion.getCodigo());
//		}
//		else{
//			codigoInstitucion= usuario.getCodigoInstitucionInt();
//		}
//		
//		comp.obtenerComponentesDataSet("ConsultaFacturasVarias");
//		String newquery=ConsultasBirt.impresionFacturaVaria(codigoInstitucion, forma.getDtoFacturaVaria().getConsecutivo().intValue(), manejaMultiInstitucion);
//		comp.modificarQueryDataSet(newquery);
//		
//		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
//		comp.lowerAliasDataSet();
//		String newPathReport = comp.saveReport1(false);
//		comp.updateJDBCParameters(newPathReport);
//		// se mandan los parámetros al reporte
//		newPathReport += "&institucion="+codigoInstitucion+"&consecutivoFactura="+forma.getDtoFacturaVaria().getConsecutivo();
//		
//		if(!newPathReport.equals(""))
//		{
//			request.setAttribute("isOpenReport", "true");
//			request.setAttribute("newPathReport", newPathReport);
//		}
//		UtilidadBD.closeConnection(con);
		
		
		BigDecimal consecutivoFactura  = new BigDecimal(forma.getDtoFacturaVaria().getConsecutivo());
		
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		GenModFacturasVarias genModFacturasVariasMundo = new GenModFacturasVarias();
	
		String newPathReport = genModFacturasVariasMundo.imprimirFacturaVaria(usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(institucion.getCodigo()),
																				consecutivoFactura.intValue(), usuario);
		
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
	}

	private ActionErrors accionGuardarBonos(VentaTarjetaClienteForm forma) {
		return forma.validarBonos();
	}

	/**
	 * Validar guardar tarjetas (Débito y crédito)
	 * @param forma Formulario
	 * @return Errores presentados
	 */
	private ActionErrors accionGuardarTarjeta(VentaTarjetaClienteForm forma) {
		return forma.validarTarjetas();
	}

	/**
	 * Validar las formas de pago, para que concuerden con
	 * el total de la venta
	 * @param forma
	 * @return Lista de errores
	 */
	private ActionMessages validacionFormasPagos(VentaTarjetaClienteForm forma) {
		
		ActionMessages errores=new ActionMessages();
		double valor=0;
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
		int tamanio=forma.getDtoVentas().getListaInformacionFormasPago().size();
		for(int i=0; i<tamanio-1; i++)
		{
			DtoInformacionFormaPago formaPago=forma.getDtoVentas().getListaInformacionFormasPago().get(i);
			if(formaPago.getValor()==0)
			{
				errores.add("totalFormasPago", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.valorFormaPago", i+1)));
			}
			else if(formaPago.getFormaPago().getTipoDetalle().getCodigo()==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
			{
				formaPago.setValido(true);
			
			}else if(!formaPago.isValido())
			{
				errores.add("totalFormasPago", new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.incompleta", i+1)));
			
			}
			valor+=formaPago.getValor();
		}
		
		/*
		 * Se revisa si existen formas de pago Bonos y se realizan las respectivas validaciones.
		 */
		errores.add(forma.validarTodasFormasPagoBono());
		
		if(valor!=forma.getDtoVentas().getValorTotalTarjetas())
		{
			errores.add("totalFormasPago", new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.valorDiferenteTotalTarjeta")));
		}
		
		return errores;
	}

	/**
	 * Validar guardar cheques
	 * @param forma Formulario
	 * @return Errores presentados
	 */
	private ActionErrors accionGuardarCheque(VentaTarjetaClienteForm forma) {
		return forma.validarCheques();
	}

	/**
	 * Control al seleccionar el tipo de tarjeta
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @param usuario Usuario en sesión
	 */
	private void accionSeleccionarTipoTarjeta(VentaTarjetaClienteForm forma, UsuarioBasico usuario) throws IPSException {
		UtilidadTransaccion.getTransaccion().begin();
		/*
		 * Se limpia la información de la sección tipo tarjeta
		 */
		forma.limpiarInformacionTipoTarjeta();

		// Se validan los campos que se deben mostrar en la sección
		this.validarCamposAMostrarSeccionTipoTarjeta(forma, usuario);
		if(forma.getMsgInformacionTipoTarjeta()==null)
		{
			/*
			 * Se valida el consecutivo serial para el tipo
			 * de tarjeta seleccionado 
			 */
			this.validarConsecutivoSerial(forma);
		}

		if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
		{
			/*
			 * Se valida si para el tipo y clase de venta seleccionado se
			 * tiene parametrizado el servicio en la paramétrica
			 * Tipos de Tarjeta Cliente
			 */
			if(forma.getMsgInformacionTipoTarjeta()==null)
			{
				this.validacionServicioPersonal(forma);
			}
			if(forma.getMsgInformacionTipoTarjeta()==null)
			{
				/*
				 * Crear los beneficiarios
				 */
				this.accionPostularCantidadPersonal(forma);
				//this.crearBeneficiariosPersonal(forma);
			}
		}
		if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
		{
			this.validacionServicioFamiliar(forma);
			if(forma.getMsgInformacionTipoTarjeta()==null)
			{
				/*
				 * Crear los beneficiarios
				 */
				this.accionPostularCantidadFamiliar(forma);
				this.crearBeneficiariosFamiliar(forma);
			}
		}

		if(forma.getMsgInformacionTipoTarjeta()==null)
		{
			/*
			 * Método para cargar los convenios
			 */
			this.accionCargarConvenio(forma, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt() );
		}

		/*
		 * Se carga la tarifa del servicio
		 */
		if(forma.getMsgInformacionTipoTarjeta()==null)
		{
			this.obtenerTarifaServicio(forma, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt());
		}
		// Organizar foco campos
		
		for(DtoTarjetaCliente tarjeta:forma.getListaTarjetaCliente())
		{
			if(tarjeta.getCodigoPk()==forma.getDtoVentas().getTipoTarjeta())
			{
				forma.getDtoVentas().setConvenioTipoTarjeta(tarjeta.getConvenio().getCodigo());
				DtoConvenio convenio=new DtoConvenio();
				convenio.setCodigo(tarjeta.getConvenio().getCodigo());
				ArrayList<DtoContrato> listaContratosTarjeta=ConveniosFabricaMundo.crearConveniosMundo().listarContratosConvenio(convenio);
				if(listaContratosTarjeta!=null && listaContratosTarjeta.size()>0)
				{
				}				
			}
		}
		
		this.accionPonerFocoSeleccionTipoTarjeta(forma);
	}

	/**
	 * Limpia los seriales de los beneficiarios
	 * @param forma
	 */
	private void limpiarSerialesBeneficiarios(VentaTarjetaClienteForm forma) {
		for(DtoBeneficiarioCliente beneficiario:forma.getDtoVentas().getListaBeneficiarios())
		{
			beneficiario.setSerial("");
		}
	}

	/**
	 * Limpiar los numeros de tarjetas de los beneficiarios
	 * @param forma
	 */
	private void limpiarNroTarjetasBeneficiarios(VentaTarjetaClienteForm forma) {
		for(DtoBeneficiarioCliente beneficiario:forma.getDtoVentas().getListaBeneficiarios())
		{
			beneficiario.setNumTarjeta("");
		}
	}

	/**
	 * Cargar los datos del comprador de la tarjeta cliente
	 * en caso de que existan en el sistema
	 * @param forma
	 * @param usuario
	 */
	private void accionCargarComprador(VentaTarjetaClienteForm forma, UsuarioBasico usuario)
	{
		/*
		 * Se limpia el campo de los errores
		 */
		forma.setMsgInformacionComprador(null);
		
		if(!UtilidadTexto.isEmpty(forma.getDtoCompradorTarjeta().getTipoIdentificacion()) && !UtilidadTexto.isEmpty(forma.getDtoCompradorTarjeta().getNumeroIdentificacion()))
		{
			/*
			 * Método para cargar el comprador 
			 */
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarPacienteParaVentaTarjeta(usuario.getCodigoInstitucionInt())))
			{
				this.accionCargarPaciente(forma);
			}
			else
			{
				this.accionCargarPersona(forma);
			}

			/*
			 * Se valida si el comprador tiene o no tarjeta
			 */
			boolean resultadoValidacionComprador=false;
			if(forma.getMsgInformacionComprador()==null || forma.getMsgInformacionComprador().isEmpty())
			{
				resultadoValidacionComprador=this.validacionCompradorConTarjeta(forma);
			}
			DtoBeneficiarioCliente dtoBeneficiarioCliente=new DtoBeneficiarioCliente();
			dtoBeneficiarioCliente.setConsecutivo(1);
			dtoBeneficiarioCliente.setIndicativoPrincipal(ConstantesBD.acronimoSi);
			if(resultadoValidacionComprador)
			{
				dtoBeneficiarioCliente.setEsComprador(true);
				dtoBeneficiarioCliente.setTieneTarjetaPrevia(false);
				dtoBeneficiarioCliente.setDtoPersonas(forma.getDtoCompradorTarjeta());
			}
			forma.getDtoVentas().setListaBeneficiarios(new ArrayList<DtoBeneficiarioCliente>());
			forma.getDtoVentas().getListaBeneficiarios().add(dtoBeneficiarioCliente);
		}
		else
		{
			this.limpiarDatosCompradorTarjeta(forma, false);
		}
	}

	/**
	 * Obtener la tarifa de los servicios
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @param codigoInstitucion 
	 * @param codigoCentroCosto 
	 */
	private void obtenerTarifaServicio(VentaTarjetaClienteForm forma, int codigoCentroCosto, int codigoInstitucion) throws IPSException {
		forma.getDtoVentas().setValorUnitarioTarjeta(ConstantesBD.codigoNuncaValidoDouble);
		forma.getDtoVentas().setValorTotalTarjetas(ConstantesBD.codigoNuncaValidoDouble);
		if(forma.getDtoVentas().getContratoTarjeta().getCodigo()>0)
		{
			
			//int esquemaTarifario=TarjetaCliente.obtenerEsquemaTarifarioTarjetaCliente(forma.getDtoVentas().getContratoTarjeta().getCodigo());
			Connection con=UtilidadBD.abrirConexion();
			
			//int tarifarioOficial=EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, esquemaTarifario);
			
			//InfoTarifaVigente tarifa=Cargos.obtenerTarifaBaseServicio(con, tarifarioOficial, forma.getDtoVentas().getCodigoServicio(), esquemaTarifario, UtilidadFecha.getFechaActual());
			
			//  convenio =   forma.getDtoVentas().getConvenioTipoTarjeta() ?
			
			
			String utilizaProgramas = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion);
			boolean utilizaProgramasFlag = false;
			
			if(utilizaProgramas.equals(ConstantesBD.acronimoSi)){
				
				utilizaProgramasFlag = true;
			}
			
	
			//PacientesMundo pacientemMundo = new PacientesMundo();
			
			//DtoPaciente persona = pacientemMundo.cargarPacienteCompleto(forma.getDtoCompradorTarjeta().getNumeroIdentificacion(), forma.getDtoCompradorTarjeta().getTipoIdentificacion());
			
			InfoTarifaServicioPresupuesto infoTarifa = CargosOdon.obtenerTarifaUnitariaXServicio(forma.getDtoVentas().getCodigoServicio(), forma.getDtoVentas().getConvenioTipoTarjeta(), 
					forma.getDtoVentas().getContratoTarjeta().getCodigo(), UtilidadFecha.getFechaActual(), codigoInstitucion, new BigDecimal(-1), utilizaProgramasFlag, codigoCentroCosto);
			
			UtilidadBD.closeConnection(con);
			
			if(infoTarifa!=null && infoTarifa.getValorTarifaTotalConDctos()!=null)
			{
				forma.getDtoVentas().setValorUnitarioTarjeta(infoTarifa.getValorTarifaTotalConDctos().doubleValue());
				
				if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
				{
					forma.getDtoVentas().setValorTotalTarjetas(infoTarifa.getValorTarifaTotalConDctos().doubleValue()/* *forma.getDtoVentas().getCantidad()*/);
				}
				if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
				{
					forma.getDtoVentas().setValorTotalTarjetas(infoTarifa.getValorTarifaTotalConDctos().doubleValue()/* *forma.getDtoVentas().getCantidad()*/);
				}
				if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa))
				{
					forma.getDtoVentas().setValorTotalTarjetas(infoTarifa.getValorTarifaTotalConDctos().doubleValue()*forma.getDtoVentas().getCantidad());
				}
			}
			else
			{
				forma.setMsgInformacionTarifa(
							new Errores("VentaTarjetaClienteForm.error.noTarifa", 
							new String[]{(String)ValoresPorDefecto.getIntegridadDominio(forma.getDtoVentas().getTipoVenta())}, 
							Errores.Tipo.ERROR)
				);
			}
		}
	}

	/**
	 * Guardar venta tarjeta cliente personal
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @author Juan David Ramírez
	 * @param usuario Usuario en sesión
	 * @since 11 Septiembre 2010
	 */
	private ArrayList<Errores> accionGuardarPersonal(VentaTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		if(!forma.existeError())
		{
			IVentaTarjetaClienteServicio ventaTarjetaServicio=VentaTarjetaServicioFabrica.crearVentaTarjetaClienteServicio();
	
			forma.getDtoFacturaVaria().setCentroAtencion(usuario.getCodigoCentroAtencion());
			forma.getDtoFacturaVaria().setCentroCosto(usuario.getCodigoCentroCosto());
			forma.getDtoFacturaVaria().setInstitucion(usuario.getCodigoInstitucionInt());
			forma.getDtoVentas().setInstitucion(usuario.getCodigoInstitucionInt());
			forma.getDtoVentas().setUsuarioModifica(usuario.getLoginUsuario());
			
			return ventaTarjetaServicio.guardarVenta(forma.getDtoVentas(), forma.getDtoFacturaVaria(), forma.getDtoCompradorTarjeta());
		}
		else
		{
			ArrayList<Errores> errores=new ArrayList<Errores>();
			errores.add(new Errores("VentaTarjetaClienteForm.error.verificarInformacionComprador", Tipo.ERROR));
			return errores;
		}
	}

	/**
	 * Guardar venta tarjeta cliente familiar
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @author Juan David Ramírez
	 * @param usuario Usuario en sesión
	 * @since 11 Septiembre 2010
	 */
	private ArrayList<Errores> accionGuardarFamiliar(VentaTarjetaClienteForm forma, UsuarioBasico usuario) {
		IVentaTarjetaClienteServicio ventaTarjetaServicio=VentaTarjetaServicioFabrica.crearVentaTarjetaClienteServicio();

		forma.getDtoFacturaVaria().setCentroAtencion(usuario.getCodigoCentroAtencion());
		forma.getDtoFacturaVaria().setCentroCosto(usuario.getCodigoCentroCosto());
		forma.getDtoFacturaVaria().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoVentas().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoVentas().setUsuarioModifica(usuario.getLoginUsuario());
		
		return ventaTarjetaServicio.guardarVenta(forma.getDtoVentas(), forma.getDtoFacturaVaria(), forma.getDtoCompradorTarjeta());
	}

	/**
	 * Encargado de poner el foco en el campo específico
	 * según la selección del tipo de tarjeta
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 */
	private void accionPonerFocoSeleccionTipoTarjeta(VentaTarjetaClienteForm forma)
	{
		/*
		 * Si hay errores en la selección se deja el foco en el campo tipo tarjeta,
		 * de lo contrario se pasa a convenio
		 */
		if(forma.getMsgInformacionTipoTarjeta()==null)
		{
			forma.setFocusSeccionTipoTarjeta(VentaTarjetaClienteIdCampos.CONVENIO.getStyleId());
		}
		else
		{
			forma.getDtoVentas().setTipoTarjeta(0);
			forma.setFocusSeccionTipoTarjeta(VentaTarjetaClienteIdCampos.TIPO_TARJETA.getStyleId());
		}
	}

	/**
	 * Método para buscar los contratos del convenio seleccionado
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 */
	private void accionBuscarContratosConvenio(VentaTarjetaClienteForm forma, int codigoCentroCosto, int codigoInstitucion) throws IPSException {
		IConvenioServicio convenioServicio=ConveniosFabricaServicio.crearConvenioServicio();
		convenioServicio.listarContratosConvenio(forma.getConvenioTarjeta());
		
		forma.getDtoVentas().setValorUnitarioTarjeta(ConstantesBD.codigoNuncaValidoDouble);
		forma.getDtoVentas().setValorTotalTarjetas(ConstantesBD.codigoNuncaValidoDouble);
		
		// Si solamente hay un contrato, se asigna automáticamente
		if(forma.getConvenioTarjeta().getNumeroContratos()==1)
		{
			forma.getDtoVentas().setContratoTarjeta(forma.getConvenioTarjeta().getListContrato().get(0));
			this.obtenerTarifaServicio(forma, codigoCentroCosto, codigoInstitucion);
		}
	}


	/**
	 * Encargado de poner el foco en el campo específico
	 * según la cantidad de contratos vigentes existentes
	 * para el convenio
	 * @param forma
	 */
	private void accionPonerFocoSeleccionConvenio(VentaTarjetaClienteForm forma)
	{
		if(forma.getConvenioTarjeta().getNumeroContratos()==0)
		{
			forma.setFocusSeccionTipoTarjeta(VentaTarjetaClienteIdCampos.CONVENIO.getStyleId());
		}
		else if(forma.getConvenioTarjeta().getNumeroContratos()==1)
		{
			if(forma.isMostrarCampoSerial())
			{
				forma.setFocusSeccionTipoTarjeta(VentaTarjetaClienteIdCampos.SERIAL.getStyleId());
			}
			else if(forma.isMostrarCampoNroTarjeta())
			{
				forma.setFocusSeccionTipoTarjeta(VentaTarjetaClienteIdCampos.NUMERO_TARJETA.getStyleId());
			}
		}
		else
		{
			forma.setFocusSeccionTipoTarjeta(VentaTarjetaClienteIdCampos.CONTRATO.getStyleId());
		}
	}

	/**
	 * Validar si para el tipo seleccionado y clase de venta familiar se
	 * tiene parametrizado el servicio en la paramétrica
	 * Tipos de Tarjeta Cliente
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @author Juan David Ramírez
	 */
	private void validacionServicioPersonal(VentaTarjetaClienteForm forma)
	{
		if(forma.getDtoVentas().getTipoTarjeta()>0)
		{
			ITipoTarjetaClienteServicio tipoTarjetaClienteServicio=AdministracionFabricaServicio.crearTipoTarjetaClienteServicio();
			DtoTipoTarjetaCliente tipoTarjeta=tipoTarjetaClienteServicio.consultarTipoTarjetaCliente(forma.getDtoVentas().getTipoTarjeta(), forma.getDtoVentas().getTipoVenta());
			
			if(tipoTarjeta.getServicioPersonal()==null || tipoTarjeta.getServicioPersonal()==0)
			{
				forma.setColspanSeccionSerialesTarjeta(forma.getColspan());
				forma.setMsgInformacionTipoTarjeta(new Errores("VentaTarjetaClienteForm.error.noServicio", new String[]{(String)ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona)}));
			}
			else
			{
				forma.getDtoVentas().setCodigoServicio(tipoTarjeta.getServicioPersonal());
			}
		}
		else
		{
			forma.setMsgInformacionTipoTarjeta(null);
		}
	}

	/**
	 * Validar si para el tipo seleccionado y clase de venta familiar se
	 * tiene parametrizado el servicio en la paramétrica
	 * Tipos de Tarjeta Cliente
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @author Juan David Ramírez
	 */
	private void validacionServicioFamiliar(VentaTarjetaClienteForm forma)
	{
		ITipoTarjetaClienteServicio tipoTarjetaClienteServicio=AdministracionFabricaServicio.crearTipoTarjetaClienteServicio();
		DtoTipoTarjetaCliente tipoTarjeta=tipoTarjetaClienteServicio.consultarTipoTarjetaCliente(forma.getDtoVentas().getTipoTarjeta(), forma.getDtoVentas().getTipoVenta());
		
		if(tipoTarjeta==null || tipoTarjeta.getServicioFamiliar()==null || tipoTarjeta.getServicioFamiliar()==0)
		{
			forma.setColspanSeccionSerialesTarjeta(forma.getColspan());
			forma.setMsgInformacionTipoTarjeta(new Errores("VentaTarjetaClienteForm.error.noServicio", new String[]{(String)ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar)}));
		}
		else
		{
			forma.getDtoVentas().setCodigoServicio(tipoTarjeta.getServicioFamiliar());
		}
	}


	/**
	 * Validar los campos que se deben mostrar en la sección
	 * tipo de tarjeta
	 * @author Juan David Ramírez
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 */
	private void validarCamposAMostrarSeccionTipoTarjeta(VentaTarjetaClienteForm forma, UsuarioBasico usuario)
	{
		// Validaciones campo Serial
		IEmisionTarjetaClienteServicio emi=AdministracionFabricaServicio.crearEmisionTarjetaClienteServicio();
		ArrayList<DtoEmisionTarjetaCliente> listaEmisionTarjeta=emi.consultarEmisionTarjeta((int)forma.getDtoVentas().getTipoTarjeta(), usuario.getCodigoCentroAtencion());
		if(listaEmisionTarjeta!=null && !listaEmisionTarjeta.isEmpty())
		{
			forma.setMostrarCampoSerial(true);
		}
		else
		{
			forma.setMostrarCampoSerial(false);
		}
		// Validaciones campo Nro Tarjeta
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaVentaTarjetaClienteOdontosinEmision(usuario.getCodigoInstitucionInt())))
		{
			forma.setMostrarCampoNroTarjeta(true);
		}
		else
		{
			forma.setMostrarCampoNroTarjeta(false);
		}
		
		//Se organizan los colspan de la sub sección
		if(forma.isMostrarCampoSerial() && forma.isMostrarCampoNroTarjeta())
		{
			forma.setColspanSeccionSerialesTarjeta(forma.getColspan()/2);
		}
		if(forma.isMostrarCampoSerial() ^ forma.isMostrarCampoNroTarjeta())
		{
			forma.setColspanSeccionSerialesTarjeta(forma.getColspan());
		}
		else if(!forma.isMostrarCampoSerial() && !forma.isMostrarCampoNroTarjeta())
		{
			// En caso de no mostrar ningún campo, se muestra mensaje de error
			forma.setColspanSeccionSerialesTarjeta(forma.getColspan());
			forma.setMsgInformacionTipoTarjeta(new Errores("VentaTarjetaClienteForm.error.noSerialNoTarjeta"));
		}
	}


	/**
	 * Valida que para el tipo de venta seleccionado
	 * @author Juan David Ramírez
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 */
	private void validarConsecutivoSerial(VentaTarjetaClienteForm forma)
	{
		double codigoTipoTarjeta=forma.getDtoVentas().getTipoTarjeta();
		for(DtoTarjetaCliente tipoTarjeta:forma.getListaTarjetaCliente())
		{
			if(tipoTarjeta.getCodigoPk()==codigoTipoTarjeta)
			{
				if(tipoTarjeta.getConsecutivoSerial()==ConstantesBD.codigoNuncaValido)
				{
					forma.setMsgInformacionTipoTarjeta(new Errores("VentaTarjetaClienteForm.error.noSerial"));
					break;
				}
			}
		}
	}

	/**
	 * Validaciones generales donde se verifica si el comprador tiene o no tarjeta
	 * @author Juan David Ramírez
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @since 6 Septiembre 2010
	 */
	private boolean validacionCompradorConTarjeta(VentaTarjetaClienteForm forma)
	{
		IBeneficiarioServicio beneficiarioServicio=VentaTarjetaServicioFabrica.crearBeneficiarioServicio();
		if(forma.getDtoCompradorTarjeta().getCodigo()>0)
		{
			BeneficiarioTarjetaCliente beneficiario=beneficiarioServicio.obtenerBeneficiarioPersona(forma.getDtoCompradorTarjeta().getCodigo(), false);
			if(beneficiario!=null)
			{
				if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
				{
					forma.setMsgInformacionComprador(new Errores("VentaTarjetaClienteForm.error.yaTarjeta", new String[]{(String)ValoresPorDefecto.getIntegridadDominio(forma.getDtoVentas().getTipoVenta())}, Errores.Tipo.ERROR));
					return false;
				}
				if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
				{
					forma.setMsgInformacionComprador(new Errores("VentaTarjetaClienteForm.error.yaTarjeta", new String[]{(String)ValoresPorDefecto.getIntegridadDominio(forma.getDtoVentas().getTipoVenta())}, Errores.Tipo.INFORMATIVO));
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Busca la persona en el sistema basado en el tipo y número de identificación
	 * @author Juan David Ramírez
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @since 4 Septiembre 2010
	 */
	private void accionCargarPersona(VentaTarjetaClienteForm forma)
	{
		/*
		 * 1. Buscar persona
		 */
		Paciente paciente = new Paciente();
		paciente.setTipoIdentificacion(forma.getDtoCompradorTarjeta().getTipoIdentificacion());
		paciente.setNumeroIdentificacion(forma.getDtoCompradorTarjeta().getNumeroIdentificacion());
	
		ArrayList<Paciente> listaPaciente = new ArrayList<Paciente>();
		// OJO, Este método carga una persona sin importar si es paciente o no
		listaPaciente=UtilidadesManejoPaciente.obtenerDatosPaciente(paciente);
		
		
		if(listaPaciente!=null && listaPaciente.size()>0)
		{
			Paciente pac=listaPaciente.get(0);
			forma.getDtoCompradorTarjeta().setPrimerNombre(pac.getPrimerNombrePersona());
			forma.getDtoCompradorTarjeta().setSegundoNombre(pac.getSegundoNombrePersona());
			forma.getDtoCompradorTarjeta().setPrimerApellido(pac.getPrimerApellidoPersona());
			forma.getDtoCompradorTarjeta().setSegundoApellido(pac.getSegundoApellidoPersona());
			forma.getDtoCompradorTarjeta().setSegundoApellido(pac.getSegundoApellidoPersona());
			forma.getDtoCompradorTarjeta().setCodigo(pac.getCodigoPersona());
			forma.setPermitirModificacionInformacionPaciente(false);
			forma.getDtoCompradorTarjeta().setTipoPersona(ConstantesBD.tipoPersonaGeneral);
			return;
		}

		/*
		 * 2. Buscar Deudor 
		 */

/*
		DtoDeudor dtoDeudor  = new DtoDeudor();
		dtoDeudor.setTipoIdentificacion(forma.getDtoCompradorTarjeta().getTipoIdentificacion());
		dtoDeudor.setNumeroIdentificacion(forma.getDtoCompradorTarjeta().getNumeroIdentificacion());
		dtoDeudor.setTipoDeudor(ConstantesIntegridadDominio.acronimoOtro);
*/
		
		/*
		 * 3. Buscar la información del Deudor
		 */
/*
		ArrayList<DtoDeudor> listaDeudores=UtilidadesFacturacion.obtenerDeudores(dtoDeudor); 
		
		if(listaDeudores!=null && listaDeudores.size()>0)
		{
			DtoDeudor deudor=listaDeudores.get(0);
			forma.getDtoCompradorTarjeta().setPrimerNombre(deudor.getPrimerNombre());
			forma.getDtoCompradorTarjeta().setSegundoNombre(deudor.getSegundoNombre());
			forma.getDtoCompradorTarjeta().setPrimerApellido(deudor.getPrimerApellido());
			forma.getDtoCompradorTarjeta().setSegundoApellido(deudor.getSegundoApellido());
			forma.setPermitirModificacionInformacionPaciente(false);
			forma.getDtoCompradorTarjeta().setTipoPersona(ConstantesBD.tipoPersonaGeneral);
			return;
		}
*/

		/*
		 * 4. Buscar Tercero
		 */
/*		InfoDeudorTerceroDto dto = new InfoDeudorTerceroDto();
		dto.getDtoTercero().setNumeroIdentificacion(forma.getDtoCompradorTarjeta().getTipoIdentificacion());
		dto.getDtoTercero().setNumeroIdentificacion(forma.getDtoCompradorTarjeta().getNumeroIdentificacion());
		dto.getDtoTercero().getDtoTipoTercero().setCodigo(ConstantesBD.codigoTipoTerceroPersonaNatural);
		ArrayList<InfoDeudorTerceroDto> listaTercero=Tercero.cargarTerceroArray(dto);
		
		if(listaTercero!=null && listaTercero.size()>0)
		{
			InfoDeudorTerceroDto tercero=listaTercero.get(0);
			forma.getDtoCompradorTarjeta().setPrimerNombre(deudor.getPrimerNombre());
			forma.getDtoCompradorTarjeta().setSegundoNombre(deudor.getSegundoNombre());
			forma.getDtoCompradorTarjeta().setPrimerApellido(deudor.getPrimerApellido());
			forma.getDtoCompradorTarjeta().setSegundoApellido(deudor.getSegundoApellido());
			forma.setPermitirModificacionInformacionPaciente(false);
			return;
		}
*/
		limpiarDatosCompradorTarjeta(forma, true);
	}

	/**
	 * Método que carga los datos Básicos para mostrar en la interfaz gráfica
	 * Acción cargar Datos Básicos
	 * @author Edgar Carvajal Ruiz
	 * @param mapping Objeto utilizado para la redirección de JSP's
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @param usuario Usuario en sesión
	 * @param request request para almacenar los errores
	 * @return Página Forward de página venta tarjeta cliente
	 */
	private ActionForward accionCargarDatosBasicos(ActionMapping mapping,
													VentaTarjetaClienteForm forma, 
													UsuarioBasico usuario, HttpServletRequest request) {
		/*
		 * Validaciones para verificar si se debe y se puede generar
		 * recibo de caja automático
		 */
		forma.setMensajeInformativoIngreso(this.validacionesIngreso(usuario, forma));
		
		/*
		 * Cargar las clases de venta según el rol del usuario
		 */
		Errores errores=this.accionCargarClasesDeVenta(forma, usuario);
		
		if(errores!=null)
		{
			request.setAttribute("codigoDescripcionError", errores.getRecurso());
			return mapping.findForward("paginaError");
		}

		/*
		 * Acción cargar Tipos de Tarjeta  
		 */
		this.accionCargarTipoTarjeta(forma, usuario);
		
		/*
		 * Acción cargar tipo de Identificación 
		 */
		this.accionCargarTipoIdentificacion(forma, usuario);
		
		/*
		 * Acción cargar usuarios 
		 */
		this.accionCargarUsuario(forma, usuario);
		
		/*
		 * Cargar parentesco   
		 */
		this.accionCargarParentescos(forma);
		/*------------ Fin carga Datos generales -----------------*/

		/*
		 * Cargar conceptos de facturas varias
		 */
		this.accionCargarConceptosFacturasVarias(forma);
		
		/*
		 * Cargar la forma de pago
		 */
		this.accionCargarFormasPago(forma, usuario.getCodigoInstitucionInt());
		
		/*
		 * Cargar las entidades financieras
		 */
		this.accionCargarEntidadesFinancieras(forma);
		
		/*
		 * Listar las ciudades existentes
		 */
		this.accionListarCiudades(forma);
		
		/*
		 * Crea la primera forma de pago existente en el sistema
		 */
		this.crearPrimeraFormaDePago(forma);
		
		/*
		 * Crear las tarjetas financieras
		 */
		this.listarTarjetasFinancieras(forma);
		
		/*
		 * Despliega la página venta tarjeta según tipo seleccionado
		 */
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Listar las tarjetas financieras existentes en el sistema
	 * @param forma {@link VentaTarjetaClienteForm} formulario
	 */
	private void listarTarjetasFinancieras(VentaTarjetaClienteForm forma) {
		ITarjetaFinancieraServicio tarjetaFinancieraServicio=TesoreriaFabricaServicio.crearTarjetasFinancieras();
		forma.setListaTarjetasFinancieras(tarjetaFinancieraServicio.listarTarjetasFinancieras());
	}
	
	/**
	 * Crea la primera forma de pago existente en el sistema
	 * @param forma Formulario
	 */
	private void crearPrimeraFormaDePago(VentaTarjetaClienteForm forma) {
		forma.getDtoVentas().setListaInformacionFormasPago(new ArrayList<DtoInformacionFormaPago>());
		forma.getDtoVentas().getListaInformacionFormasPago().add(new DtoInformacionFormaPago());		
	}

	/**
	 * Listar las ciudades existentes en el sistema
	 * @param forma
	 */
	private void accionListarCiudades(VentaTarjetaClienteForm forma) {
		ILocalizacionServicio localizacionServicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		forma.setListaCiudades(localizacionServicio.listarCiudades()); 
	}

	/**
	 * Cargar el  listado de las entidades financieras
	 * @param forma
	 */
	private void accionCargarEntidadesFinancieras(VentaTarjetaClienteForm forma) {
		forma.setListaEntidadesFinancieras(EntidadesFinancieras.consultarEntidadesFinancieras(true));
	}

	/**
	 * Cargar las formas de pago
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @param institucion Institución del usuario
	 */
	private void accionCargarFormasPago(VentaTarjetaClienteForm forma, int institucion) {
		// Verificar la institución para las formas de pago
		ArrayList<DtoFormaPago> listaFormasPago=VentaTarjetaServicioFabrica.crearVentaTarjetaClienteServicio().listarFormasPagoActivas();
		forma.setListaFormasPago(listaFormasPago);
	}

	/**
	 * Cargar los coneptos de facturas varias
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 */
	private void accionCargarConceptosFacturasVarias(VentaTarjetaClienteForm forma) {
		forma.setListaConceptos(FacturasVariasFabricaServicio.crearFacturasVariasServicio().listarConceptosFacturasVarias());
	}

	/**
	 * Carga los datos básicos utilizados en las páginas
	 * de específicas de cada clase de venta
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @author Juan David Ramírez
	 * @since 1 Septiembre 2010
	 */
	private void cargarDatosParaClaseVenta(VentaTarjetaClienteForm forma)
	{
		if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
		{
			this.accionPostularCantidadPersonal(forma);
		}
		// Para las otras clases de venta no se postula la cantidad
	}

	/**
	 * Método que postula el campo cantidad para la clase de venta personal
	 * @param forma
	 */
	private void accionPostularCantidadPersonal(VentaTarjetaClienteForm forma)
	{
		if(forma.getDtoVentas().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
		{
			// Para la personal se postula 1
			forma.getDtoVentas().setCantidad(1);
		}
	}

	/**
	 * Método que postula el campo cantidad para la clase de venta familiar
	 * @param forma
	 */
	private void accionPostularCantidadFamiliar(VentaTarjetaClienteForm forma)
	{
		/*
		 * Para la familiar se postula la cantidad de seriales parametrizados en
		 * el tipo de tarjeta
		 */
		double codigoTipoTarjeta=forma.getDtoVentas().getTipoTarjeta();
		for(DtoTarjetaCliente dtoTarjeta :forma.getListaTarjetaCliente())
		{
			if(dtoTarjeta.getCodigoPk()==codigoTipoTarjeta)
			{
				int cantidad=dtoTarjeta.getNumBeneficiariosFam();
				forma.getDtoVentas().setCantidad(cantidad);
			}
		}
	}

	/**
	 * Cargar los beneficiarios de las venta para la clase de venta familiar
	 * @author Juan David Ramírez
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @since 06 Septiembre 2010
	 */
	private void crearBeneficiariosFamiliar(VentaTarjetaClienteForm forma)
	{
		/*
		 * Para la familiar se postula la cantidad de seriales parametrizados en
		 * el tipo de tarjeta
		 */
		int cantidad=forma.getDtoVentas().getCantidad();
		DtoBeneficiarioCliente dtoBeneficiario;
		if(forma.getDtoVentas().getListaBeneficiarios().size()==0)
		{
			dtoBeneficiario=new DtoBeneficiarioCliente();
		}
		else
		{
			dtoBeneficiario=forma.getDtoVentas().getListaBeneficiarios().get(0);
		}
		dtoBeneficiario.setIndicativoPrincipal(ConstantesBD.acronimoSi);
		forma.getDtoVentas().setListaBeneficiarios(new ArrayList<DtoBeneficiarioCliente>());
		forma.getDtoVentas().getListaBeneficiarios().add(dtoBeneficiario);
		// Asignar los datos del comprador
		for(int i=1; i<cantidad; i++)
		{
			DtoBeneficiarioCliente beneficiario=new DtoBeneficiarioCliente();
			beneficiario.setIndicativoPrincipal(ConstantesBD.acronimoNo);
			beneficiario.setConsecutivo(i+1);
			forma.getDtoVentas().getListaBeneficiarios().add(beneficiario);
		}
	}


	/**
	 * Cargar las clases de venta según el rol del usuario
	 * @author Juan David Ramírez
	 * @param forma Bean del formulario
	 * @param usuario Usuario en sesión
	 * @since 1 Septiembre 2010
	 */
	private Errores accionCargarClasesDeVenta(VentaTarjetaClienteForm forma, UsuarioBasico usuario)
	{
		ArrayList<String> listaConstantes=new ArrayList<String>();
		Connection con=UtilidadBD.abrirConexion();
		// Se validan roles
		// Empresarial
		if(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 1069))
		{
			listaConstantes.add(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa);
		}
		//Familiar
		if(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 1070))
		{
			listaConstantes.add(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar);
		}
		//Personal
		if(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 1071))
		{
			listaConstantes.add(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona);
		}
		forma.setListaClaseVenta(Utilidades.generarListadoConstantesIntegridadDominio(con, (String[])listaConstantes.toArray(new String[listaConstantes.size()]), true));
		if(forma.getListaClaseVenta()==null || forma.getListaClaseVenta().size()==0)
		{
			return new Errores("errors.usuario.noPermisos");
		}
		UtilidadBD.closeConnection(con);
		return null;
	}


	/**
	 * Validaciones para el ingreso a la funcionalidad
	 * Se muestran mensajes informativos para la generación de
	 * los recibos de caja.
	 * @author Juan David Ramírez
	 * @param usuario {@link UsuarioBasico} en session
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @return String con el mensaje registrado en: <code>VentaTarjetaClienteForm.properties</code>,
	 * null en caso de no existir error
	 * @since 30 Agosto 2010
	 */
	private String validacionesIngreso(UsuarioBasico usuario, VentaTarjetaClienteForm forma)
	{
		/*
		 * Validar parámetro general
		 * Se genera recibo de caja automático en la venta de tarjeta?
		 */
		String mensajeIngreso=null;
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getReciboCajaAutomaticoVentaTarjeta(usuario.getCodigoInstitucionInt())))
		{
			/*
			 * Validar el rol del usuario
			 */
			if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadRecibosCaja))
			{
				CentroAtencionServicio centroAtencionServicio = new CentroAtencionServicio();
				CentroAtencion centroAtencion = new CentroAtencion();
				centroAtencion = centroAtencionServicio.buscarPorCodigoPK(usuario.getCodigoCentroAtencion());
				/*
				 * Validar si el usuario tiene caja de tipo recaudo en estado 'activo'
				 */
				//
				ITurnoDeCajaServicio turnoDeCajaServicio= TesoreriaFabricaServicio.crearTurnoDeCajaServicio();
				DtoCaja caja=turnoDeCajaServicio.validarTurnoUsuario(usuario.getLoginUsuario(), new Integer[]{ConstantesBD.codigoTipoCajaRecaudado},centroAtencion.getConsecutivo());
				if(caja!=null)
				{
					Cajas cajaOrm=new Cajas();
					cajaOrm.setConsecutivo(caja.getConsecutivo());
					cajaOrm.setCodigo(caja.getCodigo());
					forma.getDtoVentas().setCaja(cajaOrm);
					forma.setTurnoAbierto(true);
				}
				else
				{
					mensajeIngreso="VentaTarjetaClienteForm.warning.noTieneTurnoAbierto";
				}
			}
			else
			{
				mensajeIngreso="VentaTarjetaClienteForm.warning.noTieneRolCajero";
			}
		}
		return mensajeIngreso;
	}

	/**
	 * Cargar convenio de la Tarjeta cliente
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @return {@link DtoConvenio} Convenio perteneciente a la tarjeta
	 */
	private DtoConvenio accionCargarConvenioTarjetaCliente(
			VentaTarjetaClienteForm forma) {
		
		double codigoTipoTarjeta=forma.getDtoVentas().getTipoTarjeta();
		
		for(DtoTarjetaCliente dtoTarjeta :forma.getListaTarjetaCliente() ){
			if(dtoTarjeta.getCodigoPk()==codigoTipoTarjeta)
			{
				DtoConvenio dtoConvenioTarjeta= new DtoConvenio();
				dtoConvenioTarjeta.setCodigo(dtoTarjeta.getConvenio().getCodigo());
				dtoConvenioTarjeta.setDescripcion(dtoTarjeta.getConvenio().getDescripcion());
				return dtoConvenioTarjeta;
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * Método para cargar convenios
	 * Cargar Convenio Tarifa de Venta 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionCargarConvenio(VentaTarjetaClienteForm forma, int codigoCentroCosto, int codigoInstitucion) throws IPSException {
		
		forma.getDtoVentas().setContratoTarjeta(new DtoContrato());
		
		/*
		 * Se limpia el array
		 */
		forma.setListaConvenioTarifaVenta(new ArrayList<DtoConvenio>());

		 /*
		  * Se carga el convenio de la tarjeta
		  */
		this.accionCargarYAdicionarConvenioTarjeta(forma);
		
		/*
		 * Si no hay convenios se cargan los contratos - convenios
		 * que provienen de los parámetros generales
		 */
		if(forma.getListaConvenioTarifaVenta().size()==0)
		{
			this.accionCargaConveniosPorDefecto(forma);
		}
		
		/*
		 * Se cargan los convenios del paciente
		 */
		this.accionCargarConveniosPaciente(forma);
		
		if(forma.getNumeroConvenios()==1)
		{
			// Si solamente existe 1 convenio se asigna automáticamente y se buscan los contratos activos
			forma.setConvenioTarjeta(forma.getListaConvenioTarifaVenta().get(0));
			this.accionBuscarContratosConvenio(forma, codigoCentroCosto, codigoInstitucion);
		}
		
	}
	
	/**
	 * Cargar los convenios asociados al paciente
	 * @param {@link VentaTarjetaClienteForm} Formulario
	 */
	private void accionCargarConveniosPaciente(VentaTarjetaClienteForm forma) {
		/*
		 * Debo serciorarme de que el dato nunca llegue null, por el momento
		 * lo dejo con la validación porque hay mucho afán
		 */
		if(forma.getDtoCompradorTarjeta().getTipoPersona()!=null && forma.getDtoCompradorTarjeta().getTipoPersona().equals(ConstantesBD.tipoPersonaPaciente))
		{
			ArrayList<DtoConvenio> conveniosPaciente=ConveniosFabricaServicio.crearConvenioServicio().listarConveniosPaciente(forma.getDtoCompradorTarjeta().getCodigo(), ConstantesBD.acronimoSiChar);
			ArrayList<DtoConvenio> conveniosFinal=new ArrayList<DtoConvenio>();
			if(conveniosPaciente!=null && conveniosPaciente.size()>0)
			{
				conveniosPaciente:for(DtoConvenio convenio:conveniosPaciente)
				{
					if(convenio.isActivo() && UtilidadTexto.getBoolean(convenio.getEsConvenioTarjetaCliente()))
					{
						for(DtoConvenio convenioExistente:forma.getListaConvenioTarifaVenta())
						{
							if(convenioExistente.getCodigo()==convenio.getCodigo())
							{
								continue conveniosPaciente;
							}
						}
						conveniosFinal.add(convenio);
					}
				}
			}
			if(conveniosFinal!=null && conveniosFinal.size()>0)
			{
				forma.getListaConvenioTarifaVenta().addAll(conveniosFinal);
			}
		}
	}

	/**
	 * Cargar y adicionar a la lista de convenios el convenio de la tarjeta
	 * parametrizado en tipos tarjeta cliente
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @author Juan David Ramírez
	 */
	private void accionCargarYAdicionarConvenioTarjeta(VentaTarjetaClienteForm forma)
	{
		DtoConvenio convenioTarjeta=accionCargarConvenioTarjetaCliente(forma);
		if(convenioTarjeta!=null)
		{
			// Se valida que no esté repetido en los convenios por defecto
			boolean existe=false;
			for(DtoConvenio convenio:forma.getListaConvenioTarifaVenta())
			{
				if(convenio.getCodigo()==convenioTarjeta.getCodigo())
				{
					existe=true;
					break;
				}
			}
			if(!existe)
			{
				forma.getListaConvenioTarifaVenta().add(convenioTarjeta);
			}
		}
	}


	/**
	 * Método que carga los convenio por defecto parametrizados en los valores por defecto
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionCargaConveniosPorDefecto(VentaTarjetaClienteForm forma) {
		
		ArrayList<HashMap<String, Object>> array= ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();
		
		/*
		 * Se recorre la lista de convenios por default
		 */
		for(HashMap<String, Object> mapa: array)
		{
			/*
			 * Dto Convenio
			 */
			DtoConvenio dtoConvenio = new DtoConvenio();
			dtoConvenio.setCodigo(Utilidades.convertirAEntero(mapa.get("codigoConvenio")+""));
			dtoConvenio.setDescripcion( mapa.get("descripcionConvenio")+"");
			
			/*
			 * Dto contrato 
			 */
			DtoContrato dtoContrato= new DtoContrato();
			dtoContrato.setCodigo(Utilidades.convertirAEntero(mapa.get("codigoContrato")+""));
			dtoContrato.setNumeroContrato(mapa.get("numeroContrato")+"");
			
			
			boolean convenioTarjetaCliente=Convenio.esConvenioTarjetaCliente(dtoConvenio.getCodigo());
			
			// Se valida si el convenio se encuentra activo y es convenio tarjeta cliente
			boolean convenioActivo=this.validarConvenioActivo(dtoConvenio);

			// Si el convenio está activo se evalúan los contratos
			if(convenioActivo && convenioTarjetaCliente)
			{
				/*
				 * Se valida la vigencia del contrato
				 */
				boolean contratoVigente=this.validarVigenciaContrato(dtoContrato);
				
				// si el contrato es vigente debe adicionar el contrato
				if(contratoVigente)
				{
					/*
					 * Adicionar contrato a la lista de convenio 
					 */
					dtoConvenio.getListContrato().add(dtoContrato);
				}
				
				dtoConvenio.setMarcadoPorDefecto(true);
				
				/*
				 * Adicionar Lista Convenio Tarifa Venta 
				 */
				forma.getListaConvenioTarifaVenta().add(dtoConvenio);
			}
		}
	}

	/**
	 * Valida si el convenio seleccionado se encuentra activo en el sistema
	 * @param dtoConvenio Convenio a evaluar
	 * @return true en caso de que el convenio se encuentre activo
	 */
	private boolean validarConvenioActivo(DtoConvenio dtoConvenio) {
		IConvenioServicio convenioServicio=ConveniosFabricaServicio.crearConvenioServicio();
		DtoConvenio convenioTemporal=convenioServicio.buscarConvenio(dtoConvenio.getCodigo());
		return convenioTemporal.isActivo();
	}

	/**
	 * Validar la vigencia de un contrato específico
	 * @param dtoContrato
	 * @return true en caso de ser vigente el contrato, false de lo contrario
	 */
	private boolean validarVigenciaContrato(DtoContrato dtoContrato)
	{
		IContratoServicio contratoServicio=ConveniosFabricaServicio.crearContratoServicio();
		return contratoServicio.esVigenteContrato(dtoContrato);
	}

	/**
	 * Método para cargar los parentescos de un usuario Comprador
	 * @author Edgar Carvajal Ruiz
	 * @param forma {@link VentaTarjetaClienteForm}
	 */
	private void accionCargarParentescos(VentaTarjetaClienteForm forma) {
		DtoParentesco objetoParentezco= new DtoParentesco();
		forma.setListaParentesco(UtilidadOdontologia.obtenerParentezco(objetoParentezco));
	}

	/**
	 * Acción cargar usuarios
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionCargarUsuario(VentaTarjetaClienteForm forma, UsuarioBasico usuario) {
		Usuario objUsuario = new Usuario();
		forma.setListaUsuarios(UtilidadesAdministracion.obtenerUsuarios(objUsuario, usuario.getCodigoInstitucionInt(),true));
	}

	/**
	 * Acción cagar Tipos de identificación
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionCargarTipoIdentificacion(VentaTarjetaClienteForm forma,
			UsuarioBasico usuario) {

		IIngresosEgresosCajaServicioServicio ingresosEgresosCajaServicioServicio 	
		= AdministracionFabricaServicio.crearIIngresosEgresosCajaServicioServicio();

		String[] listaTipoDeTiposIdentificacion = { 
				ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionPersona,
				ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionAmbos
		};
		
		forma.setTiposIdentificacion(ingresosEgresosCajaServicioServicio.listarTiposIdentificacionPorTipo(listaTipoDeTiposIdentificacion));
	}

	
	/**
	 * Acción cargar tipo de tarjeta 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionCargarTipoTarjeta(VentaTarjetaClienteForm forma, UsuarioBasico usuario) {
		DtoTarjetaCliente dto = new DtoTarjetaCliente();
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		dto.setAliado(ConstantesBD.acronimoNo);
		forma.setListaTarjetaCliente(TarjetaCliente.cargar(dto));
	}
	
	
	
	
	
	
	/**
	 * Cargar los datos personales del paciente
	 * @author Juan David Ramírez
	 * @param forma {@link VentaTarjetaClienteForm} Formulario
	 * @since 4 Septiembre 2010
	 */
	private void accionCargarPaciente(VentaTarjetaClienteForm forma) 
	{
		/*
		 * Se busca el Paciente en la BD 
		 */
		PersonaBasica personaBasica=new PersonaBasica();
		Connection con=UtilidadBD.abrirConexion();
		
		try
		{
			//Este método carga un paciente, ojo, valida que si sea paciente
			personaBasica.cargar(con, new TipoNumeroId(forma.getDtoCompradorTarjeta().getTipoIdentificacion(), forma.getDtoCompradorTarjeta().getNumeroIdentificacion()));
		} catch (SQLException e)
		{
			Log4JManager.info("error cargando la persona", e);
			UtilidadBD.closeConnection(con);
			return;
		}
		if(personaBasica!=null && personaBasica.getCodigoPersona()>0)
		{
			forma.getDtoCompradorTarjeta().setPrimerNombre(personaBasica.getPrimerNombre());
			forma.getDtoCompradorTarjeta().setSegundoNombre(personaBasica.getSegundoNombre());
			forma.getDtoCompradorTarjeta().setPrimerApellido(personaBasica.getPrimerApellido());
			forma.getDtoCompradorTarjeta().setSegundoApellido(personaBasica.getSegundoApellido());
			forma.getDtoCompradorTarjeta().setCodigo(personaBasica.getCodigoPersona());
			UtilidadBD.closeConnection(con);
			forma.setPermitirModificacionInformacionPaciente(false);
			forma.getDtoCompradorTarjeta().setTipoPersona(ConstantesBD.tipoPersonaPaciente);
			return;
		}
		UtilidadBD.closeConnection(con);

		forma.setMsgInformacionComprador(new Errores("VentaTarjetaClienteForm.error.noPaciente", Tipo.ERROR));
		forma.getDtoCompradorTarjeta().setTipoIdentificacion("");
		forma.getDtoCompradorTarjeta().setNumeroIdentificacion("");
		limpiarDatosCompradorTarjeta(forma, false);
	}

	/**
	 * Limpia la información del comprador de la tarjeta
	 * @param forma {@link VentaTarjetaClienteForm}
	 * @param permitirModificarInformacion Permitir modificar la información del paciente
	 */
	private void limpiarDatosCompradorTarjeta(VentaTarjetaClienteForm forma, boolean permitirModificarInformacion)
	{
		forma.getDtoCompradorTarjeta().setPrimerNombre("");
		forma.getDtoCompradorTarjeta().setSegundoNombre("");
		forma.getDtoCompradorTarjeta().setPrimerApellido("");
		forma.getDtoCompradorTarjeta().setSegundoApellido("");
		forma.getDtoCompradorTarjeta().setCodigo(ConstantesBD.codigoNuncaValido);
		forma.setPermitirModificacionInformacionPaciente(permitirModificarInformacion);
	}
	
}
