package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentesVacunasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.AntecedentesVacunas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


public class AntecedentesVacunasAction extends Action {

	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(AntecedentesVacunasAction.class);

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;

		try {
			if (form instanceof AntecedentesVacunasForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				AntecedentesVacunasForm forma = (AntecedentesVacunasForm) form;
				String estado=forma.getEstado();
				logger.warn("\n\n  El Estado en AntecedentesVacunasAction [" + estado + "] \n\n ");

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de Antecedentes de Vacunas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else
				{
					//Valida que el  paciente esté cargado
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
					//Validar que el usuario se un profesional de la salud
					if(!UtilidadValidacion.esProfesionalSalud(usuario))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud", "errors.usuario.noAutorizado", true);
					}

					if (estado.equals("empezar"))
					{
						accionEmpezar(forma, con, paciente.getCodigoPersona());
						return mapping.findForward("principal");
					}
					//----Se guardan la información de vacunas ----//
					else if (estado.equals("guardar"))
					{
						return this.accionGuardar(con, mapping, forma, paciente.getCodigoPersona(), usuario);
					}
					else if (estado.equals("resumen"))
					{
						accionEmpezar(forma, con, paciente.getCodigoPersona());
						return mapping.findForward("resumen");
					}
					else
					{
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
				}//else
			}//if
			return null;
		} catch (Exception e) {
			Log4JManager .error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	
	/**
	 * Metodo para iniciar la funcionalidad. 
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void accionEmpezar(AntecedentesVacunasForm forma, Connection con, int codigoPaciente) throws SQLException
	{
		HashMap mapaParam = new HashMap();  
		AntecedentesVacunas mundo = new AntecedentesVacunas();
		
		//----Limpiar la Información
		forma.reset();
		
		//-----Se establece el código del paciente
		mapaParam.put("codigoPaciente", codigoPaciente+"");
		
		//---- Establecer el numero de la consulta 1.
		mapaParam.put("nroConsulta","1"); 
		
		//----Consultar los tipos de inmunización de los antecedentes de vacunas
		forma.setMapaTiposInmunizacion(mundo.consultarInformacion(con, mapaParam));
		
		//---- Establecer el numero de la consulta 2.
		mapaParam.put("nroConsulta","2");
		//-----Se consultan las observaciones de los antecedentes de vacunas del paciente ------//
		HashMap mapaTemp=new HashMap();
		
		mapaTemp.putAll(mundo.consultarInformacion(con, mapaParam));
		
		//------Si el número de registros es igual a 1 se realiza la consulta de los datos ------------//
		if(UtilidadCadena.noEsVacio(mapaTemp.get("numRegistros")+""))
		{
			if(Integer.parseInt(mapaTemp.get("numRegistros")+"")==1)
			{
				//------Se asignan las observaciones generales al form --------//
				forma.setObservacionesGrales(mapaTemp.get("observaciones_0")+"");
				
				//---- Establecer el numero de la consulta 3.
				mapaParam.put("nroConsulta","3");
				
				//----Se consulta la posible información guardada al paciente de dosis, refuerzo y comentarios de las vacunas ----//
				forma.setMapaDatosVacunas(mundo.consultarInformacion(con, mapaParam));
			}
		}
		
		
		UtilidadBD.cerrarConexion(con);
	}
	
	/**
	 * Método que guarda o actualiza la información de las vacunas
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param codigoPaciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, AntecedentesVacunasForm forma, int codigoPaciente, UsuarioBasico usuario) throws SQLException
	{
		AntecedentesVacunas mundo = new AntecedentesVacunas();
		
		//-----------Se pasa el mapa que tiene los tipos de inmunizaciones del form al mundo ----------//
		mundo.setMapaTiposInmunizacion(forma.getMapaTiposInmunizacion());
		
		//-----------Se pasa el mapa que tiene los datos del form al mundo ----------//
		mundo.setMapaAntVacunas(forma.getMapaAntVacunas());
		
		//---------Se pasan las observaciones generales -----------//
		if (!forma.getObservacionesGralesNueva().trim().equals(""))
		{
			forma.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(forma.getObservacionesGrales(),forma.getObservacionesGralesNueva(),usuario, false));
			mundo.setObservacionesGrales(forma.getObservacionesGrales());
		}
		else
		{
			mundo.setObservacionesGrales(forma.getObservacionesGrales());
		}
		
		//-----------Se insertan los registros agregados al mapa --------------------//
		try
		{
			mundo.insertarModificarAntecedentesVacunas (con, codigoPaciente, usuario);
		} 
		catch (SQLException e)
		{
			logger.warn("Error en la inserción/modificación de Antecedentes Vacunas (AntecedentesVacunasAction) " +e.toString());
		}
		
		accionEmpezar(forma, con, codigoPaciente);
		return mapping.findForward("principal");
	}
	
}
