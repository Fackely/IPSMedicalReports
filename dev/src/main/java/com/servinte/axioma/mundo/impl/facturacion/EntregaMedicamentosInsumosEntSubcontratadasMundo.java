package com.servinte.axioma.mundo.impl.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.fabrica.administracion.especialidad.AdministracionDAOFabrica;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.fabrica.ordenes.OrdenesFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.dao.interfaz.administracion.IUsuarioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEntidadesSubcontratadasDAO;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.dao.interfaz.inventario.IAurorizacionesEntSubCapitacionDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDespachoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDespachoPedidoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachoPedidoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachosDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetallePedidosDAO;
import com.servinte.axioma.dao.interfaz.inventario.IPedidoDAO;
import com.servinte.axioma.dao.interfaz.ordenes.IAutorizacionArticuloDespachoDAO;
import com.servinte.axioma.dao.interfaz.ordenes.IRegistroEntregaEntSubDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolDespachoSinAutoDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.AutorizacionArticuloDespacho;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.Despacho;
import com.servinte.axioma.orm.DespachoPedido;
import com.servinte.axioma.orm.DetalleDespachoPedido;
import com.servinte.axioma.orm.DetalleDespachoPedidoId;
import com.servinte.axioma.orm.DetalleDespachos;
import com.servinte.axioma.orm.DetalleDespachosId;
import com.servinte.axioma.orm.DetallePedidos;
import com.servinte.axioma.orm.DetallePedidosId;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.EstadosPedido;
import com.servinte.axioma.orm.Pedido;
import com.servinte.axioma.orm.RegistroEntregaEntSub;
import com.servinte.axioma.orm.RegistroEntregaEntsubPedido;
import com.servinte.axioma.orm.RegistroEntregaEntsubSolici;
import com.servinte.axioma.orm.SolDespachoSinAuto;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.UsuariosEntidadSub;

/**
 * Implementación de la Interfaz IEntregaMedicamentosInsumosEntSubcontratadasMundo
 * @author Cristhian Murillo
 */
public class EntregaMedicamentosInsumosEntSubcontratadasMundo implements IEntregaMedicamentosInsumosEntSubcontratadasMundo 
{
	
	private IEntidadesSubcontratadasDAO entidadesSubcontratadasDAO;
	private IAurorizacionesEntSubCapitacionDAO aurorizacionesEntSubCapitacionHibernateDAO;
	private IArticuloDAO articuloDAO;
	private IUsuarioDAO usuarioDAO;
	private IDespachoDAO despachoDAO;
	private IDetalleDespachosDAO detalleDespachosDAO;
	private ICentroCostosDAO centroCostosDAO;
	private IAutorizacionArticuloDespachoDAO autorizacionArticuloDespachoDAO;
	private IRegistroEntregaEntSubDAO registroEntregaEntSubDAO;
	private IPedidoDAO pedidoDAO;
	private IDetallePedidosDAO detallePedidosDAO;
	private IDespachoPedidoDAO despachoPedidoDAO;
	private IDetalleDespachoPedidoDAO detalleDespachoPedidoDAO;
	private ISolicitudesDAO solicitudesDAO;
	private ISolDespachoSinAutoDAO solDespachoSinAutoDAO;
	
	
	/**
	 * Método constructor de la clase
	 */
	public EntregaMedicamentosInsumosEntSubcontratadasMundo()
	{
		entidadesSubcontratadasDAO 					= FacturacionFabricaDAO.crearEntidadesSubcontratadasDAO();
		aurorizacionesEntSubCapitacionHibernateDAO	= ManejoPacienteDAOFabrica.crearAurorizacionesEntSubCapitacion();
		articuloDAO 								= InventarioDAOFabrica.crearArticuloDAO();
		usuarioDAO									= AdministracionFabricaDAO.crearUsuarioDAO();	
		despachoDAO									= InventarioDAOFabrica.crearDespachoDAO();
		detalleDespachosDAO 						= InventarioDAOFabrica.crearDetalleDespachosDAO();
		centroCostosDAO								= AdministracionDAOFabrica.crearCentroCostoDAO();
		autorizacionArticuloDespachoDAO			 	= OrdenesFabricaDAO.crearAutorizacionArticuloDespachoDAO();
		registroEntregaEntSubDAO					= OrdenesFabricaDAO.crearRegistroEntregaEntSubDAO();
		pedidoDAO									= InventarioDAOFabrica.crearPedidoDAO();
		detallePedidosDAO							= InventarioDAOFabrica.crearDetallePedidosDAO();
		despachoPedidoDAO							= InventarioDAOFabrica.crearDespachoPedidoDAO();
		detalleDespachoPedidoDAO					= InventarioDAOFabrica.crearDetalleDespachoPedidoDAO();
		solicitudesDAO								= OrdenesFabricaDAO.crearSolicitudesDAO();
		solDespachoSinAutoDAO						= OrdenesFabricaDAO.crearSolDespachoSinAutoDAO();
	}	
		

	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo(
			int codCentroCosto) {
		return entidadesSubcontratadasDAO.listarEntidadesSubXCentroCostoActivo(codCentroCosto);
	}

	@Override
	public ArrayList<UsuariosEntidadSub> buscarUsuariosEntidadSubPorUsuarioEntidad(
			String login, long entidadSub) {
		return entidadesSubcontratadasDAO.buscarUsuariosEntidadSubPorUsuarioEntidad(login, entidadSub);
	}

	

	@Override
	public boolean guardarEntregaMedicamentoInsumoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion, UsuarioBasico usuario, int codigoPersona) 
	{
		boolean save = true;
		
		try {
			
			//---------------------------------- Autorizaciones
			AutorizacionesEntSubArticu autorizacionesEntSubArticu; 		
			autorizacionesEntSubArticu = new AutorizacionesEntSubArticu(); 
			autorizacionesEntSubArticu = aurorizacionesEntSubCapitacionHibernateDAO
				.obtenerAutorizacionesEntSubArticuPorId(dtoAutorizacionEntSubcontratadasCapitacion.getAutorizacion());
			
			AutorizacionesEntidadesSub autorizacionesEntidadesSub; 		
			autorizacionesEntidadesSub = new AutorizacionesEntidadesSub(); 
			autorizacionesEntidadesSub = aurorizacionesEntSubCapitacionHibernateDAO
				.obtenerAutorizacionesEntidadesSubPorId(dtoAutorizacionEntSubcontratadasCapitacion.getAutorizacion());
			//----------------------------------
			
			//---------------------------------- Usuario
			Usuarios usuarios;	
			usuarios = new Usuarios();
			usuarios = usuarioDAO.buscarPorLogin(usuario.getLoginUsuario());
			//----------------------------------
			
			//---------------------------------- Centro de Costo
			CentrosCosto centroCosto;	
			centroCosto = new CentrosCosto();
			centroCosto =  centroCostosDAO.findById(usuario.getCodigoCentroAtencion());
			//----------------------------------
			
			//----------------- ----------------- Entidades Subcontratadas
			EntidadesSubcontratadas entidadesSubcontratadas;	
			entidadesSubcontratadas = new EntidadesSubcontratadas();
			entidadesSubcontratadas = entidadesSubcontratadasDAO.obtenerEntidadesSubcontratadasporId(dtoAutorizacionEntSubcontratadasCapitacion.getCodigoEntidadSubcontratada());
			//----------------------------------
			
			//---------------------------------- Datos de entrega
			Date fechaActual 	= UtilidadFecha.getFechaActualTipoBD();
			String horaActual 	= UtilidadFecha.getHoraActual();
			//Date horaActualDate	= UtilidadFecha.getHoraActualFormatoDate();
			//----------------------------------
			
			//---------------------------------- Despacho
			Despacho despacho;	
			despacho = new Despacho();
			despacho.setUsuarios(usuarios);
			despacho.setFecha(fechaActual);
			despacho.setHora(horaActual);
			despacho.setEsDirecto(dtoAutorizacionEntSubcontratadasCapitacion.isEntregaDirectaPaciente());
			//despacho.setNumeroSolicitud(null);
			despacho.setPersonaRecibeMed(dtoAutorizacionEntSubcontratadasCapitacion.getPrimerNombrePersonaRecibe());
			despacho.setContabilizado(ConstantesBD.acronimoNo);
			despachoDAO.attachDirty(despacho);
			//----------------------------------
			
			//---------------------------------- Pedido
			Pedido pedido;	
			pedido = new Pedido();
			pedido.setEntidadesSubcontratadas(entidadesSubcontratadas);
			pedido.setCentrosCostoByCentroCostoSolicitado(centroCosto);
			/* FIXME: Como centro de costo solicitante tomar el centro de costo de la autorización 
			 de medicamentos e insumos para ingreso estancia.  */
			pedido.setCentrosCostoByCentroCostoSolicitante(centroCosto);  
			pedido.setUsuarios(usuarios);
				EstadosPedido estadosPedido = new EstadosPedido(); 
				estadosPedido.setCodigo(ConstantesBD.codigoEstadoPedidoDespachado); 
			pedido.setEstadosPedido(estadosPedido);
			pedido.setFechaGrabacion(fechaActual);
			pedido.setHoraGrabacion(horaActual);
			pedido.setFecha(fechaActual);
			pedido.setHora(horaActual);
			pedido.setUrgente(false);
			pedido.setObservacionesGenerales(autorizacionesEntidadesSub.getObservaciones());
			//pedido.setContabilizado(); 0 ó -1 ?
			pedido.setEsQx(ConstantesBD.acronimoNo);
			pedido.setAutoPorSubcontratacion(ConstantesBD.acronimoSiChar);
			pedidoDAO.attachDirty(pedido);
			//----------------------------------
			
			//---------------------------------- Despacho Pedido
			DespachoPedido despachoPedido;	
			despachoPedido = new DespachoPedido();
			despachoPedido.setPedido(pedido.getCodigo());
			despachoPedido.setUsuarios(usuarios);
			despachoPedido.setFecha(fechaActual);
			despachoPedido.setHora(horaActual);
			despachoPedido.setContabilizado(ConstantesBD.acronimoNo);
			Connection con = null;
	    	con = UtilidadBD.abrirConexion();
			despachoPedido.setConsecutivo(UtilidadBD.obtenerSiguienteValorSecuencia(con, "inventarios.seq_despacho_pedido"));
			UtilidadBD.closeConnection(con);
			despachoPedidoDAO.persist(despachoPedido);
			//----------------------------------
			
			
			//---------------------------------- Despacho - Detalle despachos - Autorizacion Articulo Despacho
			for (DtoArticulosAutorizaciones dtoArticulosAutorizaciones : dtoAutorizacionEntSubcontratadasCapitacion.getListaArticulos()) 
			{
				Articulo articuloAutorizado;	
				articuloAutorizado = new Articulo();
				articuloAutorizado = articuloDAO.obtenerArticuloPorId(dtoArticulosAutorizaciones.getCodigoArticulo());
				
				AutorizacionesEntSubArticu autorizacionesEntSubArticuPorArticulo; 		
				autorizacionesEntSubArticuPorArticulo = new AutorizacionesEntSubArticu(); 
				autorizacionesEntSubArticuPorArticulo = aurorizacionesEntSubCapitacionHibernateDAO
					.obtenerAutorizacionesEntSubArticuPorId(dtoArticulosAutorizaciones.getAutorizacionPropia());
				
				
				// Autorizacion Articulo
				if(dtoAutorizacionEntSubcontratadasCapitacion.isEntregaTotal()){
					autorizacionesEntSubArticuPorArticulo.setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregado);
				}
				int sumaDespacho = dtoArticulosAutorizaciones.getDespachoTotal() +  dtoArticulosAutorizaciones.getDespachoEntregar();
				if(sumaDespacho == dtoArticulosAutorizaciones.getTotalUnidadesFormulacion() ){
					autorizacionesEntSubArticuPorArticulo.setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregado);
				}
				if(!autorizacionesEntSubArticuPorArticulo.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)){
					aurorizacionesEntSubCapitacionHibernateDAO.attachDirtyAutorizacionesEntSubArticu(autorizacionesEntSubArticuPorArticulo);
				}
				
				// Autorizacion Articulo Despacho
				AutorizacionArticuloDespacho autorizacionArticuloDespacho;	
				autorizacionArticuloDespacho = new AutorizacionArticuloDespacho();
				autorizacionArticuloDespacho.setArticuloByArticuloAutorizado(articuloAutorizado);
				autorizacionArticuloDespacho.setAutorizacionesEntSubArticu(autorizacionesEntSubArticuPorArticulo);
				autorizacionArticuloDespacho.setCantidadAutrizado(dtoArticulosAutorizaciones.getTotalUnidadesFormulacion());
				autorizacionArticuloDespacho.setCantidadDespachada(dtoArticulosAutorizaciones.getDespachoEntregar());
				// --- Cuando se pueda cambiar el articulo despachado se deben cambiar estos campos siguientes 
				autorizacionArticuloDespacho.setCantidadEquivalente(dtoArticulosAutorizaciones.getDespachoEntregar()); 
				autorizacionArticuloDespacho.setArticuloByArticuloDespachado(articuloAutorizado);
				autorizacionArticuloDespachoDAO.attachDirty(autorizacionArticuloDespacho);
				
				// Detalle despachos 
				DetalleDespachosId detDespachoId;	
				detDespachoId = new DetalleDespachosId();
				detDespachoId.setDespacho(despacho.getOrden());
				detDespachoId.setArticulo(articuloAutorizado.getCodigo());
				DetalleDespachos detDespacho;	detDespacho = new DetalleDespachos();
				detDespacho.setId(detDespachoId);
				detDespacho.setDespacho(despacho);
				detDespacho.setArticuloByArticulo(articuloAutorizado);
				detDespacho.setCantidad(dtoArticulosAutorizaciones.getDespachoEntregar());
				detDespacho.setCentrosCosto(centroCosto);
				if(!UtilidadTexto.isEmpty(dtoArticulosAutorizaciones.getObservacionesArticulo())){
					detDespacho.setObservaciones(dtoArticulosAutorizaciones.getObservacionesArticulo());
				}
				detalleDespachosDAO.persist(detDespacho);
				
				// Detalle pedidos 
				DetallePedidosId detallePedidosId;	
				detallePedidosId = new DetallePedidosId();
				detallePedidosId.setArticulo(articuloAutorizado.getCodigo());
				detallePedidosId.setPedido(pedido.getCodigo());
				DetallePedidos detallePedidos;	detallePedidos = new DetallePedidos();
				detallePedidos.setId(detallePedidosId);
				detallePedidos.setPedido(pedido);
				detallePedidos.setArticulo(articuloAutorizado);
				detallePedidos.setCantidad(dtoArticulosAutorizaciones.getDespachoEntregar());
				detallePedidosDAO.persist(detallePedidos);
				
				// Detalle despacho pedidos
				DetalleDespachoPedidoId detalleDespachoPedidoId; 
				detalleDespachoPedidoId = new DetalleDespachoPedidoId();
				detalleDespachoPedidoId.setArticulo(articuloAutorizado.getCodigo());
				detalleDespachoPedidoId.setPedido(pedido.getCodigo());
				
				DetalleDespachoPedido detalleDespachoPedido;	
				detalleDespachoPedido = new DetalleDespachoPedido();
				detalleDespachoPedido.setId(detalleDespachoPedidoId);
				detalleDespachoPedido.setDespachoPedido(despachoPedido);
				detalleDespachoPedido.setArticulo(articuloAutorizado);
				detalleDespachoPedido.setCantidad(dtoArticulosAutorizaciones.getDespachoEntregar());
				detalleDespachoPedidoDAO.persist(detalleDespachoPedido);
			}
			
			RegistroEntregaEntSub registroEntregaEntSub;	
			registroEntregaEntSub = new RegistroEntregaEntSub();
			registroEntregaEntSub.setDespacho(despacho);
			registroEntregaEntSub.setUsuarios(usuarios);
			registroEntregaEntSub.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			registroEntregaEntSub.setFechaModifica(fechaActual);
			registroEntregaEntSub.setHoraModifica(horaActual);
			
			RegistroEntregaEntsubPedido registroEntregaEntsubPedido;	
			registroEntregaEntsubPedido = new RegistroEntregaEntsubPedido();
			registroEntregaEntsubPedido.setPedido(pedido);
			registroEntregaEntsubPedido.setRegistroEntregaEntSub(registroEntregaEntSub);
			HashSet<RegistroEntregaEntsubPedido> setRegistroEntregaEntsubPedido = new HashSet<RegistroEntregaEntsubPedido>();
			setRegistroEntregaEntsubPedido.add(registroEntregaEntsubPedido);
			
			registroEntregaEntSub.setRegistroEntregaEntsubPedidos(setRegistroEntregaEntsubPedido);
 			
			registroEntregaEntSubDAO.attachDirty(registroEntregaEntSub);
			
		} catch (Exception e) 
		{
			Log4JManager.error("Error guardando despacho de medicamentos e insumos",e);
			save = false;
		}
		
		return save;
	}


	
	
	@Override
	public boolean guardarRegistroEntregaMedicamentoInsumoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion, UsuarioBasico usuario) 
	{
		boolean save = true;
		
		/*if(dtoAutorizacionEntSubcontratadasCapitacion.getAutorizacion() <= 0)
		{
			// Si no existe autorizacion de entidad subcontratada no guarda nada
			return save; 
		} */
		
		/** Mensajes  */
		MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.medicamentos.DespachoMedicamentosForm");
		String mensajeNoAutorizacionCapitacion = fuenteMensaje.getMessage("DespachoMedicamentosForm.mensajeNoAutorizacionCapitacion");
		
		
		try {

			AutorizacionesEntidadesSub autorizacionesEntidadesSub; 		
			autorizacionesEntidadesSub = new AutorizacionesEntidadesSub(); 
			autorizacionesEntidadesSub = aurorizacionesEntSubCapitacionHibernateDAO
				.obtenerAutorizacionesEntidadesSubPorId(dtoAutorizacionEntSubcontratadasCapitacion.getAutorizacion());
			
			//---------------------------------- Usuario
			Usuarios usuarios;	
			usuarios = new Usuarios();
			usuarios = usuarioDAO.buscarPorLogin(usuario.getLoginUsuario());
			//----------------------------------
			
			//---------------------------------- Datos de entrega
			Date fechaActual 	= UtilidadFecha.getFechaActualTipoBD();
			String horaActual 	= UtilidadFecha.getHoraActual();
			//----------------------------------
		
			//---------------------------------- Despacho
			Despacho despacho;	
			despacho = new Despacho();
			despacho.setOrden(dtoAutorizacionEntSubcontratadasCapitacion.getNumeroDespacho());
			//----------------------------------
			
			//---------------------------------- Solicitud
			Solicitudes solicitudes;	
			solicitudes = new Solicitudes();
			solicitudes = solicitudesDAO.obtenerSolicitudPorId(dtoAutorizacionEntSubcontratadasCapitacion.getNumeroOrden());
			//----------------------------------
			
			//---------------------------------- Despacho - Detalle despachos - Autorizacion Articulo Despacho
			for (DtoArticulosAutorizaciones dtoArticulosAutorizaciones : dtoAutorizacionEntSubcontratadasCapitacion.getListaArticulos()) 
			{
				Articulo articuloAutorizado;	
				articuloAutorizado = new Articulo();
				articuloAutorizado = articuloDAO.obtenerArticuloPorId(dtoArticulosAutorizaciones.getCodigoArticulo());
				
				AutorizacionesEntSubArticu autorizacionesEntSubArticuPorArticulo; 		
				autorizacionesEntSubArticuPorArticulo = new AutorizacionesEntSubArticu(); 
				autorizacionesEntSubArticuPorArticulo = aurorizacionesEntSubCapitacionHibernateDAO
					.obtenerAutorizacionesEntSubArticuPorId(dtoArticulosAutorizaciones.getAutorizacionPropia());
				
				if(autorizacionesEntSubArticuPorArticulo != null)
				{
					// Autorizacion Articulo
					if(dtoAutorizacionEntSubcontratadasCapitacion.isEntregaTotal()){
						autorizacionesEntSubArticuPorArticulo.setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregado);
					}
					int sumaDespacho = dtoArticulosAutorizaciones.getDespachoTotal() +  dtoArticulosAutorizaciones.getDespachoEntregar();
					if(sumaDespacho == dtoArticulosAutorizaciones.getTotalUnidadesFormulacion() ){
						autorizacionesEntSubArticuPorArticulo.setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregado);
					}
					if(!autorizacionesEntSubArticuPorArticulo.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)){
						aurorizacionesEntSubCapitacionHibernateDAO.attachDirtyAutorizacionesEntSubArticu(autorizacionesEntSubArticuPorArticulo);
					}
					
					// Autorizacion Articulo Despacho
					AutorizacionArticuloDespacho autorizacionArticuloDespacho;	
					autorizacionArticuloDespacho = new AutorizacionArticuloDespacho();
					autorizacionArticuloDespacho.setArticuloByArticuloAutorizado(articuloAutorizado);
					autorizacionArticuloDespacho.setAutorizacionesEntSubArticu(autorizacionesEntSubArticuPorArticulo);
					autorizacionArticuloDespacho.setCantidadAutrizado(dtoArticulosAutorizaciones.getTotalUnidadesFormulacion());
					autorizacionArticuloDespacho.setCantidadDespachada(dtoArticulosAutorizaciones.getDespachoEntregar());
					// --- Cuando se pueda cambiar el articulo despachado se deben cambiar estos campos siguientes 
					autorizacionArticuloDespacho.setCantidadEquivalente(dtoArticulosAutorizaciones.getDespachoEntregar()); 
					autorizacionArticuloDespacho.setArticuloByArticuloDespachado(articuloAutorizado);
					autorizacionArticuloDespachoDAO.attachDirty(autorizacionArticuloDespacho);
					
					
					if(!dtoArticulosAutorizaciones.isTieneAutorizacion())
					{
						SolDespachoSinAuto solDespachoSinAuto;	
						solDespachoSinAuto = new SolDespachoSinAuto();
						solDespachoSinAuto.setArticulo(articuloAutorizado);
						solDespachoSinAuto.setDespacho(despacho);
						solDespachoSinAuto.setSolicitudes(solicitudes);
						solDespachoSinAuto.setObservacion(mensajeNoAutorizacionCapitacion);
						solDespachoSinAutoDAO.attachDirtySolDespachoSinAuto(solDespachoSinAuto);
					}
					
				}
				else{
					SolDespachoSinAuto solDespachoSinAuto;	
					solDespachoSinAuto = new SolDespachoSinAuto();
					solDespachoSinAuto.setArticulo(articuloAutorizado);
					solDespachoSinAuto.setDespacho(despacho);
					solDespachoSinAuto.setSolicitudes(solicitudes);
					solDespachoSinAuto.setObservacion(mensajeNoAutorizacionCapitacion);
					solDespachoSinAutoDAO.attachDirtySolDespachoSinAuto(solDespachoSinAuto);
				}
			}
			
			RegistroEntregaEntSub registroEntregaEntSub;	
			registroEntregaEntSub = new RegistroEntregaEntSub();
			registroEntregaEntSub.setDespacho(despacho);
			registroEntregaEntSub.setUsuarios(usuarios);
			
			if(dtoAutorizacionEntSubcontratadasCapitacion.getAutorizacion() > 0)
			{
				registroEntregaEntSub.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			}
			
			
			registroEntregaEntSub.setFechaModifica(fechaActual);
			registroEntregaEntSub.setHoraModifica(horaActual);
			
			RegistroEntregaEntsubSolici registroEntregaEntsubSolici;	
			registroEntregaEntsubSolici = new RegistroEntregaEntsubSolici();
			registroEntregaEntsubSolici.setSolicitudes(solicitudes);
			registroEntregaEntsubSolici.setRegistroEntregaEntSub(registroEntregaEntSub);
			HashSet<RegistroEntregaEntsubSolici> setRegistroEntregaEntsubSolici = new HashSet<RegistroEntregaEntsubSolici>();
			setRegistroEntregaEntsubSolici.add(registroEntregaEntsubSolici);
			
			registroEntregaEntSub.setRegistroEntregaEntsubSolicis(setRegistroEntregaEntsubSolici);
 			
			registroEntregaEntSubDAO.attachDirty(registroEntregaEntSub);
			
		} catch (Exception e) 
		{
			Log4JManager.error("Error guardando registro de entrega de medicamentos e insumos",e);
			save = false;
		}
		
		return save;
	}
	
	

}
