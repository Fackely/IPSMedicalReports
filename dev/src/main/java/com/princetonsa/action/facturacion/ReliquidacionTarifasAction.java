package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ReliquidacionTarifasForm;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.ReliquidacionTarifas;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author wilson
 *
 */
public class ReliquidacionTarifasAction extends Action
{
	 /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ReliquidacionTarifasAction.class);
	
	/**
	 * 
	 */
	private static final String[] indicesEsquemasInventario={"claseinventario_","nombreclaseinventario_","esquematarifario_","nombreesquematarifario_"};
		
	/**
	 * 
	 */
	private static final String[] indicesEsquemasProcedimientos={"gruposervicio_","nombregruposervicio_","esquematarifario_","nombreesquematarifario_"};


	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ReliquidacionTarifasForm)
			{

				con = UtilidadBD.abrirConexion();	

				ReliquidacionTarifasForm forma =(ReliquidacionTarifasForm)form;
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=forma.getEstado(); 
				logger.info("\n\n\nESTADO ReliquidacionTarifasAction ------------------------------------------------------>"+estado);

				ActionForward forward= validacionesAcceso(paciente, mapping, request, con, usuario);
				if(forward!=null)
					return forward;

				if(UtilidadTexto.isEmpty(estado))
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de reliquidacion de tarifas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(con, mapping, forma, usuario, paciente, request);
				}
				else if(estado.equals("seleccionTipoLiquidacion"))
				{
					return this.accionSeleccionTipoLiquidacion(con, mapping,paciente, forma, usuario);
				}
				else if(estado.equals("refrescarEsquemas") || estado.equals("procesoExitoso"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("tipoLiquidacion");
				}
				else if(estado.equals("nuevoEsquemaInventarios"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getEsquemasInventario(), indicesEsquemasInventario, "numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("tipoLiquidacion");
				}
				else if(estado.equals("nuevoEsquemaProcedimientos"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getEsquemasProcedimientos(), indicesEsquemasProcedimientos, "numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("tipoLiquidacion");
				}
				else if(estado.equals("eliminarEsquemaInventarios"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getEsquemasInventario(),new HashMap(),forma.getPosEliminar(),indicesEsquemasInventario,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("tipoLiquidacion");
				}
				else if(estado.equals("eliminarEsquemaProcedimientos"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getEsquemasProcedimientos(),new HashMap(),forma.getPosEliminar(),indicesEsquemasProcedimientos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("tipoLiquidacion");
				}
				else if(estado.equals("reliquidar"))
				{
					return this.accionReliquidar(con, mapping, forma, usuario, paciente, request); 
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
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionReliquidar(Connection con, ActionMapping mapping, ReliquidacionTarifasForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException 
	{
		String resultadoExitoso="";
		
		Cuenta cuenta=new Cuenta();
		cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
		String fechaApertura=cuenta.getCuenta().getFechaApertura();
		//1. INICIAMOS LA TRANSACCION
		UtilidadBD.iniciarTransaccion(con);
		forma.setErroresCargo(new Vector());
		
		//2. cargamos las solicitudes articulos - servicios segun corresponda, sin tener encuenta las de cx
		HashMap solicitudesMap=new HashMap();
		if(forma.getReliquidarServicios() && forma.getReliquidarArticulos())
		{
			solicitudesMap= ReliquidacionTarifas.obtenerSolicitudesReliquidar(con, forma.getListadoCuentasMap("subcuenta_"+forma.getIndexMapa()).toString(), "");
		}
		else if(forma.getReliquidarServicios())
		{
			solicitudesMap= ReliquidacionTarifas.obtenerSolicitudesReliquidar(con, forma.getListadoCuentasMap("subcuenta_"+forma.getIndexMapa()).toString(), ConstantesBD.acronimoSi);
		}
		else if(forma.getReliquidarArticulos())
		{
			solicitudesMap= ReliquidacionTarifas.obtenerSolicitudesReliquidar(con, forma.getListadoCuentasMap("subcuenta_"+forma.getIndexMapa()).toString(), ConstantesBD.acronimoNo);
		}
		
		for(int w=0; w<Utilidades.convertirAEntero(solicitudesMap.get("numRegistros").toString()); w++)
		{
			int codigoTipoSolicitud= Utilidades.convertirAEntero(solicitudesMap.get("codigotiposolicitud_"+w).toString());
			int numeroSolicitud= Utilidades.convertirAEntero(solicitudesMap.get("numerosolicitud_"+w).toString());
			int estadoCargo= Utilidades.convertirAEntero(solicitudesMap.get("estadocargo_"+w).toString());
			double codigoDetalleCargoAntesMod= Utilidades.convertirADouble(solicitudesMap.get("codigocargo_"+w).toString());
			int cantidad=Utilidades.convertirAEntero(solicitudesMap.get("cantidad_"+w).toString());
			String esPortatil= solicitudesMap.get("esportatil_"+w)+"";
			
			if((codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialHospitalizacion
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialUrgencias
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudEvolucion
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudEstancia
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCita
					|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudPaquetes) && forma.getReliquidarServicios())
			{
				Cargos cargos= new Cargos();
				try
				{
					/*comienzo la transacción para la generación del cambio*/
					cargos.recalcularCargoServicioValidandoCobertura(con, numeroSolicitud, usuario, ConstantesBD.codigoNuncaValido, paciente.getCodigoIngreso(),"" /*observaciones*/,Utilidades.convertirAEntero(solicitudesMap.get("servicio_"+w).toString()) , ConstantesBD.codigoNuncaValido/*subCuentaOpcional*/, this.obtenerEsquemaTarifario(solicitudesMap.get("gruposervicio_"+w).toString(),forma,true), false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, esPortatil/*esPortatil*/,fechaApertura);
					
					if(estadoCargo==ConstantesBD.codigoEstadoFInactiva || estadoCargo==ConstantesBD.codigoEstadoFExento)
					{	
						//dejamos nuevamente el estado del cargo como estaba para el caso de inactiva y excento
						Cargos.modificarEstadoCargo(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), estadoCargo);
					}	
				}
				catch(Exception e)
				{
					logger.warn("No se pudo generar el cargo "+e);
					forma.reset();
					ArrayList atributosError = new ArrayList();
					atributosError.add("Error generando el cargo ");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");	
				}
				
				//adicionamos los errores al vector
				if(cargos.getInfoErroresCargo().getTieneErrores())
				{
					for(int x=0; x<cargos.getInfoErroresCargo().getMensajesErrorDetalle().size(); x++)
					{	
						forma.setErroresCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle().get(x).toString());
					}	
				}
				
				if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudPaquetes)
				{
					//despues de calcularlo debemos actualizar los componentes del paquete
					ActionForward forward= recalcularCargoComponentesPaquetes(con, forma.getListadoCuentasMap("subcuenta_"+forma.getIndexMapa()).toString(),  cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), usuario, forma, request, mapping, true,paciente.getCodigoIngreso());
					codigoDetalleCargoAntesMod=cargos.getDtoDetalleCargo().getCodigoDetalleCargo();
					if(forward!=null)
						return forward;
				}
			}
			//no poner else
			if((codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudMedicamentos || 
			        codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos ||
			        codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudPaquetes) && forma.getReliquidarArticulos())
			{
				//solamente debe recalcular los componenetes de los paquetes tipo articulo
				if(codigoTipoSolicitud!=ConstantesBD.codigoTipoSolicitudPaquetes)
				{
					//generarCagroMedicamentos
					Cargos cargos= new Cargos();
					boolean inserto=cargos.recalcularCargoArticuloValidandoCobertura(con, numeroSolicitud, paciente.getCodigoIngreso(),usuario, Utilidades.convertirAEntero(solicitudesMap.get("articulo_"+w).toString()) , ConstantesBD.codigoNuncaValido /*subCuentaOPCIONAL*/, cantidad /*cantidadArticuloOPCIONAL*/, this.obtenerEsquemaTarifario(solicitudesMap.get("clasearticulo_"+w).toString(),forma,false), false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/,Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud));
					
					if(!inserto)
					{
						logger.warn("No se pudo generar el cargo ");
						forma.reset();
						ArrayList atributosError = new ArrayList();
						atributosError.add("Error generando el cargo ");
						request.setAttribute("codigoDescripcionError", "errors.invalid");				
						request.setAttribute("atributosError", atributosError);
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaError");
					}
					
					if(estadoCargo==ConstantesBD.codigoEstadoFInactiva || estadoCargo==ConstantesBD.codigoEstadoFExento)
					{	
						//dejamos nuevamente el estado del cargo como estaba para el caso de inactiva y excento
						Cargos.modificarEstadoCargo(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), estadoCargo);
					}
					
					//adicionamos los errores al vector
					if(cargos.getInfoErroresCargo().getTieneErrores())
					{
						for(int x=0; x<cargos.getInfoErroresCargo().getMensajesErrorDetalle().size(); x++)
						{	
							forma.setErroresCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle().get(x).toString());
						}	
					}
				}
				else
				{
					logger.info("LLEGA A RECALCULAS LOS COMPONENTES DE PAQUETES TIPO ARTICULO");
					//despues de calcularlo debemos actualizar los componentes del paquete
					ActionForward forward= recalcularCargoComponentesPaquetes(con, forma.getListadoCuentasMap("subcuenta_"+forma.getIndexMapa()).toString(),  codigoDetalleCargoAntesMod, usuario, forma, request, mapping, false,paciente.getCodigoIngreso()); 
					if(forward!=null)
						return forward;
				}
			}
		}
		
		logger.info("\n\n errores->"+forma.getErroresCargo());
		
		if(forma.getErroresCargo().size()>0)
		{	
			UtilidadBD.abortarTransaccion(con);
			resultadoExitoso=ConstantesBD.acronimoNo;
		}	
		else
		{	
			//UtilidadBD.abortarTransaccion(con);
			UtilidadBD.finalizarTransaccion(con);
			forma.setEstado("procesoExitoso");
			resultadoExitoso=ConstantesBD.acronimoSi;
		}
		
		//insertamos el log tipo bd
		int esqTarServNuevo=ConstantesBD.codigoNuncaValido;
		int esqTarArtNuevo=ConstantesBD.codigoNuncaValido;

		//TODO insertar el log de la reliquidacion de tarifas.
		//ReliquidacionTarifas.insertarReliquidacion(con, usuario.getLoginUsuario(), resultadoExitoso, forma.getEsquemasProcedimientos(), forma.getEsquemasProcedimientos());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tipoLiquidacion");
	}

	/**
	 * 
	 * @param string
	 * @param forma
	 * @param esServicio
	 * @return
	 */
	private int obtenerEsquemaTarifario(String grupoClase, ReliquidacionTarifasForm forma, boolean esServicio) 
	{
		int esquemaTarifario=ConstantesBD.codigoNuncaValido;
		if(esServicio)
		{
			for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("numRegistros")+"");i++)
			{
				if(Utilidades.convertirAEntero(grupoClase)==Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("gruposervicio_"+i)+""))
				{
					return Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("esquematarifario_"+i)+"");
				}
				else if(UtilidadTexto.isEmpty(forma.getEsquemasProcedimientos().get("gruposervicio_"+i)+""))//esquema generico
				{
					esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("esquematarifario_"+i)+"");
				}
					
			}
		}
		else
		{
			for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasInventario().get("numRegistros")+"");i++)
			{
				if(Utilidades.convertirAEntero(grupoClase)==Utilidades.convertirAEntero(forma.getEsquemasInventario().get("claseinventario_"+i)+""))
				{
					return Utilidades.convertirAEntero(forma.getEsquemasInventario().get("esquematarifario_"+i)+"");
				}
				else if(UtilidadTexto.isEmpty(forma.getEsquemasInventario().get("claseinventario_"+i)+""))//esquema generico
				{
					esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasInventario().get("esquematarifario_"+i)+"");
				}
			}
		}
		return esquemaTarifario;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoDetalleCargoPadre
	 * @param esServicios
	 * @param usuario
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param ingreso 
	 * @return
	 */
	private ActionForward recalcularCargoComponentesPaquetes(Connection con, String subCuenta, double codigoDetalleCargoPadreNuevo, UsuarioBasico usuario, ReliquidacionTarifasForm forma, HttpServletRequest request, ActionMapping mapping, boolean esServicios, int ingreso) throws IPSException 
	{
		if(esServicios)
		{	
			HashMap solicitudesMap= ReliquidacionTarifas.obtenerComponentesPaqueteReliquidar(con, subCuenta, codigoDetalleCargoPadreNuevo, ConstantesBD.acronimoSi);
			
			for(int w=0; w<Utilidades.convertirAEntero(solicitudesMap.get("numRegistros").toString()); w++)
			{
				int numeroSolicitud= Utilidades.convertirAEntero(solicitudesMap.get("numerosolicitud_"+w).toString());
				int estadoCargo= Utilidades.convertirAEntero(solicitudesMap.get("estadocargo_"+w).toString());
				String esPortatil= solicitudesMap.get("esportatil_"+w)+"";
				
				Cargos cargos= new Cargos();
				try
				{
					/*comienzo la transacción para la generación del cambio*/
					cargos.recalcularCargoServicioValidandoCobertura(con, numeroSolicitud, usuario, ConstantesBD.codigoNuncaValido,ingreso, "" /*observaciones*/, Utilidades.convertirAEntero(solicitudesMap.get("servicio_"+w).toString()) , ConstantesBD.codigoNuncaValido/*subCuentaOpcional*/, this.obtenerEsquemaTarifario(solicitudesMap.get("gruposervicio_"+w).toString(),forma,true), true /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoSi /*esComponentePaquete*/, esPortatil/*esPortatil*/,Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud));
					
					if(estadoCargo==ConstantesBD.codigoEstadoFInactiva || estadoCargo==ConstantesBD.codigoEstadoFExento)
					{	
						//dejamos nuevamente el estado del cargo como estaba para el caso de inactiva y excento
						Cargos.modificarEstadoCargo(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), estadoCargo);
					}
					
					//actualizamos el cargo padre
					Cargos.modificarCargoPadre(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), codigoDetalleCargoPadreNuevo);
					
				}
				catch(Exception e)
				{
					logger.warn("No se pudo generar el cargo "+e);
					forma.reset();
					ArrayList atributosError = new ArrayList();
					atributosError.add("Error generando el cargo ");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");	
				}
				
				//adicionamos los errores al vector
				if(cargos.getInfoErroresCargo().getTieneErrores())
				{
					for(int x=0; x<cargos.getInfoErroresCargo().getMensajesErrorDetalle().size(); x++)
					{	
						forma.setErroresCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle().get(x).toString());
					}	
				}
			}
		}
		else
		{	
			logger.info("LLEGA ES ARTICULO****************************************");
			HashMap solicitudesMap= ReliquidacionTarifas.obtenerComponentesPaqueteReliquidar(con, subCuenta, codigoDetalleCargoPadreNuevo, ConstantesBD.acronimoNo);
			for(int w=0; w<Utilidades.convertirAEntero(solicitudesMap.get("numRegistros").toString()); w++)
			{
				int numeroSolicitud= Utilidades.convertirAEntero(solicitudesMap.get("numerosolicitud_"+w).toString());
				int estadoCargo= Utilidades.convertirAEntero(solicitudesMap.get("estadocargo_"+w).toString());
				int cantidad=Utilidades.convertirAEntero(solicitudesMap.get("cantidad_"+w).toString());
				
				//generarCagroMedicamentos
				Cargos cargos= new Cargos();
				boolean inserto=cargos.recalcularCargoArticuloValidandoCobertura(con, numeroSolicitud,ingreso, usuario, Utilidades.convertirAEntero(solicitudesMap.get("articulo_"+w).toString()) , ConstantesBD.codigoNuncaValido /*subCuentaOPCIONAL*/, cantidad /*cantidadArticuloOPCIONAL*/, this.obtenerEsquemaTarifario(solicitudesMap.get("clasearticulo_"+w).toString(),forma,false), true /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoSi /*esComponentePaquete*/,Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud));
				if(!inserto)
				{
					logger.warn("No se pudo generar el cargo ");
					forma.reset();
					ArrayList atributosError = new ArrayList();
					atributosError.add("Error generando el cargo ");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			
				if(estadoCargo==ConstantesBD.codigoEstadoFInactiva || estadoCargo==ConstantesBD.codigoEstadoFExento)
				{	
					//dejamos nuevamente el estado del cargo como estaba para el caso de inactiva y excento
					Cargos.modificarEstadoCargo(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), estadoCargo);
				}
			
				//actualizamos el cargo padre
				Cargos.modificarCargoPadre(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), codigoDetalleCargoPadreNuevo);
			
				//adicionamos los errores al vector
				if(cargos.getInfoErroresCargo().getTieneErrores())
				{
					for(int x=0; x<cargos.getInfoErroresCargo().getMensajesErrorDetalle().size(); x++)
					{	
						forma.setErroresCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle().get(x).toString());
					}	
				}
			}	
		}
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param paciente 
	 * @param forma
	 * @return
	 */
	private ActionForward accionSeleccionTipoLiquidacion(Connection con, ActionMapping mapping, PersonaBasica paciente, ReliquidacionTarifasForm forma, UsuarioBasico usuario) 
	{
		Cuenta cuenta=new Cuenta();
		cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
		forma.setEsquemasInventario(Contrato.obtenerEsquemasTarifariosInventariosVigentes(con,paciente.getCodigoIngreso(),cuenta.getCuenta().getFechaApertura()));
		forma.setEsquemasProcedimientos(Contrato.obtenerEsquemasTarifariosProcedimientosVigentes(con,paciente.getCodigoIngreso(),cuenta.getCuenta().getFechaApertura()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tipoLiquidacion");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, ReliquidacionTarifasForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		forma.reset();
		//consultamos las cuentas que cumplan con los requisitos
		forma.setListadoCuentasMap(ReliquidacionTarifas.obtenerCuentasReliquidar(con, usuario.getCodigoCentroAtencion(), paciente.getCodigoPersona()+""));
		if(Integer.parseInt(forma.getListadoCuentasMap("numRegistros").toString())>0)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("seleccionCuenta");
		}
		//de lo contrario es error
		return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error (ReliquidacionTarifasAction)", "error.reliquidacionTarifas.estadoInvalido", true);
	}

	/**
	 * 
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward validacionesAcceso(PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, Connection con, UsuarioBasico usuario) 
	{
		//Se verifica que el paciente se encuentre cargado en sesión
        if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
        /**
		 * Validar concurrencia
		 * Si ya está en proceso de facturación, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
		}
		/**
		 * Validar concurrencia
		 * Si ya está en proceso de distribucion, no debe dejar entrar
		 **/
		else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
		}
        
        return null;
  	}	
}
