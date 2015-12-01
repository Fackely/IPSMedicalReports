package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.ReportePromocionesOdontologicasForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.odontologia.promocionesOdontologicas.GeneradorReportePlanoPromocionesOdontologicas;
import com.servinte.axioma.generadorReporte.odontologia.promocionesOdontologicas.GeneradorReportePromocionesOdontologicas;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.PromocionesOdontologicasFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.IPromocionesOdontologicasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

public class ReportePromocionesOdontologicasAction extends Action{
	
	/**
	 * Método execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		request.setAttribute("focus", "filtroPromociones.fechaGenInicial");
		if (form instanceof ReportePromocionesOdontologicasForm) {
			
			@SuppressWarnings("unused")
			ActionForward actionForward=null;
			ReportePromocionesOdontologicasForm forma = (ReportePromocionesOdontologicasForm) form;
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				if (estado.equals("empezar")) {
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= empezar(forma, usuario, mapping, ins);
				}else
				if(estado.equals("cargarCiudades")){
					listarCiudades(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}else
				if(estado.equals("cargarCentrosAtencionCiudad")){
					forma.getFiltroPromociones().setCodigoRegionSeleccionada(ConstantesBD.codigoNuncaValido);
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					request.setAttribute("focus", "filtroPromociones.codigoRegionSeleccionada");
					forward= mapping.findForward("principal");
				}else
				if(estado.equals("cargarCentrosAtencionRegion")){
					forma.getFiltroPromociones().setCiudadDeptoPais(ConstantesBD.codigoNuncaValido+"");
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}else
				if(estado.equals("cargarCentrosAtencion")){
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}else
				if(estado.equals("generarReporte")){
					String nombreUsuario = usuario.getNombreUsuario();
					forma.getFiltroPromociones().setNombreUsuario(nombreUsuario);
					
					if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
						this.imprimir(forma,request,usuario,ins);
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
					}
					
					forward= mapping.findForward("principal");
				}
				
			
				UtilidadTransaccion.getTransaccion().commit();
				return forward;
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error generando el Reporte de Promociones Odontológicas", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);

	}

	
	/**
	 * Este método se encarga de enviar los datos para realizar la consulta
	 * de promociones odontológicas y generar el reporte con el resultado
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param ins Institución del usuario en sessión
	 * @author Fabian Becerra
	 */
	private void imprimir(ReportePromocionesOdontologicasForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
        int tipoSalida         = Integer.parseInt(forma.getTipoSalida());
        String nombreArchivo="";
        if (tipoSalida > 0) {
        	
        	ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consultaPromociones=null;
    		if(tipoSalida==EnumTiposSalida.PDF.getCodigo()||tipoSalida==EnumTiposSalida.HOJA_CALCULO.getCodigo()){
    			IPromocionesOdontologicasServicio servicio = PromocionesOdontologicasFabricaServicio.crearIPromocionesOdontologicasServicio();
    			consultaPromociones = servicio.consolidarInfoReportePromociones(forma.getFiltroPromociones());
    		}else{
    			IPromocionesOdontologicasServicio servicio = PromocionesOdontologicasFabricaServicio.crearIPromocionesOdontologicasServicio();
    			consultaPromociones = servicio.consolidarInfoReportePromocionesPlano(forma.getFiltroPromociones());
    			
    		}
    		
	    	if (consultaPromociones != null && consultaPromociones.size()>0) {	
	    		forma.getFiltroPromociones().setUbicacionLogo(ins.getUbicacionLogo());
	    		String rutaLogo = ins.getLogoJsp();
	    		forma.getFiltroPromociones().setRutaLogo(rutaLogo);
	    		
	    		GeneradorReportePromocionesOdontologicas generadorReporte=null;
				JasperPrint reporte=null;
				GeneradorReportePlanoPromocionesOdontologicas generadorReportePlano=null;
	            
	            if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
	            		generadorReporte =
	                    new GeneradorReportePromocionesOdontologicas(consultaPromociones, forma.getFiltroPromociones());
	            		reporte = generadorReporte.generarReporte();
	                    forma.setEnumTipoSalida(EnumTiposSalida.PDF);
	                    
	            } else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
	            		generadorReporte=
	                    new GeneradorReportePromocionesOdontologicas(consultaPromociones, forma.getFiltroPromociones());
	            		reporte = generadorReporte.generarReporte();
	                    forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
	                    
	            } else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
	            		generadorReportePlano =
	            			
	                    new GeneradorReportePlanoPromocionesOdontologicas(consultaPromociones,forma.getFiltroPromociones());
	            		reporte = generadorReportePlano.generarReporte();
	                    forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
	            }
	            
	            switch (forma.getEnumTipoSalida()) {
	            
	            case PDF:
	                    nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReportePromocionesOdontologicas");
	                    break;
	                    
	            case PLANO:
	            		nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reporte, "ReportePromocionesOdontologicas");
	                    break;
	                    
	            case HOJA_CALCULO:
	                    nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReportePromocionesOdontologicas");
	                    break;
	            }
	            
	            forma.setNombreArchivoGenerado(nombreArchivo);
	            
	        }
	        else{
	        	
	        	ActionErrors errores=new ActionErrors();
	            errores.add("No se encontraron resultados", new ActionMessage("errors.modOdontoReportePromocionesOdonto"));
	            saveErrors(request, errores);
	            
	            
	        }
	     }
                
        			
        	
	}
	
	
	/**
	 * Este método se encarga de inicializar los valores de los objetos de la
	 * página de reporte promociones odontólogicas
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 * 
	 * @throws Exception
	 * @author Fabian Becerra
	 * @param ins 
	 */
	public ActionForward empezar(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins)
			throws Exception {
		
		forma.reset();	
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		forma.getFiltroPromociones().setInstitucionMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		forma.setInstitucionMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		forma.getFiltroPromociones().setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		
		//Atributo del parametro institucion utiliza programas odontologicos
		String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion);
		forma.setUtilizaProgramasOdonto(utilizaProgramasOdonto);
		
		Connection con=HibernateUtil.obtenerConexion();
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con,
		        ConstantesBD.codigoFuncionalidadReportesPromociones));
		
		//cargar paises
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
		String[] codigosPais=codigoPaisResidencia.split("-");
		
		if(!codigoPaisResidencia.trim().equals("-")){
			forma.getFiltroPromociones().setCodigoPaisSeleccionado(codigosPais[0]);
		}
		forma.setPaises(AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises());
		
		//cargar ciudades
		listarCiudades(forma, usuario, mapping);
		
		// Cargamos las Regiones
		listarRegiones(forma, usuario, mapping);
		
		// Cargar empresas institucion
		if (forma.getInstitucionMultiempresa().equals(ConstantesBD.acronimoSi)) {
			forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
		}
		
		//cargar centro atencion
		listarCentrosAtencion(forma, usuario, mapping);
		
		//Cargar los sexos
		listarSexoPaciente(forma, usuario, mapping);
		
		//Cargar los estados de la promocion
		listarEstadosPromocion(forma, usuario, mapping);
		
		//Cargar los convenios de atencion odontologica
		forma.setListadoConveniosAtencionOdonto(Utilidades.obtenerConvenios("","",false,"",false,ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico,"",""));
		
		//Cargar los estados civil
		listarEstadosCivil(forma, usuario, mapping);
		
		//Cargar las ocupaciones del paciente
		listarOcupaciones(forma, usuario, mapping);
		
		//Cargar Especialidades
		this.listarEspecialidadesOdonto(forma, usuario, mapping);
		
		//Razon social de la institucion
		String razonSocial = ins.getRazonSocial();
		forma.getFiltroPromociones().setRazonSocial(razonSocial);
		
		
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Este método se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado país.
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCiudades(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String codigoPaisSeleccionado= forma.getFiltroPromociones().getCodigoPaisSeleccionado();
		
		if(UtilidadTexto.isEmpty(codigoPaisSeleccionado) || codigoPaisSeleccionado.trim().equals("-1")){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroPromociones().getCodigoPaisResidencia()));
			
		}else{
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroPromociones().getCodigoPaisSeleccionado()));
			
		}
	}
	
	
	/**
	 * 
	 * Este método se encarga de generar el listado de las regiones de 
	 * cobertura. 
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarRegiones(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		Long codigoRegion= forma.getFiltroPromociones().getCodigoRegionSeleccionada();
		
		if (codigoRegion == 0 || codigoRegion == -1  ) {
			forma.setRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarRegionesCoberturaActivas());
			
		} else {
			forma.setRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarRegionesCoberturaActivas());
			forma.setDeshabilitaCiudad(true);
		}
	}
	
	
	/**
	 * Este método se encarga de listar los centros de atencion del sistema
	 * de acuerdo a unos criterios en especifico.
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCentrosAtencion(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		long empresaInstitucion = forma.getFiltroPromociones().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getFiltroPromociones().getCiudadDeptoPais();
		long codigoRegion = forma.getFiltroPromociones().getCodigoRegionSeleccionada();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		
		
		if (codigoRegion <= 0 
				&& empresaInstitucion <= 0 
				&& (UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +"")) ) {
			
			//lista todos los centros de atencion del sistema
			 
				 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
			 
			 
					
		}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals("-1")) {
			
			String vec[]=forma.getFiltroPromociones().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
			forma.getFiltroPromociones().setCodigoCiudad(vec[0]);
			forma.getFiltroPromociones().setCodigoDpto(vec[1]);
			forma.getFiltroPromociones().setCodigoPais(vec[2]);
			
			if (empresaInstitucion <= 0) {	
				//lista todos por ciudad
				listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
		
			} else {
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, forma.getFiltroPromociones().getCodigoCiudad(),
						forma.getFiltroPromociones().getCodigoPais(), 
						forma.getFiltroPromociones().getCodigoDpto());
		
			}
			
		}else if (codigoRegion > 0) {
			if (empresaInstitucion > 0) {
				//lista todos por region y empresa institucion
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYRegion(
						empresaInstitucion, codigoRegion);
		
				
			} else {
				//listar por region
				listaCentrosAtencion = servicio.listarTodosPorRegion(codigoRegion);
		
			}
			
		} else {
			//lista todos por institucion
			listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucion(empresaInstitucion);
		
		}
		
		if (listaCentrosAtencion != null && listaCentrosAtencion.size()>0) {
			forma.setCentrosAtencion(listaCentrosAtencion);
		}else{
			forma.setCentrosAtencion(null);
		}
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de listar los sexos de los pacientes.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarSexoPaciente(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String[] listadoSexo = new String[]{ConstantesIntegridadDominio.acronimoFemenino,
				ConstantesIntegridadDominio.acronimoMasculino};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listadoSexoPaciente=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoSexo, false);
		
		UtilidadBD.closeConnection(con);
		
		forma.setListaSexoPaciente(listadoSexoPaciente);
		
	}
	
	
	/**
	 * Este método se encarga de listar los estados de la promoción.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Fabian Becerra
	 */
	private void listarEstadosPromocion(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String[] listadoEstados = new String[]{ConstantesIntegridadDominio.acronimoEstadoActivo,
				ConstantesIntegridadDominio.acronimoEstadoInactivo};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listadoEstadosPromocion=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoEstados, false);
		
		UtilidadBD.closeConnection(con);
		
		forma.setListaEstadosPromocion(listadoEstadosPromocion);
		
	}
	
	
	/**
	 * Este método se encarga de listar los estados civiles.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Fabian Becerra
	 */
	@SuppressWarnings("rawtypes")
	private void listarEstadosCivil(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		Connection con=UtilidadBD.abrirConexion();
		
		HashMap listadoEstadosCivil=Utilidades.consultarEstadosCiviles(con);
		forma.setListaEstadosCiviles(listadoEstadosCivil);
				
		UtilidadBD.closeConnection(con);
	
	}
	
	/**
	 * Este método se encarga de listar las ocupaciones del paciente.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Fabian Becerra
	 */
	@SuppressWarnings("rawtypes")
	private void listarOcupaciones(ReportePromocionesOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		Connection con=UtilidadBD.abrirConexion();
		
		HashMap listadoOcupaciones=Utilidades.consultarOcupaciones(con);
		
		forma.setListaOcupaciones(listadoOcupaciones);
				
		UtilidadBD.closeConnection(con);
	
	}
	
	/**
	 * Este método se encarga de obtener el listado de las 
	 * especialidades odontológicas creadas en el sistema.
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 *
	 * @author Yennifer Guerrero
	 */
	private void listarEspecialidadesOdonto(
			ReportePromocionesOdontologicasForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		IEspecialidadServicio servicio = AdministracionFabricaServicio.crearEspecialidadServicio();
		
		Especialidades especialidades = new Especialidades();
		especialidades.setTipoEspecialidad("ODON");
		
		List<Especialidades> listadoEspecialidadesOdonto= servicio.listarEspe(especialidades);
		
		if (listadoEspecialidadesOdonto != null && listadoEspecialidadesOdonto.size() > 0) {
			forma.setEspecialidades(listadoEspecialidadesOdonto);
			
			int codigoEspecialidad = forma.getFiltroPromociones().getCodigoEspecialidad();
			
			for (Especialidades registro : listadoEspecialidadesOdonto) {
				
				if (codigoEspecialidad == registro.getCodigo()) {
					forma.getFiltroPromociones().setNombreEspecialidad(registro.getNombre());
				}
			}
			
		} else {
			forma.setEspecialidades(null);
		}
		
	}
}
