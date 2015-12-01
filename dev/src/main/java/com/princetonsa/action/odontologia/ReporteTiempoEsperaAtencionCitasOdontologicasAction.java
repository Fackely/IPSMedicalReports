package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
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
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.odontologia.ReporteTiempoEsperaAtencionCitasOdontologicasForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.odontologia.tiempoEsperaAtencionCitasOdontologicas.GeneradorReportePlanoTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.generadorReporte.odontologia.tiempoEsperaAtencionCitasOdontologicas.GeneradorReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.OdontologiaServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadesConsultaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

public class ReporteTiempoEsperaAtencionCitasOdontologicasAction extends Action  
{
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		
		
		request.setAttribute("focus", "filtroTiempoEspera.fechaInicial");
		if(form instanceof ReporteTiempoEsperaAtencionCitasOdontologicasForm){
			
			ReporteTiempoEsperaAtencionCitasOdontologicasForm forma = (ReporteTiempoEsperaAtencionCitasOdontologicasForm)form;
			String estado = forma.getEstado(); 
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			//----------- Empezar
			else if(estado.equals("empezar"))
			{
				this.accionEmpezar(forma,usuario, mapping, ins);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			}
			else if (forma.getEstado().equals("cargarCiudades")) {
				listarCiudades(forma, usuario, mapping);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			} 
			else if (forma.getEstado().equals("cargarCentrosAtencionCiudad")) {
				forma.getFiltroTiempoEspera().setCodigoRegionSeleccionada(ConstantesBD.codigoNuncaValido);
				listarCentrosAtencion(forma, usuario, mapping);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				request.setAttribute("focus", "filtroTiempoEspera.codigoRegionSeleccionada");
				//if(forma.getFiltroTiempoEspera().getCiudadDeptoPais())
				return mapping.findForward("principal");
			}
			else if (forma.getEstado().equals("cargarCentrosAtencionRegion")) {
				forma.getFiltroTiempoEspera().setCiudadDeptoPais(ConstantesBD.codigoNuncaValido+"");
				listarCentrosAtencion(forma, usuario, mapping);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			}
			else if (forma.getEstado().equals("cargarCentrosAtencion")) {
				listarCentrosAtencion(forma, usuario, mapping);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminarServicio"))
			{
				forma.getFiltroTiempoEspera().getServicios().remove(forma.getIndiceEliminarServicio());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminarServicio"))
			{
				forma.getFiltroTiempoEspera().getServicios().remove(forma.getIndiceEliminarServicio());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarUnidadAgendaUsuarios"))
			{
				//cargar unidad agenda
				IUnidadesConsultaServicio servicioUC=UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadConsultaServicio();
				int codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
				if(forma.getFiltroTiempoEspera().getConsecutivoCentroAtencionSeleccionado()>=0)
					codigoCentroAtencion=forma.getFiltroTiempoEspera().getConsecutivoCentroAtencionSeleccionado();
				int codigoEspecialidad=ConstantesBD.codigoNuncaValido;
				if(forma.getFiltroTiempoEspera().getCodigoEspecialidad()>=0)
					codigoEspecialidad=forma.getFiltroTiempoEspera().getCodigoEspecialidad();
				
				Log4JManager.info("\n\ncodigoCentroAtencion "+codigoCentroAtencion );
				Log4JManager.info("codigoEspecialidad "+codigoEspecialidad );
				
				forma.setUnidadesConsulta(servicioUC.cargarUnidadesConsultaTipoEspecialidad(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica,codigoEspecialidad,codigoCentroAtencion,false));
				forma.setTamanoUnidadesConsulta(forma.getUnidadesConsulta().size());
				
				//cargar usuarios
				IUsuariosServicio usuarioServicio=AdministracionFabricaServicio.crearUsuariosServicio();
				if(forma.getFiltroTiempoEspera().getConsecutivoCentroAtencionSeleccionado()!=ConstantesBD.codigoNuncaValido)
					forma.setUsuarios(usuarioServicio.obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(forma.getFiltroTiempoEspera().getConsecutivoCentroAtencionSeleccionado()));
				else
					forma.setUsuarios(usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(),false));
				
				//cargar profesionales
				IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
				int codigoInstitucion = usuario.getCodigoInstitucionInt();
				if(forma.getFiltroTiempoEspera().getConsecutivoCentroAtencionSeleccionado()!=ConstantesBD.codigoNuncaValido){
					ArrayList<DtoPersonas> listaProfesionales = servicio.obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(codigoInstitucion, forma.getFiltroTiempoEspera().getConsecutivoCentroAtencionSeleccionado());
					if (listaProfesionales != null) {
						forma.setProfesionales(listaProfesionales);
					} else {
						forma.setProfesionales(null);
					}
				}
				else{
					listarProfesionales(forma, usuario, mapping, ins);
				}
				
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("recarga"))
			{
				DtoServicios servicio=new DtoServicios();
				servicio.setCodigoServicio(forma.getCodigoServicio());
				servicio.setDescripcionPropietarioServicio(forma.getNombreServicio());
				forma.getFiltroTiempoEspera().getServicios().add(servicio);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			}
			if(estado.equals("generarReporte")){
				
				String nombreUsuario = usuario.getNombreUsuario();
				forma.getFiltroTiempoEspera().setNombreUsuario(nombreUsuario);
				
				if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
					this.imprimir(forma,request,usuario,ins);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
				}
				
				return mapping.findForward("principal");
			}
			
		}
		return null;
	}
	
	/**
	 * Este método se encarga de enviar los datos para realizar la consulta
	 * de tiempos de espera atención citas odontológicas y generar el reporte con el resultado
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param ins Institución del usuario en sessión
	 * @author Fabian Becerra
	 */
	private void imprimir(ReporteTiempoEsperaAtencionCitasOdontologicasForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
        int tipoSalida         = Integer.parseInt(forma.getTipoSalida());
        String nombreArchivo="";
        if (tipoSalida > 0) {
        	
        	//valida los servicios enviados en la consulta
        	forma.getFiltroTiempoEspera().setCodigosServicios(new ArrayList<Integer>());
        	ArrayList<Integer> codigos= new ArrayList<Integer>();
        	if(!Utilidades.isEmpty(forma.getFiltroTiempoEspera().getServicios())){
        		for(int i=0;i<forma.getFiltroTiempoEspera().getServicios().size();i++)
        		{
        			codigos.add(forma.getFiltroTiempoEspera().getServicios().get(i).getCodigoServicio());
        			
        		}
        		forma.getFiltroTiempoEspera().setCodigosServicios(codigos);
        	}
        	
        	//valida los usuarios que se encuentren en el jsp
        	forma.getFiltroTiempoEspera().setLoginUsuarios(new ArrayList<String>());
        	ArrayList<String> loginusuarios= new ArrayList<String>();
        	if(!Utilidades.isEmpty(forma.getUsuarios())){
        		for(int i=0;i<forma.getUsuarios().size();i++)
        		{
        			loginusuarios.add(forma.getUsuarios().get(i).getLogin());
        			
        		}
        		forma.getFiltroTiempoEspera().setLoginUsuarios(loginusuarios);
        	}
        	
        	ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consultaTiempoEspera=null;
    		if(tipoSalida==EnumTiposSalida.PDF.getCodigo()||tipoSalida==EnumTiposSalida.HOJA_CALCULO.getCodigo()){
    			ICitaOdontologicaServicio servicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
    			consultaTiempoEspera = servicio.consolidarInfoReporteTiemposEspera(forma.getFiltroTiempoEspera());
    		}else{
    			ICitaOdontologicaServicio servicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
    			consultaTiempoEspera = servicio.consolidarInfoReporteTiemposEsperaPlano(forma.getFiltroTiempoEspera());
    			
    		}
    		
	    	if (consultaTiempoEspera != null && consultaTiempoEspera.size()>0) {	
	    		forma.getFiltroTiempoEspera().setUbicacionLogo(ins.getUbicacionLogo());
	    		String rutaLogo = ins.getLogoJsp();
	    		forma.getFiltroTiempoEspera().setRutaLogo(rutaLogo);
	    		
	    		GeneradorReporteTiempoEsperaAtencionCitasOdonto generadorReporte=null;
				JasperPrint reporte=null;
				GeneradorReportePlanoTiempoEsperaAtencionCitasOdonto generadorReportePlano=null;
	            
	            if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
	            		generadorReporte =
	                    new GeneradorReporteTiempoEsperaAtencionCitasOdonto(consultaTiempoEspera, forma.getFiltroTiempoEspera());
	            		reporte = generadorReporte.generarReporte();
	                    forma.setEnumTipoSalida(EnumTiposSalida.PDF);
	                    
	            } else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
	            		generadorReporte=
	                    new GeneradorReporteTiempoEsperaAtencionCitasOdonto(consultaTiempoEspera, forma.getFiltroTiempoEspera());
	            		reporte = generadorReporte.generarReporte();
	                    forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
	                    
	            } else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
	            		generadorReportePlano =
	            			
	                    new GeneradorReportePlanoTiempoEsperaAtencionCitasOdonto(consultaTiempoEspera,forma.getFiltroTiempoEspera());
	            		reporte = generadorReportePlano.generarReporte();
	                    forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
	            }
	            
	            switch (forma.getEnumTipoSalida()) {
	            
	            case PDF:
	                    nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteTiempoEsperaAtencionCitasOdontologicas");
	                    break;
	                    
	            case PLANO:
	            		nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reporte, "ReporteTiempoEsperaAtencionCitasOdontologicas");
	                    break;
	                    
	            case HOJA_CALCULO:
	                    nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteTiempoEsperaAtencionCitasOdontologicas");
	                    break;
	            }
	            
	            forma.setNombreArchivoGenerado(nombreArchivo);
	            
	        }
	        else{
	        	
	        	ActionErrors errores=new ActionErrors();
	            errores.add("No se encontraron resultados", new ActionMessage("errors.modOdontoReporteTiemposEspera"));
	            saveErrors(request, errores);
	            
	            
	        }
	     }
                
        			
        	
	}
	
	
	/**
	 * Este método se encarga de inicializar los valores de los objetos de la
	 * página para la búsqueda de los criterios con el fin de realizar 
	 * el reporte de tiempos de espera atención citas odontológicas
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void accionEmpezar(ReporteTiempoEsperaAtencionCitasOdontologicasForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins){
		
		forma.reset();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		forma.getFiltroTiempoEspera().setInstitucionMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		forma.setInstitucionMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		
		
		
		
		
		Connection con=HibernateUtil.obtenerConexion();
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con,
		        ConstantesBD.codigoFuncionalidadMenuReportesCitasOdonto));
		
		//cargar paises
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
		String[] codigosPais=codigoPaisResidencia.split("-");
		
		if(!codigoPaisResidencia.trim().equals("-")){
			forma.getFiltroTiempoEspera().setCodigoPaisSeleccionado(codigosPais[0]);
		}
		forma.setPaises(AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises());
		
		//cargar los profesionales.
		forma.setOcupacionOdontologo(ValoresPorDefecto.getOcupacionOdontologo(codigoInstitucion));
        listarProfesionales(forma, usuario, mapping, ins);
        
      //cargar los usuarios.
		IUsuariosServicio usuarioServicio=AdministracionFabricaServicio.crearUsuariosServicio();
		forma.setUsuarios(usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(),false));
		
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
		
		//Cargar Especialidades
		this.listarEspecialidadesOdonto(forma, usuario, mapping);
		
		//cargar Unidades Consulta
		IUnidadesConsultaServicio servicioUC=UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadConsultaServicio();
		int codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
		int codigoEspecialidad=ConstantesBD.codigoNuncaValido;
		if(forma.getCentrosAtencion().size()==1)
			codigoCentroAtencion=forma.getCentrosAtencion().get(0).getConsecutivo();
		if(forma.getEspecialidades()!=null){
			if(forma.getEspecialidades().size()==1)
				codigoEspecialidad=forma.getEspecialidades().get(0).getCodigo();
		}
		forma.setUnidadesConsulta(servicioUC.cargarUnidadesConsultaTipoEspecialidad(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica,codigoEspecialidad,codigoCentroAtencion,false));
		
		forma.getFiltroTiempoEspera().setCiudadDeptoPais(new String());
		
		//Razon social de la institucion
		String razonSocial = ins.getRazonSocial();
		forma.getFiltroTiempoEspera().setRazonSocial(razonSocial);
		
		forma.setTamanoUnidadesConsulta(forma.getUnidadesConsulta().size());
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
	private void listarCiudades(ReporteTiempoEsperaAtencionCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String codigoPaisSeleccionado= forma.getFiltroTiempoEspera().getCodigoPaisSeleccionado();
		
		if(UtilidadTexto.isEmpty(codigoPaisSeleccionado) || codigoPaisSeleccionado.trim().equals("-1")){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroTiempoEspera().getCodigoPaisResidencia()));
			
		}else{
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroTiempoEspera().getCodigoPaisSeleccionado()));
			
		}
	}
	
	/**
	 * Este método se encarga de listar los médicos o profesionales
	 * de la salud dependiendo el parámetro general ocupación odontólogo
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 * 
	 * @author fabian becerra
	 * @param forma
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 */
	private void listarProfesionales(ReporteTiempoEsperaAtencionCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins) {
		
		IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		//retorna los profesionales dependiendo el parametro ocupacion odontologo
		ArrayList<DtoPersonas> listaProfesionales = servicio.obtenerMedicosOdontologos(codigoInstitucion);
		
		if (listaProfesionales != null) {
			forma.setProfesionales(listaProfesionales);
			
		} else {
			forma.setProfesionales(null);
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
	private void listarRegiones(ReporteTiempoEsperaAtencionCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		Long codigoRegion= forma.getFiltroTiempoEspera().getCodigoRegionSeleccionada();
		
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
	private void listarCentrosAtencion(ReporteTiempoEsperaAtencionCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		long empresaInstitucion = forma.getFiltroTiempoEspera().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getFiltroTiempoEspera().getCiudadDeptoPais();
		long codigoRegion = forma.getFiltroTiempoEspera().getCodigoRegionSeleccionada();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		
		
		if (codigoRegion <= 0 
				&& empresaInstitucion <= 0 
				&& (UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +"")) ) {
			
			//lista todos los centros de atencion del sistema
			 
				 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
			 
			 
					
		}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals("-1")) {
			
			String vec[]=forma.getFiltroTiempoEspera().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
			forma.getFiltroTiempoEspera().setCodigoCiudad(vec[0]);
			forma.getFiltroTiempoEspera().setCodigoDpto(vec[1]);
			forma.getFiltroTiempoEspera().setCodigoPais(vec[2]);
			
			if (empresaInstitucion <= 0) {	
				//lista todos por ciudad
				listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
		
			} else {
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, forma.getFiltroTiempoEspera().getCodigoCiudad(),
						forma.getFiltroTiempoEspera().getCodigoPais(), 
						forma.getFiltroTiempoEspera().getCodigoDpto());
		
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
	 * Este método se encarga de obtener el listado de las 
	 * especialidades odontológicas creadas en el sistema.
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 *
	 * @author Yennifer Guerrero
	 */
	private void listarEspecialidadesOdonto(
			ReporteTiempoEsperaAtencionCitasOdontologicasForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		IEspecialidadServicio servicio = AdministracionFabricaServicio.crearEspecialidadServicio();
		
		Especialidades especialidades = new Especialidades();
		especialidades.setTipoEspecialidad("ODON");
		
		List<Especialidades> listadoEspecialidadesOdonto= servicio.listarEspe(especialidades);
		
		if (listadoEspecialidadesOdonto != null && listadoEspecialidadesOdonto.size() > 0) {
			forma.setEspecialidades(listadoEspecialidadesOdonto);
			
			int codigoEspecialidad = forma.getFiltroTiempoEspera().getCodigoEspecialidad();
			
			for (Especialidades registro : listadoEspecialidadesOdonto) {
				
				if (codigoEspecialidad == registro.getCodigo()) {
					forma.getFiltroTiempoEspera().setNombreEspecialidad(registro.getNombre());
				}
			}
			
		} else {
			forma.setEspecialidades(null);
		}
		
	}
	
	
	
	
	

}
