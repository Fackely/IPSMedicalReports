package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;
import util.adjuntos.DTOArchivoAdjunto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.ConsultarEntregaTransportadoraForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.administracion.DtoUsuario;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaATransportadora.GeneradorReporteEntregaTransportadora;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AdjuntosMovimientosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TransportadoraValores;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITercerosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IAdjuntoMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IEntidadesFinancierasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITipoCuentaBancariaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITransportadoraValoresServicio;

/**
 * Clase que controla el flujo de la funcionalidad Consultar entrega a transportadora_1042
 * @author Diana Carolina G&oacute;mez
 * @since Julio 10/2010
 * 
 */
public class ConsultarEntregaTransportadoraAction extends Action {
	
	@Override
	public ActionForward execute(ActionMapping mapping, 
								ActionForm form,
								HttpServletRequest request, 
								HttpServletResponse response)
			throws Exception
	{
		
		
		if (form instanceof ConsultarEntregaTransportadoraForm)
		{
			
			
			ConsultarEntregaTransportadoraForm forma =(ConsultarEntregaTransportadoraForm)form;
			String estado=forma.getEstado();
			Log4JManager.info("ESTADO "+estado);
			
			try {
			
				/*
				 * usuario de session 
				 */
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				InstitucionBasica institucionBasica = 
					(InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				
				forma.setInstitucion(institucionBasica);
				
				/*InstitucionBasica institucionBasica = 
					(InstitucionBasica)session.getAttribute("institucionBasica");*/
				
				if(estado==null){
					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					
					//UtilidadTransaccion.getTransaccion().begin();
					
					accionCargarDatosConsultaEntregaTransportadora(forma, usuario); // Se postula el Centro de Atenci&oacute;n del usuario.
					return mapping.findForward("principal");
				}
				else if (estado.equals("cargarCajeros")) 						   // Cuando se selecciona un Centro de Atenci&oacute;n
				{
					/*
					 * 3. Carga los cajeros de la instituci&oacute;n
					 */
					try{
						HibernateUtil.beginTransaction();
						ICajasCajerosServicio cajasCajeroServicios = TesoreriaFabricaServicio.crearCajasCajerosServicio();
						UtilidadTransaccion.getTransaccion().begin();
						forma.setListaCajero(cajasCajeroServicios.obtenerCajerosCentrosAtencion(forma.getDtoConsultaEntregaTransp().getCodigoCentroAtencion()));
						TransportadoraValores transportadora= new TransportadoraValores();
						transportadora.setActivo(ConstantesBD.acronimoSiChar);
						ITransportadoraValoresServicio transportadoraValoresServicios= TesoreriaFabricaServicio.crearTransportadoraValoresServicio();
						forma.setListaTransportadoraValores(transportadoraValoresServicios.listarTodos(transportadora,ConstantesBD.codigoNuncaValido, forma.getDtoConsultaEntregaTransp().getCodigoCentroAtencion()));
						HibernateUtil.endTransaction();
					}
					catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
					return mapping.findForward("principal");
					
				}
				else if(estado.equals("consultar"))
				{
					try{
						HibernateUtil.beginTransaction();
					    IMovimientosCajaServicio movimientosCajaServicio = TesoreriaFabricaServicio.crearMovimientosCajaServicio();
						forma.setListaDtoInformacionEntrega(movimientosCajaServicio.consultaEntregaTransportadora(forma.getDtoConsultaEntregaTransp()));
						List<DtoInformacionEntrega> lista= forma.getListaDtoInformacionEntrega();
						if(lista.size()==0)
						{
							ActionErrors errores=new ActionErrors();
							MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarEntregaTransportadoraForm");
							errores.add("No se encontraron resultados", new ActionMessage("errors.notEspecific", mensajes.getMessage("errores.modTesoreria.consultarEntregaTransportadora")));
							saveErrors(request, errores);	
						}
						HibernateUtil.endTransaction();
					}
					catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
					return mapping.findForward("principal");
				}
				else if(forma.getEstado().equals("empezarDetalle"))
				{
					return buscarDetalleEntregaTransportadora(mapping, forma);
					
				}
				else if(forma.getEstado().equals("adicionarDocumento"))
				{
					accionAdicionarAdjuntoMovimiento(forma, usuario);
					return mapping.findForward("mostrarDetalle");
				}
				else if(forma.getEstado().equals("eliminarDocumento"))
				{
					accionEliminar(forma);
					return mapping.findForward("mostrarDetalle");
				}
				
				else if(estado.equals( "guardarAdjuntar"))
				{
					//1. Construir objeto
				 	for(String document :  forma.getDtoConsultaEntregaTransp().getDocumentosAdjuntos())
				 	{
				 		Log4JManager.info(document);
				 	}
				 	//2. insertarlo 
				 	
				 	// retornar a la pagina
				 	return mapping.findForward("mostrarDetalle");
				}
				else if (estado.equals("confirmarAdjuntos"))
				{
					
				//	InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
					confirmarAdjuntosMovimientoCaja(forma, usuario);
					return buscarDetalleEntregaTransportadora(mapping, forma);
					
				}else if(estado.equals("ordenar")){
					
					return accionOrdenar(forma, mapping);
				}
				else if(estado.equals("impresionTransportadoraValores")){
					this.imprimirEntregaTransportadora(forma, request, usuario, institucionBasica);
					return mapping.findForward("mostrarDetalle");
				}
			
			} catch (Exception e) {
				Log4JManager.error("Error consultando las entregas de transportadoras", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);
	}


	private ActionForward buscarDetalleEntregaTransportadora(
			ActionMapping mapping, ConsultarEntregaTransportadoraForm forma) {
		forma.setDtoInformacionEntrega(forma.getListaDtoInformacionEntrega().get(forma.getPosArray()));
		
		
		String login="";
		
			try {
				HibernateUtil.beginTransaction();
				login= forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getUsuarios().getLogin();
				IUsuariosServicio servicioUsuario= AdministracionFabricaServicio.crearUsuariosServicio();
				forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().setUsuarios(servicioUsuario.buscarPorLogin(login)) ;

				if(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias()!=null)
				{
					char codigoTipoCuenta= forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getTipoCuentaBancaria().getCodigo();
			        ITipoCuentaBancariaServicio servicioTipoCuentaBancarias = TesoreriaFabricaServicio.crearTipoCuentaBancariaServicio(); 			
					forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().setTipoCuentaBancaria(servicioTipoCuentaBancarias.findById(codigoTipoCuenta)) ;
					 
					int codigoTercero= forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getEntidadesFinancieras().getTerceros().getCodigo();
					Log4JManager.info(codigoTercero);
					ITercerosServicio servicioTerceros =FacturacionServicioFabrica.crearTercerosServicio();
					forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getEntidadesFinancieras().setTerceros(servicioTerceros.findById(codigoTercero)) ;
				}
				
				DtoUsuario usuarioDTO= new DtoUsuario();
				
				if(!UtilidadTexto.isEmpty(login))
				{
					usuarioDTO.setLogin(login);
					DtoPersonas personasDTO= UtilidadesAdministracion.cargarPersona(usuarioDTO);
					forma.setNombreCompletoCajero(personasDTO.getPrimerNombre()+" "+personasDTO.getSegundoNombre()+" "+personasDTO.getPrimerApellido()+ " "+personasDTO.getSegundoApellido());
				}
				else{
					Log4JManager.error(" \n **********************************************************-> Login Blancooooooooooooooo");
				}
	
				IAdjuntoMovimientosCajaServicio adjuntoMovimientosCajaServicio = TesoreriaFabricaServicio.crearAdjuntoMovimiento();
				ArrayList <DTOArchivoAdjunto> listaAdjuntos = adjuntoMovimientosCajaServicio.consultarDocumentosSoporteAdjuntos(forma.getDtoInformacionEntrega().getIdMovimientoCaja());
				forma.getConsolidadoMovimientoDTO().setArchivosAdjuntosMovimiento(listaAdjuntos);
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error("ERROR buscarDetalleEntregaTransportadora",e);
				HibernateUtil.abortTransaction();
			}
		return mapping.findForward("mostrarDetalle");
	}








	/**
	 * 
	 * Este mï¿½todo se encarga de
	 
	 *
	 */
	private void accionEliminar(ConsultarEntregaTransportadoraForm forma) {
		
		try{
			HibernateUtil.beginTransaction();
			IAdjuntoMovimientosCajaServicio adjuntoServicio = TesoreriaFabricaServicio.crearAdjuntoMovimiento();
			
			AdjuntosMovimientosCaja tmp = forma.getListaAdjuntos().get(forma.getPostArrayAdjunto());
			if(tmp.getCodigoPk()>0)
			{
				adjuntoServicio.eliminar(forma.getListaAdjuntos().get(forma.getPostArrayAdjunto()));
				forma.getListaAdjuntos().remove(forma.getPostArrayAdjunto());
			
			}
			else
			{
				forma.getListaAdjuntos().remove(forma.getPostArrayAdjunto());
			}
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}








	/**
	 * 
	 * Este mï¿½todo se encarga de
	 * 
	 *
	 */
	private void accionAdicionarAdjuntoMovimiento(	ConsultarEntregaTransportadoraForm forma, UsuarioBasico usuario) {
		try{
			HibernateUtil.beginTransaction();
			IAdjuntoMovimientosCajaServicio adjuntoServicio = TesoreriaFabricaServicio.crearAdjuntoMovimiento();
			
			
			/*
			 *
			 * Deberia ser en el mundo o en  un helper. tare karo
			 * armar estructura adjunto movimiento
			 */
			forma.getAdjuntoMovimiento().setFecha(UtilidadFecha.getFechaActualTipoBD());
			forma.getAdjuntoMovimiento().setUsuarios(new Usuarios());
			forma.getAdjuntoMovimiento().getUsuarios().setLogin(usuario.getLoginUsuario());
			forma.getAdjuntoMovimiento().setMovimientosCaja(new MovimientosCaja());
			forma.getAdjuntoMovimiento().getMovimientosCaja().setCodigoPk(forma.getDtoInformacionEntrega().getMovimientosCaja().getCodigoPk());
			
			/*
			 * adicionar a la lista
			 */
			forma.getListaAdjuntos().add(forma.getAdjuntoMovimiento());
			
			adjuntoServicio.insertar(forma.getAdjuntoMovimiento());
			/*
			 *limpiar objeto 
			 */
			forma.setAdjuntoMovimiento(new AdjuntosMovimientosCaja());
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}


	/**
	 * Este M&eacute;todo se encarga de inicializar los valores para los
	 * objetos de la p&aacute;gina de consulta 
	 * 
	 * @author Diana Carolina G
	 * @param ConsultarEntregaTransportadoraForm , usuario
	 * 
	 */
	private void accionCargarDatosConsultaEntregaTransportadora(ConsultarEntregaTransportadoraForm forma, UsuarioBasico usuario) {
		
		/*
		 * 1. Cargar Centro de Atenci&oacute;n
		 */
		try{
			HibernateUtil.beginTransaction();
			ICentroAtencionServicio  centroAtencionServicios = AdministracionFabricaServicio.crearCentroAtencionServicio();
			
			Connection  con = HibernateUtil.obtenerConexion();
			forma.setPath(Utilidades.obtenerPathFuncionalidad(con,ConstantesBD.codigoFuncionalidadMenuControlCaja));
			
			DtoCentrosAtencion dtoCentro= new DtoCentrosAtencion();
			dtoCentro.setActivo(Boolean.TRUE);
			dtoCentro.setCodInstitucion(usuario.getCodigoInstitucionInt());
			forma.setListaCentrosAtencion( centroAtencionServicios.listarCentrosAtencion(dtoCentro));
			forma.getDtoConsultaEntregaTransp().setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
			/*
			 * 2. Cargar cajas por centro de atenci&oacute;n seleccionado 
			 */
			ICajasServicio  cajasServicios= TesoreriaFabricaServicio.crearCajasServicio();
			forma.setListaCajas(cajasServicios.listarCajasPorCentrosAtencionPorTipoCaja(usuario.getCodigoCentroAtencion(), ConstantesBD.codigoTipoCajaRecaudado));
			
			/*
			 * 3. Cargar cajeros por centro de atenci&oacute;n seleccionado 
			 */
			
			ICajasCajerosServicio cajasCajeroServicios = TesoreriaFabricaServicio.crearCajasCajerosServicio();
			forma.setListaCajero(cajasCajeroServicios.obtenerCajerosCentrosAtencion(usuario.getCodigoCentroAtencion()));
			
			/*
			 * 4. Cargar las Transportadoras de la Instituci&oacute;n 
			 */
			TransportadoraValores transportadora= new TransportadoraValores();
			transportadora.setActivo('S');
			ITransportadoraValoresServicio transportadoraValoresServicios= TesoreriaFabricaServicio.crearTransportadoraValoresServicio();
			forma.setListaTransportadoraValores(transportadoraValoresServicios.listarTodos(transportadora, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido));		
			
			/*
			 * 5. Consultar entidades de al Instituci&oacute;n
			 */
			IEntidadesFinancierasServicio entidadFinancierasServicio = TesoreriaFabricaServicio.crearEntidadesFinancierasServicio();
			forma.setListaEntidades(entidadFinancierasServicio.obtenerEntidadesPorInstitucion(usuario.getCodigoInstitucionInt() , Boolean.TRUE));
			HibernateUtil.endTransaction();
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("ERROR accionCargarDatosConsultaEntregaTransportadora", e);
		}
	}
	
	/**
	 * 
	 * Este Método se encarga de Confirmar
	 * Los Archivos Adjuntos
	 * @author, Carolina G&oacute;mez
	 *
	 */
	private void confirmarAdjuntosMovimientoCaja(ConsultarEntregaTransportadoraForm forma, 
												UsuarioBasico usuario
											) throws Exception {
		try{
			HibernateUtil.beginTransaction();
			IAdjuntoMovimientosCajaServicio adjuntoMovimientosCajaServicio = TesoreriaFabricaServicio.crearAdjuntoMovimiento();
			
			ArrayList<DTOArchivoAdjunto> dtoAdjuntos = new ArrayList<DTOArchivoAdjunto>();
			dtoAdjuntos= forma.getConsolidadoMovimientoDTO().getArchivosAdjuntosMovimiento();
			
			forma.setDtoInformacionEntrega(forma.getListaDtoInformacionEntrega().get(forma.getPosArray()));
			long codigoPK = forma.getDtoInformacionEntrega().getIdMovimientoCaja();
			//long codigoPk = forma.getDtoDetalle().getCodigoPk();
			String login = usuario.getLoginUsuario(); 
			
			ArrayList<DTOArchivoAdjunto> archivosAlmacenados = adjuntoMovimientosCajaServicio.confirmarAdjuntos(dtoAdjuntos, codigoPK, login);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}	
	}

	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public ActionForward accionOrdenar(ConsultarEntregaTransportadoraForm forma, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaDtoInformacionEntrega(),sortG);

		return mapping.findForward("principal");
	}
	
	/**
	 * Método que genera el reporte de entrega transportadora DCU 1025
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirEntregaTransportadora(ConsultarEntregaTransportadoraForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		
		try{
			HibernateUtil.beginTransaction();
			//GENERAR EL REPORTE
		    String nombreArchivo="";
		    forma.setListaNombresReportes(new ArrayList<String>());
		    
		
	    	UsuariosDelegate usu= new UsuariosDelegate();
			Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
			
	    	//TITULO REPORTE
			String razonSocial = ins.getRazonSocial();
			forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
			forma.getDtoFiltroReporte().setNit(ins.getNit());
	    	forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
	    	forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
	    	forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
	    	forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
	    	forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());
	    	
	    	//ENCABEZADO REPORTE
	   		forma.getDtoFiltroReporte().setNroConsecutivo(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().getConsecutivo()+"");
			
			String fecha=UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFecha());
			String hora	 = forma.getDtoInformacionEntrega().getMovimientosCaja().getHora();
	    	forma.getDtoFiltroReporte().setFechaGeneracion(fecha);
	    	forma.getDtoFiltroReporte().setHoraGeneracion(hora);
	    	
	    	forma.getDtoFiltroReporte().setCaja("(" +forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCodigo()+ 
	    			") " + forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getDescripcion() + 
	    			" - " + forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion());
	    	
	    	
	    	Usuarios cajero = forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getUsuarios();
	    	forma.getDtoFiltroReporte().setCajero(cajero.getPersonas().getPrimerApellido()
	    			+" "+cajero.getPersonas().getPrimerNombre()
	    			+" - "+cajero.getLogin());
	    	
	    	forma.getDtoFiltroReporte().setTransportadora(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getTransportadoraValores().getTerceros().getDescripcion());
	    	forma.getDtoFiltroReporte().setNitTransportadora(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getTransportadoraValores().getTerceros().getNumeroIdentificacion());
	    	
	    	forma.getDtoFiltroReporte().setResponsableRecibe(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getResponsable());
	    	forma.getDtoFiltroReporte().setNumCarnet((UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarnet())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarnet()));
	    	
	    	forma.getDtoFiltroReporte().setNumGuia((UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getGuia())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getGuia()));
	    	forma.getDtoFiltroReporte().setNumCarro((UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarro())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarro()));
	    	
	    	if(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias()!=null)
	    	{
	    		forma.getDtoFiltroReporte().setEntidadFinanciera(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getEntidadesFinancieras().getTerceros().getDescripcion());
	        	forma.getDtoFiltroReporte().setTipoNroCuentaBancaria(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getTipoCuentaBancaria().getDescripcion() + 
	    				" - " +	forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getNumCuentaBan());
	    	}
	    	
			forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
					+" "+usuarioCompleto.getPersonas().getPrimerApellido()
					+" ("+usuarioCompleto.getLogin()+")"
					);
	    	
	    	String rutaLogo = ins.getLogoJsp();
	    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
	    	
	    	JasperReportBuilder reporte=null;
			
	    	GeneradorReporteEntregaTransportadora generadorReporte =
		            new GeneradorReporteEntregaTransportadora(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), forma.getMostrarParaSeccionEspecial());
	        		reporte = generadorReporte.generarReporte();
	        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaTransportadora");
	        		forma.getListaNombresReportes().add(nombreArchivo);
	        
	            
			forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	            
	 }
}
