package com.princetonsa.action.carteraPaciente;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.carteraPaciente.ExtractoDeudoresCPForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.ExtractoDeudoresCP;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ExtractoDeudoresCPAction extends Action
{
	Logger logger = Logger.getLogger(ExtractoDeudoresCPAction.class);
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof ExtractoDeudoresCPForm) 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//ActionErrors
				ActionErrors errores = new ActionErrors();

				ExtractoDeudoresCPForm forma = (ExtractoDeudoresCPForm)form;		
				String estado = forma.getEstado();	

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if (estado.equals("empezar"))
				{
					forma.clean();
					forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
					forma.setListaTiposId(Utilidades.obtenerTiposIdentificacion(con, "", usuario.getCodigoInstitucionInt()));
					return mapping.findForward("filtroBusqueda");
				}

				if (estado.equals("buscarDatosDeudor"))
				{
					forma.setListaExtractos(ExtractoDeudoresCP.consultaDatosDeudor(forma.getDtoDeudor()));
					forma.setNumRegistros(forma.getListaExtractos().size());
					return mapping.findForward("resultadoBusquedaDeudores");
				}

				if (estado.equals("resultadoExtractosDeudor"))
				{
					forma.setDtoBusquedaExtractos(forma.getListaExtractos().get(forma.getIndice()).getDtoDeudor());
					forma.setListadoExtractosDeudor(ExtractoDeudoresCP.consultaExtractosDeudor(forma.getDtoBusquedaExtractos()));
					return mapping.findForward("resultadoExtractosDeudor");
				}

				if (estado.equals("generarReporte"))
				{
					return accionGenerarReporteExtractosDeudor(con, forma, usuario, request, mapping);
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
	
		
	private ActionForward accionGenerarReporteExtractosDeudor(Connection con, ExtractoDeudoresCPForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) 
	{
		
		String nombreRptDesign = "extractoDeudoresPaciente.rptdesign";
		InstitucionBasica ins = (InstitucionBasica) request.getSession().getAttribute("institucionBasica");
		Vector v;
		String consulta;
		
		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "carteraPaciente/", nombreRptDesign);

		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());

		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");

		v = new Vector();
		v.add(ins.getRazonSocial());
		v.add("\nIMPRESIÓN EXTRACTO DEUDORES CARTERA PACIENTE");
		comp.insertLabelInGridOfMasterPage(0, 1, v);

		// Parametros de Generación
		comp.insertGridHeaderOfMasterPageWithName(1, 0, 1, 1, "param");
		v = new Vector();

		String filtroo = "";
		v.add(filtroo);
		
		
		comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v,DesignChoiceConstants.TEXT_ALIGN_LEFT);

		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "+UtilidadFecha.getFechaActual() +" Hora: "+ UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario: "+ usuario.getLoginUsuario());
		// ****************** FIN INFORMACIÓN DEL CABEZOTE

		comp.obtenerComponentesDataSet("consultarExtractos");

		// ***************** NUEVA CONSULTA DEL REPORTE
		consulta=ConsultasBirt.consultarExtractosDeudor(con, forma.getDtoBusquedaExtractos());
		comp.modificarQueryDataSet(consulta);
		// *****************

		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);

		request.setAttribute("isOpenReport", "true");
		request.setAttribute("newPathReport", newPathReport);
	
		// ******************* GENERAR ARCHIVO PLANO
		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo)) 
		{
			String encabezado="ARCHIVO PLANO EXTRACTO DEUDORES CARTERA PACIENTE";
			
			String nombre = CsvFile.armarNombreArchivo("extractoDeudoresPaciente", usuario);
			ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport,nombre, encabezado);
			if (resultado.isTrue()) {
				String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
				forma.setUrlArchivoPlano(rutas[0]);
				forma.setPathArchivoPlano(rutas[2]);
			} else {
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				saveErrors(request, errores);
			}
		}
		// ******************* FIN GENERAR ARCHIVO PLANO

		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoExtractosDeudor");
	}
}