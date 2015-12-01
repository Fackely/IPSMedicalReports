package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.ConsultaCierreArqueoForm;
import com.princetonsa.actionform.tesoreria.MovimientosCajaForm;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;

/**
 * Action que centraliza las operaciones que son comunes a los Anexos:
 * 
 * 226 - Arqueos Caja,
 * 227 - Arqueo Entrega parcial,
 * 228 - Cierre Turno de Caja.
 * 
 * @author Jorge Armando Agudelo Quintero 
 * 
 */
public class ComunMovimientosCaja extends Action{

	/**
	 * M&eacute;todo que inicializa y reinicia el proceso para realizar un movimiento de caja
	 * de tipo Arqueo Caja, Arqueo Entrega Parcial y Cierre Turno de Caja
	 * @param forma
	 * @param request 
	 * @return
	 */
	public static String accionEmpezar(MovimientosCajaForm forma, HttpServletRequest request,Integer institucion , Integer centroAtencion) {
		try{
			HibernateUtil.beginTransaction();
		
			ICajasCajerosServicio cajasCajerosServicio = getCajasCajerosServicio();
		
			UsuarioBasico usuario=(UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			
			forma.reset();
			forma.resetTipoArqueo();
			
			listarTiposArqueoSegunRol (forma, usuario);
			
			forma.setListadoCajeros((List<DtoUsuarioPersona>) cajasCajerosServicio.obtenerListadoCajeros());
			forma.setListadoCajaCajeroParametrizado(cajasCajerosServicio.consultarCajaCajerosParametrizados(String.valueOf(institucion), String.valueOf(centroAtencion)));
			
			
			forma.setInhabilitaListados(true);
			forma.setInhabilitaFechaArqueo(true);
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR accionEmpezar",e);
			HibernateUtil.abortTransaction();
		}
		return "principal";
	}

	/**
	 * 
	 * M&eacute;todo que se encarga de agregar a la lista de Arqueos disponibles para su selecci&oacute;n
	 * solo los disponibles seg&uacute;n el rol del usuario que realiza el proceso.
	 * 
	 * (Solo si el rol tiene definida la(s) funcionalidad(es))
	 * 
	 * @param list 
	 * 
	 */
	private static void listarTiposArqueoSegunRol(MovimientosCajaForm forma, UsuarioBasico usuario) {
		
		IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
	
		List<TiposMovimientoCaja> movimientosArqueo = movimientosCajaServicio.obtenerListadoTiposArqueo();
		
		if(forma instanceof ConsultaCierreArqueoForm){
			
			forma.setListadoTiposArqueo((ArrayList<TiposMovimientoCaja>) movimientosArqueo);
			
		}else{
		
			ArrayList<TiposMovimientoCaja> movimientosArqueoDefinitivo =  new ArrayList<TiposMovimientoCaja>();
			
			int codigoFuncionalidad = ConstantesBD.codigoNuncaValido;
			
			for (TiposMovimientoCaja tipoMovimientoCajaArqueo : movimientosArqueo) {
				
				if(tipoMovimientoCajaArqueo.getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCaja)
				{
					codigoFuncionalidad = ConstantesBD.codigoFuncionalidadTipoArqueoCaja;
	
				}else if(tipoMovimientoCajaArqueo.getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial)
				{
					codigoFuncionalidad = ConstantesBD.codigoFuncionalidadTipoArqueoEntregaParcial;
				
				}else if(tipoMovimientoCajaArqueo.getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCierreTurno)
				{
					codigoFuncionalidad =  ConstantesBD.codigoFuncionalidadTipoArqueoCierreTurno;
				}
				
				if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), codigoFuncionalidad)){
					
					movimientosArqueoDefinitivo.add(tipoMovimientoCajaArqueo);
				}
			}
			
			forma.setListadoTiposArqueo(movimientosArqueoDefinitivo);
		}
		
	}

	
	/**
	 * M&eacute;todo que dependiendo del tipo de arqueo seleccionado, realiza una serie de verificaciones
	 * para ese movimiento.
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static String accionSeleccionaMovimiento(MovimientosCajaForm forma, UsuarioBasico usuario) {
		try{
			HibernateUtil.beginTransaction();
			forma.setExisteConsecutivoFaltante(true);
			forma.setInhabilitaListados(true);
			
			if(forma.getTipoArqueo()!=null){
				
				if(forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCierreTurno || 
					forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial){
				
					String valorInstitucion = ValoresPorDefecto.getInstitucionManejaCajaPrincipal(usuario.getCodigoInstitucionInt());
					ICajasServicio cajasServicio = TesoreriaFabricaServicio.crearCajasServicio();
					
					if(valorInstitucion.equals(ConstantesBD.acronimoSi)){
		
						forma.setListadoCajasPrincipalMayor(cajasServicio.listarCajasPorCentrosAtencionPorTipoCaja(usuario.getCodigoCentroAtencion(), ConstantesBD.codigoTipoCajaPpal));
						
					}else {
		
						forma.setListadoCajasPrincipalMayor((ArrayList<Cajas>) cajasServicio.listarCajasPorInstitucionPorTipoCaja (usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCajaMayor));
					}
					
					forma.setExisteConsecutivoFaltante(consecutivoFaltanteSobranteParametrizado(usuario.getCodigoInstitucionInt()));
				}
				
				forma.setInhabilitaListados(false);
			}
			
			forma.setFechaArqueo(null);
			forma.resetListados();
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR accionSeleccionaMovimiento",e);
			HibernateUtil.abortTransaction();
		}
		return "principal";
	}
	
	
	/**
	 * @param forma
	 * @param request
	 * @param institucion
	 * @param centroAtencion
	 */
	public static  void consultarCajerosPorCajaSeleccionada(MovimientosCajaForm forma, HttpServletRequest request,Integer institucion , Integer centroAtencion){
		
		try{
			HibernateUtil.beginTransaction();
			forma.setFechaArqueo(null);
			
			ICajasCajerosServicio cajasCajerosServicio = getCajasCajerosServicio();
			
			if(forma.getCajero()!=null){
				forma.setListadoDatosCaja(cajasCajerosServicio.consultarDatosCajaParametrizados(forma.getCajero().getCodigo(),String.valueOf(institucion), String.valueOf(centroAtencion)));
			}else{
				DtoUsuarioPersona cajero=cajasCajerosServicio.obtenerCajeroXCodigo(forma.getCodigoCajeroHelper());
				forma.setCajero(cajero);
				forma.setListadoDatosCaja(cajasCajerosServicio.consultarDatosCajaParametrizados(null,String.valueOf(institucion), String.valueOf(centroAtencion)));
			}
		
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR consultarCajerosPorCajaSeleccionada",e);
			HibernateUtil.abortTransaction();
		}
	}
	
	/**
	 * M&eacute;todo que cambia el listado de cajas disponibles para seleccionar, seg&uacute;n el 
	 * cajero seleccionado previamente.
	 * 
	 * @param forma
	 * @param usuario
	 * @param validarSelectCaja 
	 * @return
	 */ 
	public static String accionSeleccionaCajero(MovimientosCajaForm forma, UsuarioBasico usuario, ActionErrors errores, boolean validarSelectCaja) {
		try{
			HibernateUtil.beginTransaction();
			ICajasServicio cajasServicio = getCajasServicio();
			forma.setFechaArqueo(null);
			forma.setCajaHelper(ConstantesBD.codigoNuncaValido);
			
			if(forma.getCajero()!=null){
				forma.setListadoCajas((ArrayList<Cajas>) cajasServicio.obtenerCajasPorCajeroActivasXCentroAtencion(forma.getCajero(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoTipoCajaRecaudado));
			
				if(forma.getListadoCajas()!=null && forma.getListadoCajas().size()==1){
					
					forma.setCajaHelper(forma.getListadoCajas().get(0).getConsecutivo());
					
					if(validarSelectCaja){
						
						accionSeleccionaCaja(forma, usuario, errores);
					}
					
				}
			}else{
				
				forma.setListadoCajas(null);
			}
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR accionSeleccionaCajero",e);
			HibernateUtil.abortTransaction();
		}
		return "principal";
	}
	
	/**
	 * M&eacute;todo que se encarga de consultar para la caja y el cajero, si existe un turno 
	 * de caja abierto. De ser asi, se postula en la fecha del sistema, la fecha del &uacute;ltimo movimiento 
	 * registrado para la caja/cajero/turno abierto correspondiente.
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static String accionSeleccionaCaja(MovimientosCajaForm forma, UsuarioBasico usuario, ActionErrors errores) {
		
		if(forma.getCajero()!=null && forma.getCaja()!=null){
			
			UtilidadTransaccion.getTransaccion().begin();
			
			TurnoDeCaja turnoDeCaja = getMovimientosCajaServicio().obtenerTurnoCajaAbiertoPorCajaCajero(forma.getCajero(), forma.getCaja().getConsecutivo(), usuario.getCodigoCentroAtencion());
			
			if(turnoDeCaja!=null){
				
				forma.setTurnoDeCaja(turnoDeCaja);
				forma.setInhabilitaFechaArqueo(false);
				
				if(forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCierreTurno || 
						forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial){
					
					IMovimientosCajaServicio movimientosCajaServicio = TesoreriaFabricaServicio.crearMovimientosCajaServicio();
					long timeUltimoMovimiento = movimientosCajaServicio.obtenerUltimaFechaMovimientoCaja(turnoDeCaja);
					
					forma.setFechaUltimoMovimiento(timeUltimoMovimiento);
					
					if(timeUltimoMovimiento != ConstantesBD.codigoNuncaValidoLong){
						
						forma.setFechaArqueo(new Date(timeUltimoMovimiento));
						
					}else{
						
						forma.setFechaArqueo(UtilidadFecha.getFechaActualTipoBD());
					}
				}
			}else{

				cargarMensaje(forma, errores, 1);
				forma.resetListados();
			}
			
			HibernateUtil.endTransaction();
			
		}else{
			
			forma.setFechaArqueo(null);
			forma.setCajaRecaudoHelper(ConstantesBD.codigoNuncaValido);
		}
	
		return "principal";
	}
	
	/**
	 * M&eacute;todo que se encarga de crear un movimiento de un tipo de arqueo espec&iacute;fico, asignandole
	 * un grupo de informaci&oacute;n b&aacute;sica.
	 * 
	 * @param forma
	 * @param usuario
	 * @param errores
	 * @return
	 */
	public static MovimientosCaja generarArqueo(MovimientosCajaForm forma, UsuarioBasico usuario, ActionErrors errores) {
		
		TurnoDeCaja turnoDeCaja = forma.getTurnoDeCaja();
		
		if(turnoDeCaja!=null){
			 
			boolean resultado = false;
			
			if(forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCaja){
				resultado = validarFechaArqueo(forma, errores, turnoDeCaja.getFechaApertura().getTime());
			} else {
				resultado = validarFechaArqueoParcialCierre(forma, errores, turnoDeCaja.getFechaApertura().getTime());
			}
			
			if(resultado){
				MovimientosCaja movimientosCaja = new MovimientosCaja();
				try{
					HibernateUtil.beginTransaction();
					IUsuariosServicio usuariosServicio = getUsuariosServicio ();
					movimientosCaja.setFechaMovimiento(forma.getFechaArqueo());
					movimientosCaja.setTiposMovimientoCaja(forma.getTipoArqueo());
					movimientosCaja.setUsuarios(usuariosServicio.buscarPorLogin(usuario.getLoginUsuario()));
					movimientosCaja.setTurnoDeCaja(turnoDeCaja);
					movimientosCaja.setContabilizado('N');
	
					asignarTipoFuncionalidad(forma);
					forma.getConsolidadoMovimientoDTO().setMovimientosCaja(movimientosCaja);
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();					
				}
				return movimientosCaja;
			}
			
		}else{
			
			cargarMensaje(forma, errores, 1);
			forma.resetListados();
		}

		return null;
		
	}
	
	
	/**
	 * M&eacute;todo que se encarga de validar si la fecha seleccionada cumple 
	 * con las restricciones impuestas por la funcionalidad.
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static boolean validarFechaArqueoParcialCierre(MovimientosCajaForm forma, ActionErrors errores, long fechaAperturaTurnoCaja) {
		
		boolean validacion = true;
		
		long fechaArqueo = forma.getFechaArqueo().getTime();
		long fechaSistema = UtilidadFecha.getFechaActualTipoBD().getTime();
		long fechaAComparar = forma.getFechaUltimoMovimiento();
		int codigoError = 0;
		
		
		/*
		 * En el caso que no existan movimientos de caja registrados, se debe tener en cuenta para
		 * la validación, la fecha de apertura del turno de caja.
		 */
		if(fechaAComparar == ConstantesBD.codigoNuncaValidoLong){
			
			fechaAComparar = fechaAperturaTurnoCaja;
		}
		
		if(!(fechaArqueo <= fechaSistema && fechaArqueo >= fechaAComparar)){
			
			validacion = false;
			
			if(fechaArqueo > fechaSistema){
				
				codigoError = 2;
				
			}else if(fechaArqueo < fechaAComparar){
				
				if(forma.getFechaUltimoMovimiento() != ConstantesBD.codigoNuncaValidoLong ){
					
					codigoError = 3;
				
				}else{
					
					codigoError = 4;
				}
			}
			
			cargarMensaje(forma, errores, codigoError);
	
		}
		
		return validacion;
	}

	/**
	 * M&eacute;todo que se encarga de validar si la fecha seleccionada cumple 
	 * con las restricciones impuestas por la funcionalidad.
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static boolean validarFechaArqueo(MovimientosCajaForm forma, ActionErrors errores, long fechaAperturaTurnoCaja) {
		
		boolean validacion = true;
		
		if (forma.getFechaArqueo() != null) {
			long fechaArqueo = forma.getFechaArqueo().getTime();
			long fechaSistema = UtilidadFecha.getFechaActualTipoBD().getTime();
			long fechaAComparar = fechaAperturaTurnoCaja;
			int codigoError = 0;

			if(!(fechaArqueo <= fechaSistema && fechaArqueo >= fechaAComparar)){

				validacion = false;

				if(fechaArqueo > fechaSistema){

					codigoError = 2;

				}else if(fechaArqueo < fechaAComparar){

					codigoError = 4;

				}

				cargarMensaje(forma, errores, codigoError);

			}
		}
		return validacion;
	}
	
	/**
	 * Asigna el tipo de funcionalidad dependiendo del movimiento seleccionado
	 * @autor Jorge Armando Agudelo - Luis Alejandro Echandia
	 * @param forma
	 * @since 24/06/2010
	 */
	public static void asignarTipoFuncionalidad(MovimientosCajaForm forma) {
		
		if(forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCaja)
		{
			forma.setTipoFuncionalidad(ConstantesBD.codigoFuncionalidadTipoArqueoCaja);
		
		}else if(forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial)
		{
			forma.setTipoFuncionalidad(ConstantesBD.codigoFuncionalidadTipoArqueoEntregaParcial);
		
		}else if(forma.getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCierreTurno)
		{
			forma.setTipoFuncionalidad(ConstantesBD.codigoFuncionalidadTipoArqueoCierreTurno);
		}
	}

	/**
	 * M&eacute;todo que indica si se encuentra realizada la parametrizaci&oacute;n del 
	 * registro del consecutivo para los faltante / sobrante que se generen
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean consecutivoFaltanteSobranteParametrizado (int codigoInstitucion){
		
		Connection con=HibernateUtil.obtenerConexion();
		String valor=UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante, codigoInstitucion);
		boolean existe = false;
		
		if(UtilidadTexto.isNumber(valor)){
			
			double valorConsecutivo = Utilidades.convertirADouble(valor);
			
			if(valorConsecutivo>0){
				
				existe = true;
			}
		}
		
		return existe;
		
	}
	
	/**
	 * M&eacute;todo que se encarga de cargar el respectivo mensaje de error, seg&uacute;n un indicativo
	 * 
	 * @param forma
	 * @param errores
	 * @param tipoMensaje
	 */
	private static void cargarMensaje(MovimientosCajaForm forma,ActionErrors errores, int tipoMensaje) {
		
		MessageResources mensages=MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.MovimientosCajaForm");
		String mensaje = "";
		String nombreCompleto = forma.getCajero().getNombre()+" "+forma.getCajero().getApellido();
		
		switch (tipoMensaje) {
		
		/*
		 * Este caso corresponde a un error relacionado a la apertura del turno
		 */
		case 1:
			
			mensaje = mensages.getMessage("MovimientosCajaForm.informar.noExisteTurnoAbierto", nombreCompleto, forma.getCaja().getDescripcion() , forma.getTipoArqueo().getDescripcion());
			
			errores.add("Sin Turno Abierto", new ActionMessage("errors.notEspecific", mensaje));

			break;

		/*
		 * Este caso corresponde a un error relacionado con la fecha
		 */
		case 2:
			
			mensaje = mensages.getMessage("MovimientosCajaForm.informar.menorIgualFechaSistema", UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaArqueo()), 
						UtilidadFecha.getFechaActual()); 
			
			errores.add("Menor Igual Fecha Sistema", new ActionMessage("errors.notEspecific", mensaje));

			break;	
		
		/*
		 * Este caso corresponde a un error relacionado con la fecha
		 */
		case 3:
			
			mensaje = mensages.getMessage("MovimientosCajaForm.informar.mayorIgualFechaMovimiento", UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaArqueo()), 
						 UtilidadFecha.conversionFormatoFechaAAp(new Date(forma.getFechaUltimoMovimiento())));
			
			errores.add("Mayor Igual Fecha Movimiento", new ActionMessage("errors.notEspecific", mensaje));

			break;	
	
		/*
		 * Este caso corresponde a un error relacionado con la fecha
		 */
		case 4:
			
			mensaje = mensages.getMessage("MovimientosCajaForm.informar.mayorIgualFechaApertura",  UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaArqueo()),
					UtilidadFecha.conversionFormatoFechaAAp(forma.getTurnoDeCaja().getFechaApertura()));
			
			errores.add("Mayor Igual Fecha Apertura", new ActionMessage("errors.notEspecific", mensaje));

			break;	
			
		default:
			break;
		}
		
		forma.setFechaArqueo(null);
	
	}
	
	/**
	 * M&eacute;todo que se encarga de devolver una instancia del objeto que implementa la interfaz
	 * MovimientosCajaServicio
	 *
	 * @return IMovimientosCajaServicio
	 */
	private static IMovimientosCajaServicio getMovimientosCajaServicio() {
		
		return TesoreriaFabricaServicio.crearMovimientosCajaServicio();
	}
	
	/**
	 * M&eacute;todo que se encarga de devolver una instancia del objeto que implementa la interfaz
	 * ICajasCajerosServicio
	 *
	 * @return ICajasCajerosServicio
	 */
	public static ICajasCajerosServicio getCajasCajerosServicio() {
		return TesoreriaFabricaServicio.crearCajasCajerosServicio();
	}

	/**
	 * M&eacute;todo que se encarga de devolver una instancia del objeto que implementa la interfaz
	 * ICajasServicio
	 *
	 * @return ICajasServicio
	 */
	public static ICajasServicio getCajasServicio() {
		return TesoreriaFabricaServicio.crearCajasServicio();
	}
	
	/**
	 * M&eacute;todo que se encarga de devolver una instancia del objeto que implementa la interfaz
	 * IUsuariosServicio
	 *
	 * @return IUsuariosServicio
	 */
	public static IUsuariosServicio getUsuariosServicio() {
		return AdministracionFabricaServicio.crearUsuariosServicio();
	}

	
}
