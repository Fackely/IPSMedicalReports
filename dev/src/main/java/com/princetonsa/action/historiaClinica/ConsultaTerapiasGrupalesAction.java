package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.ConsultaTerapiasGrupalesForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ConsultaTerapiasGrupales;


/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class ConsultaTerapiasGrupalesAction extends Action
{	
	
	Logger logger = Logger.getLogger(ConsultaTerapiasGrupalesAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {		
		Connection con = null;
		try {
			if(response == null);

			if (form instanceof ConsultaTerapiasGrupalesForm) 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");			 
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");


				ConsultaTerapiasGrupalesForm forma = (ConsultaTerapiasGrupalesForm)form;

				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				logger.info("-------------------------------------");
				logger.info("Valor del Estado >> "+forma.getEstado());
				logger.info("-------------------------------------");

				//*******************************************************************************
				//********************************Estados del Action*****************************

				if(estado == null)
				{
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo de Documentos Garantia (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				 
				}	
				else if(estado.equals("porPaciente"))
				{
					if((paciente==null || paciente.getTipoIdentificacionPersona().equals("")))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
					}	

					accionCargarInformacionPorPaciente(con,forma,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("porPaciente");
				}
				else if(estado.equals("porPeriodo"))
				{
					accionCargarInformacionPorPeriodo(con,forma,usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("porPeriodo");
				}
				else if(estado.equals("consultaPeriodo"))
				{
					accionConsultarInformacionPeriodo(con,forma,usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("tGrupalesGeneral");
				}
				else if(estado.equals("cargarDetalleTerapia"))
				{
					accionCargarDetalleTerapia(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalletGrupales"); 
				}
				else if(estado.equals("regresartGrupalesGen"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("tGrupalesGeneral");
				}
			}	 

			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
			 
			 
	 
			 
			 	
	//********************************************************************
	//********************************Metodos*****************************
	
	/**
	 * Carga la consulta de Terapias Grupales por Paciente
	 * @param Connection con
	 * @param ConsultaTerapiasGrupalesForm forma
	 * @param PersonaBasica paciente
	 * */
	private void accionCargarInformacionPorPaciente(Connection con,ConsultaTerapiasGrupalesForm forma, PersonaBasica paciente)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPersona",paciente.getCodigoPersona());
		forma.setTerapiasGrupalesMap(ConsultaTerapiasGrupales.consultarTGrupalPaciente(con, parametros));
	}
	
	
	/**
	 * Carga la informacion necesaria para la consulta por periodo
	 * @param Connection con
	 * @param ConsultaTerapiasGrupalesForm forma
	 * */
	public void accionCargarInformacionPorPeriodo(Connection con, ConsultaTerapiasGrupalesForm forma,int instituciones)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",instituciones);		
		forma.setResponsableRegistra(ConsultaTerapiasGrupales.consultarResponsableRegistro(con, parametros));
		forma.setBusquedaMap("centroAtencion","");
		forma.setBusquedaMap("fechaInicialTerapia","");
		forma.setBusquedaMap("fechaFinalTerapia","");
		forma.setBusquedaMap("profesionalRegistra","");
		forma.setBusquedaMap("centroCosto","");
	} 
	
	
	/**
	 * Consulta la informacion general de las terapias Grupales
	 * @param Connection con
	 * @param ConsultaTerapiasGrupalesForm forma
	 * */
	public void accionConsultarInformacionPeriodo(Connection con,ConsultaTerapiasGrupalesForm forma, int institucion)
	{
		HashMap parametros = new HashMap();
		
		parametros.put("institucion",institucion);	
		
		if(!forma.getBusquedaMap("centroAtencion").equals("") && !forma.getBusquedaMap("centroAtencion").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("centroAtencion",forma.getBusquedaMap("centroAtencion"));
		
		if(!forma.getBusquedaMap("fechaInicialTerapia").equals(""))
		{	
			parametros.put("fechaInicialTerapiaABD",UtilidadFecha.conversionFormatoFechaABD(forma.getBusquedaMap("fechaInicialTerapia").toString()));
			parametros.put("fechaFinalTerapiaABD",UtilidadFecha.conversionFormatoFechaABD(forma.getBusquedaMap("fechaFinalTerapia").toString()));
		}
							
		if(!forma.getBusquedaMap("profesionalRegistra").equals("") && !forma.getBusquedaMap("profesionalRegistra").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("profesionalRegistra",forma.getBusquedaMap("profesionalRegistra"));
		
		if(!forma.getBusquedaMap("centroCosto").equals("") && !forma.getBusquedaMap("centroCosto").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("centroCosto",forma.getBusquedaMap("centroCosto"));
		
		forma.setTerapiasGrupalesMap(ConsultaTerapiasGrupales.consultarTerapiasGrupales(con, parametros));
	}
	
	/**
	 * Consulta la informacion del detalle de la Terapia
	 * @param Connection con
	 * @param  ConsultaTerapiasGrupalesForm forma
	 * */
	public void accionCargarDetalleTerapia(Connection con,ConsultaTerapiasGrupalesForm forma)
	{
		HashMap parametros = new HashMap();
		
		forma.setDetalleMap("numRegistros","0");
		forma.setDetalleMap("descripcionServicio","");
		forma.setDetalleMap("observaciones","");
		forma.setDetalleMap("especialidadProfesional", "");
		forma.setDetalleMap("descripcionusuario","");
		
		if(!forma.getIndexTerapiasGrupalesMap().equals(""))
		{
			parametros.put("codigoTerapia",forma.getTerapiasGrupalesMap("codigoterapia_"+forma.getIndexTerapiasGrupalesMap()));			
			forma.setDetalleMap(ConsultaTerapiasGrupales.consultarDetalleTGrupal(con, parametros));
			
			forma.setDetalleMap("descripcionServicio",forma.getTerapiasGrupalesMap("descripcionservicio_"+forma.getIndexTerapiasGrupalesMap()));
			forma.setDetalleMap("observaciones",forma.getTerapiasGrupalesMap("observaciones_"+forma.getIndexTerapiasGrupalesMap()));
			forma.setDetalleMap("especialidadProfesional", forma.getTerapiasGrupalesMap("especialidadprofesional_"+forma.getIndexTerapiasGrupalesMap()));
			forma.setDetalleMap("descripcionusuario", forma.getTerapiasGrupalesMap("descripcionusuario_"+forma.getIndexTerapiasGrupalesMap()));
		}		
	}
}				