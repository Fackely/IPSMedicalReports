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

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.odontologia.ReporteCambioServiciosCitasOdontologicasForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.odontologia.CambioServiciosCitasOdontologicas.GeneradorReporteCambioServiciosCitasOdontologicas;
import com.servinte.axioma.generadorReporte.odontologia.CambioServiciosCitasOdontologicas.GeneradorReportePlanoCambioServiciosCitasOdontologicas;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.OdontologiaServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;


public class ReporteCambioServiciosCitasOdontologicasAction extends Action  
{
	
	/**
	 * MEtodo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		
		
		
		if(form instanceof ReporteCambioServiciosCitasOdontologicasForm){
			
			ReporteCambioServiciosCitasOdontologicasForm forma = (ReporteCambioServiciosCitasOdontologicasForm)form;
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
				return mapping.findForward("principal");
			}
			else if (forma.getEstado().equals("cargarCiudades")) {
				listarCiudades(forma, usuario, mapping);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			} else if (forma.getEstado().equals("cargarCentrosAtencionCiudad")) {
				forma.setCodigoRegionSeleccionada(ConstantesBD.codigoNuncaValido);
				listarCentrosAtencion(forma, usuario, mapping);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			} else if (forma.getEstado().equals("cargarCentrosAtencionRegion")) {
				forma.setCiudadDeptoPais(ConstantesBD.codigoNuncaValido+"");
				listarCentrosAtencion(forma, usuario, mapping);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			} else if (forma.getEstado().equals("cargarCentrosAtencion")) {
				listarCentrosAtencion(forma, usuario, mapping);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("recarga"))
			{
				DtoServicios servicio=new DtoServicios();
				servicio.setCodigoServicio(forma.getCodigoServicio());
				servicio.setDescripcionPropietarioServicio(forma.getNombreServicio());
				forma.getServicios().add(servicio);
				forma.setTipoSalida(null);
				forma.setEnumTipoSalida(null);
				forma.setNombreArchivoGenerado(null);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminarServicio"))
			{
				forma.getServicios().remove(forma.getIndiceEliminarServicio());
				return mapping.findForward("principal");
			}
			if(estado.equals("generarReporte")){
				
				String nombreUsuario = usuario.getNombreUsuario();
				forma.setNombreUsuario(nombreUsuario);
				
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
	 * Este m&eacute;todo se encarga de enviar los datos para realizar la consulta
	 * de solicitudes de cambio de servicios y generar el reporte con el resultado
	 * @param forma
	 *
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez 
	 */
	private void imprimir(ReporteCambioServiciosCitasOdontologicasForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
        int tipoSalida         = Integer.parseInt(forma.getTipoSalida());
        String nombreArchivo="";
        
        if (tipoSalida > 0) {
                
        		DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio = new DtoFiltroReporteCambioServCitaOdonto();
        		filtroCambioServicio.setFechaInicial(forma.getFechaInicialFormateado());
        		filtroCambioServicio.setFechaFinal(forma.getFechaFinalFormateado());
        		filtroCambioServicio.setRazonSocial(forma.getRazonSocial());
        		filtroCambioServicio.setUsuarioProcesa(forma.getNombreUsuario());
        		filtroCambioServicio.setSexoPaciente(forma.getSexoPaciente());
        		if(forma.getRangoEdadInicialPaciente()!=null)
        		{
        			filtroCambioServicio.setRangoEdadInicialFecha(UtilidadFecha.calcularFechaNacimiento(1, forma.getRangoEdadInicialPaciente()));
        			filtroCambioServicio.setRangoEdadInicial(Integer.toString(forma.getRangoEdadInicialPaciente()));
        		}
        			
        		if(forma.getRangoEdadFinalPaciente()!=null){
        			filtroCambioServicio.setRangoEdadFinalFecha(UtilidadFecha.calcularFechaNacimiento(1, forma.getRangoEdadFinalPaciente()));
        			filtroCambioServicio.setRangoEdadFinal(Integer.toString(forma.getRangoEdadFinalPaciente()));
        		}
        		
        		filtroCambioServicio.setEsInstitucionMultiempresa(forma.getInstitucionMultiempresa());
        		filtroCambioServicio.setCodigoPaisSeleccionado(forma.getCodigoPaisSeleccionado());
        		filtroCambioServicio.setCiudadDeptoPais(forma.getCiudadDeptoPais());
        		filtroCambioServicio.setcodigoPrograma(forma.getCodigoPrograma());
        		filtroCambioServicio.setTiposEstadoSolicitud(forma.getTiposEstadoSolicitud());
        		filtroCambioServicio.setCodigoRegionSeleccionada(forma.getCodigoRegionSeleccionada());
        		
        		filtroCambioServicio.setCodigoEmpresaInstitucionSeleccionada(forma.getCodigoEmpresaInstitucion());
        		filtroCambioServicio.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
        		
        		filtroCambioServicio.setConsecutivoCentroAtencionSeleccionado(forma.getConsecutivoCentroAtencionSeleccionado());
        		filtroCambioServicio.setLoginProfesional(forma.getLoginProfesional());
        		filtroCambioServicio.setLoginUsuario(forma.getLoginUsuario());
        		filtroCambioServicio.setServicios(forma.getServicios());
        		filtroCambioServicio.setcodigoEspecialidad(forma.getCodigoEspecialidad());
        		
        	    this.cargarNombreEspecialidad(forma,forma.getCodigoEspecialidad());
        		filtroCambioServicio.setNombreEspecialidad(forma.getNombreEspecialidad());
        		filtroCambioServicio.setNombrePrograma(forma.getDtoSerProSerPro().getProgramas().getNombre());
        		ArrayList<Integer> codigos= new ArrayList<Integer>();
        		for(int i=0;i<forma.getServicios().size();i++)
        		{
        			codigos.add(forma.getServicios().get(i).getCodigoServicio());
        			
        		}
        		filtroCambioServicio.setCodigosServicios(codigos);
        		filtroCambioServicio.setCodigoManualEstandarBusquedaServicios(forma.getCodigoManualEstandarBusquedaServicios());
        			
        		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consultaCambioServicios;
        		if(tipoSalida==EnumTiposSalida.PDF.getCodigo()||tipoSalida==EnumTiposSalida.HOJA_CALCULO.getCodigo()){
        			ICitaOdontologicaServicio servicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
            		consultaCambioServicios = servicio.consolidarInfoReporteCitas(filtroCambioServicio);
        		}else{
        			ICitaOdontologicaServicio servicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
        			consultaCambioServicios = servicio.consolidarInfoReporteCitasPlano(filtroCambioServicio);
        			
        		}
        		
        	if (consultaCambioServicios != null && consultaCambioServicios.size()>0) {	
        		filtroCambioServicio.setUbicacionLogo(ins.getUbicacionLogo());
        		String rutaLogo = ins.getLogoJsp();
    			filtroCambioServicio.setRutaLogo(rutaLogo);
        		
    			GeneradorReporteCambioServiciosCitasOdontologicas generadorReporte=null;
    			JasperPrint reporte=null;
    			GeneradorReportePlanoCambioServiciosCitasOdontologicas generadorReportePlano=null;
                
                if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
                		generadorReporte =
                        new GeneradorReporteCambioServiciosCitasOdontologicas(consultaCambioServicios, filtroCambioServicio);
                		reporte = generadorReporte.generarReporte();
                        forma.setEnumTipoSalida(EnumTiposSalida.PDF);
                        
                } else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
                		generadorReporte=
                        new GeneradorReporteCambioServiciosCitasOdontologicas(consultaCambioServicios, filtroCambioServicio);
                		reporte = generadorReporte.generarReporte();
                        forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
                        
                } else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
                		generadorReportePlano =
                			
                        new GeneradorReportePlanoCambioServiciosCitasOdontologicas(consultaCambioServicios);
                		reporte = generadorReportePlano.generarReporte();
                        forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
                }
                
                switch (forma.getEnumTipoSalida()) {
                
                case PDF:
                        nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteCambioServiciosCitasOdontologicas");
                        break;
                        
                case PLANO:
                		nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reporte, "ReporteCambioServiciosCitasOdontologicas");
                        break;
                        
                case HOJA_CALCULO:
                        nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteCambioServiciosCitasOdontologicas");
                        break;
                }
                
                forma.setNombreArchivoGenerado(nombreArchivo);
                
	        }
	        else{
	        	forma.setListaResultadoConsultaCambioServOdonto(null);
	        	ActionErrors errores=new ActionErrors();
	            errores.add("No se encontraron resultados", new ActionMessage("errors.modOdontoReportesCambioServicios"));
	            saveErrors(request, errores);
	            
	            
	        }
        }	
        	
	}
	
	
	
	
	/**
	 * Este m&eacute;todo se encarga de inicializar los valores de los objetos de la
	 * p&aacute;gina para la b&uacute;squeda de los criterios con el fin de realizar 
	 * el reporte de cambio de servicios de citas odontologicas.
	 * @param forma
	 * @param usuario Usuario que genera el reporte
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez
	 */
	private void accionEmpezar(ReporteCambioServiciosCitasOdontologicasForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins){
		
		forma.setTipoReporte(ConstantesBD.codigoNuncaValido);
		
		forma.reset();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		forma.setInstitucionMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		forma.setCodigoManualEstandarBusquedaServicios(Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)));
		forma.setServicios(new ArrayList<DtoServicios>());
		Connection con=HibernateUtil.obtenerConexion();                
        forma.setPath(Utilidades.obtenerPathFuncionalidad(con,
        ConstantesBD.codigoFuncionalidadMenuReportesCitasOdonto));
		
		//cargar los profesionales.
        listarProfesionales(forma, usuario, mapping, ins);
		
		//cargar los usuarios.
		IUsuariosServicio usuarioServicio=AdministracionFabricaServicio.crearUsuariosServicio();
		forma.setUsuarios(usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(),false));
		
		//cargar paises
		
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
		String[] codigosPais=codigoPaisResidencia.split("-");
		
		if(!codigoPaisResidencia.trim().equals("-")){
			forma.setCodigoPaisSeleccionado(codigosPais[0]);
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

		//Cargar Especialidades
		this.listarEspecialidadesOdonto(forma, usuario, mapping);
		
		//Atributo del parametro institucion utiliza programas odontologicos
		
		String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion);
		forma.setUtilizaProgramasOdonto(utilizaProgramasOdonto);
		
		//Cargar los sexos
		listarSexoPaciente(forma, usuario, mapping);
		
		//Razon social de la institucion
		String razonSocial = ins.getRazonSocial();
		forma.setRazonSocial(razonSocial);
		
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de listar los sexos de los pacientes.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarSexoPaciente(ReporteCambioServiciosCitasOdontologicasForm forma,
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
	 * Este m&eacute;todo se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado pa&iacute;s.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCiudades(ReporteCambioServiciosCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String codigoPaisSeleccionado= forma.getCodigoPaisSeleccionado();
		
		if(UtilidadTexto.isEmpty(codigoPaisSeleccionado) || codigoPaisSeleccionado.trim().equals("-1")){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getCodigoPaisResidencia()));
			
		}else{
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getCodigoPaisSeleccionado()));
			
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de listar los centros de atencion del sistema
	 * de acuerdo a unos criterios en especifico.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCentrosAtencion(ReporteCambioServiciosCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		long empresaInstitucion = forma.getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getCiudadDeptoPais();
		long codigoRegion = forma.getCodigoRegionSeleccionada();
		@SuppressWarnings("unused")
		String esInstitucionMultiempresa= forma.getInstitucionMultiempresa();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		
		
		if (codigoRegion <= 0 && empresaInstitucion <= 0 && (UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +"")) ) {
			
			//lista todos los centros de atencion del sistema
			 
				 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
			 
			 
					
		}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals("-1")) {
			
			String vec[]=forma.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
			forma.setCodigoCiudad(vec[0]);
			forma.setCodigoDpto(vec[1]);
			forma.setCodigoPais(vec[2]);
			
			if (empresaInstitucion <= 0) {	
				//lista todos por ciudad
				listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
		
			} else {
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, forma.getCodigoCiudad(),
						forma.getCodigoPais(), 
						forma.getCodigoDpto());
		
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
	 * 
	 * Este m&eacute;todo se encarga de generar el listado de las regiones de 
	 * cobertura. 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarRegiones(ReporteCambioServiciosCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		Long codigoRegion= forma.getCodigoRegionSeleccionada();
		
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
	 * Este m&eacute;todo se encarga de listar los m&eacute;dicos o profesionales
	 * de la salud que son de tipo odont&oacute;logo o auxiliar de odontolog&iacute;a
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 * @param ins 
	 */
	private void listarProfesionales(ReporteCambioServiciosCitasOdontologicasForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins) {
		
		IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		ArrayList<DtoPersonas> listaProfesionales = servicio.obtenerTodosMedicosOdonto(codigoInstitucion);
		
		if (listaProfesionales != null) {
			forma.setProfesionales(listaProfesionales);
			
		} else {
			forma.setProfesionales(null);
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el listado de las 
	 * especialidades odontol&oacute;gicas creadas en el sistema.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 *
	 * @author Yennifer Guerrero
	 */
	private void listarEspecialidadesOdonto(
			ReporteCambioServiciosCitasOdontologicasForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		IEspecialidadServicio servicio = AdministracionFabricaServicio.crearEspecialidadServicio();
		
		Especialidades especialidades = new Especialidades();
		especialidades.setTipoEspecialidad("ODON");
		
		List<Especialidades> listadoEspecialidadesOdonto= servicio.listarEspe(especialidades);
		
		if (listadoEspecialidadesOdonto != null && listadoEspecialidadesOdonto.size() > 0) {
			forma.setEspecialidades(listadoEspecialidadesOdonto);
			
			int codigoEspecialidad = forma.getCodigoEspecialidad();
			
			for (Especialidades registro : listadoEspecialidadesOdonto) {
				
				if (codigoEspecialidad == registro.getCodigo()) {
					forma.setNombreEspecialidad(registro.getNombre());
				}
			}
			
		} else {
			forma.setEspecialidades(null);
		}
		
	}
	
	/**
	 * Este método se encarga de obtener el nombre de la especialidad
	 * odontológica dependiendo el codigo de la especialidad que se le envia
	 * como parámetro
	 * @param forma
	 * @param codigoEspecialidad
	 *
	 * @author Fabian Becerra
	 */
	private void cargarNombreEspecialidad(ReporteCambioServiciosCitasOdontologicasForm forma, int codigoEspecialidadd){
		
		IEspecialidadServicio servicio = AdministracionFabricaServicio.crearEspecialidadServicio();
		
		Especialidades especialidades = new Especialidades();
		especialidades.setTipoEspecialidad("ODON");
		
		List<Especialidades> listadoEspecialidadesOdonto= servicio.listarEspe(especialidades);
		
		if (listadoEspecialidadesOdonto != null && listadoEspecialidadesOdonto.size() > 0) {
			if(codigoEspecialidadd==ConstantesBD.codigoNuncaValido){
				forma.setNombreEspecialidad("");
			}else{
				for (Especialidades registro : listadoEspecialidadesOdonto) {
					
					if (codigoEspecialidadd == registro.getCodigo()) {
						forma.setNombreEspecialidad(registro.getNombre());
					}
				}
			}
			
			
		} else {
			forma.setEspecialidades(null);
			
		}
	}
	

}


