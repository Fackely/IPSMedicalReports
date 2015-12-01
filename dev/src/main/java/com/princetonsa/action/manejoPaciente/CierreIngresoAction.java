package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.CierreIngresoForm;
import com.princetonsa.mundo.CuentasPaciente;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.CierreIngreso;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Mauricio Jaramillo
 * Fecha Mayo de 2008
 */

public class CierreIngresoAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(CierreIngresoAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof CierreIngresoForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				CierreIngresoForm forma = (CierreIngresoForm) form;
				CierreIngreso mundo = new CierreIngreso();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado = forma.getEstado();
				logger.warn("[CierreIngreso]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(con, paciente, forma,  usuario, mapping, request);
				}
				else if(estado.equals("cerrar"))
				{
					return this.accionCerrar(con, paciente, forma, mundo, usuario, mapping, request);
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
				logger.error("El form no es compatible con el form de CierreIngresoForm");
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
	 * Metodo encargado de cerrar el ingreso del paciente
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionCerrar(Connection con, PersonaBasica paciente, CierreIngresoForm forma, CierreIngreso mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		HashMap vo = new HashMap();
		boolean cierre ;
		//Realizar el split del motivo ingreso y descripcion ingreso
		String ingreso[];
		ingreso = forma.getMotivoCierre().split(ConstantesBD.separadorSplit);
		forma.setMotivoCierre(ingreso[0]);
		forma.setDesIngreso(ingreso[1]);
		//Se llena el mapa de datos para insertar
		vo.put("idIngreso", forma.getIdIngreso());
		vo.put("motivoCierre", forma.getMotivoCierre());
		vo.put("usuarioCierre", usuario.getLoginUsuario());
		vo.put("activo", ConstantesBD.acronimoSi);
		vo.put("institucion", usuario.getCodigoInstitucion());
		UtilidadBD.iniciarTransaccion(con);
		cierre = mundo.cerrarIngreso(con, vo);
		//Se vuelve a resetear variable errores para que no pinte en la JSP
		forma.setErrores(false);
		if(cierre)
		{
			forma.setMensaje(new ResultadoBoolean(true, "El ingreso "+paciente.getConsecutivoIngreso()+" del paciente ha cambiado a estado Cerrado sin ningún problema"));
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true, "Se presentaron problemas en el cierre del ingreso "+paciente.getConsecutivoIngreso()));
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
		setObservable(paciente, request, true);
		
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que inicializa la funcion validando y cargando en la forma
	 * los datos de la cuenta del paciente que se le pretende cerrar
	 * el ingreso
	 * @param con
	 * @param paciente
	 * @param forma
	 * @param medico
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(Connection con, PersonaBasica paciente, CierreIngresoForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws SQLException, IPSException
	{
		
		//*****************************VALIDACIONES INICIALES**************************************************************
		/**
		 * Validacion Cargado Paciente
		 */
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		if(!resp.puedoSeguir)
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true);
		
		/* davgommo MT 5540 SOLO SE DEJAN LAS VALIDACIONES DE PACIENTES CARGADO Y CUENTA EN ESTADO ACTIVO O FACTURADA PARCIAL
//		if(UtilidadValidacion.puedoCrearCuentaAsocio(con, paciente.getCodigoIngreso()))
//			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.asocioPendiente", "errors.paciente.asocioPendiente", true);
	/**
		 * Validacion Centro de Atencion del paciente con el de sesion
		 * Se valida el centro de atencion en la linea anterior ya que
		 * cuando el centro atencion no coincide con el de sesion no se
		 * cargan los ingresos de ese paciente en ese centro de atencion
		 */
		/*if(Utilidades.obtenerCentroAtencionPaciente(con, paciente.getCodigoArea()) != usuario.getCodigoCentroAtencion())
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.manejoPaciente.centroAtencionInvalido", "error.manejoPaciente.centroAtencionInvalido", true);
		}*/
		
		/**
		 * Validacion Estado Ingreso "Abierto"
		 */
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		
		/**
		 * Validamos si el ingreso tiene preingresos pendientes
		 * Si no tiene preingreso pendiente se debe validar el egreso medico
		 */
		// davgommo MT 5540 SOLO SE DEJAN LAS VALIDACIONES DE PACIENTES CARGADO Y CUENTA EN ESTADO ACTIVO O FACTURADA PARCIAL
//		if(UtilidadesManejoPaciente.tienePreingresoPendiente(con, usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona()) == ConstantesBD.codigoNuncaValido)
//		{
//			/**
//			 * Validar Estados Historia Clinica
//			 */
//			if(UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(), usuario.getCodigoInstitucionInt(), false, true).isTrue())
//				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.manejoPaciente.cierreIngresos", "error.manejoPaciente.cierreIngresos", true);
//			
//			/*
//			 * Se modifico la validacion del egreso medico por el egreso administrativo
//			 * por la tarea 10724
//			 */
//			/**
//			 * Validacion verificar el egreso administrativo para
//			 * la via de ingreso de hospitalizacion y de urgencias
//			 */
//			if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
//			{
//				if(!UtilidadValidacion.existeEgresoCompleto(con, paciente.getCodigoCuenta()))
//					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.pacienteSinSalidaoEgreso", "error.facturacion.pacienteSinSalidaoEgreso", true);
//			}
//			
//			/**
//			 * Validacion verificar el egreso medico si la via de ingreso es Hospitalizacion
//			 */
//			/*if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
//			{
//				if(Utilidades.obtenerDestinoSalidaEgresoEvolucion(con, paciente.getCodigoCuenta()) == ConstantesBD.codigoNuncaValido)
//					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.pacienteSinSalidaoEgreso", "error.facturacion.pacienteSinSalidaoEgreso", true);
//			}*/
//			
//			/**
//			 * Validacion verificar el egreso medico si la via de ingreso es Urgencias
//			 */
//			/*else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
//			{
//				int destinoSalida = Utilidades.obtenerDestinoSalidaEgresoEvolucion(con, paciente.getCodigoCuenta());
//				if(destinoSalida == ConstantesBD.codigoNuncaValido)
//				{
//					resp = UtilidadValidacion.existeEgresoAutomatico(con, paciente.getCodigoCuenta());
//					if(!resp.puedoSeguir)
//						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.pacienteSinSalidaoEgreso", "error.facturacion.pacienteSinSalidaoEgreso", true);
//				}
//			}*/
//		}
		//*****************************FIN VALIDACIONES INICIALES**********************************************************
		
		//Despues de validar se llena la forma con la informacion del paciente
		forma.reset();
		forma.setIdIngreso(paciente.getCodigoIngreso());
		forma.setFechaIngreso(paciente.getFechaIngreso());
		forma.setHoraIngreso(paciente.getHoraIngreso());
		//Captura la información de las cuentas del paciente
		int tamano = paciente.getCuentasPacienteArray().size();
		forma.setCuentasIngreso("numRegistros", tamano);
		for(int i = 0; i<tamano; i++)
		{
			CuentasPaciente cpacientes = (CuentasPaciente)paciente.getCuentasPacienteArray(i);
			forma.setCuentasIngreso("codigoCuenta_"+i, cpacientes.getCodigoCuenta());
			forma.setCuentasIngreso("estadoCuenta_"+i, cpacientes.getDescripcionEstadoCuenta());
			forma.setCuentasIngreso("descViaIngreso_"+i, cpacientes.getDescripcionViaIngreso());
			forma.setCuentasIngreso("descConvenio_"+i, UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, paciente.getCodigoIngreso(), true, new String[0], false, "" /*subCuenta*/,paciente.getCodigoUltimaViaIngreso()));
		}
		//Utilidades.imprimirMapa(forma.getCuentasIngreso());
		forma.setTiposMotivoCierre(Utilidades.obtenerMotivosAperturaCierre(con, ConstantesIntegridadDominio.acronimoCierreIngreso));
		forma.setMensaje(new ResultadoBoolean(true, "Este proceso cambia el estado del ingreso "+paciente.getConsecutivoIngreso()+" a Cerrado. De estar seguro por favor confirmar."));
		return mapping.findForward("principal");
	}
	
	/**
	 * Método para hacer que el paciente
	 * pueda ser visto por todos los usuario en la aplicacion
	 * @param paciente
	 */
	private void setObservable(PersonaBasica paciente, HttpServletRequest request, boolean cargarPaciente)
	{
		if(cargarPaciente)
		{	
			/**para cargar el paciente que corresponda**/
			Connection con=UtilidadBD.abrirConexion();
			try {
				paciente.cargar(con,paciente.getCodigoPersona());
			} catch (SQLException e) {
				// @TODO Auto-generated catch block
				e.printStackTrace();
			}
			UtilidadBD.closeConnection(con);
		}	
		
		//Código necesario para registrar este paciente como Observer
		ObservableBD observable = (ObservableBD) getServlet().getServletContext().getAttribute("observable");
		if (observable != null) 
		{
			paciente.setObservable(observable);
			// Si ya lo habíamos añadido, la siguiente línea no hace nada
			observable.addObserver(paciente);
		}
		//Se sube a sesión el paciente activo
		request.getSession().setAttribute("pacienteActivo", paciente);
		
	    if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
}