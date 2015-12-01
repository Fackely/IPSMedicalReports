/**
 * 
 */
package com.princetonsa.action.odontologia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.actionform.odontologia.ReporteCitasOdontologicasForm;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.Especialidades;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.RegionesCobertura;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.dto.administracion.DtoDepartamentos;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoTipoCitaEstadoCita;
import com.servinte.axioma.generadorReporte.odontologia.citaOdontologica.reporteCitasOdontologicas.GeneradorReporteCitasOdontologicas;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.servicio.fabrica.odontologia.OdontologiaServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IReporteCitasOdontologicasServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadesConsultaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * @author armando
 *
 */
public class ReporteCitasOdontologicasAction  extends Action 
{
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
	{
		

		if(form instanceof ReporteCitasOdontologicasForm)
		{
			ReporteCitasOdontologicasForm forma=(ReporteCitasOdontologicasForm) form;
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			Log4JManager.info("estado -->"+forma.getEstado());
			
			if(forma.getEstado().equals("empezar"))
			{
				this.accionEmpezar(forma,usuario);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarCiudades"))
			{
				if (forma.getIndicePaisSeleccionado() == -1) {
					return mapping.findForward("principal");
				} else {
					forma.setIndiceCiudadSeleccionada(ConstantesBD.codigoNuncaValido);
					HibernateUtil.beginTransaction();
					ILocalizacionServicio servicioLocalizacion=AdministracionFabricaServicio.crearLocalizacionServicio();
					forma.setCiudad(servicioLocalizacion.listarCiudadesDtoPorPais(forma.getPaises().get(forma.getIndicePaisSeleccionado()).getCodigoPais()));
					HibernateUtil.endTransaction();
					return mapping.findForward("principal");
				}
			}
			else if(forma.getEstado().equals("cargarCentrosAtencionCiudad"))
			{
				forma.setIndiceRegionSeleccionada(ConstantesBD.codigoNuncaValido);
				accionCargarCentrosAtencion(forma);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarCentrosAtencionRegion"))
			{
				forma.setIndiceCiudadSeleccionada(ConstantesBD.codigoNuncaValido);
				accionCargarCentrosAtencion(forma);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarCentrosAtencion"))
			{
				accionCargarCentrosAtencion(forma);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarUnidadAgenda"))
			{
				//cargar Unidades Consulta
				IUnidadesConsultaServicio servicioUC=UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadConsultaServicio();
				int codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
				if(forma.getIndiceCentroAtencionSeleccionado()>=0)
					codigoCentroAtencion=forma.getCentrosAtencion().get(forma.getIndiceCentroAtencionSeleccionado()).getConsecutivo();
				int codigoEspecialidad=ConstantesBD.codigoNuncaValido;
				if(forma.getIndiceEspecialidadUA()>=0)
					codigoEspecialidad=forma.getEspecialidades().get(forma.getIndiceEspecialidadUA()).getCodigo();
				forma.setUnidadesConsulta(servicioUC.cargarUnidadesConsultaTipoEspecialidad(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica,codigoEspecialidad,codigoCentroAtencion,false));
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("recarga"))
			{
				DtoServicios servicio=new DtoServicios();
				servicio.setCodigoServicio(forma.getCodigoServicio());
				servicio.setDescripcionPropietarioServicio(forma.getNombreServicio());
				forma.getServicios().add(servicio);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminarServicio"))
			{
				forma.getServicios().remove(forma.getIndiceEliminarServicio());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("generarReporte"))
			{
				String nombreUsuario = usuario.getNombreUsuario();
				forma.getFiltro().setNombreUsuario(nombreUsuario);
				boolean hayRegistros = accionImprimirCitasOdontologicas(forma, usuario, ins);
				
				if (!hayRegistros) {
					forma.setEstado("sinDatos");
				}
				
				return mapping.findForward("principal");
			}
			
			else if(forma.getEstado().equals("cargarCanceladaPor"))
			{
				return mapping.findForward("principal");
			}
			
			else if (forma.getEstado().equals("imprimirReporte")) {
				if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
					imprimirReporte(forma);
					request.setAttribute("tipoSalida", forma.getTipoSalida());
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
				}
				
				return mapping.findForward("principal");
			}
			
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		
	}

	/**
	 * 
	 * @param forma
	 */
	private void generarArchvoPlano(ReporteCitasOdontologicasForm forma) 
	{
		StringBuffer resultado=new StringBuffer();
		String finDeLinea="\n";
		resultado.append("Reporte Citas Odontol�gicas");
		if(forma.getFiltro().getTipoReporte()==1)
		{
			generarReporteResumido(forma,resultado,finDeLinea);
		}
		else if(forma.getFiltro().getTipoReporte()==2)
		{
			generarReporteDetallado(forma,resultado,finDeLinea);
		}
	}

	

	/**
	 * 
	 * @param forma
	 * @param resultado
	 * @param finDeLinea 
	 */
	private void generarReporteResumido(ReporteCitasOdontologicasForm forma,StringBuffer resultado, String finDeLinea) 
	{
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		String dir = path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator");
		String nombreArchivo ="ReporteCitasOdontologicasResumido"+ new Random().nextInt()+ ".csv";
		
		generarEncabezadoReporte(forma,resultado,"Resumido",finDeLinea);
		generarCuerpoReporteResumido(forma.getResultadoReporte(),resultado,finDeLinea);
		
		generarArchivo(dir,nombreArchivo,resultado);
		forma.setNombreArchivoGenerado(CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator")+nombreArchivo);
		
	}
	
	

	/**
	 * 
	 * @param forma
	 * @param resultado
	 */
	private void generarReporteDetallado(ReporteCitasOdontologicasForm forma,StringBuffer resultado, String finDeLinea) 
	{

		String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		String dir = path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator");
		String nombreArchivo ="ReporteCitasOdontologicasDetallado"+ new Random().nextInt()+ ".csv";
		
		generarEncabezadoReporte(forma,resultado,"Detallado",finDeLinea);
		generarCuerpoReporteDetallado(forma.getResultadoReporte(),resultado,finDeLinea);
		
		generarArchivo(dir,nombreArchivo,resultado);
		forma.setNombreArchivoGenerado(CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator")+nombreArchivo);
	}

	/**
	 * 
	 * @param resultadoReporte
	 * @param resultado
	 */
	private void generarCuerpoReporteDetallado(ArrayList<DtoResultadoReporteCitasOdontologicas> resultadoReporte,StringBuffer resultado, String finDeLinea) 
	{
		resultado.append("Instituci�n|Centro de Atenci�n|Ciudad | Pa�s | Regi�n | Especialidad | Unidad de Agenda: | Fecha| Hora| Id Paciente| " +
				"Nro ID Paciente| Primer Apellido Paciente| Segundo Apellido Paciente| Primer Nombre Paciente | Segundo Nombre Paciente | " +
				"Tel�fono| Primer Apellido Profesional | Segundo Apellido Profesional| Primer Nombre Profesional| " +
				"Segundo Nombre Profesional|Tipo| Est Cita| Con Cancelacion| Mot Can| Servicio| Usuario| Vlr Cita"+finDeLinea);
		for(DtoResultadoReporteCitasOdontologicas dto:resultadoReporte)
		{
			for(DtoCentroAtencionReporte dtoCA:dto.getCentrosAtencion())
			{
				for(DtoEspecialidadReporte dtoEsp:dtoCA.getEspecialidades())
				{
					generarDetalleReporteCitas(dto,dtoCA,dtoEsp,dtoEsp.getCitasConCancelacionPaciente(),resultado,finDeLinea);					
					generarDetalleReporteCitas(dto,dtoCA,dtoEsp,dtoEsp.getCitasConCancelacionInstitucion(),resultado,finDeLinea);
					generarDetalleReporteCitas(dto,dtoCA,dtoEsp,dtoEsp.getCitasSinCancelacion(),resultado,finDeLinea);
					generarDetalleReporteCitas(dto,dtoCA,dtoEsp,dtoEsp.getCitasSinEspecialidad(),resultado,finDeLinea);
				}
			}
		}
	}

	/**
	 * 
	 * @param dtoEsp 
	 * @param dtoCA 
	 * @param dto 
	 * @param citasConCancelacionPaciente
	 * @param resultado
	 * @param finDeLinea 
	 */
	private void generarDetalleReporteCitas(
			DtoResultadoReporteCitasOdontologicas dto, DtoCentroAtencionReporte dtoCA, DtoEspecialidadReporte dtoEsp, ArrayList<DtoCitasReporte> citas,
			StringBuffer resultado, String finDeLinea) {
		
		if (citas != null && citas.size()> 0) {
			for(DtoCitasReporte cita:citas)
			{
				String cancelacion="";
				if(cita.getTipoCancelacion()>0)
				{
					if(cita.getTipoCancelacion()==ConstantesBD.codigoEstadoCitaCanceladaInstitucion)
					{
						cancelacion="Si-Instituci�n";
					}
					else if(cita.getTipoCancelacion()==ConstantesBD.codigoEstadoCitaCanceladaPaciente)
					{
						cancelacion="Si-Paciente";
					}
				}
				else
				{
					cancelacion="No";
				}
				
				String descripcionServicio = " ";
				
				if (cita.getServicios() != null && cita.getServicios().size()>0) {
					descripcionServicio = cita.getServicios().get(0).getCodigoPropietarioServicio();
				}
				
				resultado.append(dto.getDescripcionEmpresaInstitucion()+"|"+dtoCA.getDescripcionCentroAtencion()+
						"|"+dtoCA.getDescripcionCiudadCA()+"|"+dtoCA.getDescripcionPais()+"|"+dtoCA.getDescripcionRegionCA()+
						"|"+dtoEsp.getDescripcionEspecialidad()+"|"+dtoEsp.getDescripcionUnidadAgenda()+"|"+cita.getFecha()+
						"|"+cita.getHora()+"|"+cita.getTipoIDPaciente()+"|"+cita.getNumeroIDPaciente()+
						"|"+cita.getPrimerApellidoPac()+"|"+cita.getSegundoApellidoPac()+
						"|"+cita.getPrimerNombrePac()+"|"+cita.getSegundoNombrePac()+"|"+cita.getTelefono()+
						"|"+cita.getPrimerApellidoProf()+"|"+ cita.getSegundoApellidoProf() +
						"|"+cita.getPrimerNombreProf()+ "|"+ cita.getSegundoNombreProf() +
						"|"+cita.getTipoCita()+"|"+cita.getEstadoCita()+"|"+cancelacion+
						"|"+cita.getDescripcionMotivoCancelacion()+
						"|"+descripcionServicio);
				
				if(cita.getServicios() != null && cita.getServicios().size()>1)
				{
					for(int i=1;i<cita.getServicios().size();i++)
					{
						resultado.append("-"+  cita.getServicios().get(i).getCodigoPropietarioServicio());
					}
					resultado.append("|");
					
				}else {
					resultado.append("|");
				}
				
				resultado.append(cita.getUsuario()+"|"+cita.getValorCita()+finDeLinea);	
			}
		}
	}

	/**
	 * 
	 * @param resultadoReporte
	 * @param resultado
	 */
	private void generarCuerpoReporteResumido(ArrayList<DtoResultadoReporteCitasOdontologicas> resultadoReporte,StringBuffer resultado, String finDeLinea) 
	{
		resultado.append("Instituci�n| Centro de Atenci�n| Ciudad| Pa�s| Regi�n| Especialidad Unid Agenda| Tipo Cita| Estado Cita| Total Citas"+finDeLinea);
		resultado.append("|||||||||Total Citas Especialidad"+finDeLinea);
		for(DtoResultadoReporteCitasOdontologicas dto:resultadoReporte)
		{
			for(DtoCentroAtencionReporte dtoCA:dto.getCentrosAtencion())
			{
				for(DtoEspecialidadReporte dtoEsp:dtoCA.getEspecialidades())
				{
					int totalCitas=0;
					for(DtoTipoCitaEstadoCita dtoResumen:dtoEsp.getResumenCitas())
					{
						totalCitas=totalCitas+dtoResumen.getNumeroCitas();
						resultado.append(dto.getDescripcionEmpresaInstitucion()+"|"+dtoCA.getDescripcionCentroAtencion()+"|"+
								dtoCA.getDescripcionCiudadCA()+"|"+ dtoCA.getDescripcionPais()+"|"+ dtoCA.getDescripcionRegionCA()+"|"+
								dtoEsp.getDescripcionEspecialidad()+"|"+ValoresPorDefecto.getIntegridadDominio(dtoResumen.getTipoCita())+
								"|"+ValoresPorDefecto.getIntegridadDominio(dtoResumen.getEstadoCita())+"|"+dtoResumen.getNumeroCitas()+finDeLinea);
					}
					if (dtoEsp.getCodigoEspecialidad() >0) {
						resultado.append("||||||||TOTAL CITAS "+dtoEsp.getDescripcionEspecialidad()+" |"+totalCitas+finDeLinea);
					}else{
						resultado.append("||||||||TOTAL CITAS SIN ESPECIALIDAD |"+totalCitas+finDeLinea);
					}
					
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param string
	 * @param string2
	 * @param resultado
	 */
	private void generarArchivo(String rutaArchivo, String nombreArchivo,
			StringBuffer resultado) {
		File directorio = new File(rutaArchivo);
		
		Log4JManager.info("********************* generado por armando" + nombreArchivo);

		if (!directorio.isDirectory() && !directorio.exists()) 
		{
			if (!directorio.mkdirs()) 
			{
				Log4JManager.error("Error creando el directorio "+ rutaArchivo);
			}
		}
		try 
		{
			File archivo=new File(rutaArchivo+nombreArchivo);
			FileOutputStream fos=new FileOutputStream(archivo);
			OutputStreamWriter osw=new OutputStreamWriter(fos, "8859_1");
			osw.write(resultado.toString());
			osw.flush();
			osw.close();
			fos.close();
		} 
		catch (IOException e) 
		{
			Log4JManager.error("error generando reporte",e);
		}
		
	}

	/**
	 * 
	 * @param forma
	 * @param resultado
	 */
	private void generarEncabezadoReporte(ReporteCitasOdontologicasForm forma, StringBuffer resultado,String tipoReporte, String finDeLinea) 
	{
		
		String servicios="";
		if(forma.getFiltro().getServicios().size()==0)
		{
			servicios="Todos";
		}
		else
		{
			for(int i=0;i<forma.getFiltro().getServicios().size();i++)
			{
				servicios=servicios+forma.getFiltro().getServicios().get(i).getDescripcionPropietarioServicio();
			}
		}
		
		String tiposCita="";
		if(forma.getFiltro().getAyudanteTiposCita().length==0)
		{
			tiposCita="Todos";
		}
		else
		{
			for(int i=0;i<forma.getFiltro().getAyudanteTiposCita().length;i++)
			{
				tiposCita=tiposCita + forma.getFiltro().getAyudanteTiposCita()[i] + " ";
			}
		}
		
		String estadosCita="";
		if(forma.getFiltro().getAyudanteEstadosCita().length==0)
		{
			estadosCita="Todos";
		}
		else
		{
			for(int i=0;i<forma.getFiltro().getAyudanteEstadosCita().length;i++)
			{
				estadosCita=estadosCita+forma.getFiltro().getAyudanteEstadosCita()[i] + " ";
			}
		}
		
		String prof=(UtilidadTexto.isEmpty(forma.getFiltro().getProfesional().getPrimerNombre())?"":forma.getFiltro().getProfesional().getPrimerNombre())+" "+(UtilidadTexto.isEmpty(forma.getFiltro().getProfesional().getPrimerApellido())?"":forma.getFiltro().getProfesional().getPrimerApellido());
		
		String canceladoPor="";
		if(forma.getFiltro().getCanceladaPor()==ConstantesBD.codigoEstadoCitaCanceladaPaciente)
		{
			canceladoPor="Canceladas por: Paciente";
		}
		else if(forma.getFiltro().getCanceladaPor()==ConstantesBD.codigoEstadoCitaCanceladaInstitucion)
		{
			canceladoPor="Canceladas por: Institucion";
		}
		else
		{
			canceladoPor="Canceladas por: Ambos";
		}
		
		resultado.append(finDeLinea+tipoReporte+finDeLinea);
		resultado.append("Fecha Inicial:"+forma.getFiltro().getFechaInicial()+"|Fecha Final:"+forma.getFiltro().getFechaFinal()+finDeLinea);
		resultado.append("Especialidad Unid Agenda:"+(UtilidadTexto.isEmpty(forma.getFiltro().getEspecialidad().getDescripcion())?"Todas":forma.getFiltro().getEspecialidad().getDescripcion())+"|Unidad de Agenda "+(UtilidadTexto.isEmpty(forma.getFiltro().getUnidadAgenda().getDescripcion())?"Todos": forma.getFiltro().getUnidadAgenda().getDescripcion())+"| Servicios: "+servicios+finDeLinea);
		resultado.append("Tipo Cita: "+tiposCita+"|Estado Cita: "+estadosCita+"|Profesional "+(UtilidadTexto.isEmpty(prof)?"Todos":prof)+"|Usuario: "+(UtilidadTexto.isEmpty(forma.getFiltro().getUsuario().getLogin())?"Todos":forma.getFiltro().getUsuario().getLogin())+finDeLinea);
		resultado.append("Citas con cancelaci�n: "+(UtilidadTexto.getBoolean(forma.getFiltro().getCancelacionCita())?": Si":": Todas")+"|"+canceladoPor+finDeLinea);
	}

	/**
	 * 
	 * @param forma
	 * @param usuario 
	 * @param ins 
	 */
	private boolean accionImprimirCitasOdontologicas(
			ReporteCitasOdontologicasForm forma, UsuarioBasico usuario, InstitucionBasica ins) {
		
		DtoFiltroReporteCitasOdontologicas dtoFiltro=new DtoFiltroReporteCitasOdontologicas();
		
		
		String fechaInicialFormateada = UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaInicial());
		String fechaFinalFormateada = UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaFinal());
		String nombreUsuario = usuario.getNombreUsuario();
		String razonSocial = ins.getRazonSocial();
		String rutaLogo = ins.getLogoJsp();
		
		dtoFiltro.setNombreUsuario(nombreUsuario);
		dtoFiltro.setInstitucion(usuario.getCodigoInstitucionInt());
		dtoFiltro.setUsuarioReporte(usuario.getLoginUsuario());
		dtoFiltro.setFechaInicial(fechaInicialFormateada);
		dtoFiltro.setFechaFinal(fechaFinalFormateada);
		dtoFiltro.setRazonSocial(razonSocial);
		
		forma.setUbicacionLogo(ins.getUbicacionLogo());
		forma.setRutaLogo(rutaLogo);
		
		dtoFiltro.setUbicacionLogo(ins.getUbicacionLogo());
		dtoFiltro.setRutaLogo(rutaLogo);
		
		if(forma.getIndicePaisSeleccionado()>=0)
			dtoFiltro.setPais(forma.getPaises().get(forma.getIndicePaisSeleccionado()));
		else
			dtoFiltro.setPais(new DtoPaises());
		if(forma.getIndiceCiudadSeleccionada()>=0)
			dtoFiltro.setCiudad(forma.getCiudad().get(forma.getIndiceCiudadSeleccionada()));
		else
		{
			dtoFiltro.setCiudad(new DtoCiudades());
			dtoFiltro.getCiudad().setDepartamento(new DtoDepartamentos());
			dtoFiltro.getCiudad().getDepartamento().setPais(new DtoPaises());
		}
		if(forma.getIndiceRegionSeleccionada()>=0)
			dtoFiltro.setRegionesCobertura(forma.getRegiones().get(forma.getIndiceRegionSeleccionada()));
		else
			dtoFiltro.setRegionesCobertura(new DtoRegionesCobertura());
		if(forma.getIndiceEmpresaInstitucionSeleccionada()>=0)
			dtoFiltro.setEmpresaInstitucion(forma.getEmpresasInstitucion().get(forma.getIndiceEmpresaInstitucionSeleccionada()));
		else
			dtoFiltro.setEmpresaInstitucion(new DtoEmpresasInstitucion());
		if(forma.getIndiceCentroAtencionSeleccionado()>=0)
			dtoFiltro.setCentroAtencion(forma.getCentrosAtencion().get(forma.getIndiceCentroAtencionSeleccionado()));
		else
			dtoFiltro.setCentroAtencion(new DtoCentrosAtencion());
		dtoFiltro.setTiposCita(forma.getTiposCita());
		dtoFiltro.setEstadosCita(forma.getEstadosCita());
		dtoFiltro.setCancelacionCita(forma.getCancelacionCita());
		dtoFiltro.setCanceladaPor(forma.getCanceladaPor());
		if(forma.getIndiceEspecialidadUA()>=0)
			dtoFiltro.setEspecialidad(forma.getEspecialidades().get(forma.getIndiceEspecialidadUA()));
		else
			dtoFiltro.setEspecialidad(new DtoEspecialidades());
		if(forma.getIndiceUnidadAgenda()>=0)
			dtoFiltro.setUnidadAgenda(forma.getUnidadesConsulta().get(forma.getIndiceUnidadAgenda()));
		else
			dtoFiltro.setUnidadAgenda(new DtoUnidadesConsulta());
		if(forma.getIndiceProfesional()>=0)
			dtoFiltro.setProfesional(forma.getProfesionales().get(forma.getIndiceProfesional()));
		else
			dtoFiltro.setProfesional(new DtoProfesional());
		if(forma.getIndiceUsuario()>=0)
			dtoFiltro.setUsuario(forma.getUsuarios().get(forma.getIndiceUsuario()));
		else
			dtoFiltro.setUsuario(new DtoUsuarioPersona());
		
		dtoFiltro.setServicios(forma.getServicios());
		
		dtoFiltro.setTipoReporte(forma.getTipoReporte());
		
		IReporteCitasOdontologicasServicio reporte=OdontologiaServicioFabrica.crearReporteCitasOdontologicas();
		
		ArrayList<DtoResultadoReporteCitasOdontologicas> resultado = new ArrayList<DtoResultadoReporteCitasOdontologicas>();
		
		forma.setResultadoReporte(reporte.consultarCitasOdontologiaDetallado(dtoFiltro));
		
		resultado = forma.getResultadoReporte();
		
		forma.setReporteGenerado(resultado.size()>0);
		
		forma.setFiltro(dtoFiltro);
		
		if (Utilidades.isEmpty(resultado)) {
			return false;
		}else {
			return true;
		}
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionCargarCentrosAtencion(ReporteCitasOdontologicasForm forma) 
	{
		try{
			HibernateUtil.beginTransaction();
			ILocalizacionServicio servicioLocalizacion=AdministracionFabricaServicio.crearLocalizacionServicio();
			if(forma.getIndiceCiudadSeleccionada()>ConstantesBD.codigoNuncaValido)
			{
				if(forma.getIndiceEmpresaInstitucionSeleccionada()>ConstantesBD.codigoNuncaValido)
				{
					//cargar con ciudad y empresa insititucion
					forma.setCentrosAtencion(servicioLocalizacion.listarTodosPorEmpresaInstitucionYCiudad(forma.getEmpresasInstitucion().get(forma.getIndiceEmpresaInstitucionSeleccionada()).getCodigo().longValue(), forma.getCiudad().get(forma.getIndiceCiudadSeleccionada()).getCodigoCiudad(), forma.getPaises().get(forma.getIndicePaisSeleccionado()).getCodigoPais(), forma.getCiudad().get(forma.getIndiceCiudadSeleccionada()).getDepartamento().getCodigoDepartamento()));
				}
				else
				{
					//cargar solo con ciudad
					forma.setCentrosAtencion(servicioLocalizacion.listarTodosPorCiudad(forma.getCiudad().get(forma.getIndiceCiudadSeleccionada()).getCodigoCiudad(), forma.getPaises().get(forma.getIndicePaisSeleccionado()).getCodigoPais(), forma.getCiudad().get(forma.getIndiceCiudadSeleccionada()).getDepartamento().getCodigoDepartamento()));
				}
			}
			else if(forma.getIndiceRegionSeleccionada()>ConstantesBD.codigoNuncaValido)
			{
				if(forma.getIndiceEmpresaInstitucionSeleccionada()>ConstantesBD.codigoNuncaValido)
				{
					//cargar con region y empresa institucion
					forma.setCentrosAtencion(servicioLocalizacion.listarTodosPorEmpresaInstitucionYRegion(forma.getEmpresasInstitucion().get(forma.getIndiceEmpresaInstitucionSeleccionada()).getCodigo().longValue(), forma.getRegiones().get(forma.getIndiceRegionSeleccionada()).getCodigo().longValue()));
				}
				else
				{
					//Cargar solo con region
					forma.setCentrosAtencion(servicioLocalizacion.listarTodosPorRegion(forma.getRegiones().get(forma.getIndiceRegionSeleccionada()).getCodigo().longValue()));
				}
			}
			else
			{
				if(forma.getIndiceEmpresaInstitucionSeleccionada()>ConstantesBD.codigoNuncaValido)
				{
					//Cargar solo con empresa institucion
					forma.setCentrosAtencion(servicioLocalizacion.listarTodosPorEmpresaInstitucion(forma.getEmpresasInstitucion().get(forma.getIndiceEmpresaInstitucionSeleccionada()).getCodigo().longValue()));
				}
				else
				{
					forma.setCentrosAtencion(servicioLocalizacion.listarTodosCentrosAtencion());
				}
			}
			
			//cargar Unidades Consulta
			IUnidadesConsultaServicio servicioUC=UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadConsultaServicio();
			int codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
			int codigoEspecialidad=ConstantesBD.codigoNuncaValido;
			if(forma.getCentrosAtencion().size()==1)
				codigoCentroAtencion=forma.getCentrosAtencion().get(0).getConsecutivo();
			if(forma.getEspecialidades().size()==1)
				codigoEspecialidad=forma.getEspecialidades().get(0).getCodigo();
			forma.setUnidadesConsulta(servicioUC.cargarUnidadesConsultaTipoEspecialidad(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica,codigoEspecialidad,codigoCentroAtencion,false));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("ERROR ", e);
		}
	}

	
	/**
	 * @param forma 
	 * @param usuario 
	 * 
	 */
	private void accionEmpezar(ReporteCitasOdontologicasForm forma, UsuarioBasico usuario) 
	{
		Connection con=null;
		try{
			forma.setTipoReporte(ConstantesBD.codigoNuncaValido);
			
			forma.reset();
			HibernateUtil.beginTransaction();
			con=UtilidadBD.abrirConexion();	
			forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
					ConstantesBD.codigoFuncionalidadMenuReportesCitasOdonto));
			
			String fechaActual = UtilidadFecha.getFechaActual(con);
			forma.setFechaActual(fechaActual);
			UtilidadBD.closeConnection(con);
			
			forma.setServicios(new ArrayList<DtoServicios>());
			
			//cargar paises
			ILocalizacionServicio servicioLocalizacion=AdministracionFabricaServicio.crearLocalizacionServicio();
			forma.setPaises(servicioLocalizacion.listarPaisesDto());
			
			if(forma.getPaises().size()>1)
			{
				String paisResidencia=ValoresPorDefecto.getPaisResidencia(usuario.getCodigoInstitucionInt());
				if(!UtilidadTexto.isEmpty(paisResidencia))
				{
					for(int i=0;i<forma.getPaises().size();i++)
					{
						if(forma.getPaises().get(i).getCodigoPais().equals(paisResidencia.split("-")[0]))
						{
							forma.setIndicePaisSeleccionado(i);
							i=forma.getPaises().size();
						}
					}
				}
			}
			else if(forma.getPaises().size()==1)
			{
				forma.setIndicePaisSeleccionado(0);
			}
			
			
			//cargar ciudades
			if(forma.getIndicePaisSeleccionado()>=0)
			{
				forma.setCiudad(servicioLocalizacion.listarCiudadesDtoPorPais(forma.getPaises().get(forma.getIndicePaisSeleccionado()).getCodigoPais()));
			}
			else
			{
				forma.setCiudad(new ArrayList<DtoCiudades>());
			}
			
			// Cargamos las Regiones
			DtoRegionesCobertura dtoRegiones = new DtoRegionesCobertura();
			dtoRegiones.setInstitucion(usuario.getCodigoInstitucionInt());
			forma.setRegiones(RegionesCobertura.cargar(dtoRegiones));
			
			
			//Cargar empresas institucion
			forma.setInstitucionMultiempresa(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())));
			if(forma.isInstitucionMultiempresa())
			{
				DtoEmpresasInstitucion dto = new DtoEmpresasInstitucion();
				dto.setInstitucion(usuario.getCodigoInstitucionInt());
				forma.setEmpresasInstitucion(ParametrizacionInstitucion.listaInstitucionEmpresa(dto));
			}
			else
			{
				forma.setEmpresasInstitucion(new ArrayList<DtoEmpresasInstitucion>());
			}
	
			//cargar centro atencion.
			forma.setCentrosAtencion(servicioLocalizacion.listarTodosCentrosAtencion());
	
			
			forma.setEspecialidades(Especialidades.cargarEspecialidadesTipo(usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
			
			
			//cargar Unidades Consulta
			IUnidadesConsultaServicio servicioUC=UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadConsultaServicio();
			int codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
			int codigoEspecialidad=ConstantesBD.codigoNuncaValido;
			if(forma.getCentrosAtencion().size()==1)
				codigoCentroAtencion=forma.getCentrosAtencion().get(0).getConsecutivo();
			if(forma.getEspecialidades().size()==1)
				codigoEspecialidad=forma.getEspecialidades().get(0).getCodigo();
			forma.setUnidadesConsulta(servicioUC.cargarUnidadesConsultaTipoEspecialidad(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica,codigoEspecialidad,codigoCentroAtencion,false));
			
			
			//cargar los profesionales.
			ArrayList<Integer> ocupaciones=new ArrayList<Integer>();
			ocupaciones.add(ConstantesBD.codigoOcupacionOdontologo);
			ocupaciones.add(ConstantesBD.codigoOcupacionAuxiliarOdontologia);
			forma.setProfesionales(UtilidadesAdministracion.listarProfesionales(ocupaciones,false));
			
			//cargar los usuarios.
			IUsuariosServicio usuarioServicio=AdministracionFabricaServicio.crearUsuariosServicio();
			forma.setUsuarios(usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(),false));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("ERROR ",e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	private void imprimirReporte(ReporteCitasOdontologicasForm forma) {
		
		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";
		
		if (tipoSalida > 0) {
			
			
			if (tipoSalida == EnumTiposSalida.PLANO.getCodigo()) {
				generarArchvoPlano(forma);
			}else {
				ArrayList<DtoResultadoReporteCitasOdontologicas> listadoResultado = new ArrayList<DtoResultadoReporteCitasOdontologicas>();
				listadoResultado= forma.getResultadoReporte();
				
				DtoFiltroReporteCitasOdontologicas filtro = forma.getFiltro();
				
				GeneradorReporteCitasOdontologicas generadorReporte = 
					new GeneradorReporteCitasOdontologicas(listadoResultado, filtro);
				
				JasperPrint reporte = generadorReporte.generarReporte();
				
				if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ) {
					forma.setEnumTipoSalida(EnumTiposSalida.PDF);
					
				} else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
					forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
					
				} 
				
				switch (forma.getEnumTipoSalida()) {
				
				case PDF:
					nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteCitasOdontoPDF");
					forma.setNombreArchivoGenerado(nombreArchivo);
					break;
					
				case HOJA_CALCULO:
					nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteCitasOdontoEXCEL");
					forma.setNombreArchivoGenerado(nombreArchivo);
					break;
				}
				
			}
		}
		
	}
}
