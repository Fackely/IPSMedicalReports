/*
 * Creado el 27/10/2005
 * Juan David Ramírez López
 */
package com.princetonsa.action.salasCirugia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.mercury.mundo.odontologia.TratamientoOdontologia;
import com.princetonsa.action.ComunAction;
import com.princetonsa.action.ordenesmedicas.OrdenMedicaAction;
import com.princetonsa.actionform.salasCirugia.PeticionQxForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DTOReporteEstandarAutorizacionServiciosArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.salas.DtoMaterial;
import com.princetonsa.dto.salas.DtoPeticion;
import com.princetonsa.dto.salas.DtoServicioAsociado;
import com.princetonsa.dto.salasCirugia.DtoProfesionalesCirugia;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.parametrizacion.RegistroDiagnosticos;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.servinte.axioma.bl.administracion.facade.AdministracionFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.manejoPaciente.DtoValidacionGeneracionAutorizacionCapitada;
import com.servinte.axioma.dto.ordenes.ProfesionalEspecialidadesDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionServicios.GeneradorReporteFormatoEstandarAutorservicio;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;
import com.servinte.axioma.generadorReporte.salas.peticiones.GeneradorPDFPeticion;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IValidacionGeneracionAutorizacionCapitadaMundo;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoGeneralServicio;

/**
 * 
 * @author Juan David Ramírez
 * 
 * CopyRight Princeton S.A.
 * 27/10/2005
 */
public class PeticionQxAction extends Action
{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(OrdenMedicaAction.class);
	
	MessageResources fuenteMensaje = MessageResources.getMessageResources("mensajes.ApplicationResources");
	
	private static final String MOTIVO_ANULACION = "Modificación en la petición de cirugía";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof PeticionQxForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
					if(con == null){
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				ActionErrors errores = new ActionErrors();
				PeticionQxForm forma = (PeticionQxForm) form;
				String estado=forma.getEstado();

				logger.warn("Estado PeticionQxAction  [" + estado + "]");
				//Se hace esta validación para el caso de autorizaciones de servicios dado que no se pueden resetear antes los valores reiniciados a continuación
				if (estado != null && !estado.equals("imprimirAutorizacion")){
					forma.setListaNombresReportes(new ArrayList<String>());
					forma.setValidacionesAutorizaciones(new DtoValidacionGeneracionAutorizacionCapitada());
					forma.setImprimirAutorizacion(false);
				}
				if(estado.equals("empezar"))
				{
					return accionEmpezar(forma, mapping, con, request, errores, paciente, usuario);
				}
				else if(estado.equals("guardar"))
				{
					return accionGuardar(con, forma, mapping, usuario, request, paciente, errores);
				}
				else if(estado.equals("guardarModificar"))
				{
					return accionGuardarModificar(con, forma, mapping, usuario, request, paciente, errores);
				}
				else if(estado.equals("anular"))
				{
					return this.accionAnular(con, forma, usuario, mapping, request, errores);
				}
				else if(estado.equals("preanestesia"))
				{
					return this.accionPreanestesia(request, response, mapping, con);
				}
				else if(estado.equals("menuConsultarPeticion"))
				{
					cerrarConexion(con);
					return mapping.findForward("menuConsultarPeticion");
				}
				else if(estado.equals("consultarGeneral"))
				{
					//--Valida que el  paciente esté cargado
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
					//Validar que el usuario no se autoatienda
					ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
					if(respuesta.isTrue())
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);

					return accionConsultarGeneral(forma, mapping, con, paciente.getCodigoPersona(), usuario, paciente.getCodigoCuenta() );

				}
				else if(estado.equals("decisionIngresoPacienteSistemaValidado"))
				{
					return accionIngresarPeticion(forma, mapping, request, con, usuario);
				}
				else if(estado.equals("empezarContinuar"))
				{
					return accionInsertarArticulo(mapping, con, forma);
				}
				else if(estado.equals("empezarContinuarServicio"))
				{
					return accionInsertarServicio(mapping, con, forma);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(mapping, con, forma);
				}
				else if(estado.equals("consultarDetallePeticionTodos"))
				{
					return this.consultarDetallePeticionTodos(forma, mapping, con, request, paciente, usuario, false);
				}
				else if(estado.equals("consultarDetallePeticionTodosModificar"))
				{
					return this.consultarDetallePeticionTodos(forma, mapping, con, request, paciente, usuario, true);
				}
				else if(estado.equals("consultarDetallePeticionPaciente"))
				{
					String nro = (String) forma.getMapaConsultaPeticiones().get("nroPeticion");
					return accionConsultarDetallePeticion(forma, Integer.parseInt(nro), mapping, con);
				}
				else if(estado.equals("consultarDetallePeticionPacienteModificar"))
				{
					String nro = (String) forma.getMapaConsultaPeticiones().get("nroPeticion");
					int nroPeticion=Integer.parseInt(nro);
					forma.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
					return accionCargarParaModificarPeticion(forma, mapping, con, nroPeticion, true, usuario);
				}
				else if ( estado.equals("busquedaPeticiones") ) 
				{
					return accionConsultarPeticionTodos(forma,  usuario.getCodigoInstitucionInt(), mapping, con);
				}
				else if ( estado.equals("resultadoPeticiones") ) 
				{
					//-------Si el origen es preanestesia se envía el centro de atención del usuario en sesión
					if ( forma.getOrigen().equals(PeticionQxForm.origenPreanestesia))
						return accionListarPeticionesTodos(forma, mapping, con, usuario.getCodigoCentroAtencion());
					else
						return accionListarPeticionesTodos(forma, mapping, con, -1);
				}
				else if ( estado.equals("ordenarPeticionesPaciente") ) 
				{
					return accionOrdenarPeticionesPaciente(forma, mapping, con);
				}	
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("ordenarPeticionesTodos"))  //-Ordenar el listado de peticiones generales ....
				{			    
					return accionOrdenarPeticionesTodos(forma, mapping, con);
				}
				else if(estado.equals("agregarCambiarObservacion"))
				{
					return accionAgregarCambiarObservacion(con, mapping, forma);
				}
				else if(estado.equals("asignarPropiedad")){

					forma.getMapaPeticionEncabezado().put("urgente", forma.isUrgente());
					return null;
				}
				else if (estado.equals("imprimirAutorizacion"))
				{
					//this.accionImprimirAutorizacion(con, forma,usuario,paciente, errores, request);
					forma.setEstado("resumen");
					return mapping.findForward("paginaPrincipal");
				}else if (estado.equals("imprimirResumen")){
						return this.accionImprimirResumen(con, mapping, forma, paciente,(InstitucionBasica)request.getSession().getAttribute("institucionBasica"),(UsuarioBasico)request.getSession().getAttribute("usuarioBasico"),request);
				}else if (estado.equals("imprimirResumenGuardar")){
					return this.accionImprimirResumenGuardar(con, mapping, forma, paciente,(InstitucionBasica)request.getSession().getAttribute("institucionBasica"),(UsuarioBasico)request.getSession().getAttribute("usuarioBasico"),request);
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

	
	

	private ActionForward accionImprimirResumen(Connection con,
			ActionMapping mapping, PeticionQxForm forma, PersonaBasica paciente,InstitucionBasica institucionBasica,UsuarioBasico usuarioBasico, HttpServletRequest request) {
		
		DtoPeticion peticion=new DtoPeticion();
		
		peticion.setLogoIzquierda(institucionBasica.getLogoJsp());
		
		/*INFO INSTITUCION*/
		peticion.setNombreInstitucion(institucionBasica.getRazonSocial());
		peticion.setNit(institucionBasica.getNit());
		peticion.setActividadEconomica(institucionBasica.getActividadEconomica());
		peticion.setDireccion(institucionBasica.getDireccion()!=null?!institucionBasica.getDireccion().trim().equals("")?institucionBasica.getDireccion():null:null);
		peticion.setTelefono(institucionBasica.getTelefono()!=null?!institucionBasica.getTelefono().trim().equals("")?institucionBasica.getTelefono():null:null);
		peticion.setCentroAtencion(usuarioBasico.getCentroAtencion());
		peticion.setUsuario(usuarioBasico.getNombreUsuario()+" ("+usuarioBasico.getLoginUsuario()+")");
		
		/*INFO PACIENTE*/		
		
		String datosPaciente=paciente.getPrimerNombre()+(paciente.getSegundoNombre()!=null?(" "+paciente.getSegundoNombre()):"")+" "+paciente.getPrimerApellido()+(paciente.getSegundoApellido()!=null?(" "+paciente.getSegundoApellido()):"")+",";
		
		datosPaciente +=" "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona();
		if(paciente.getEdadDetallada()!=null&&!paciente.getEdadDetallada().trim().equals("")){
			datosPaciente +=", Edad: "+paciente.getEdadDetallada();
		}
		if(paciente.getTelefono()!=null&&!paciente.getTelefono().trim().equals("")){
			datosPaciente +=", Tel: "+paciente.getTelefono();
		}
		
		peticion.setPaciente(datosPaciente);
		peticion.setIngreso(paciente.getConsecutivoIngreso());
		peticion.setCuenta(""+paciente.getCodigoCuenta());
		//peticion.setTipoAfiliado(paciente.getTipoAfiliado());
		//peticion.setCategoria(paciente.getClasificacionSE());
		
		Cuenta c=new Cuenta();
		c.cargarAfiliacionClasificacionSocEconomica(con, paciente.getCodigoCuenta()+"",paciente.getCodigoIngreso()+"",paciente.getCodigoConvenio()+"");
		peticion.setTipoAfiliado(c.getTipoAfiliado());
		peticion.setCategoria(c.getEstrato());
		
		peticion.setConvenio(c.getConvenio());
		
		//Obtener el ultimo diagnostico
		RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos();
		peticion.setDiagnostico(mundoRegistroDiagnosticos.getUltimoDiagnosticoPaciente(con, paciente.getCodigoCuenta()));
		
		peticion.setNroPeticion(forma.getMapaPeticionEncabezado("consecutivo_0"));
		peticion.setFechaHoraPeticion(forma.getMapaPeticionEncabezado("fecha_peticion_0")+"--"+forma.getMapaPeticionEncabezado().get("hora_peticion_0"));
		peticion.setEstadoPeticion(forma.getMapaPeticionEncabezado("estado_peticion_0"));
		peticion.setFechaEstimadaCirugia(forma.getMapaPeticionEncabezado("fecha_cirugia_0"));
		peticion.setDuracion(forma.getMapaPeticionEncabezado("duracion_0"));
		peticion.setUrgente(forma.isUrgente()?"Si":"No");
		peticion.setTipoPaciente(forma.getMapaPeticionEncabezado("tipo_paciente_0"));
		peticion.setTipoAnestecia(forma.getMapaPeticionEncabezado("nombre_tipo_anestesia_0"));
		peticion.setRequiereUCI(UtilidadTexto.getBoolean(forma.getMapaPeticionEncabezado("requiere_uci_0"))?"Si":"No");
		
		peticion.setCentroAtencionSolicitante(forma.getMapaPeticionEncabezado("centro_atencion_0"));
		
		String profesionalSolicitante="";
		
		String nombresEspecialidades="";
		String numRegistroProf="";
		try {
			List<DtoEspecialidades>especialidades=TratamientoOdontologia
			.consultarEspecialidadesMedico(Integer.parseInt(forma.getMapaPeticionEncabezado("codigo_medico_0")));
			
			if(especialidades.get(ConstantesBD.valorInicial)!=null){
				numRegistroProf=UtilidadTexto.splitBy(especialidades.get(ConstantesBD.valorInicial)
					.getNumeroRegistro(), ConstantesBD.splitterNumeroRegistro)[ConstantesBD.valorInicial];
			}
			
			if (especialidades.size() == 1) {
				nombresEspecialidades=especialidades
						.get(ConstantesBD.valorInicial).getDescripcion();

			} else {
				for (int i = 0; i < especialidades.size(); i++) {
					// se obtienen la especilidades separadas por --> ,
					if (i < especialidades.size() - 1) {
						nombresEspecialidades += especialidades.get(i)
								.getDescripcion()
								+ ", ";
					}
				}
				nombresEspecialidades += especialidades
						.get(especialidades.size() - 1)
						.getDescripcion();
			}
			profesionalSolicitante=forma.getMapaPeticionEncabezado("medico_0")+ "-"
				+ numRegistroProf
				+ ", " + nombresEspecialidades;
			
			peticion.setProfesionalSolicitante(profesionalSolicitante);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int numRegistros=(Integer) forma.getMapaPeticionServicios().get("numRegistros");
		
		HashMap<?, ?>mapaSer=forma.getMapaPeticionServicios();
		
		List<DtoServicioAsociado>servicios=new ArrayList<DtoServicioAsociado>(0);
		
		for(int i=0;i<numRegistros;i++){
			String servicio=(String) mapaSer.get("servicio_" + i);
			if(mapaSer.get("es_pos_" + i)!=null&&mapaSer.get("es_pos_" + i).equals("POS")){
				servicio+="--POS";
			}else{
				servicio+="--NO POS";
			}
			String especialidad=(String) mapaSer.get("especialidad_" + i);
			String observaciones=(String)mapaSer.get("observaciones_" + i);
			
			DtoServicioAsociado servicioAsociado=new DtoServicioAsociado();
			servicioAsociado.setServicio(servicio);
			servicioAsociado.setEspecialidad(especialidad);
			servicioAsociado.setObservacion(observaciones);
			
			servicios.add(servicioAsociado);
		}
		
		peticion.setServiciosAsociados(servicios);
		
		numRegistros=(Integer) forma.getMapaPeticionProfesionales().get("numRegistros");
		HashMap<?, ?>mapaProf=forma.getMapaPeticionProfesionales();
		
		List<DtoProfesionalesCirugia>profesionales=new ArrayList<DtoProfesionalesCirugia>(0);
		
		for(int i=0;i<numRegistros;i++){
			String profesional=(String) mapaProf.get("medico_" + i);
			String especialidad=(String) mapaProf.get("especialidad_" + i);
			String tipoProf=(String) mapaProf.get("tipo_profesional_" + i);
			
			DtoProfesionalesCirugia profesionalesCirugia=new DtoProfesionalesCirugia();
			
			
			profesionalesCirugia.setNombre(profesional);
			
			profesionalesCirugia.setNombreEspecialidad2(especialidad);
			profesionalesCirugia.setTipoEspecialista(tipoProf);
			
			profesionales.add(profesionalesCirugia);
			
		}
		
		peticion.setProfesionales(profesionales);
		
		HashMap<?,?> mapaMat=forma.getMapaPeticionMateriales();
		
		numRegistros=(Integer) forma.getMapaPeticionMateriales().get("numRegistros");
		
		List<DtoMaterial>materiales=new ArrayList<DtoMaterial>(0);
		
		for(int i=0;i<numRegistros;i++){
			String articulo=(String) mapaMat.get("articulo_" + i);
			if (UtilidadTexto.getBoolean(mapaMat.get("tipoPosResultados_" + i)+"")||UtilidadTexto.getBoolean(mapaMat.get("es_pos_" + i))) {
				articulo+="--POS";
			}else{
				articulo+="--NO POS";
			}
			String cantidad=(String) mapaMat.get("cantidad_" + i);
			
			DtoMaterial material=new DtoMaterial();
			material.setArticulo(articulo);
			material.setCantidad(cantidad);
			
			materiales.add(material);
		}
		
		peticion.setMateriales(materiales);
		
		String observacion=null;
		observacion=forma.getMapaPeticionMateriales("observaciones");
		
		if(observacion==null){
			observacion=forma.getMapaPeticionMateriales("observaciones_0");
			if(observacion==null){
				observacion=forma.getMapaPeticionEncabezado("observMaterEspe");
				if(observacion==null){
					observacion=forma.getMapaPeticionEncabezado("observMaterEspe_0");
				}
			}
		}
		peticion.setObservaciones(observacion);
		
		GeneradorPDFPeticion generadorPDFPeticion=new GeneradorPDFPeticion(peticion);
		JasperPrint reporte = generadorPDFPeticion.generarReporte();
		forma.setNombreArchivoGenerado(generadorPDFPeticion.exportarReportePDF(reporte, "ReportePeticionCirugia"));
		forma.setNombreArchivoCopia(generadorPDFPeticion.exportarReportePDF(reporte, "ReportePeticionCirugiaCopia"));
		
		forma.setEstado("consultarDetallePeticion");
		return mapping.findForward("detallePeticion");
	}
	
	private ActionForward accionImprimirResumenGuardar(Connection con,
			ActionMapping mapping, PeticionQxForm forma, PersonaBasica paciente,InstitucionBasica institucionBasica,UsuarioBasico usuarioBasico, HttpServletRequest request) {
		
		DtoPeticion peticion=new DtoPeticion();
		
		peticion.setLogoIzquierda(institucionBasica.getLogoJsp());
		
		/*INFO INSTITUCION*/
		peticion.setNombreInstitucion(institucionBasica.getRazonSocial());
		peticion.setNit(institucionBasica.getNit());
		peticion.setActividadEconomica(institucionBasica.getActividadEconomica());
		peticion.setDireccion(institucionBasica.getDireccion()!=null?!institucionBasica.getDireccion().trim().equals("")?institucionBasica.getDireccion():null:null);
		peticion.setTelefono(institucionBasica.getTelefono()!=null?!institucionBasica.getTelefono().trim().equals("")?institucionBasica.getTelefono():null:null);
		peticion.setCentroAtencion(usuarioBasico.getCentroAtencion());
		peticion.setUsuario(usuarioBasico.getNombreUsuario()+" ("+usuarioBasico.getLoginUsuario()+")");
		
		/*INFO PACIENTE*/		
		
		String datosPaciente=paciente.getPrimerNombre()+(paciente.getSegundoNombre()!=null?(" "+paciente.getSegundoNombre()):"")+" "+paciente.getPrimerApellido()+(paciente.getSegundoApellido()!=null?(" "+paciente.getSegundoApellido()):"")+",";
		
		datosPaciente +=" "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona();
		if(paciente.getEdadDetallada()!=null&&!paciente.getEdadDetallada().trim().equals("")){
			datosPaciente +=", Edad: "+paciente.getEdadDetallada();
		}
		if(paciente.getTelefono()!=null&&!paciente.getTelefono().trim().equals("")){
			datosPaciente +=", Tel: "+paciente.getTelefono();
		}
		
		peticion.setPaciente(datosPaciente);
		peticion.setIngreso(paciente.getConsecutivoIngreso());
		peticion.setCuenta(""+paciente.getCodigoCuenta());
		//peticion.setTipoAfiliado(paciente.getTipoAfiliado());
		//peticion.setCategoria(paciente.getClasificacionSE());
		
		Cuenta c=new Cuenta();
		c.cargarCuenta(con, paciente.getCodigoCuenta()+"");
		peticion.setTipoAfiliado(c.getTipoAfiliado());
		peticion.setCategoria(c.getEstrato());
		
		peticion.setConvenio(paciente.getConvenioPersonaResponsable());
		
		//Obtener el ultimo diagnostico
		RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos();
		peticion.setDiagnostico(mundoRegistroDiagnosticos.getUltimoDiagnosticoPaciente(con, paciente.getCodigoCuenta()));
		
		peticion.setNroPeticion(forma.getMapaPeticionEncabezado("numeroPeticion"));
		peticion.setFechaHoraPeticion(forma.getMapaPeticionEncabezado("fechaPeticion")+"--"+forma.getMapaPeticionEncabezado().get("horaPeticion"));
		peticion.setEstadoPeticion(forma.getMapaPeticionEncabezado("estadoPeticion"));
		peticion.setFechaEstimadaCirugia(forma.getMapaPeticionEncabezado("fechaEstimada"));
		peticion.setDuracion(forma.getMapaPeticionEncabezado("duracion"));
		peticion.setUrgente(forma.isUrgente()?"Si":"No");
		peticion.setTipoPaciente(forma.getMapaPeticionEncabezado("nombreTipoPaciente"));
		peticion.setTipoAnestecia(forma.getMapaPeticionEncabezado("nombreTipoAnestesia"));
		peticion.setRequiereUCI(UtilidadTexto.getBoolean(forma.getMapaPeticionEncabezado("requiereUci"))?"Si":"No");
		
		peticion.setCentroAtencionSolicitante(usuarioBasico.getCentroAtencion());
		
		String profesionalSolicitante="";
		
		String nombresEspecialidades="";
		String numRegistroProf="";
		try {
			List<DtoEspecialidades>especialidades=TratamientoOdontologia
			.consultarEspecialidadesMedico(Integer.parseInt(forma.getMapaPeticionEncabezado("solicitante")));
			
			if(especialidades.get(ConstantesBD.valorInicial)!=null){
				numRegistroProf=UtilidadTexto.splitBy(especialidades.get(ConstantesBD.valorInicial)
					.getNumeroRegistro(), ConstantesBD.splitterNumeroRegistro)[ConstantesBD.valorInicial];
			}
			
			if (especialidades.size() == 1) {
				nombresEspecialidades=especialidades
						.get(ConstantesBD.valorInicial).getDescripcion();

			} else {
				for (int i = 0; i < especialidades.size(); i++) {
					// se obtienen la especilidades separadas por --> ,
					if (i < especialidades.size() - 1) {
						nombresEspecialidades += especialidades.get(i)
								.getDescripcion()
								+ ", ";
					}
				}
				nombresEspecialidades += especialidades
						.get(especialidades.size() - 1)
						.getDescripcion();
			}
			profesionalSolicitante=forma.getMapaPeticionEncabezado("nombreSolicitante")+ "-"
				+ numRegistroProf
				+ ", " + nombresEspecialidades;
			
			peticion.setProfesionalSolicitante(profesionalSolicitante);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int numRegistros=Integer.parseInt((String) forma.getMapaPeticionServicios().get("numeroFilasMapaServicios"));
		
		HashMap<?, ?>mapaSer=forma.getMapaPeticionServicios();
		
		List<DtoServicioAsociado>servicios=new ArrayList<DtoServicioAsociado>(0);
		
		for(int i=0;i<numRegistros;i++){
			String servicio=(String) mapaSer.get("descripcionServicio_" + i);
			if(mapaSer.get("esPos_" + i).equals("POS")||UtilidadTexto.getBoolean((String) mapaSer.get("esPos_" + i))){
				servicio+="--POS";
			}else{
				servicio+="--NO POS";
			}
			String especialidad=(String) mapaSer.get("descripcionEspecialidad_" + i);
			String observaciones=(String)mapaSer.get("observaciones_" + i);
			
			DtoServicioAsociado servicioAsociado=new DtoServicioAsociado();
			servicioAsociado.setServicio(servicio);
			servicioAsociado.setEspecialidad(especialidad);
			servicioAsociado.setObservacion(observaciones);
			
			servicios.add(servicioAsociado);
		}
		
		peticion.setServiciosAsociados(servicios);
		
		numRegistros=Integer.parseInt((String) forma.getMapaPeticionProfesionales().get("numeroProfesionales"));
		HashMap<?, ?>mapaProf=forma.getMapaPeticionProfesionales();
		
		List<DtoProfesionalesCirugia>profesionales=new ArrayList<DtoProfesionalesCirugia>(0);
		
		for(int i=0;i<numRegistros;i++){
			String profesional=(String) mapaProf.get("nombreProfesional_" + i);
			String especialidad=(String) mapaProf.get("nombreEspecialidad_" + i);
			String tipoProf=(String) mapaProf.get("nombre_tipo_participante_" + i);
			
			DtoProfesionalesCirugia profesionalesCirugia=new DtoProfesionalesCirugia();
			
			
			profesionalesCirugia.setNombre(profesional);
			
			profesionalesCirugia.setNombreEspecialidad2(especialidad);
			profesionalesCirugia.setTipoEspecialista(tipoProf);
			
			profesionales.add(profesionalesCirugia);
			
		}
		
		peticion.setProfesionales(profesionales);
		
		HashMap<?,?> mapaMat=forma.getMapaPeticionMateriales();
		
		numRegistros=forma.getNumeroFilasMateriales();
		
		List<DtoMaterial>materiales=new ArrayList<DtoMaterial>(0);
		
		for(int i=0;i<numRegistros;i++){
			String articulo=(String) mapaMat.get("descripcionArticulo_" + i);
			if (mapaMat.get("tipoPosResultados_" + i)!=null&&mapaMat.get("tipoPosResultados_" + i).equals("POS")) {
				articulo+="--POS";
			}else{
				articulo+="--NO POS";
			}
			String cantidad=(String) mapaMat.get("cantidadDespachadaArticulo_" + i);
			
			DtoMaterial material=new DtoMaterial();
			material.setArticulo(articulo);
			material.setCantidad(cantidad);
			
			materiales.add(material);
		}
		
		peticion.setMateriales(materiales);
		
		String observacion=null;
		observacion=forma.getMapaPeticionMateriales("observaciones");
		
		if(observacion==null){
			observacion=forma.getMapaPeticionMateriales("observaciones_0");
			if(observacion==null){
				observacion=forma.getMapaPeticionEncabezado("observMaterEspe");
				if(observacion==null){
					observacion=forma.getMapaPeticionEncabezado("observMaterEspe_0");
				}
			}
		}
		peticion.setObservaciones(observacion);
		
		GeneradorPDFPeticion generadorPDFPeticion=new GeneradorPDFPeticion(peticion);
		JasperPrint reporte = generadorPDFPeticion.generarReporte();
		forma.setNombreArchivoGenerado(generadorPDFPeticion.exportarReportePDF(reporte, "ReportePeticionCirugia"));
		forma.setNombreArchivoCopia(generadorPDFPeticion.exportarReportePDF(reporte, "ReportePeticionCirugiaCopia"));
		
		forma.setEstado("resumen");
		return mapping.findForward("paginaPrincipal");
	}




	/**
	 * Éste método retorna a la página principal
	 * para que la observación quede almacenada en
	 * los datos del form
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return Página origen
	 */
	private ActionForward accionAgregarCambiarObservacion(Connection con, ActionMapping mapping, PeticionQxForm forma)
	{
		cerrarConexion(con);
	    return this.mostrarPaginaSegunOrigen(forma.getOrigen(), mapping);
	}

	/**
	 * Método que ejecuta la anulación de una peticion de cirugias
	 * @param con Conexión con la BD
	 * @param forma Forma con los datos de la anulación
	 * @param usuario Usuario que realiza la anulación
	 * @param mapping Mapping para hacer la redirección a la página de resumen
	 * @param request Para el manejo de los errores
	 * @return Pagina de detalle mostrando el motivo de la anulación
	 */
	@SuppressWarnings({ "deprecation", "finally" })
	private ActionForward accionAnular(Connection con, PeticionQxForm forma, UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, ActionErrors errores)throws IPSException
	{
		try{
			forma.setEstado("resumen");
			String nro = (String) forma.getMapaPeticionEncabezado().get("numeroPeticion");
			int nroPeticion=Integer.parseInt(nro);
			
			//La anulacion de la peticion y la autorizacion se realiza en este metodo
			this.cargarInfoParaAnulacionAutorizacion(forma, usuario);
			
			return accionCargarParaModificarPeticion(forma, mapping, con, nroPeticion, true, usuario);
			
		}catch(IPSException ipse){
			cerrarConexion(con);
			Log4JManager.error(ipse);
			ipse.getParamsMsg();
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString(),ipse.getParamsMsg()));
		}
		catch(Exception e){
			cerrarConexion(con);
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}finally{
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			return mapping.findForward("paginaModificar");
		}
	}

	/**
	 * Método que controla la modificación de la petición de cirugías
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	private ActionForward accionGuardarModificar(Connection con, PeticionQxForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, ActionErrors errores) throws IPSException
	{
		//Datos Guardar los cambios en la peticion
		Peticion peticion=new Peticion();
		int[] resultado = null;
			
		OrdenesFacade ordenesFacade = new OrdenesFacade();
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
		ContratosDelegate contratosDelegate = new ContratosDelegate();
		
		List<AutorizacionPorOrdenDto> listaAutorizacionesPorOrdenExistentes = null;
		List<AutorizacionCapitacionDto> listaAutorizacionesCapitacion = null;
		
		String cubierto = ConstantesBD.acronimoNo;
		int contratoConvenio = ConstantesBD.codigoNuncaValido;
		int convenio = ConstantesBD.codigoNuncaValido;
		boolean esModificacion = false;
		
		List<InfoResponsableCobertura> listaInfoResponsableCoberturaPeticion = null;
		
		try {
			llenarMundoIngresoPeticion(forma, peticion);
			
			int codigoPersona=paciente.getCodigoPersona();
			
			DtoDiagnostico diagnostico=new DtoDiagnostico();
			diagnostico=Utilidades.getDiagnosticoPacienteIngreso(con, forma.getIngresoId());
			
			
		  	//hermorhu - MT5642
		  	//Se realiza validacion de Cobertura -437- para cada uno de los servicios de la peticion
			listaInfoResponsableCoberturaPeticion = new ArrayList<InfoResponsableCobertura>();
			
			/*Verifico lo definido en el parametro 'Vía Ingreso para Validaciones de Ordenes Ambulatorias y Peticiones'*/
			String viaIngresoPrametro= ValoresPorDefecto.getViaIngresoValidacionesPeticiones(usuario.getCodigoInstitucionInt());
			/*Obtener el tipo de paciente según la vía de Ingreso */
			ArrayList<HashMap<String,Object>> listaTiposPaciente=new ArrayList<HashMap<String,Object>>();
			listaTiposPaciente = UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, viaIngresoPrametro);
		  	String tipoPacienteCobertura=listaTiposPaciente.get(0).get("codigoTipoPaciente").toString();
			
		  	for(int i=0 ; i<Integer.parseInt(forma.getMapaPeticionServicios().get("numeroFilasMapaServicios").toString()) ; i++) {
				InfoResponsableCobertura infoResponsableCobertura= 
			    		Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"" ,
			    				Utilidades.convertirAEntero(viaIngresoPrametro), tipoPacienteCobertura, 
			    				Utilidades.convertirAEntero(forma.getMapaPeticionServicios().get("codigoServicio_"+i)+""), usuario.getCodigoInstitucionInt(), false, "");
				
				if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()) {
					forma.getMapaPeticionServicios().put("cubierto_"+i, ConstantesBD.acronimoSi);
		  		} else {
		  			forma.getMapaPeticionServicios().put("cubierto_"+i, ConstantesBD.acronimoNo);
		  		}
				forma.getMapaPeticionServicios().put("contrato_convenio_"+i, infoResponsableCobertura.getDtoSubCuenta().getContrato());
				
				listaInfoResponsableCoberturaPeticion.add(infoResponsableCobertura);
		  	}
			
			contratoConvenio = listaInfoResponsableCoberturaPeticion.get(0).getDtoSubCuenta().getContrato();
			convenio = listaInfoResponsableCoberturaPeticion.get(0).getDtoSubCuenta().getConvenio().getCodigo();

			
			//verifica halla agregado o eliminado servicios
			for(int i=0 ; i < Integer.parseInt(forma.getMapaPeticionServicios().get("numeroFilasMapaServicios").toString()) ; i++){
				
				if((forma.getMapaPeticionServicios().get("estabd_"+i) == null && !UtilidadTexto.getBoolean(forma.getMapaPeticionServicios().get("fueEliminadoServicio_"+i).toString()))
						|| (forma.getMapaPeticionServicios().get("estabd_"+i) != null && UtilidadTexto.getBoolean(forma.getMapaPeticionServicios().get("fueEliminadoServicio_"+i).toString()))){				
					
					esModificacion = true;
					break;
				}
				boolean requiereUci=false;
				int tipoPaciente=0;
				int tipoAnestesia=0;
				String reqUci = (String)forma.getMapaDetallePeticionInicial().get("requiere_uci_"+0);

				if (!reqUci.isEmpty()){
					requiereUci = UtilidadTexto.getBoolean((String)forma.getMapaDetallePeticionInicial().get("requiere_uci_"+0));
				}
				
				String tipPaciente = (String)forma.getMapaDetallePeticionInicial().get("codigo_tipo_paciente_"+0);
				if(tipPaciente.isEmpty()){
					tipPaciente=Integer.toString(tipoPaciente);
				}
				
				
				String tipAnestesia=(String)forma.getMapaDetallePeticionInicial().get("tipo_anestesia_"+0);
				if(tipAnestesia.isEmpty()){
					tipAnestesia=Integer.toString(tipoAnestesia);
				}
				
				if (!esModificacion){
					if (UtilidadTexto.getBoolean((String)forma.getMapaDetallePeticionInicial().get("urgente_"+0))==UtilidadTexto.getBoolean(forma.getMapaPeticionEncabezado().get("urgente").toString())
							&& requiereUci==UtilidadTexto.getBoolean(forma.getMapaPeticionEncabezado().get("requiereUci").toString())
							&& ((String)forma.getMapaDetallePeticionInicial().get("fecha_cirugia_"+0)).equals(forma.getMapaPeticionEncabezado().get("fechaEstimada").toString())
							&& ((String)forma.getMapaDetallePeticionInicial().get("duracion_"+0)).equals(forma.getMapaPeticionEncabezado().get("duracion").toString())
							&& tipPaciente.equals(forma.getMapaPeticionEncabezado().get("tipoPaciente").toString())
							&& tipAnestesia.equals(forma.getMapaPeticionEncabezado().get("tipoAnestesia").toString())) {
						
						esModificacion = false;
					}else {
						esModificacion = true;
					}
				}
				
			}
				
			//valida que existan ordenes con Autorizaciones de Capitacion Subcontratada en estado Autorizada	
			List<String> estados = new ArrayList<String>();
			estados.add(ConstantesIntegridadDominio.acronimoAutorizado);
			listaAutorizacionesPorOrdenExistentes = ordenesFacade.obtenerAutorizacionCapitacion(ConstantesBD.claseOrdenPeticion, ConstantesBD.codigoTipoOrdenAmbulatoriaServicios , Long.parseLong(forma.getMapaPeticionEncabezado("numeroPeticion")), estados);
				
			//Si no tiene asociada Autorizacion de Capitacion Subcontratada 
			if(listaAutorizacionesPorOrdenExistentes == null || listaAutorizacionesPorOrdenExistentes.isEmpty()){
				//Guardan los cambios en la peticion		
				resultado=peticion.insertar(con, forma.getMapaPeticionEncabezado(), forma.getMapaPeticionServicios(), forma.getMapaPeticionProfesionales(), forma.getMapaPeticionMateriales(), codigoPersona, ConstantesBD.codigoNuncaValido, usuario, false, true);
					
				if(resultado[0]<1){
					logger.error("Error modificando la petición");
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					cerrarConexion(con);
					return mapping.findForward("paginaError");
					
				}else{
					//Llamado a DCU 966  
					listaAutorizacionesCapitacion = this.generarAutorizacionCapitacionPeticion(con, paciente, usuario, request, forma, diagnostico, Integer.parseInt(forma.getMapaPeticionEncabezado("numeroPeticion")), cubierto, contratoConvenio, convenio, paciente.getCodigoIngreso());
				}
			} 
			//Si tiene asociada Autorizacion de Capitacion Subcontratada
			else{
	
				if(esModificacion){
					//Si tiene Autorizacion de Entidad Suncontratada
					if(listaAutorizacionesPorOrdenExistentes.get(0).getConsecutivoAutorizacion() != null && !listaAutorizacionesPorOrdenExistentes.get(0).getConsecutivoAutorizacion().isEmpty()){
						errores.add("", new ActionMessage("errors.autorizacion.peticionEntidadSubContratadaAsoc"));
						saveErrors(request, errores);
						return accionCargarParaModificarPeticion(forma, mapping, con, Integer.parseInt(forma.getMapaConsultaPeticiones().get("nroPeticion").toString()), true, usuario);
					}
					//Si no existe Autorizacion de Entidad Suncontratada
					else {
						//Guardan los cambios en la peticion
						resultado=peticion.insertar(con, forma.getMapaPeticionEncabezado(), forma.getMapaPeticionServicios(), forma.getMapaPeticionProfesionales(), forma.getMapaPeticionMateriales(), codigoPersona, ConstantesBD.codigoNuncaValido, usuario, false, true);
							
						if(resultado[0]<1){
							logger.error("Error modificando la petición");
							request.setAttribute("codigoDescripcionError", "errors.problemasBd");
							cerrarConexion(con);
							return mapping.findForward("paginaError");
						}else{
	
							AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto= new AnulacionAutorizacionSolicitudDto();
							anulacionAutorizacionDto.setMotivoAnulacion(MOTIVO_ANULACION);
							anulacionAutorizacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
							anulacionAutorizacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
							anulacionAutorizacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
							
							boolean anulacionExitosa = false;
							anulacionExitosa = ordenesFacade.procesoAnulacionAutorizacion(ConstantesBD.claseOrdenPeticion, ConstantesBD.codigoTipoOrdenAmbulatoriaServicios, listaAutorizacionesPorOrdenExistentes, anulacionAutorizacionDto, usuario.getCodigoInstitucionInt());

							if(anulacionExitosa){
								//Llamado a DCU 966  
								listaAutorizacionesCapitacion = this.generarAutorizacionCapitacionPeticion(con, paciente, usuario, request, forma, diagnostico, Integer.parseInt(forma.getMapaPeticionEncabezado("numeroPeticion")), cubierto, contratoConvenio, convenio, paciente.getCodigoIngreso());
							}else {
								errores.add("", new ActionMessage("errors.autorizacion.noSeAnulaOrden"));
								saveErrors(request, errores);
							}
						}	
					}
				}
				//Si no se agrego o elimino servicios
			/*	else{
					//Guardan los cambios en la peticion		
					resultado=peticion.insertar(con, forma.getMapaPeticionEncabezado(), forma.getMapaPeticionServicios(), forma.getMapaPeticionProfesionales(), forma.getMapaPeticionMateriales(), codigoPersona, ConstantesBD.codigoNuncaValido, usuario, false, true, null, "");
						
					if(resultado[0]<1){
						logger.error("Error modificando la petición");
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
				}*/
			}
	
			//Envio los mensajes de error encontrados en el proceso de autorización de capitación
			if(listaAutorizacionesCapitacion!=null && !listaAutorizacionesCapitacion.isEmpty()){//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionesCapitacion, errores);
			}
			
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			
			forma.reset();
			forma.setEstado("resumen");
			
		}catch (IPSException ipse) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(ipse.getMessage(), ipse);
			throw ipse;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
		int codPeticion=0;
		
		if(esModificacion){
			codPeticion= resultado[1];
		}else{
			codPeticion= Integer.parseInt(((String)forma.getMapaDetallePeticionInicial().get("consecutivo_"+0)));
		}
		
		return this.accionCargarParaModificarPeticion(forma, mapping, con, codPeticion, true, usuario);
	}

	
	/**
	 * Metodo para llenar el mapa de la forma para mostrar y modificar los datos de la peticion
	 * @param mapaForma
	 * @param mapaMundo
	 * @param existeHoja Verificar existencia de la hoja QX para las salas
	 */
	public static void llenadoFormaDatosPeticion(HashMap mapaForma, HashMap mapaMundo, boolean existeHoja)
	{
		mapaForma.put("fechaPeticion", mapaMundo.get("fecha_peticion_0"));
		mapaForma.put("horaPeticion", mapaMundo.get("hora_peticion_0"));
		mapaForma.put("fechaEstimada", mapaMundo.get("fecha_cirugia_0"));
		mapaForma.put("duracion", UtilidadFecha.convertirHoraACincoCaracteres((String)mapaMundo.get("duracion_0")));
		mapaForma.put("solicitante", mapaMundo.get("codigo_medico_0"));
		mapaForma.put("nombreSolicitante", mapaMundo.get("medico_0"));
		mapaForma.put("tipoPaciente", mapaMundo.get("codigo_tipo_paciente_0"));
		mapaForma.put("nombreTipoPaciente", mapaMundo.get("tipo_paciente_0"));

		// 71212
		mapaForma.put("observMaterEspe", mapaMundo.get("observMaterEspe_0"));

		
		if(!existeHoja)
		{
			mapaForma.put("sala", mapaMundo.get("sala_0"));
			mapaForma.put("nombreSala", mapaMundo.get("nombre_sala_0"));
		}
		String valor=(String)mapaMundo.get("requiere_uci_0");
		if(valor==null || valor.equals(""))
		{
			mapaForma.put("requiereUci", "");
		}
		else if(UtilidadTexto.getBoolean(valor))
		{
			mapaForma.put("requiereUci", "Si");
		}
		else
		{
			mapaForma.put("requiereUci", "No");
		}
		String estadoPeticion=(String)mapaMundo.get("codigo_estado_peticion_0");
		mapaForma.put("codigoEstadoPeticion", estadoPeticion);
		mapaForma.put("estadoPeticion", mapaMundo.get("estado_peticion_0"));
		mapaForma.put("numeroPeticion", mapaMundo.get("consecutivo_0"));
		mapaForma.put("tipoAnestesia", mapaMundo.get("tipo_anestesia_0"));
		mapaForma.put("nombreTipoAnestesia", mapaMundo.get("nombre_tipo_anestesia_0"));
		if(Integer.parseInt(estadoPeticion)==ConstantesBD.codigoEstadoPeticionAnulada)
		{
			mapaForma.put("motivoAnulacion", mapaMundo.get("codigo_motivo_anulacion_0"));
			mapaForma.put("nombreMotivoAnulacion", mapaMundo.get("nombre_motivo_anulacion_0"));
			mapaForma.put("comentario", mapaMundo.get("comentario_anulacion_0"));
		}
		if(mapaMundo.get("urgente_0") == null || mapaMundo.get("urgente_0").toString().equals("0")){
			mapaForma.put("urgente", "false");
		}
		else{
			mapaForma.put("urgente", "true");
		}
	}
	/**
	 * Método para cargar los datos de la petición para su modificación
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param nroPeticion
	 * @param esModificar Me indica si estoy llamando el cargar para la funcionalidad de modificar o de generar
	 * @param usuario Usuario para enviar los logs
	 * @return
	 */
	private ActionForward accionCargarParaModificarPeticion(PeticionQxForm forma, ActionMapping mapping, Connection con, int nroPeticion, boolean esModificar, UsuarioBasico usuario)
	{
		Peticion mundoPeticion = new Peticion();

		/*
		forma.reset();
		
		//-Consultar el encabezado del detalle de una petición  
		forma.setMapaPeticionEncabezado( mundoPeticion.cargarEncabezadoPeticion(con, nroPeticion) );
		
		//-Consultar los Servicios asociados a la peticion a consultar
		forma.setMapaPeticionServicios(  mundoPeticion.cargarServiciosPeticionResultados(con, nroPeticion) );
		
		//-Consultar los profesionales asociados a la peticion a consultar
		forma.setMapaPeticionProfesionales(  mundoPeticion.cargarProfesionalesPeticion(con, nroPeticion) );
		
//-Consultar los Materiales asociados a la peticion a consultar
forma.setMapaPeticionMateriales(  mundoPeticion.cargarMaterialesPeticion(con, nroPeticion) );

//-Consultar los OTROS Materiales asociados a la peticion a consultar
forma.setMapaPeticionOtrosMateriales(  mundoPeticion.cargarOtrosMaterialesPeticion(con, nroPeticion) );
*/
		
		//-Consultar el encabezado del detalle de una petición  
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		HashMap mapaMundo=mundoPeticion.cargarEncabezadoPeticion(con, nroPeticion,mundoPeticion.getFiltrosMap()); //71212
		forma.setMapaDetallePeticionInicial(mapaMundo);
		HashMap mapaFormaEncabezado=new HashMap();
		String observaciones="";
		if(mapaMundo.get("observMaterEspe_0") != null){
			observaciones=mapaMundo.get("observMaterEspe_0").toString();
		}
		forma.setSexo(Integer.parseInt(mapaMundo.get("codigo_sexo_0").toString()));

		 // Generalicé este método para ser reutilizado
		llenadoFormaDatosPeticion(mapaFormaEncabezado, mapaMundo, false); //71212
		
		boolean esResumen=false;
		if(forma.getEstado().equals("resumen"))
		{
			esResumen=true;
		}
		
		String log="";
		
		if(esResumen)
		{
			log=forma.getMapaPeticionEncabezado("log");
			log+="\t\tINFORMACIÓIN DESPUES DE LA MODIFICACIÓN\n\n";
		}
		else
		{
			log="\t\tINFORMACIÓIN ANTES DE LA MODIFICACIÓN\n\n";
		}

		/*
		mapaFormaEncabezado.put("fechaPeticion", mapaMundo.get("fecha_peticion_0"));
		mapaFormaEncabezado.put("horaPeticion", mapaMundo.get("hora_peticion_0"));
		mapaFormaEncabezado.put("fechaEstimada", mapaMundo.get("fecha_cirugia_0"));
		mapaFormaEncabezado.put("duracion", UtilidadFecha.convertirHoraACincoCaracteres((String)mapaMundo.get("duracion_0")));
		mapaFormaEncabezado.put("solicitante", mapaMundo.get("codigo_medico_0"));
		mapaFormaEncabezado.put("nombreSolicitante", mapaMundo.get("medico_0"));
		mapaFormaEncabezado.put("tipoPaciente", mapaMundo.get("codigo_tipo_paciente_0"));
		mapaFormaEncabezado.put("nombreTipoPaciente", mapaMundo.get("tipo_paciente_0"));
		String valor=(String)mapaMundo.get("requiere_uci_0");
		if(valor==null)
		{
			mapaFormaEncabezado.put("requiereUci", "");
		}
		else if(UtilidadTexto.getBoolean(valor))
		{
			mapaFormaEncabezado.put("requiereUci", "Si");
		}
		else
		{
			mapaFormaEncabezado.put("requiereUci", "No");
		}
		String estadoPeticion=(String)mapaMundo.get("codigo_estado_peticion_0");
		mapaFormaEncabezado.put("codigoEstadoPeticion", estadoPeticion);
		mapaFormaEncabezado.put("estadoPeticion", mapaMundo.get("estado_peticion_0"));
		mapaFormaEncabezado.put("numeroPeticion", mapaMundo.get("consecutivo_0"));
		
		if(Integer.parseInt(estadoPeticion)==ConstantesBD.codigoEstadoPeticionAnulada)
		{
			mapaFormaEncabezado.put("motivoAnulacion", mapaMundo.get("codigo_motivo_anulacion_0"));
			mapaFormaEncabezado.put("nombreMotivoAnulacion", mapaMundo.get("nombre_motivo_anulacion_0"));
			mapaFormaEncabezado.put("comentario", mapaMundo.get("comentario_anulacion_0"));
		}
		*/

		log+="\nFecha Peticion:\t\t\t["+mapaMundo.get("fecha_peticion_0")+"]";
		log+="\nHora Peticion:\t\t\t["+mapaMundo.get("hora_peticion_0")+"]";
		log+="\nFecha Estimada:\t\t\t["+mapaMundo.get("fecha_cirugia_0")+"]";
		log+="\nDuracion:\t\t\t["+UtilidadFecha.convertirHoraACincoCaracteres((String)mapaMundo.get("duracion_0"))+"]";
		log+="\nSolicitante:\t\t\t["+mapaMundo.get("medico_0")+"]";
		log+="\nTipo Paciente:\t\t\t["+mapaMundo.get("tipo_paciente_0")+"]";
		log+="\nRequiere Uci:\t\t\t["+mapaFormaEncabezado.get("requiereUci")+"]";
		log+="\nEstado Peticion:\t\t\t["+mapaMundo.get("estado_peticion_0")+"]";
		log+="\nNúmero Petición:\t\t\t["+mapaMundo.get("consecutivo_0")+"]";
		log+="\nTipo Anestesia:\t\t\t["+mapaMundo.get("nombre_tipo_anestesia_0")+"]";

		//-Consultar los Servicios asociados a la peticion a consultar
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		mapaMundo=mundoPeticion.cargarServiciosPeticionResultados(con, nroPeticion,mundoPeticion.getFiltrosMap());
		HashMap mapaForma=new HashMap();

		int numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
		mapaForma.put("numeroFilasMapaServicios", numeroRegistros+"");
		log+="\n\t\tSERVICIOS\n\n";
		for (int i=0;i<numeroRegistros;i++)
		{
			mapaForma.put("estabd_"+i, "true");
			mapaForma.put("fueEliminadoServicio_"+i, "false");
			mapaForma.put("codigoServicio_"+i, mapaMundo.get("codigo_servicio_"+i));
			mapaForma.put("numeroServicio_"+i, mapaMundo.get("numero_servicio_"+i));
			mapaForma.put("codigoCups_"+i, mapaMundo.get("codigo_propietario_"+i));
			mapaForma.put("descripcionServicio_"+i, mapaMundo.get("servicio_"+i));
			mapaForma.put("esPos_"+i, UtilidadTexto.getBoolean(mapaMundo.get("es_pos_"+i)+"")?"POS":"NOPOS");
			mapaForma.put("descripcionEspecialidad_"+i, mapaMundo.get("especialidad_"+i));
			mapaForma.put("observaciones_"+i, UtilidadTexto.deshacerCodificacionHTML((String)mapaMundo.get("observaciones_"+i)));
			mapaForma.put("contrato_convenio_"+i, mapaMundo.get("contrato_convenio_"+i));
			mapaForma.put("cubierto_"+i, mapaMundo.get("cubierto_"+i));
			
			log+="\nCodigo Servicio ["+i+"]:\t\t\t["+mapaMundo.get("codigo_servicio_"+i)+"]";
			log+="\nNumero Servicio ["+i+"]:\t\t\t["+mapaMundo.get("numero_servicio_"+i)+"]";
			log+="\nCódigo Cups ["+i+"]:\t\t\t["+mapaMundo.get("codigo_propietario_"+i)+"]";
			log+="\nDescripción Servicio ["+i+"]:\t\t\t["+mapaMundo.get("servicio_"+i)+"]";
			log+="\nEspecialidad ["+i+"]:\t\t\t["+mapaMundo.get("especialidad_"+i)+"]";
			log+="\nObservaciones ["+i+"]:\t\t\t["+UtilidadTexto.deshacerCodificacionHTML((String)mapaMundo.get("observaciones_"+i))+"]";
		}
		forma.setMapaPeticionServicios(mapaForma);
		
		//-Consultar los profesionales asociados a la peticion a consultar
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		mapaMundo=mundoPeticion.cargarProfesionalesPeticion(con, nroPeticion,mundoPeticion.getFiltrosMap());
		mapaForma=new HashMap();

		numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
		mapaForma.put("numeroProfesionales", numeroRegistros+"");
		log+="\n\t\tPROFESIONALES\n\n";
		for (int i=0;i<numeroRegistros;i++)
		{
			mapaForma.put("profesional_"+i, mapaMundo.get("codigo_medico_"+i));
			mapaForma.put("nombreProfesional_"+i, mapaMundo.get("medico_"+i));
			mapaForma.put("especialidades_"+i, mapaMundo.get("codigo_especialidad_"+i));
			mapaForma.put("nombreEspecialidad_"+i, mapaMundo.get("especialidad_"+i));
			mapaForma.put("tipo_participante_"+i, mapaMundo.get("codigo_tipo_profesional_"+i));
			mapaForma.put("nombre_tipo_participante_"+i, mapaMundo.get("tipo_profesional_"+i));
			//mapaForma.put(""+i, mapaMundo.get(""+i));
			log+="\nProfesional ["+i+"]:\t\t\t["+mapaMundo.get("medico_"+i)+"]";
			log+="\nEspecialidad ["+i+"]:\t\t\t["+mapaMundo.get("especialidad_"+i)+"]";
			log+="\nTipo Profesional ["+i+"]:\t\t\t["+mapaMundo.get("tipo_profesional_"+i)+"]";
		}
		forma.setMapaPeticionProfesionales(mapaForma);

		
		
		//-Consultar los Materiales asociados a la peticion a consultar
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		mapaMundo=mundoPeticion.cargarMaterialesPeticion(con, nroPeticion,mundoPeticion.getFiltrosMap());
		mapaForma=new HashMap();

		numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
		forma.setNumeroFilasMateriales(numeroRegistros);
		mapaForma.put("observaciones", observaciones);
		log+="\n\t\tARTÍCULOS\n\n";
		for (int i=0;i<numeroRegistros;i++)
		{
			mapaForma.put("fueEliminadoArticulo_"+i, "false");
			mapaForma.put("codigoArticulo_"+i, mapaMundo.get("codigo_articulo_"+i));
			mapaForma.put("descripcionArticulo_"+i, mapaMundo.get("articulo_"+i));
			mapaForma.put("unidadMedidaArticulo_"+i, mapaMundo.get("unidad_medida_"+i));
			mapaForma.put("cantidadDespachadaArticulo_"+i, mapaMundo.get("cantidad_"+i));
			mapaForma.put("tipoPosResultados_"+i, UtilidadTexto.getBoolean(mapaMundo.get("es_pos_"+i))?"POS":"NOPOS");
			
			//mapaForma.put(""+i, mapaMundo.get(""+i));

			mapaForma.put("observaciones_"+i, mapaMundo.get("observMatEsp_"+i)+"");
			
			log+="\nArtículo ["+i+"]:\t\t\t["+mapaMundo.get("articulo_"+i)+"]";
			log+="\nUnidad de Medida ["+i+"]:\t\t\t["+mapaMundo.get("unidad_medida_"+i)+"]";
			log+="\nCantidad ["+i+"]:\t\t\t["+mapaMundo.get("cantidad_"+i)+"]";
		}

		forma.setMapaPeticionMateriales(mapaForma);
		
		if(esModificar && esResumen)
		{
			//@todo Terminar el log
			LogsAxioma.enviarLog(ConstantesBD.logModificarPeticionQXCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
		}
		else if(esModificar)
		{
			mapaFormaEncabezado.put("log", log);
		}
		
		forma.setMapaPeticionEncabezado(mapaFormaEncabezado);

		
		logger.info("4444444444444444444 Despues del Insert");
		logger.info("Mapa Consulta Peticiones: " + forma.getMapaConsultaPeticiones());
		logger.info("Mapa Peticion Encabezado: " + forma.getMapaPeticionEncabezado());
		logger.info("Mapa peticion Materiales: " + forma.getMapaPeticionMateriales());
		logger.info("Mapa Peticion Otros Materiales: " + forma.getMapaPeticionOtrosMateriales());
		
		//MT5061- hermorhu
		ArrayList<ProfesionalEspecialidadesDto> listProfesionalEspecialidades = null;
		
		if(forma.getProfesionalEspecialidades().isEmpty()) {
			
			AdministracionFacade administracionFacade = new AdministracionFacade();
			
			try {
				listProfesionalEspecialidades = (ArrayList<ProfesionalEspecialidadesDto>) administracionFacade.consultarProfesionalesEspecialidades(usuario.getCodigoInstitucionInt());
			} catch (IPSException ipse) {
				Log4JManager.error(ipse.getMessage(), ipse);
			}
		
			forma.setProfesionalEspecialidades(listProfesionalEspecialidades);	
		}
			
		cerrarConexion(con);
		if(esModificar)
		{
			
			return mapping.findForward("paginaModificar");
		}
		else
		{
			return mapping.findForward("paginaPrincipal");
		}
	}

	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionPreanestesia(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, Connection con)
	{
		String nroPeticion = (request.getParameter("nroPeticion") + "");
		cerrarConexion(con);
		try
		{
			response.sendRedirect("../ingresarPreanestesia/ingresarPreanestesia.do?estado=empezar&nroPeticion=" + nroPeticion+"&ocultarCabezote=0&estadoSolicitud=-1");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.error("Error redireccionando a la hoja de presanestesia "+e);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		return null;
	}

	/**
	 * Método que me hace una consulta resultado de la búsqueda
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param esModificar Indica si el listado se llama desde la funcionalidad de modificación
	 * @return
	 */
	private ActionForward consultarDetallePeticionTodos(PeticionQxForm forma, ActionMapping mapping, Connection con, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario, boolean esModificar)
	{
		//-Obtener la posicion en el HashMap del Paciente
		String Nro = (String) forma.getMapaPeticionEncabezado().get("nroPosPeticion");
		forma.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
		
		//-Carga el paciente para mostrar el detalle 
		TipoNumeroId idPaci = new TipoNumeroId();
		//-Recuperar los datos del paciente
		idPaci.setNumeroId((String) forma.getMapaPeticionEncabezado().get("nroId_" + Nro));
		idPaci.setTipoId((String) forma.getMapaPeticionEncabezado().get("tId_" + Nro));
		
		this.cargarPaciente(idPaci, paciente, con, request, usuario, false);
		
		//-Enviar el numero de la petición 
		String nro = (String) forma.getMapaPeticionEncabezado().get("consecutivo_" + Nro);
		if(esModificar)
		{
			return accionCargarParaModificarPeticion(forma, mapping, con, Integer.parseInt(nro), true, usuario);
		}
		else
		{
			return accionConsultarDetallePeticion(forma, Integer.parseInt(nro), mapping, con);
		}
	}

	/**
	 * Método que sube el paciente a sesión
	 * @param idPaci
	 * @param paciente
	 * @param con
	 * @param request
	 * @param medico
	 */
	private void cargarPaciente(TipoNumeroId idPaci, PersonaBasica paciente, Connection con, HttpServletRequest request, UsuarioBasico medico, boolean esModificacion)
	{
		if (  (paciente == null || paciente.getTipoIdentificacionPersona().equals("")) ||
				  !((paciente.getCodigoTipoIdentificacionPersona().equals(idPaci.getTipoId())) &&  (paciente.getNumeroIdentificacionPersona().equals(idPaci.getNumeroId()))) ||
				  esModificacion
				 )
		{
			try
			{
				paciente.cargar(con, idPaci);
				paciente.cargarPaciente2(con, paciente.getCodigoPersona(), medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");

				// Código necesario para registrar este paciente como Observer
				Observable observable = (Observable) this.servlet.getServletContext().getAttribute("observable");
				if (observable != null) 
				{
					paciente.setObservable(observable);
					// Si ya lo habíamos añadido, la siguiente línea no hace nada
					observable.addObserver(paciente);
				}
			}
			catch (SQLException e)
			{
				logger.error("Error cargando el paciente en sesión : "+e);
			}
			request.getSession().setAttribute("pacienteActivo", paciente);
		}
	}

	/**
	 * Método para ingresar un servicio nuevo
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionInsertarServicio(ActionMapping mapping, Connection con, PeticionQxForm forma)
	{
	    cerrarConexion(con);
	    return this.mostrarPaginaSegunOrigen(forma.getOrigen(), mapping);
	}

	/**
	 * Metodo Para Ordenar el listado de peticiones del paciente
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenarPeticionesPaciente(PeticionQxForm forma, ActionMapping mapping, Connection con)
	{
		//-String[] indices = {"codigo_peticion_", "numero_servicio_", "fecha_peticion_", "fecha_cirugia_", "medico_", "estado_peticion_"};
		String[] indices = {"codigo_peticion_", "fecha_peticion_", "fecha_cirugia_", "medico_", "codigo_medico_", "estado_peticion_", "codigo_estado_peticion_", "centro_atencion_"};
		
		
		//String[] columnas = {"codigo_peticion", "fecha_peticion", "fecha_cirugia", "medico", "codigo_medico", "estado_peticion", "codigo_estado_peticion", "centro_atencion"};
		
		
		Integer num = (Integer) forma.getMapaConsultaPeticiones().get("numRegistros");
		
        forma.setMapaConsultaPeticiones(Listado.ordenarMapa(indices,
        												    forma.getPatronOrdenar(),
										                    forma.getUltimoPatron(),
										                    forma.getMapaConsultaPeticiones(),
										                    num.intValue() ));
        
        forma.getMapaConsultaPeticiones().put("numRegistros", new Integer(num.intValue()));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        this.cerrarConexion(con);
        return mapping.findForward("paginaPrincipal");
	}

	private ActionForward accionOrdenarPeticionesTodos(PeticionQxForm forma, ActionMapping mapping, Connection con)
	{
		String[] indices = {"consecutivo_", "fecha_peticion_", "fecha_cirugia_", "tId_", "nroId_", "paciente_", "medico_", "estado_peticion_", "centro_atencion_"};

		Integer num = (Integer) forma.getMapaPeticionEncabezado().get("numRegistros");

		forma.setMapaPeticionEncabezado(Listado.ordenarMapa(indices,
															forma.getPatronOrdenar(),
										                    forma.getUltimoPatron(),
										                    forma.getMapaPeticionEncabezado(),
										                    num.intValue() ));
        
        forma.getMapaPeticionEncabezado().put("numRegistros", new Integer(num.intValue()));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        this.cerrarConexion(con);
        return mapping.findForward("resultadosBusqueda");
	}


	/**
	 * Método para insertar los datos en la BD
	 * @param con Conexión con la BD
	 * @param forma Forma
	 * @param mapping Mapping para redireccionar
	 * @param usuario Usuario del sistema
	 * @param request 
	 * @param pacienteSession 
	 * @return ActionMapping con la Página de resumen
	 * @throws IPSException 
	 */
	@SuppressWarnings({ "unchecked" })
	private ActionForward accionGuardar(Connection con, PeticionQxForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica pacienteSession, ActionErrors errores) throws IPSException
	{
		Peticion peticion=new Peticion();
		llenarMundoIngresoPeticion(forma, peticion);
		int codigoPersona=pacienteSession.getCodigoPersona();
		DtoDiagnostico diagnostico=new DtoDiagnostico();
		diagnostico=Utilidades.getDiagnosticoPacienteIngreso(con, forma.getIngresoId());
		forma.getMapaPeticionEncabezado().put("acronimoDiagnostico", diagnostico.getAcronimoDiagnostico());
		forma.getMapaPeticionEncabezado().put("tipoDiagnostico", diagnostico.getTipoCieDiagnostico());
		List<AutorizacionCapitacionDto> listaAutorizacionesCapitacion = new ArrayList<AutorizacionCapitacionDto>();
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
		List<InfoResponsableCobertura> listaInfoResponsableCoberturaPeticion = null;
		/*
		 * MT: 3796, 3458, 2756 y 3888
		 * Cambio: DCU 167 V1.53
		 * Diana Ruiz
		 */
		String cubierto = ConstantesBD.acronimoNo;
		int contratoConvenio = ConstantesBD.codigoNuncaValido;
		int convenio = ConstantesBD.codigoNuncaValido;
		 
		/*Verifico lo definido en el parametro 'Vía Ingreso para Validaciones de Ordenes Ambulatorias y Peticiones'*/
		String viaIngresoParametro= ValoresPorDefecto.getViaIngresoValidacionesPeticiones(usuario.getCodigoInstitucionInt());
		/*Obtener el tipo de paciente según la vía de Ingreso */
		ArrayList<HashMap<String,Object>> listaTiposPaciente=new ArrayList<HashMap<String,Object>>();
		listaTiposPaciente = UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, viaIngresoParametro);
	  	String tipoPaciente=listaTiposPaciente.get(0).get("codigoTipoPaciente").toString();
		
	  	/*Envio la información al proceso de cobertura */
	  	
	  	//hermorhu - MT5642
	  	//Se realiza validacion de Cobertura -437- para cada uno de los servicios de la peticion
	  	listaInfoResponsableCoberturaPeticion = new ArrayList<InfoResponsableCobertura>();
	  	
	  	for(int i=0 ; i<Integer.parseInt(forma.getMapaPeticionServicios().get("numeroFilasMapaServicios").toString()) ; i++) {
			InfoResponsableCobertura infoResponsableCobertura= 
		    		Cobertura.validacionCoberturaServicio(con, forma.getIngresoId()+"" ,
		    				Utilidades.convertirAEntero(viaIngresoParametro), tipoPaciente, 
		    				Utilidades.convertirAEntero(forma.getMapaPeticionServicios().get("codigoServicio_"+i)+""), usuario.getCodigoInstitucionInt(), false, "");
			
			if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()) {
				forma.getMapaPeticionServicios().put("cubierto_"+i, ConstantesBD.acronimoSi);
	  		} else {
	  			forma.getMapaPeticionServicios().put("cubierto_"+i, ConstantesBD.acronimoNo);
	  		}
			forma.getMapaPeticionServicios().put("contrato_convenio_"+i, infoResponsableCobertura.getDtoSubCuenta().getContrato());
			
			listaInfoResponsableCoberturaPeticion.add(infoResponsableCobertura);
	  	}
		
		contratoConvenio = listaInfoResponsableCoberturaPeticion.get(0).getDtoSubCuenta().getContrato();
		convenio = listaInfoResponsableCoberturaPeticion.get(0).getDtoSubCuenta().getConvenio().getCodigo();
	  	
		int[] resultado=peticion.insertar(con, forma.getMapaPeticionEncabezado(), forma.getMapaPeticionServicios(), forma.getMapaPeticionProfesionales(), 
				forma.getMapaPeticionMateriales(), codigoPersona, forma.getIngresoId(), usuario, false, false);
		
		if(resultado[0]<1)
		{
			logger.error("Error ingresando la petición");
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		else
		{
			
//			if (cubierto.equals(ConstantesBD.acronimoSi)){
				listaAutorizacionesCapitacion = this.generarAutorizacionCapitacionPeticion(con, pacienteSession, usuario,
						request, forma, diagnostico, resultado[1], cubierto, contratoConvenio, convenio, forma.getIngresoId());
//			} else{
//				Log4JManager.info("LOS SERVICIOS DE LA PETICION NO SE ENCUENTRAN CUBIERTOS POR EL CONVENIO RESPONSABLE. PROCESO AUTORIZACION DE CAPITACION CANCELADO");
//			}
			
			//Envio los mensajes de error encontrados en el proceso de autorización de capitación
			if(listaAutorizacionesCapitacion!=null && !listaAutorizacionesCapitacion.isEmpty()){//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionesCapitacion, errores);
			}
			
			saveErrors(request, errores);
			forma.reset();
			forma.setEstado("resumen");
			return this.accionCargarParaModificarPeticion(forma, mapping, con, resultado[1], false, usuario);
		}
	}

	/**
	 * Método para pasar los datos de la forma al mundo para el ingresoa la BD
	 * @param forma
	 * @param peticion
	 */
	private void llenarMundoIngresoPeticion(PeticionQxForm forma, Peticion peticion)
	{
		String valorPropiedad;
		
		valorPropiedad=forma.getMapaPeticionEncabezado("fechaPeticion");
		peticion.setFecha(valorPropiedad);
		
		valorPropiedad=forma.getMapaPeticionEncabezado("fechaEstimada");
		peticion.setFechaCirugia(valorPropiedad);
		
		valorPropiedad=forma.getMapaPeticionEncabezado("duracion");
		peticion.setDuracion(valorPropiedad);

		valorPropiedad=forma.getMapaPeticionEncabezado("solicitante");
		peticion.setCodigoMedicoSolicitante(Integer.parseInt(valorPropiedad));
		
		valorPropiedad=forma.getMapaPeticionEncabezado("tipoPaciente");
		peticion.setTipoPaciente(new InfoDatos(valorPropiedad));

		valorPropiedad=forma.getMapaPeticionEncabezado("requiereUci");
		peticion.setRequiere_uci(valorPropiedad);
	}

	private ActionForward accionInsertarArticulo(ActionMapping mapping, Connection con, PeticionQxForm forma)
	{
		forma.setNumeroFilasMateriales(forma.getNumeroFilasMateriales()+1);
	    cerrarConexion(con);
	    return this.mostrarPaginaSegunOrigen(forma.getOrigen(), mapping);
	}

	/**
	 * Método que revisa el origen y retorna la página principal de modificación o de generación
	 * En caso de no tener un origen específico, retorna la pagina de generación
	 * @param origen
	 * @param mapping
	 * @return
	 */
	private ActionForward mostrarPaginaSegunOrigen(String origen, ActionMapping mapping)
	{
	    if(origen.equals(PeticionQxForm.origenModificar))
		{
			return mapping.findForward("paginaModificar");
		}
		else if(origen.equals(PeticionQxForm.origenGenerar))
		{
			return mapping.findForward("paginaPrincipal");
		}
	    return mapping.findForward("paginaPrincipal");
	}

	private ActionForward accionEliminar(ActionMapping mapping, Connection con, PeticionQxForm forma)
	{
	    cerrarConexion(con);
	    return this.mostrarPaginaSegunOrigen(forma.getOrigen(), mapping);
	}

	/**
	 * Metodo para retornar la pagina de consulta de peticiones generales
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionConsultarPeticionTodos(PeticionQxForm forma,  int institucion, ActionMapping mapping, Connection con)
	{
		forma.reset();
	 	forma.setMaxPageItems( Integer.parseInt(ValoresPorDefecto.getMaxPageItems(institucion)) );

		cerrarConexion(con);
		return mapping.findForward("busquedaPeticiones");
	}
	
	/**
	 * Metodo para buscar las peticiones segun los parametros de la busqueda avanzada
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param codigoCentroAtencion --> Se utiliza para realizar el filtro de las peticiones por centro de atención en preanestesia
	 * @return
	 */
	private ActionForward accionListarPeticionesTodos(PeticionQxForm forma, ActionMapping mapping, Connection con, int codigoCentroAtencion)
	{
		Peticion mundoPeticion = new Peticion();
		llenarMundo(forma, mundoPeticion);
		
		//-Consultar  y reutilizar el mapa de encabezado para traer el listado
		//-Resultado de la busqueda.
		/*if(forma.getOrigen().equals("modificar"))
		{
			forma.setMapaPeticionEncabezado( mundoPeticion.consultarPeticiones(con, true));
		}
		else
		{*/
			//Solo listar las peticiones con indicativo programable en Si
			mundoPeticion.setProgramable(ConstantesBD.acronimoSi);
			forma.setMapaPeticionEncabezado( mundoPeticion.consultarPeticiones(con, forma.getOrigen(), codigoCentroAtencion));
		//}
		cerrarConexion(con);
		return mapping.findForward("resultadosBusqueda");
	}
	

	/**
	 * Metodo para consultar el detalle de la informacion de una peticion de Cirugia
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionConsultarDetallePeticion(PeticionQxForm forma, int nroPeticion, ActionMapping mapping, Connection con)
	{
		
		Peticion mundoPeticion = new Peticion();
		
		forma.setTiposAnestesia(Utilidades.obtenerTipoAnestecia(con, ""));
		
		//-Consultar el encabezado del detalle de una petición  
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		forma.setMapaPeticionEncabezado( mundoPeticion.cargarEncabezadoPeticion(con, nroPeticion,mundoPeticion.getFiltrosMap()));
		if(forma.getMapaPeticionEncabezado("urgente_0") == null || forma.getMapaPeticionEncabezado("urgente_0").toString().equals("0")){
			forma.setUrgente(false);
		}
		else{
			forma.setUrgente(true);
		}
		//-Consultar los Servicios asociados a la peticion a consultar
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		forma.setMapaPeticionServicios(  mundoPeticion.cargarServiciosPeticionResultados(con, nroPeticion,mundoPeticion.getFiltrosMap()) );
		
		//-Consultar los profesionales asociados a la peticion a consultar
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		forma.setMapaPeticionProfesionales(  mundoPeticion.cargarProfesionalesPeticion(con, nroPeticion,mundoPeticion.getFiltrosMap()) );
		
		//-Consultar los Materiales asociados a la peticion a consultar
		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);
		forma.setMapaPeticionMateriales(  mundoPeticion.cargarMaterialesPeticion(con, nroPeticion,mundoPeticion.getFiltrosMap()) );
		
		//-Consultar los OTROS Materiales asociados a la peticion a consultar
		forma.setMapaPeticionOtrosMateriales("numRegistros",0);
	
		//-Conservar el estado
		forma.setEstado("consultarDetallePeticion");
		
		cerrarConexion(con);
		return mapping.findForward("detallePeticion");
	}
	

	/**
	 * Metodo para enviar las variables del Form al Mundo para su
	 * respectiva gestion sobre BD. 
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(PeticionQxForm forma, Peticion mundo)
	{
		mundo.setNroIniServicio(forma.getNroIniServicio());
		mundo.setNroFinServicio(forma.getNroFinServicio());
		mundo.setFechaIniPeticion(forma.getFechaIniPeticion());
		mundo.setFechaFinPeticion(forma.getFechaFinPeticion());
		mundo.setFechaIniCirugia(forma.getFechaIniCirugia());
		mundo.setFechaFinCirugia(forma.getFechaFinCirugia());
		mundo.setProfesional(forma.getProfesional());
		mundo.setEstadoPeticion(forma.getEstadoPeticion());
		
		mundo.setMapaConsultaPeticiones( forma.getMapaConsultaPeticiones() );
		mundo.setCentroAtencion(forma.getCentroAtencion());
	}	 
	

	/**
	 * Método para rediccionar a la página de ingreso de la petición
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param con Conexión con la BD
	 * @param usuario Codigo de la institución del usuario
	 * @return ActionForward hacia la página de ingreso
	 */
	private ActionForward accionIngresarPeticion(PeticionQxForm forma, ActionMapping mapping, HttpServletRequest request, Connection con, UsuarioBasico usuario)
	{
//		PersonaBasica persona=new PersonaBasica();
//		forma.setManejoProgramacionSalas(ValoresPorDefecto.getManejoProgramacionSalasSolicitudesDyt(usuario.getCodigoInstitucionInt()));
//		try
//		{
//			TipoNumeroId id=new TipoNumeroId(forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion());
//			persona.cargar(con, id);
//			persona.cargarPaciente(con, id, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
//			
//			if(!persona.getNumeroIdentificacionPersona().equals(""))
//			{
//				forma.setPrimerNombre(persona.getPrimerNombre());
//				forma.setSegundoNombre(persona.getSegundoNombre());
//				forma.setPrimerApellido(persona.getPrimerApellido());
//				forma.setSegundoApellido(persona.getSegundoApellido());
//				forma.setFechaNacimiento(persona.getFechaNacimiento());
//				forma.setDireccion(persona.getDireccion());
//				forma.setTelefono(persona.getTelefono());
//				forma.setSexo(persona.getCodigoSexo());
//				forma.setExistePersonaEnSistema(true);
//				forma.setCodigoPersona(persona.getCodigoPersona());
//				String[] datosCiudad = Utilidades.getCodigoPaisDeptoCiudadPersona(con, forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion()).split(ConstantesBD.separadorSplit);
//				
//				forma.setCodigoPaisId(datosCiudad[0]);
//				forma.setCodigoCiudadId( datosCiudad[1]+ConstantesBD.separadorSplit+datosCiudad[2] );
//				forma.setGrupoPoblacional(persona.getCodigoGrupoPoblacional());
//				
//				//Se cargan las ciudades de un pais
//				forma.setCiudades(Utilidades.obtenerCiudadesXPais(con, datosCiudad[0]));
//				
//				Cuenta i=new Cuenta();
//				i.cargarCuenta(con, persona.getCodigoCuenta()+"");
//				forma.getMapaPeticionEncabezado().put("tipoPaciente", i.getCodigoTipoPaciente());
//			}
//			else
//			{
//				forma.setDireccion(ValoresPorDefecto.getDireccionPaciente(usuario.getCodigoInstitucionInt()));
//				forma.setExistePersonaEnSistema(false);
//				
//				//************VALIDACIONES QUE SE HACEN CUANDO NO EXISTE PACIENTE***********************
//				ActionErrors errores = new ActionErrors();
//				//1) Se valida el consecutivo de historia clinica
//				errores = validacionConsecutivoHistoriaClinica(con, usuario, errores);
//				
//				if(!errores.isEmpty())
//				{
//					saveErrors(request, errores);
//					UtilidadBD.closeConnection(con);
//					return mapping.findForward("paginaErroresActionErrors");
//				}
//				
//				
//			}
//		}
//		catch (SQLException e)
//		{
//			logger.error("Error cargando la persona : "+e);
//			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
//			cerrarConexion(con);
//			return mapping.findForward("paginaError");
//		}
		
		//Se consultan los tipos de anestesia
		forma.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));

		cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Método implementado para efectuar las validaciones del consecutivo disponible de historia clinica
	 * @param con
	 * @param ingresoPacienteForm
	 * @param usuarioActual
	 * @param errores 
	 * @return
	 */
	private ActionErrors validacionConsecutivoHistoriaClinica(Connection con, UsuarioBasico usuarioActual, ActionErrors errores) 
	{
		
		String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt());
		
		
		if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
			errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","la historia clínica"));
		else
		{
			//se asignan los nuevos valores
			
			try
			{
				//se asigna el proximo consecutivo
				Integer.parseInt(consecutivo);
			}
			catch(Exception e)
			{
				logger.error("Error en validacionConsecutivoDisponible:  "+e);
				errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de la historia clínica"));
			}
		}
		
		return errores;
	}

	/**
	 * Método que redirecciona a la página de ingreso tipo y número de Id
	 * @param forma Forma para resetear
	 * @param mapping Mapping para Redirrecion
	 * @param con Conexión con la BD
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezar(PeticionQxForm forma, ActionMapping mapping, Connection con, HttpServletRequest request, ActionErrors errores, PersonaBasica paciente, UsuarioBasico usuario)
	{
		forma.reset();
		if(paciente==null || paciente.getCodigoPersona()<1){
			ActionForward fw=ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
			UtilidadBD.closeConnection(con);
			return fw;
		}
		else{
			IIngresosMundo ingresosMundo = ManejoPacienteFabricaMundo.crearIngresosMundo();
			DtoInfoIngresoTrasladoAbonoPaciente ingresoDto=ingresosMundo.obtenerUltimoIngresoPaciente(paciente.getCodigoPersona());
			UtilidadTransaccion.getTransaccion().commit();
			if(ingresoDto!=null){
				if(ingresoDto.getFechaEgreso()!=null){
					String dias=ValoresPorDefecto.getNumDiasEgresoOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
					if(dias!=null && !dias.trim().isEmpty()){
						UtilidadTransaccion.getTransaccion().begin();
						Calendar fechaEgreso=Calendar.getInstance();
						Calendar fechaActual=Calendar.getInstance();
						fechaEgreso.setTime(ingresoDto.getFechaEgreso());
						int diasParametro=Integer.parseInt(dias);
						fechaEgreso.add(Calendar.DATE, diasParametro);
						if(ingresoDto.getHoraEgreso() != null && !ingresoDto.getHoraEgreso().isEmpty()){
							String[] horaStr=ingresoDto.getHoraEgreso().split(":");
							int horas=Integer.parseInt(horaStr[0]);
							int minutos=Integer.parseInt(horaStr[1]);
							fechaEgreso.set(Calendar.HOUR_OF_DAY, horas);
							fechaEgreso.set(Calendar.MINUTE, minutos);
						}
						if(fechaActual.getTimeInMillis()>fechaEgreso.getTimeInMillis()){
							String mensajeError=fuenteMensaje.getMessage("errors.paciente.noValidoParametroDias", new Object[]{dias});
							ActionForward fw=ComunAction.accionSalirCasoError(mapping, request, con, logger, "Validación parametro días", mensajeError, false);
							UtilidadBD.closeConnection(con);
							return fw;
				    	}
					}
					else{
						ActionForward fw=ComunAction.accionSalirCasoError(mapping, request, con, logger, "Parametro no definido", "errors.paciente.parametroDiasNoDefinido", true);
						UtilidadBD.closeConnection(con);
						return fw;
					}
					forma.setIngresoId(ingresoDto.getIdIngreso());
				}
				else{
					forma.setIngresoId(ingresoDto.getIdIngreso());
				}
			}
			else{
				ActionForward fw=ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente sin ingreso", "errors.paciente.noTieneIngresoValido", true);
				UtilidadBD.closeConnection(con);
				return fw;
			}
		}
		
		//MT5061- hermorhu
		ArrayList<ProfesionalEspecialidadesDto> listProfesionalEspecialidades = null;
		
		AdministracionFacade administracionFacade = new AdministracionFacade();
		
		try {
			listProfesionalEspecialidades = (ArrayList<ProfesionalEspecialidadesDto>) administracionFacade.consultarProfesionalesEspecialidades(usuario.getCodigoInstitucionInt());
		} catch (IPSException ipse) {
			Log4JManager.error(ipse.getMessage(), ipse);
		}
		
		forma.setProfesionalEspecialidades(listProfesionalEspecialidades);	
		
		//Se consultan los tipos de anestesia
		forma.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
		forma.setSexo(paciente.getCodigoSexo());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Metodo para realizar la consulta de todas las peticiones de cirugia del sistema
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param codigoPaciente
	 * @param usuario
	 * @param codigoCuenta
	 * @return
	 */
	private ActionForward accionConsultarGeneral(PeticionQxForm forma, ActionMapping mapping, Connection con, int codigoPaciente, UsuarioBasico usuario, int codigoCuenta) 
	{
		Peticion mundoPeticion = new Peticion();
		forma.reset();
	 	forma.setMaxPageItems( Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())) );
		
//	 	-Si viene de consultar para preanestesia entonces (filtrar la consulta)...
 		mundoPeticion.setFiltrosMap("programable",ConstantesBD.acronimoSi);		
 		
	 	if ( forma.getOrigen().equals(PeticionQxForm.origenPreanestesia) || forma.getOrigen().equals(PeticionQxForm.origenModificar))
	 	{
	 		
	 		if(forma.getOrigen().equals(PeticionQxForm.origenPreanestesia))
	 			forma.setMapaConsultaPeticiones( mundoPeticion.cargarPeticionesCirugias(con, codigoPaciente, 1, codigoCuenta, usuario.getCodigoCentroAtencion(),mundoPeticion.getFiltrosMap()));
	 		else
	 			forma.setMapaConsultaPeticiones( mundoPeticion.cargarPeticionesCirugias(con, codigoPaciente, 1, -1, -1, mundoPeticion.getFiltrosMap()));
	 			
	 	}
	 	else
	 	{   //-Mostrar todas las peticiones...
	 		forma.setMapaConsultaPeticiones( mundoPeticion.cargarPeticionesCirugias(con, codigoPaciente, 0, -1, -1, mundoPeticion.getFiltrosMap()));
	 	}
		cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Método para cerrar la conexión
	 * @param con Conextion con la BD Abierta
	 */
	private void cerrarConexion(Connection con)
	{
		try
		{
			if(con!=null && !con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión : "+e);
		}
	}
	
	
	/**
	 * Método que se encarga de setear los datos correspondientes al dtoValidacionGeneracionAutorizacionCapitadaMundo
	 * para la generación de la autorización de capitación para Peticiones
	 * RQF 02-0025
	 * 
	 * @author Ricardo Ruiz
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param forma
	 */
	private List<AutorizacionCapitacionDto> generarAutorizacionCapitacionPeticion(Connection con, PersonaBasica paciente, UsuarioBasico usuario,
							HttpServletRequest request, PeticionQxForm forma, DtoDiagnostico dtoDiagnostico, int numeroPeticion, 
							String cubierto, int contratoConvenio, int convenio, int ingresoPaciente){
		
		IValidacionGeneracionAutorizacionCapitadaMundo validacionGeneracionAutorizacionCapitadaMundo=ManejoPacienteFabricaMundo.crearValidacionGeneracionAutorizacionCapitadaMundo();
		DtoValidacionGeneracionAutorizacionCapitada dtoValidacion=new DtoValidacionGeneracionAutorizacionCapitada();
		List<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionGeneracionAutorizacionCapitada = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
		DtoServicios dtoServicios = null;
		ArrayList<DtoServicios> listaServicios	=new ArrayList<DtoServicios>();
		List<AutorizacionCapitacionDto> listaAutorizacionesCapitacion= new ArrayList<AutorizacionCapitacionDto>();

		try {
			dtoValidacion.setArticulos(null);	
			dtoValidacion.setPaciente(paciente);
			dtoValidacion.setTipoOrden(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx);
			dtoValidacion.setOrdenAmbulatoria(numeroPeticion);
			dtoValidacion.setConsecutivoOrden(String.valueOf(numeroPeticion)); 
			dtoValidacion.getCentrosCostoSolicitante().setCodigo(usuario.getCodigoCentroCosto());
			dtoValidacion.setAcronimoDiagnostico(dtoDiagnostico.getAcronimoDiagnostico());
			
			for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaPeticionServicios().get("numeroFilasMapaServicios").toString());i++)
			{
				if(!forma.getMapaPeticionServicios().get("fueEliminadoServicio_"+i).toString().equals("true")){
					dtoServicios = new DtoServicios();
					dtoServicios.setCodigoServicio(Utilidades.convertirAEntero(forma.getMapaPeticionServicios().get("codigoServicio_"+i).toString()));
					dtoServicios.setDescripcionServicio(forma.getMapaPeticionServicios().get("descripcionServicio_"+i).toString());
					dtoServicios.setCantidad(1);
					dtoServicios.setServicioCubierto(forma.getMapaPeticionServicios().get("cubierto_"+i).toString());
					listaServicios.add(dtoServicios);
				}	
			}
			dtoValidacion.setServicios(listaServicios);
			dtoValidacion.setContratoConvenioResponsable(contratoConvenio);
			dtoValidacion.setConvenio(convenio);
			dtoValidacion.setPyp(false);
			dtoValidacion.setCodIngreso(ingresoPaciente);
			
			listaValidacionGeneracionAutorizacionCapitada.add(dtoValidacion);
			
			//Se hace llamado al proceso de Generación Capitacion DCU-966
			listaAutorizacionesCapitacion = validacionGeneracionAutorizacionCapitadaMundo.generarValidacionGeneracionAutorizacionCapitada(listaValidacionGeneracionAutorizacionCapitada, usuario);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return listaAutorizacionesCapitacion;
		
	}
	
	/**
	 * M&eacute;todo encargado de generar el reporte de la o las 
     * autorizaciones
	 * @param Connection con, PeticionQxForm forma, UsuarioBasico usuario, PersonaBasica paciente 
			HttpServletRequest request 
	 * @author Ricardo Ruiz
	 */

	private void accionImprimirAutorizacion(Connection con, PeticionQxForm forma, UsuarioBasico usuario,
						PersonaBasica paciente, ActionErrors errores, HttpServletRequest request) throws IPSException {
			String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
			if(!UtilidadTexto.isEmpty(tipoFormatoImpresion))
			{		
				if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar)){
	 				generarReporteAutorizacionFormatoEstandar(forma, usuario, paciente, request);
	 			
	 			}else if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionVersalles)){
	 				generarReporteAutorizacionFormatoVersalles(con, forma, usuario, paciente, request);
	 			}
			}
			else{
				errores.add("Formato no Definifo",
						new ActionMessage("errors.notEspecific", fuenteMensaje.getMessage(
								"errors.peticiones.formatoNoDefinido")));
				saveErrors(request, errores);
			}
	}	
	
	/**
	 * M&eacute;todo encargado de generar el reporte en formato Estandar de la o las 
     * autorizaciones
	 * @param PeticionQxForm forma, UsuarioBasico usuario, PersonaBasica paciente 
			HttpServletRequest request 
	 * @author Ricardo Ruiz
	 */

	private void generarReporteAutorizacionFormatoEstandar(PeticionQxForm forma, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request) {
		
			String nombreReporte="AUTORIZACION ORDENES MEDICAS";
			String nombreArchivo ="";
			DTOReporteEstandarAutorizacionServiciosArticulos dtoReporte = 
				new DTOReporteEstandarAutorizacionServiciosArticulos();
			
			ArrayList<String> listaNombresReportes = new ArrayList<String>();
				
	    	InstitucionBasica institucion = (
		        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
						 			     			
			String nombrePaciente = paciente.getPrimerNombre() + " " + 
				paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
				" " + paciente.getSegundoApellido();
				 			     			
			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuario.getCodigoInstitucionInt());
			
			String infoParametroGeneral = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					usuario.getCodigoInstitucionInt());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					usuario.getCodigoInstitucionInt());
			
			if(UtilidadTexto.isEmpty(reporteMediaCarta)){
				reporteMediaCarta=ConstantesBD.acronimoNo;
			}
				
			dtoReporte.setNombrePaciente(nombrePaciente);
			dtoReporte.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoReporte.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoReporte.setFormatoMediaCarta(reporteMediaCarta);
			dtoReporte.setInfoParametroGeneral(infoParametroGeneral);
			dtoReporte.setInfoPiePagina(infoPiePagina);
			dtoReporte.setEntidadAutoriza(usuario.getInstitucion());
			dtoReporte.setUsuarioAutoriza(usuario.getLoginUsuario());
			dtoReporte.setRutaLogo(institucion.getLogoJsp());
			
			DtoValidacionGeneracionAutorizacionCapitada dtoValidacion = forma.getValidacionesAutorizaciones();
			dtoReporte.setTipoContrato(dtoValidacion.getTipoContrato());	 			     			
			dtoReporte.setEntidadSubcontratada(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getRazonSocial());
			dtoReporte.setDireccionEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getDireccion());
			dtoReporte.setTelefonoEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getTelefono());
			for(DTOAutorEntidadSubcontratadaCapitacion autorizacion : dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones()){
				
				dtoReporte.setNumeroAutorizacion(autorizacion.getConsecutivoAutorizacion()+"");
				dtoReporte.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaAutorizacion()));	 			     				
				dtoReporte.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaVencimiento()));
				
				if(!UtilidadTexto.isEmpty(autorizacion.getEstado())){
					
					String estado = (String)ValoresPorDefecto.getIntegridadDominio(
							autorizacion.getEstado());	 			     					
					dtoReporte.setEstadoAutorizacion(estado);	 
				} 			     				
				dtoReporte.setListaServiciosAutorizados(autorizacion.getListaServicios());
				GeneradorReporteFormatoEstandarAutorservicio generadorReporteServicios = 
					new GeneradorReporteFormatoEstandarAutorservicio(dtoReporte);
				
				JasperPrint reporte = generadorReporteServicios.generarReporte();
				nombreArchivo = generadorReporteServicios.exportarReportePDF(reporte, nombreReporte);					
				listaNombresReportes.add(nombreArchivo);	 			     				
			}
			if(listaNombresReportes!=null && listaNombresReportes.size()>0){
				forma.setListaNombresReportes(listaNombresReportes);
			}	
		
	}
	
	/**
	 * M&eacute;todo encargado de generar el reporte en formato Versalles de la o las 
     * autorizaciones
	 * @param Connection con, PeticionQxForm forma, UsuarioBasico usuario, PersonaBasica paciente 
			HttpServletRequest request 
	 * @author Ricardo Ruiz
	 */
	private void generarReporteAutorizacionFormatoVersalles(Connection con, PeticionQxForm forma, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request) throws IPSException {
    	
    	String nombreReporte="AUTORIZACION ORDENES MEDICAS";
		String nombreArchivo ="";
		ArrayList<String> listaNombresReportes = new ArrayList<String>();		
		DtoGeneralReporteServiciosAutorizados dtoReporteServicios = new DtoGeneralReporteServiciosAutorizados();
		DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
		DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
		
		//**********Datos Comunes para todas las autorizaciones que llegan
		Cuenta cuenta= new Cuenta();
	    cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
	    
	    int codigoServicio = forma.getValidacionesAutorizaciones().getServicios().get(0).getCodigoServicio();
	    
	    InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
		infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", 
				paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(),codigoServicio, 
				usuario.getCodigoInstitucionInt(),false, "");
	    
	    DtoSubCuentas subcuenta =  infoResponsableCobertura.getDtoSubCuenta();
	    
	    InstitucionBasica institucion = (
        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
	    
	    String nombrePaciente = paciente.getPrimerNombre() + " " + 
		paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
		" " + paciente.getSegundoApellido();
		 			     			
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
				usuario.getCodigoInstitucionInt());
		
		String infoEncabezado = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
				usuario.getCodigoInstitucionInt());
		
		String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
				usuario.getCodigoInstitucionInt());
		
		if(UtilidadTexto.isEmpty(reporteMediaCarta)){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}	
		
		dtoPaciente.setNombrePaciente(nombrePaciente);
		dtoPaciente.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
		dtoPaciente.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
		dtoPaciente.setTipoAfiliado(cuenta.getTipoAfiliado());
		dtoPaciente.setEdadPaciente(String.valueOf(paciente.getEdad()));
		dtoPaciente.setCategoriaSocioEconomica(cuenta.getEstrato());
		dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
//		dtoPaciente.setSemanasCotizacion(String.valueOf(subcuenta.getSemanasCotizacion()));
		dtoAutorizacion.setEntidadAutoriza(usuario.getInstitucion());
		dtoAutorizacion.setUsuarioAutoriza(usuario.getLoginUsuario());
		
		dtoReporteServicios.setDtoPaciente(dtoPaciente);
		dtoReporteServicios.setDatosEncabezado(infoEncabezado);
		dtoReporteServicios.setDatosPie(infoPiePagina);
		dtoReporteServicios.setTipoReporteMediaCarta(reporteMediaCarta);
		dtoReporteServicios.setRutaLogo(institucion.getLogoJsp());
		dtoReporteServicios.setUbicacionLogo(institucion.getUbicacionLogo());
				
		//**********Datos Comunes para todas las autorizaciones que llegan
		DtoValidacionGeneracionAutorizacionCapitada dtoValidacion = forma.getValidacionesAutorizaciones();
		dtoPaciente.setTipoContratoPaciente(dtoValidacion.getTipoContrato());	
		dtoPaciente.setConvenioPaciente(dtoValidacion.getDescripcionConvenioResponsable());
						
		int idDetalleMonto = subcuenta.getMontoCobro();
		String montoCobro="";
		if(idDetalleMonto >0){
			IDetalleMontoGeneralServicio detalleMontoGeneralServicio = 
				FacturacionServicioFabrica.crearDetalleMontoGeneralServicio();
			DTOMontosCobroDetalleGeneral detalleMontoGeneral = 
				detalleMontoGeneralServicio.obtenerValorTipoMonto(idDetalleMonto);
			
			if(detalleMontoGeneral!=null){
				
				String temporalMontoCobro="";
				if(detalleMontoGeneral.getPorcentaje()!=null)
					temporalMontoCobro=detalleMontoGeneral.getPorcentaje().doubleValue()+"%";
				else if (detalleMontoGeneral.getValor()!=null)
					temporalMontoCobro=" $"+String.valueOf(detalleMontoGeneral.getValor().doubleValue());

				if(!UtilidadTexto.isEmpty(temporalMontoCobro)){
					
					if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
						ConstantesBD.codigoTipoMontoCopago){
						montoCobro=	temporalMontoCobro; 
						
					}else if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
						ConstantesBD.codigoTipoMontoCuotaModeradora){
						montoCobro=	temporalMontoCobro;
					}
				}
			}
		}
		
		dtoPaciente.setMontoCobro(montoCobro);
		dtoAutorizacion.setEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getRazonSocial());
		dtoAutorizacion.setDireccionEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getDireccion());
		dtoAutorizacion.setTelefonoEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getTelefono());
		if(dtoValidacion.getProcesoAutorizacion().getIndicadorPrioridad()!=null && dtoValidacion.getProcesoAutorizacion().getIndicadorPrioridad()>0){
//			dtoAutorizacion.setIndicadorPrioridad(String.valueOf(dtoValidacion.getProcesoAutorizacion().getIndicadorPrioridad())+" días");
		}
		
		//Por cada autorizacion
		for(DTOAutorEntidadSubcontratadaCapitacion autorizacion: dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones()){
			dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaAutorizacion()));
			dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaVencimiento()));
			if(!UtilidadTexto.isEmpty(autorizacion.getEstado())){
				
				String estado = (String)ValoresPorDefecto.getIntegridadDominio(
						autorizacion.getEstado());	 			     					
				dtoAutorizacion.setEstadoAutorizacion(estado);	 
			}
			
			dtoAutorizacion.setObservaciones(autorizacion.getObservacionesGenerales());
			dtoAutorizacion.setNumeroAutorizacion(autorizacion.getConsecutivoAutorizacion());
						
			dtoReporteServicios.setDtoAutorizacion(dtoAutorizacion);
			dtoReporteServicios.setListaServicios(autorizacion.getListaServicios());
			
			GeneradorReporteFormatoCapitacionAutorservicio generadorReporteServicios = 
				new GeneradorReporteFormatoCapitacionAutorservicio(dtoReporteServicios);
			JasperPrint reporte = generadorReporteServicios.generarReporte();
			nombreArchivo = generadorReporteServicios.exportarReportePDF(reporte, nombreReporte);
			
			listaNombresReportes.add(nombreArchivo);
			
//			dtoAutorizacion.setIndicadorPrioridad("");
			dtoAutorizacion.setObservaciones("");
		}
		if(listaNombresReportes!=null && listaNombresReportes.size()>0){
			forma.setListaNombresReportes(listaNombresReportes);
		}	
	}
	
	/**
	 * Metodo que se encarga de enviar los datos necesarios para validar la anulacion de la
	 * autorizacion
	 * 
	 * @param ordenForm
	 * @param usuario
	 * @throws IPSException
	 */
	public void cargarInfoParaAnulacionAutorizacion(PeticionQxForm peticionForm,
			UsuarioBasico usuario)throws IPSException
	{
		AnulacionAutorizacionSolicitudDto anulacionDto	= null;
		ManejoPacienteFacade manejoPacienteFacade		= null;
		Medicos medicos	= null;
		try{
			int motivoAnulacion=Integer.parseInt((String)peticionForm.getMapaPeticionEncabezado().get("motivoAnulacion"));
			String comentario=(String)peticionForm.getMapaPeticionEncabezado().get("comentario");
			String nro = (String) peticionForm.getMapaPeticionEncabezado().get("numeroPeticion");
			int nroPeticion=Integer.parseInt(nro);
			
			anulacionDto= new AnulacionAutorizacionSolicitudDto();
			anulacionDto.setCodigoMotivoAnulacion(motivoAnulacion);
			anulacionDto.setMotivoAnulacion(comentario);
			anulacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
			anulacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
			medicos		= new Medicos(); 
			medicos.setCodigoMedico(usuario.getCodigoPersona());
			anulacionDto.setMedicoAnulacion(medicos);
			anulacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
			anulacionDto.setCodigoPeticion(nroPeticion);
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			manejoPacienteFacade.validarAnulacionAutorizacionCapitaSolictud(anulacionDto, 
					ConstantesBD.claseOrdenPeticion,ConstantesBD.codigoNuncaValido,null,usuario.getCodigoInstitucionInt());
			
		}catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
}
