/*
* @(#)AntecedentePediatricoAction.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje   : Java
* Compilador : J2SDK 1.4.1_01
*
*/

package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.IdentificadoresExcepcionesSql;
import util.InfoDatos;
import util.InfoDatosBD;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentePediatricoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentePediatrico;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;
import com.princetonsa.mundo.antecedentes.InmunizacionesPediatricas;

/**
* <code>Action</code> para el ingreso y modificacin de datos de
* <code>AntecedentePediatrico</code>, toma los atributos de un objeto
* <code>AntecedentePediatricoForm</code> y los transforma en el formato
* apropiado para <code>AntecedentePediatrico</code>.
*
* @version 1.1, Jul 21, 2003
* @author <a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
 */
public class AntecedentePediatricoAction extends Action
{
	
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Consecutivos disponibles
	 */
	private Logger logger = Logger.getLogger(AntecedentePediatricoAction.class);
			
	/* no se para que se usa pero no se est� utilizando en ning�n lado
	private String agregarInfoControl(String cadena, UsuarioBasico medico)
	{
		if(cadena == null || cadena.trim().equals("") )
			return "";

		String fecha;
		String horaAct;
		String especialidades;

		fecha			= UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual() );
		horaAct			= UtilidadFecha.getHoraActual();
		especialidades	= "";

		if( (medico.getEspecialidades() != null) && (medico.getEspecialidades().length > 0) )
		{
			for(int i = 0, li_tam = medico.getEspecialidades().length; i < li_tam; i++)
				especialidades += medico.getEspecialidades()[i] + ", ";

			especialidades ="Especialidades: " + especialidades.substring(0, especialidades.length() - 2);
		}

		return fecha + ", " + horaAct + "\n" + cadena + "\n" + medico.getNombreUsuario() + " " + medico.getNumeroRegistroMedico() + ". " + especialidades + ". ";
	}*/

	private ActionForward cargar(ActionMapping mapping, ActionForm form, HttpServletRequest request, boolean esEpicrisis) throws Exception
	{
		HttpSession		session;
		PersonaBasica	paciente;

		AntecedentePediatricoForm forma = (AntecedentePediatricoForm)form;
		forma.clean();

		session		= request.getSession();
		paciente	= (PersonaBasica)session.getAttribute("pacienteActivo");
		UsuarioBasico us=(UsuarioBasico)session.getAttribute("usuarioBasico");
		
		// Paciente actual
		if(paciente == null)
		{
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		if (!UtilidadValidacion.esProfesionalSalud(us) && !esEpicrisis)
		{
			request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
			return mapping.findForward("paginaError");								
		}

		AntecedentePediatrico	antecedente;
		boolean					estaAntecedentePaciente;
		Connection				con;

		antecedente				= new AntecedentePediatrico();
		con						= null;
		estaAntecedentePaciente	= false;

		try
		{
			//--Estado para incrementar el MAPA. para el ingreso de los otros embarazos anteriores
			if ( forma.getAccion().equals("otroEmbarazoAnterior") )
			{
				if ( UtilidadCadena.noEsVacio(forma.getMapaOtros("nroNv")+"") )
				{
					forma.setMapaOtros("nroNv", "" + (Integer.parseInt(forma.getMapaOtros("nroNv")+"")+1) );
				}
				else
				{
					forma.setMapaOtros("nroNv", "1");
				}
				return mapping.findForward("ingresarAntecedentePediatrico");
			}
					
			
			// Abriendo conexi�n
			con = (DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ) ).getConnection();

			estaAntecedentePaciente = antecedente.cargar2(con, paciente.getCodigoPersona() );

			if(estaAntecedentePaciente)
			{
				antecedente.setInfoMadre(new InfoMadre() );
				antecedente.getInfoMadre().cargar(con, paciente.getCodigoPersona() );
                forma.setDescipcionOtrosMetodos(antecedente.getInfoMadre().getIi_otroMetodo());
				antecedente.setInfoPadre(new InfoPadre() );
				antecedente.getInfoPadre().cargar(con, paciente.getCodigoPersona() );

				
				//forma.setMapaPato( antecedente.consultarPatologias(con, paciente.getCodigoPersona()) );
				
				antecedente.setPaciente(paciente);
				request.setAttribute("antecedentePediatrico", antecedente);
			}

			//OJO: llame explicitamente al reset
			form.reset(mapping, request);

			if(estaAntecedentePaciente)
			{
				forma.setMapaPato( antecedente.consultarPatologias(con, paciente.getCodigoPersona()) );
			}

			// Cerrar la conexi�n
			if(con != null && !con.isClosed() )
                UtilidadBD.closeConnection(con);

			if(esEpicrisis)
				return mapping.findForward("resumenEpicrisis");
		
		
			if(estaAntecedentePaciente)
				return mapping.findForward("ingresarAntecedentePediatrico");
			else
			{
				forma.setAccion("ingresar");
				return mapping.findForward("ingresarAntecedentePediatrico");
			}
		}
		catch(SQLException ex)
		{
			// Cerrar la conexi�n
			if(con != null && !con.isClosed() )
                UtilidadBD.closeConnection(con);

			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			return mapping.findForward("paginaError");
		}
	}

	/**
	* Inicializa la informacion de la madre en el objeto de antecedentePediatrico con los datos que vienen del form
	* @param antecedente Objeto AntecedentePediatrico
	* @param antecedenteForm El form AntecedentePediatricoForm
	*/
	private void cargarObjetoInfoMadre(AntecedentePediatrico antecedente, AntecedentePediatricoForm antecedenteForm, String cargarPara)
	{
		InfoMadre		lim_madre;
		TipoNumeroId	ltni_idMadre;

		lim_madre = new InfoMadre();
		lim_madre.setCodigoMetodoSemanasGestacion(antecedenteForm.getCodigoMetodoSemanasGestacionMadre() );
		lim_madre.setIi_otroMetodo(antecedenteForm.getDescipcionOtrosMetodos());
		lim_madre.setConfiableSemanasGestacion(antecedenteForm.getConfiableSemanasGestacionMadre() );
		lim_madre.setCodigoHijo(antecedente.getPaciente().getCodigoPersona() );
		lim_madre.setInfoEmbarazoA(antecedenteForm.getInfoEmbarazoAMadre() );
		lim_madre.setInfoEmbarazoC(antecedenteForm.getInfoEmbarazoCMadre() );
		lim_madre.setInfoEmbarazoG(antecedenteForm.getInfoEmbarazoGMadre() );
		lim_madre.setInfoEmbarazoM(antecedenteForm.getInfoEmbarazoMMadre() );
		lim_madre.setInfoEmbarazoP(antecedenteForm.getInfoEmbarazoPMadre() );
		lim_madre.setInfoEmbarazoV(antecedenteForm.getInfoEmbarazoVMadre() );
		lim_madre.setPrimerApellido(antecedenteForm.getPrimerApellidoMadre() );
		lim_madre.setPrimerNombre(antecedenteForm.getPrimerNombreMadre() );
		lim_madre.setSegundoApellido(antecedenteForm.getSegundoApellidoMadre() );
		lim_madre.setSegundoNombre(antecedenteForm.getSegundoNombreMadre() );
		lim_madre.setTipoSangre(new InfoDatos(antecedenteForm.getCodigoTipoSangreMadre(), "") );
		lim_madre.setFpp(antecedenteForm.getFppMadre() );
		lim_madre.setFur(antecedenteForm.getFurMadre() );

		ltni_idMadre = new TipoNumeroId();

		if(!antecedenteForm.getTipoIdentificacionMadre().equals("") )
			ltni_idMadre.setTipoId(antecedenteForm.getTipoIdentificacionMadre().trim() );

		if(!antecedenteForm.getNumeroIdentificacionMadre().equals("") )
			ltni_idMadre.setNumeroId(antecedenteForm.getNumeroIdentificacionMadre().trim() );

		lim_madre.setIdentificacion(ltni_idMadre);

		if(!antecedenteForm.getEdadMadre().equals("") )
			lim_madre.setEdad(Integer.parseInt(antecedenteForm.getEdadMadre() ) );

		if(!antecedenteForm.getSemanasGestacion().equals("") )
			lim_madre.setSemanasGestacion(Float.parseFloat(antecedenteForm.getSemanasGestacion() ) );

		if(cargarPara.equals("ingresar") )
		{
			lim_madre.setObservaciones(UtilidadCadena.cargarObservaciones(antecedenteForm.getObservacionesEmbarazoMadre(),"",antecedente.getUsuarioBasico()));
			lim_madre.setObsPatologias(UtilidadCadena.cargarObservaciones(antecedenteForm.getObsPatologiasMadre(),"", antecedente.getUsuarioBasico() ) );
			lim_madre.setObsExamenesMedicamentos(UtilidadCadena.cargarObservaciones(antecedenteForm.getObsExamenesMedicamentosMadre(), "",antecedente.getUsuarioBasico() ) );

			//lim_madre.setObservaciones(agregarInfoControl(antecedenteForm.getObservacionesEmbarazoMadre(), antecedente.getUsuarioBasico() ) );
			//lim_madre.setObsPatologias(agregarInfoControl(antecedenteForm.getObsPatologiasMadre(), antecedente.getUsuarioBasico() ) );
			//lim_madre.setObsExamenesMedicamentos(agregarInfoControl(antecedenteForm.getObsExamenesMedicamentosMadre(), antecedente.getUsuarioBasico() ) );
		}
		//Las observaciones se tienen que acumular en caso de modificacion
		else if(cargarPara.equals("modificar") )
		{
			lim_madre.setObservaciones(UtilidadCadena.cargarObservaciones(antecedenteForm.getObservacionesEmbarazoMadre(),antecedenteForm.getObservacionesEmbarazoMadreAnteriores(),antecedente.getUsuarioBasico()));
			lim_madre.setObsPatologias(UtilidadCadena.cargarObservaciones(antecedenteForm.getObsPatologiasMadre(), antecedenteForm.getObsPatologiasMadreAnteriores(), antecedente.getUsuarioBasico() ) ) ;
			lim_madre.setObsExamenesMedicamentos(UtilidadCadena.cargarObservaciones(antecedenteForm.getObsExamenesMedicamentosMadre(), antecedenteForm.getObsExamenesMedicamentosMadreAnteriores(), antecedente.getUsuarioBasico() ) ) ;
			//lim_madre.setObservaciones(concatenarObservaciones(antecedenteForm.getObservacionesEmbarazoMadreAnteriores(), agregarInfoControl(antecedenteForm.getObservacionesEmbarazoMadre(), antecedente.getUsuarioBasico() ) ) );
			//lim_madre.setObsPatologias(concatenarObservaciones(antecedenteForm.getObsPatologiasMadreAnteriores(), agregarInfoControl(antecedenteForm.getObsPatologiasMadre(), antecedente.getUsuarioBasico() ) ) );
			//lim_madre.setObsExamenesMedicamentos(concatenarObservaciones(antecedenteForm.getObsExamenesMedicamentosMadreAnteriores(), agregarInfoControl(antecedenteForm.getObsExamenesMedicamentosMadre(), antecedente.getUsuarioBasico() ) ) );
		}

		antecedente.setInfoMadre(lim_madre);
	}

	/**
	* Inicializa la informacion del padre en el objeto de antecedentePediatrico con los datos que vienen del form
	* @param antecedente Objeto AntecedentePediatrico
	* @param antecedenteForm El form AntecedentePediatricoForm
	*/
	private void cargarObjetoInfoPadre(AntecedentePediatrico antecedente, AntecedentePediatricoForm antecedenteForm)
	{
		InfoPadre		lip_padre;
		TipoNumeroId	ltni_idPadre;

		lip_padre = new InfoPadre();
		lip_padre.setConsanguinidad(antecedenteForm.getConsanguinidadPadre() );
		lip_padre.setCodigoHijo(antecedente.getPaciente().getCodigoPersona() );
		lip_padre.setPrimerApellido(antecedenteForm.getPrimerApellidoPadre() );
		lip_padre.setPrimerNombre(antecedenteForm.getPrimerNombrePadre() );
		lip_padre.setSegundoApellido(antecedenteForm.getSegundoApellidoPadre() );
		lip_padre.setSegundoNombre(antecedenteForm.getSegundoNombrePadre() );
		lip_padre.setTipoSangre(new InfoDatos(antecedenteForm.getCodigoTipoSangrePadre(), "") );

		ltni_idPadre = new TipoNumeroId();

		if(!antecedenteForm.getTipoIdentificacionPadre().equals("") )
			ltni_idPadre.setTipoId(antecedenteForm.getTipoIdentificacionPadre().trim() );

		if(!antecedenteForm.getNumeroIdentificacionPadre().equals("") )
			ltni_idPadre.setNumeroId(antecedenteForm.getNumeroIdentificacionPadre().trim() );

		lip_padre.setIdentificacion(ltni_idPadre);

		if(!antecedenteForm.getEdadPadre().equals("") )
			lip_padre.setEdad(Integer.parseInt(antecedenteForm.getEdadPadre() ) );

		antecedente.setInfoPadre(lip_padre);
	}

	/* no se para que se usa pero no se est� utilizando en ning�n lado
	private String concatenarObservaciones(String obsAnterior, String obsNueva)
	{
		if(!obsAnterior.equals("") && !obsNueva.equals("") )
			return obsAnterior + "\n\n" + obsNueva;
		if(obsAnterior.equals("") && !obsNueva.equals("") )
			return obsNueva;
		if(!obsAnterior.equals("") && obsNueva.equals("") )
			return obsAnterior;

		return "";
	}*/

	/**
	* Ejecuta alguna de las acciones correspondientes a un
	* AntecedentePediatrico: insertar, modificar.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param form objeto con los datos provenientes del formulario
	* @param request el <i>servlet request</i> que est?siendo procesado en este momento
	* @param response el <i>servlet response</i> resultado de procesar este request
	* @return un <code>ActionForward</code> indicando la siguiente p�ina
	* dentro de la navegacin
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		

		String estado = request.getParameter("accion");
		if( (estado != null) && (estado.equals("cancelar") ) )
		{
			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
			return mapping.findForward("paginaError");
		}

		AntecedentePediatricoForm antecedenteForm = (AntecedentePediatricoForm)form;
		String accion;

		accion = ( (accion = antecedenteForm.getAccion() ) != null) ? accion.trim().toLowerCase() : "";
		
		logger.info("Estado en AntecedentePediatricoAction-->"+accion);

		if(accion.equals("ingresar") )
			return ingresar(mapping, form, request, response);
		else if(accion.equals("cargar") || accion.equalsIgnoreCase("cargarEpicrisis") )
			return cargar(mapping, form, request, accion.equalsIgnoreCase("cargarEpicrisis"));
		else if(accion.equals("modificar") )
		{
//			this.cargar(mapping, form, request);
			return modificar(mapping, form, request, response);
		}
		else if(accion.equals("pre-modificar") )
		{
			antecedenteForm.setAccion("modificar");
			return mapping.findForward("modificarAntecedentePediatrico");
		}
		else if(accion.equalsIgnoreCase("resumen") )
		{
			return mapping.findForward("resumen");
		}	
		else if(accion.equalsIgnoreCase("resumenIngreso") )
			return resumenIngreso(mapping, form, request);
		else if(accion.equalsIgnoreCase("consultarInmunizaciones") )
			return mapping.findForward("consultarInmunizaciones");
		else if(accion.equalsIgnoreCase("consultarTiposParto") )
			return mapping.findForward("consultarTiposParto");
		else if(accion.equalsIgnoreCase("volverAModificar") )
		{
			request.setAttribute("wasError", "true");
			antecedenteForm.setAccion("modificar");
			return mapping.findForward("modificarAntecedentePediatrico");
		}
		else if(accion.equalsIgnoreCase("error") )
		{
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		else
		{
			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
			return mapping.findForward("paginaError");
		}
	}

	/**
	* M�todo privado para insertar un nuevo antecedente ped�atrico.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param form objeto con los datos provenientes del formulario
	* @param request el <i>servlet request</i> que est� siendo procesado en
	* este momento
	* @return un <code>ActionForward</code> indicando la siguiente p�gina
	* dentro de la navegaci�n
	*/
	 @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private ActionForward ingresar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession		session;
		PersonaBasica	paciente;
		UsuarioBasico	usuarioMedico;

		// Obtenemos la session asociada a este request y el usuario registrado en la sesi�n
		session			= request.getSession();
		usuarioMedico	= (UsuarioBasico)session.getAttribute("usuarioBasico");

		// Usuario de session
		if(usuarioMedico == null || usuarioMedico.getLoginUsuario().equals("") )
		{
			request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return mapping.findForward("paginaError");
		}

		if(!util.UtilidadValidacion.esProfesionalSalud(usuarioMedico) )
		{
			request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
			return mapping.findForward("paginaError");
		}

		// Paciente actual
		if( (paciente = (PersonaBasica)session.getAttribute("pacienteActivo") ) == null)
		{
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}

		AntecedentePediatricoForm	antecedenteForm;
		ArrayList					tiposPartoMundo;
		Collection					c;
		Connection					con;
		InfoDatosBD					tipoParto;
		InmunizacionesPediatricas	inmunizacion;
		Iterator					iter;
		String						fecha;
		String						horaAct;
		String						key;
		String						key_codigo;
		String						ls_valorOpcionEmbarazo;
		String						motivo;
		String						tipoParto_cn;
		String[]					riesgoApgarArray_cn;
		String[]					tipoPartoArray_cn;

		// Hacemos el cast del form al objeto correspondiente, declaramos flags y variables.
		antecedenteForm			= (AntecedentePediatricoForm)form;

		//Obteniendo la fecha y hora actual
		fecha	= UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual() );
		horaAct	= UtilidadFecha.getHoraActual();
		logger.info("fecha:"+fecha+"hora:"+horaAct);
		// Creando el antecedente
		AntecedentePediatrico antecedente = new AntecedentePediatrico();
		antecedente.setUsuarioBasico(usuarioMedico);
		antecedente.setPaciente(paciente);

		/* Secci�n informaci�n de la madre durante el embarazo */
		cargarObjetoInfoMadre(antecedente, antecedenteForm, "ingresar");

		/* Secci�n informaci�n del padre */
		cargarObjetoInfoPadre(antecedente, antecedenteForm);

		if(!antecedenteForm.getDuracionPartoHoras().equals("") )
			antecedente.setDuracionPartoHoras(Integer.parseInt(antecedenteForm.getDuracionPartoHoras() ) );
		if(!antecedenteForm.getDuracionPartoMinutos().equals("") )
			antecedente.setDuracionPartoMinutos(Integer.parseInt(antecedenteForm.getDuracionPartoMinutos() ) );
		if(!antecedenteForm.getPerimetroCefalico().equals("") )
			antecedente.setPerimetroCefalico(Float.parseFloat(antecedenteForm.getPerimetroCefalico() ) );
		if(!antecedenteForm.getPerimetroToracico().equals("") )
			antecedente.setPerimetroToracico(Float.parseFloat(antecedenteForm.getPerimetroToracico() ) );
		if(!antecedenteForm.getPeso().equals("") )
			antecedente.setPeso(Integer.parseInt(antecedenteForm.getPeso() ) );
		if(!antecedenteForm.getRupturaMembranasHoras().equals("") )
			antecedente.setRupturaMembranasHoras(Integer.parseInt(antecedenteForm.getRupturaMembranasHoras() ) );
		if(!antecedenteForm.getRupturaMembranasMinutos().equals("") )
			antecedente.setRupturaMembranasMinutos(Integer.parseInt(antecedenteForm.getRupturaMembranasMinutos() ) );
		if(!antecedenteForm.getTalla().equals("") )
			antecedente.setTalla(Integer.parseInt(antecedenteForm.getTalla() ) );

		antecedente.setAnestesiaMedicamentos(antecedenteForm.getAnestesiaMedicamentos() );
		antecedente.setCaracteristicasLiquidoAmniotico(antecedenteForm.getCaracteristicasLiquidoAmniotico() );
		antecedente.setCaracteristicasPlacenta(antecedenteForm.getCaracteristicasPlacenta() );
		antecedente.setHoraNacimiento(antecedenteForm.getHoraNacimiento() );

		// tipoTrabajoParto
		if(!antecedenteForm.getTipoTrabajoParto_cn().equals("") )
		{
			String[] tipoTrabajoPartoArray_cn = antecedenteForm.getTipoTrabajoParto_cn().split("-");
			antecedente.setTipoTrabajoParto(new InfoDatos(tipoTrabajoPartoArray_cn[0], tipoTrabajoPartoArray_cn[1], antecedenteForm.getComentarioTipoTrabajoParto().trim() ) );
		}

		// presentacionNacimiento
		if(!antecedenteForm.getPresentacionNacimiento_cn().equals("") )
		{
			String[] presentacionNacimientoArray_cn = antecedenteForm.getPresentacionNacimiento_cn().split("-");

			// Caso es presentacion 'otra', tiene en cuenta el campo cual?
			if(presentacionNacimientoArray_cn[0].equals("0") )
				antecedente.setOtraPresentacionNacimiento(antecedenteForm.getOtraPresentacionNacimiento() );
			else
				antecedente.setPresentacionNacimiento(new InfoDatos(presentacionNacimientoArray_cn[0], presentacionNacimientoArray_cn[1]) );
		}

		// ubicacionFeto
		if(!antecedenteForm.getUbicacionFeto_cn().equals("") )
		{
			String[] ubicacionFetoArray_cn = antecedenteForm.getUbicacionFeto_cn().split("-");

			// Caso ubicacionFeto 'otra', tiene en cuenta el campo cual?
			if(ubicacionFetoArray_cn[0].equals("0") )
				antecedente.setOtraUbicacionFeto(antecedenteForm.getOtraUbicacionFeto() );
			else
				antecedente.setUbicacionFeto(new InfoDatos(ubicacionFetoArray_cn[0], ubicacionFetoArray_cn[1]) );
		}

		// sufrimientoFetal. Si hubo sufrimiento fetal tiene en cuenta las causas
		if(antecedenteForm.getSufrimientoFetal().equals("yes") )
		{
			antecedente.setCausasSufrimientoFetal(antecedenteForm.getCausasSufrimientoFetal() );
			antecedente.setSufrimientoFetal("true");
		}
		else if(antecedenteForm.getSufrimientoFetal().equals("no") )
			antecedente.setSufrimientoFetal("false");

		//Obteniendo los tipos de parto seleccionados
		inmunizacion	= null;
		c				= (Collection)antecedenteForm.getTiposParto().keySet();
		iter			= c.iterator();
		tiposPartoMundo = new ArrayList();

		while(iter.hasNext() )
		{
			tipoParto_cn = (String)antecedenteForm.getTiposParto().get(key = (String)iter.next() );

			if(!tipoParto_cn.equals("false") )
			{
				tipoPartoArray_cn	= tipoParto_cn.split("-");
				key_codigo			= tipoPartoArray_cn[0];
				motivo				= ( (String)antecedenteForm.getMotivosTiposParto().get(key_codigo) ).trim();
/*
				motivo = (request.getParameter("motivoTipoParto_" + key_codigo) == null )? "" : request.getParameter("motivoTipoParto_" + key_codigo).trim();
				request.removeAttribute("motivoTipoParto_" + key_codigo);
				motivo = (String)antecedenteForm.getMotivosTiposParto().get(key);
*/
				//Caso tipo parto 'Otro', obtenemos el 'cual?'
				if(tipoPartoArray_cn[0].equals("0") )
				{
					antecedente.setOtroTipoParto(antecedenteForm.getOtroTipoParto() );
					antecedente.setMotivoTipoPartoOtro(motivo);
				}
				else
				{
					tipoParto = new InfoDatosBD(tipoPartoArray_cn[0], tipoPartoArray_cn[1], motivo);
					tiposPartoMundo.add(tipoParto);
				}
			}
		}

		if(tiposPartoMundo.size() > 0)
			antecedente.setTiposParto(tiposPartoMundo);

		/*
		// Obteniendo dosis y observaciones de vacunas seleccionadas. En cuanto a las dosis y sus observaciones si las tienen
		iter						= (c = (Collection)antecedenteForm.getInmunizacionesPediatricas().keySet() ).iterator();
		lasInmunizaciones			= new ArrayList();
		observacionesInmunizacion	= "";
		tiposInmunizacionStr		= "";
		vacunaAnt					= -1;

		while(iter.hasNext() )
		{
			inmunizacion_cn = (String)antecedenteForm.getInmunizacionesPediatricas().get(key = (String)iter.next() );

			if(!inmunizacion_cn.equals("false") )
			{
				inmunizacionArray_cn	= inmunizacion_cn.split("_");
				codigoInmunizacion		= Integer.parseInt(inmunizacionArray_cn[0]);
				keyArray				= key.split("_");

				// Obteniendo la fecha del map
				fechaDosis = ( (String)antecedenteForm.getFechasDosisInmunizacionesPediatricas().get("fecha_" + keyArray[1] + "_" + keyArray[2]) ).trim();
//				(request.getParameter("fecha_"+keyArray[1]+"_"+keyArray[2]) == null )? "" : request.getParameter("fecha_"+keyArray[1]+"_"+keyArray[2]);

				// Obteniendo observaciones de vacunas con alguna dosis seleccionada, pero solo para la primera dosis que encuentra de esa vacuna
				if(vacunaAnt != codigoInmunizacion)
				{
					// solo para vacunas que van a tener al menos una dosis?
					lasDosis = new ArrayList();
					observacionesInmunizacion = ( (String)antecedenteForm.getObservacionesInmunizacionesPediatricas().get(codigoInmunizacion+"") ).trim();
/*
					observacionesInmunizacion = (request.getParameter("observacionesInmunizacionesPediatricas_" + keyArray[1]) == null )? "" : request.getParameter("observacionesInmunizacionesPediatricas_" + keyArray[1]).trim();
					request.removeAttribute("observacionesInmunizacionesPediatricas_" + keyArray[1]);

					// Agregando los datos de control para guardar en bd
					if(!observacionesInmunizacion.equals("") )
						observacionesInmunizacion = UtilidadCadena.cargarObservaciones(observacionesInmunizacion, "", usuarioMedico);

					inmunizacion = new InmunizacionesPediatricas(codigoInmunizacion, inmunizacionArray_cn[1], observacionesInmunizacion, lasDosis);
					lasInmunizaciones.add(inmunizacion);
					tiposInmunizacionStr += "," + codigoInmunizacion + ",";
				}

				// Para distinguir si se trata de un refuerzo y no un numero de dosis normal
				laDosis = new Dosis(Integer.parseInt(keyArray[2]), fechaDosis, Integer.parseInt(keyArray[2]) < 0);

				// Agregando al arreglo de dosis
				inmunizacion.getDosis().add(laDosis);
				vacunaAnt = codigoInmunizacion;
			}
		}

		// Falta incluir las observaciones de inmunizaciones que no tengan dosis seleccionada
//		OJO: arreglar
//		OJO:numero maximo de vacunas 26?
//		int numVacunas = Integer.parseInt(antecedenteForm.getMaxVacunas() );
		iter = (c = (Collection)antecedenteForm.getObservacionesInmunizacionesPediatricas().keySet() ).iterator();

		while(iter.hasNext() )
		{
			codigoInmunizacion = Integer.parseInt(key = (String)iter.next() );

			// Si no se inicializo antes esta inmunizacion, se debe crear y a�adir a la lista
			if(tiposInmunizacionStr.indexOf("," + new Integer(codigoInmunizacion).toString() + ",") == -1)
			{
				observacionesInmunizacion = ( (String)antecedenteForm.getObservacionesInmunizacionesPediatricas().get(codigoInmunizacion + "") ).trim();

				if(!observacionesInmunizacion.equals("") )
				{
					// Agregando los datos de control para guardar en bd
					observacionesInmunizacion = UtilidadCadena.cargarObservaciones(observacionesInmunizacion,"",usuarioMedico);

					inmunizacion = new InmunizacionesPediatricas(codigoInmunizacion, "", observacionesInmunizacion, null);
					lasInmunizaciones.add(inmunizacion);
				}
			}
		}

		if(lasInmunizaciones.size() > 0)
			antecedente.setInmunizacionesPediatricas(lasInmunizaciones);
			
			*/

		//APGAR. Si se escribio el APGAR se debe obtener el riesgo asociado
		if(!antecedenteForm.getApgarMinuto1().equals("") )
		{
			riesgoApgarArray_cn = antecedenteForm.getRiesgoApgarMinuto1_cn().split("-");
			antecedente.setApgarMinuto1(Integer.parseInt(antecedenteForm.getApgarMinuto1() ) );
			antecedente.setRiesgoApgarMinuto1(new InfoDatos(riesgoApgarArray_cn[0], riesgoApgarArray_cn[1]) );
		}

		if(!antecedenteForm.getApgarMinuto5().equals("") )
		{
			riesgoApgarArray_cn = antecedenteForm.getRiesgoApgarMinuto5_cn().split("-");
			antecedente.setApgarMinuto5(Integer.parseInt(antecedenteForm.getApgarMinuto5() ) );
			antecedente.setRiesgoApgarMinuto5(new InfoDatos(riesgoApgarArray_cn[0], riesgoApgarArray_cn[1]) );
		}

		if(!antecedenteForm.getApgarMinuto10().equals("") )
		{
			riesgoApgarArray_cn = antecedenteForm.getRiesgoApgarMinuto10_cn().split("-");
			antecedente.setApgarMinuto10(Integer.parseInt(antecedenteForm.getApgarMinuto10() ) );
			antecedente.setRiesgoApgarMinuto10(new InfoDatos(riesgoApgarArray_cn[0], riesgoApgarArray_cn[1]) );
		}

		/* Secci�n de embarazos anteriores */
		antecedente.setEmbarazosAnterioresOtros(antecedenteForm.getEmbarazosAnterioresOtros() );
		antecedente.setIncompatibilidadABO(antecedenteForm.getIncompatibilidadABO() );
		antecedente.setIncompatibilidadRH(antecedenteForm.getIncompatibilidadRH() );
		antecedente.setMacrosomicos(antecedenteForm.getMacrosomicos() );
		antecedente.setMalformacionesCongenitas(antecedenteForm.getMalformacionesCongenitas() );
		antecedente.setMortinatos(antecedenteForm.getMortinatos() );
		antecedente.setMuertesFetalesTempranas(antecedenteForm.getMuertesFetalesTempranas() );
		antecedente.setPrematuros(antecedenteForm.getPrematuros() );

		/* Secci�n de embarazo actual */
		antecedente.setCodigoSerologia(antecedenteForm.getCodigoSerologia() );
		antecedente.setCodigoTestSullivan(antecedenteForm.getCodigoTestSullivan() );
		antecedente.setControlPrenatal(antecedenteForm.getControlPrenatal() );
		antecedente.setDescripcionSerologia(antecedenteForm.getDescripcionSerologia() );
		antecedente.setDescripcionTestSullivan(antecedenteForm.getDescripcionTestSullivan() );
		antecedente.setFrecuenciaControlPrenatal(antecedenteForm.getFrecuenciaControlPrenatal() );
		antecedente.setLugarControlPrenatal(antecedenteForm.getLugarControlPrenatal() );

		/* Secci�n de trabajo de parto */
		antecedente.setAmnionitis(antecedenteForm.getAmnionitis() );
		antecedente.setAmnionitisFactorAnormal(antecedenteForm.getAmnionitisFactorAnormal() );
		antecedente.setAnestesia(antecedenteForm.getAnestesia() );
		antecedente.setAnestesiaTipo(antecedenteForm.getAnestesiaTipo() );

		if(!antecedenteForm.getDuracionExpulsivoHoras().equals("") )
			antecedente.setDuracionExpulsivoHoras(Integer.parseInt(antecedenteForm.getDuracionExpulsivoHoras() ) );
		if(!antecedenteForm.getDuracionExpulsivoMinutos().equals("") )
			antecedente.setDuracionExpulsivoMinutos(Integer.parseInt(antecedenteForm.getDuracionExpulsivoMinutos() ) );

		antecedente.setNst(antecedenteForm.getNst() );
		antecedente.setNstDescripcion(antecedenteForm.getNstDescripcion() );
		antecedente.setOtrosExamenesTrabajoParto(antecedenteForm.getOtrosExamenesTrabajoParto() );
		antecedente.setPerfilBiofisico(antecedenteForm.getPerfilBiofisico() );
		antecedente.setPerfilBiofisicoDescripcion(antecedenteForm.getPerfilBiofisicoDescripcion() );
		antecedente.setPtc(antecedenteForm.getPtc() );
		antecedente.setPtcDescripcion(antecedenteForm.getPtcDescripcion() );

		/* Informaci�n de la secci�n atenci�n del parto */
		if(!antecedenteForm.getEdadGestacional().equals("") )
			antecedente.setEdadGestacional(Integer.parseInt(antecedenteForm.getEdadGestacional() ) );

		if(!antecedenteForm.getIntrauterinoAnormalidad().equals("") )
			antecedente.setIntrauterinoAnormalidad(Integer.parseInt(antecedenteForm.getIntrauterinoAnormalidad() ) );

		antecedente.setComplicacionesParto(antecedenteForm.getComplicacionesParto() );
		antecedente.setCordonUmbilicalCaracteristicas(antecedenteForm.getCordonUmbilicalCaracteristicas() );
		antecedente.setCordonUmbilicalDescripcion(antecedenteForm.getCordonUmbilicalDescripcion() );
		antecedente.setEdadGestacionalDescripcion(antecedenteForm.getEdadGestacionalDescripcion() );
		antecedente.setGemelo(antecedenteForm.getGemelo() );
		antecedente.setGemeloDescripcion(antecedenteForm.getGemeloDescripcion() );
		antecedente.setIntrauterinoPeg(antecedenteForm.getIntrauterinoPeg() );
		antecedente.setIntrauterinoPegCausa(antecedenteForm.getIntrauterinoPegCausa() );
		antecedente.setIntrauterinoAnormalidadCausa(antecedenteForm.getIntrauterinoAnormalidadCausa() );
		antecedente.setIntrauterinoArmonico(antecedenteForm.getIntrauterinoArmonico() );
		antecedente.setIntrauterinoArmonicoCausa(antecedenteForm.getIntrauterinoArmonicoCausa() );
		antecedente.setLiqAmnioticoClaro(antecedenteForm.getLiqAmnioticoClaro() );
		antecedente.setLiqAmnioticoMeconiado(antecedenteForm.getLiqAmnioticoMeconiado() );
		antecedente.setLiqAmnioticoMeconiadoGrado(antecedenteForm.getLiqAmnioticoMeconiadoGrado() );
		antecedente.setLiqAmnioticoSanguinolento(antecedenteForm.getLiqAmnioticoSanguinolento() );
		antecedente.setLiqAmnioticoFetido(antecedenteForm.getLiqAmnioticoFetido() );
		antecedente.setMuestraCordonUmbilical(antecedenteForm.getMuestraCordonUmbilical() );
		antecedente.setMuestraCordonUmbilicalDescripcion(antecedenteForm.getMuestraCordonUmbilicalDescripcion() );
		antecedente.setPlacentaCaracteristicas(antecedenteForm.getPlacentaCaracteristicas() );
		antecedente.setPlacentaCaracteristicasDescripcion(antecedenteForm.getPlacentaCaracteristicasDescripcion() );
		antecedente.setReanimacion(antecedenteForm.getReanimacion() );
		antecedente.setReanimacionAspiracion(antecedenteForm.getReanimacionAspiracion() );
		antecedente.setReanimacionMedicamentos(antecedenteForm.getReanimacionMedicamentos() );
		antecedente.setSano(antecedenteForm.getSano() );
		antecedente.setSanoDescripcion(antecedenteForm.getSanoDescripcion() );
		antecedente.setSexoDescripcion(antecedenteForm.getSexoDescripcion() );

		// Observaciones:
		// Formato para guardar las observaciones:
		// <fecha>|<hora>|<nombreMedico>:<especialidades separadas por coma>|<numRegistro>|<observacion>_&_<fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>
		if(!antecedenteForm.getObservacionesGenerales().trim().equals("") ){
			antecedente.setObservaciones(UtilidadCadena.cargarObservaciones( antecedenteForm.getObservacionesGenerales(),"",usuarioMedico));
			//antecedente.setObservaciones(fecha + "|" + horaAct + "|" + nombreMedico + especialidadesMedico + "|" + registroMedico + "|" + antecedenteForm.getObservacionesGenerales() );
		}


		/* Informaci�n de opciones de embarazo */
		iter = (c = (Collection)antecedenteForm.getEmbarazoOpcionCampos().keySet() ).iterator();

		while(iter.hasNext() )
		{
			ls_valorOpcionEmbarazo = (String)antecedenteForm.getEmbarazoOpcionCampo(key = (String)iter.next() );

			if(ls_valorOpcionEmbarazo != null && !ls_valorOpcionEmbarazo.equals("") )
				antecedente.setEmbarazoOpcionCampo(new InfoDatos(key, ls_valorOpcionEmbarazo) );
		}

		con = null;

		try
		{
			//Abriendo conexi�n
			con = (DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ) ).getConnection();

			// Insertando el antecedente ped�atrico
			antecedente.insertar(con);

			// Cerrar la conexi�n
			if(con != null && !con.isClosed() )
                UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			// Cerrar la conexi�n
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
			logger.info("No se pudo hacer la inserci�n/modificaci�n de los antecedentes, se present� Un Error :"+e.toString()+" ID --> "+e.getSQLState());
			if(e.getSQLState().equals(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente))
			{
				request.setAttribute("descripcionError","No se pudo hacer la inserci�n de los antecedentes, se present� Un Error : Otro Usuario esta insertando el Antecedente.");
			}
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			}
			return mapping.findForward("paginaError");
		}

		// Si se debe salir del flujo normal de grabaci�n del antecedente para irse a otra p�gina
		if(request.getParameter("estado").equals("salir") )
		{
			String paginaSiguiente;

			if( (paginaSiguiente = request.getParameter("paginaSiguiente") ) != null )
				response.sendRedirect(paginaSiguiente);

			return null;
		}
		// Si sigue el flujo normal debe mostrar los datos grabados
		else
		{
			this.cargar(mapping, form, request, false);
			return mapping.findForward("ingresarAntecedentePediatrico");
		}
	}

	/**
	* M�todo privado para modificar un antecedente pad�atrico ya existente.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param form objeto con los datos provenientes del formulario
	* @param request el <i>servlet request</i> que est?siendo procesado en este momento
	* @return un <code>ActionForward</code> indicando la siguiente p?ina dentro de la navegacin
	*/
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private ActionForward modificar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession		session;
		UsuarioBasico	usuarioMedico;

		// Obtenemos la session asociada a este request y el usuario registrado en la sesi�n
		session			= request.getSession();
		usuarioMedico	= (UsuarioBasico)session.getAttribute("usuarioBasico");

		// Usuario de session
		if(usuarioMedico == null || usuarioMedico.getLoginUsuario().equals("") )
		{
			request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return mapping.findForward("paginaError");
		}

		AntecedentePediatrico antecedente;

		// Buscando el antecedente
		//OJO debe estar en sesion o en request?
		if( (antecedente = (AntecedentePediatrico)session.getAttribute("antecedentePediatrico") ) != null)
		{
			AntecedentePediatricoForm antecedenteForm;

			// Hacemos el cast del form al objeto correspondiente, declaramos flags y variables.
			antecedenteForm = (AntecedentePediatricoForm)form;

			//Ya viene con paciente y todos sus datos llenos de la bd. Falta cambiar el usuario de sesi�n
			antecedente.setUsuarioBasico(usuarioMedico);

			if(!util.UtilidadValidacion.esProfesionalSalud(usuarioMedico) )
			{
				request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
				return mapping.findForward("paginaError");
			}

			Collection					c;
			Connection					con;
			InfoDatosBD					tipoParto;
			Iterator					iter;
			String						key;
			String						ls_valorOpcionEmbarazo;
			String						motivo;
			String						tipoParto_cn;
			String[]					riesgoApgarArray_cn;
			String[]					tipoPartoArray_cn;

			/* Secci�n informaci�n de la madre durante el embarazo */
			cargarObjetoInfoMadre(antecedente, antecedenteForm, "modificar");

			/* Secci�n informaci�n del padre */
			cargarObjetoInfoPadre(antecedente, antecedenteForm);

			// Por view se impide modificar informaci�n que ya se haya ingresado y este en la bd, por tanto todos los datos se vuelven a capturar y modificar
			if(!antecedenteForm.getDuracionPartoHoras().equals("") )
				antecedente.setDuracionPartoHoras(Integer.parseInt(antecedenteForm.getDuracionPartoHoras() ) );
			if(!antecedenteForm.getDuracionPartoMinutos().equals("") )
				antecedente.setDuracionPartoMinutos(Integer.parseInt(antecedenteForm.getDuracionPartoMinutos() ) );
			if(!antecedenteForm.getPerimetroCefalico().equals("") )
				antecedente.setPerimetroCefalico(Float.parseFloat(antecedenteForm.getPerimetroCefalico() ) );
			if(!antecedenteForm.getPerimetroToracico().equals("") )
				antecedente.setPerimetroToracico(Float.parseFloat(antecedenteForm.getPerimetroToracico() ) );
			if(!antecedenteForm.getPeso().equals("") )
				antecedente.setPeso(Integer.parseInt(antecedenteForm.getPeso() ) );
			if(!antecedenteForm.getRupturaMembranasHoras().equals("") )
				antecedente.setRupturaMembranasHoras(Integer.parseInt(antecedenteForm.getRupturaMembranasHoras() ) );
			if(!antecedenteForm.getRupturaMembranasMinutos().equals("") )
				antecedente.setRupturaMembranasMinutos(Integer.parseInt(antecedenteForm.getRupturaMembranasMinutos() ) );
			if(!antecedenteForm.getTalla().equals("") )
				antecedente.setTalla(Integer.parseInt(antecedenteForm.getTalla() ) );

			antecedente.setAnestesiaMedicamentos(antecedenteForm.getAnestesiaMedicamentos() );
			antecedente.setCaracteristicasLiquidoAmniotico(antecedenteForm.getCaracteristicasLiquidoAmniotico() );
			antecedente.setCaracteristicasPlacenta(antecedenteForm.getCaracteristicasPlacenta() );
			antecedente.setHoraNacimiento(antecedenteForm.getHoraNacimiento() );

			// tipoTrabajoParto
			if(!antecedenteForm.getTipoTrabajoParto_cn().equals("") )
			{
				String[] tipoTrabajoPartoArray_cn = antecedenteForm.getTipoTrabajoParto_cn().split("-");

				if( (antecedente.getTipoTrabajoParto() != null) && (antecedente.getTipoTrabajoParto().getAcronimo().equals(tipoTrabajoPartoArray_cn[0]) ) )
				{
					// Si el antecedente tiene ese tipo de parto, pero no tiene comentario, se le inserta
					if(!antecedenteForm.getComentarioTipoTrabajoParto().trim().equals("") )
					{
						if(antecedente.getTipoTrabajoParto().getDescripcion() == null || (antecedente.getTipoTrabajoParto().getDescripcion() != null && antecedente.getTipoTrabajoParto().getDescripcion().equals("") ) )
							antecedente.getTipoTrabajoParto().setDescripcion(antecedenteForm.getComentarioTipoTrabajoParto().trim() );
					}
				}
				else if(antecedente.getTipoTrabajoParto() == null)
					antecedente.setTipoTrabajoParto(new InfoDatos(tipoTrabajoPartoArray_cn[0], tipoTrabajoPartoArray_cn[1], antecedenteForm.getComentarioTipoTrabajoParto().trim() ) );
			}

			// presentacionNacimiento
			if(!antecedenteForm.getPresentacionNacimiento_cn().equals("") )
			{
				String[] presentacionNacimientoArray_cn = antecedenteForm.getPresentacionNacimiento_cn().split("-");

				// Solo si no se habia inicializado antes
				if(antecedente.getPresentacionNacimiento() == null)
				{
					// Caso es presentacion 'otra', tiene en cuenta el campo cual?
					if(presentacionNacimientoArray_cn[0].equals("0") )
						antecedente.setOtraPresentacionNacimiento(antecedenteForm.getOtraPresentacionNacimiento() );
					else
						antecedente.setPresentacionNacimiento(new InfoDatos(presentacionNacimientoArray_cn[0], presentacionNacimientoArray_cn[1]) );
				}
			}

			// ubicacionFeto
			if(!antecedenteForm.getUbicacionFeto_cn().equals("") )
			{
				String[] ubicacionFetoArray_cn = antecedenteForm.getUbicacionFeto_cn().split("-");

				// Solo si no se habia inicializado antes
				if(antecedente.getUbicacionFeto() == null )
				{
					// Caso es ubicacion 'otra', tiene en cuenta el campo cual?
					if(ubicacionFetoArray_cn[0].equals("0") )
						antecedente.setOtraUbicacionFeto(antecedenteForm.getOtraUbicacionFeto() );
					else
						antecedente.setUbicacionFeto(new InfoDatos(ubicacionFetoArray_cn[0], ubicacionFetoArray_cn[1]) );
				}
			}

			// sufrimientoFetal. Si hubo sufrimiento fetal tiene en cuenta las causas. Estas no son modificables una vez ingresadas
			if(antecedenteForm.getSufrimientoFetal().equals("yes") )
			{
				if( (antecedente.getSufrimientoFetal().equalsIgnoreCase("true") && antecedente.getCausasSufrimientoFetal().equals("") ) || antecedente.getSufrimientoFetal().equals("") )
				{
					antecedente.setCausasSufrimientoFetal(antecedenteForm.getCausasSufrimientoFetal() );
					antecedente.setSufrimientoFetal("true");
				}
			}
			else if(antecedenteForm.getSufrimientoFetal().equals("no") && antecedente.getSufrimientoFetal().equals("") )
				antecedente.setSufrimientoFetal("false");

			// Obteniendo los tipos de parto que vienen del antecedente cargado
			// Estos no se pueden deschequear y sus comentarios no se pueden modificar si ya se han ingresado
			// Por cada tipo de parto ya ingresado, obteniendo comentario en caso de no tenerlo antes
			// OJO si no tiene partos
			if(antecedente.getTiposParto() != null)
				for(int i = 0, li_tam = antecedente.getTiposParto().size(); i < li_tam; i++)
					if( (tipoParto = (InfoDatosBD)antecedente.getTiposParto().get(i) ).getDescripcion().equals("") )
					{
						motivo = ( (String)antecedenteForm.getMotivosTiposParto().get(tipoParto.getCodigo()+"") ).trim();
						tipoParto.setDescripcion(motivo);
/*
						motivo = (request.getParameter("motivoTipoParto_" + tipoParto.getCodigo() ) == null) ? "": request.getParameter("motivoTipoParto_" + tipoParto.getCodigo() ).trim();
						request.removeAttribute("motivoTipoParto_" + tipoParto.getCodigo() );
						motivo = (request.getParameter("motivoTiposParto(" + key + ")") == null) ? "": request.getParameter("motivoTiposParto(" + key + ")");
						motivo = (String)antecedenteForm.getMotivosTiposParto().get(key);
*/
					}

			// Para el tipo de parto otro, si ya lo tiene el antecedente lo unico que puede suceder es que se quiera poner el motivo o causa en caso de no tenerla antes.
			if(!antecedente.getOtroTipoParto().equals("") && antecedente.getMotivoTipoPartoOtro().equals("") )
			{
				//Tipo parto otro tiene codigo 0
				motivo = ( (String)antecedenteForm.getMotivosTiposParto().get("0") ).trim();
				antecedente.setMotivoTipoPartoOtro(motivo);
/*
				motivo = (String)antecedenteForm.getMotivosTiposParto().get(key);
				motivo = (request.getParameter("motivoTipoParto_0") == null )? "" : request.getParameter("motivoTipoParto_0").trim();
				request.removeAttribute("motivoTipoParto_0");
*/
			}

			// Obteniendo los tipos de parto seleccionados
			// Por cada tipo de parto seleccionado, ver cual no estaba antes e ingresarlo
			c		= (Collection)antecedenteForm.getTiposParto().keySet();
			iter	= c.iterator();

			while(iter.hasNext() )
			{
				tipoParto_cn = (String)antecedenteForm.getTiposParto().get(key = (String)iter.next() );

				if(!tipoParto_cn.equals("false") )
				{
					tipoPartoArray_cn = tipoParto_cn.split("-");

					if(!antecedente.estaTipoParto(tipoPartoArray_cn[0]) )
					{
						motivo = ( (String)antecedenteForm.getMotivosTiposParto().get(tipoPartoArray_cn[0]) ).trim();
/*
						motivo = (request.getParameter("motivoTipoParto_"+tipoPartoArray_cn[0]) == null )? "" : request.getParameter("motivoTipoParto_"+tipoPartoArray_cn[0]).trim();
						request.removeAttribute("motivoTipoParto_"+tipoPartoArray_cn[0]);
*/
						// Caso tipo parto 'Otro', obtenemos el 'cual?'
						if(tipoPartoArray_cn[0].equals("0") )
						{
							antecedente.setOtroTipoParto(antecedenteForm.getOtroTipoParto() );
							antecedente.setMotivoTipoPartoOtro(motivo);
						}
						else
						{
							tipoParto = new InfoDatosBD(tipoPartoArray_cn[0], tipoPartoArray_cn[1], motivo);
							if(antecedente.getTiposParto() == null)
								antecedente.setTiposParto(new ArrayList() );

							antecedente.getTiposParto().add(tipoParto);
						}
					}
				}
			}

			/*		
			// Obteniendo dosis y observaciones de vacunas seleccionadas
			// En cuanto a las dosis y sus observaciones si las tienen
			inmunizacion = null;
			c = (Collection)antecedenteForm.getInmunizacionesPediatricas().keySet();
			List l = new ArrayList(c); 
			Collections.sort(l);
			
			iter = l.iterator();
			observacionesInmunizacion = "";
			tiposInmunizacionStr = "";
			vacunaAnt					= -1;

			//OJO ARREGLAR EL FORMATO DE OBSERVACIONES DE INMUNIZACION
			while(iter.hasNext() )
			{
				inmunizacion_cn = (String)antecedenteForm.getInmunizacionesPediatricas().get(key = (String)iter.next() );

				if(!inmunizacion_cn.equals("false") )
				{
					inmunizacionArray_cn	= inmunizacion_cn.split("_");
					codigoInmunizacion		= Integer.parseInt(inmunizacionArray_cn[0]);
					
					logger.info("codigo inmunizacion "+codigoInmunizacion);
					
					keyArray				= key.split("_");

					// Si no se tenia en bd la inmunizacion seleccionada
					if( (inmunizacionAntecedente = antecedente.darInmunizacionPediatrica(codigoInmunizacion) ) == null)
					{
						// Obteniendo observaciones de vacunas con alguna dosis seleccionada, pero solo para la primera dosis que encuentra de esa vacuna
						if(vacunaAnt != codigoInmunizacion)
						{
							// Solo para vacunas que van a tener al menos una dosis?
							lasDosis					= new ArrayList();
//							observacionesInmunizacion	= (request.getParameter("observacionesInmunizacionesPediatricas_ " + keyArray[1]) == null) ? "": request.getParameter("observacionesInmunizacionesPediatricas_" + keyArray[1]).trim();
							observacionesInmunizacion	= ( (String)antecedenteForm.getObservacionesInmunizacionesPediatricas().get(codigoInmunizacion + "") ).trim();

							if(!observacionesInmunizacion.equals("") )
								observacionesInmunizacion = UtilidadCadena.cargarObservaciones(observacionesInmunizacion, "", usuarioMedico);								

							inmunizacion = new InmunizacionesPediatricas(codigoInmunizacion, inmunizacionArray_cn[1], observacionesInmunizacion, lasDosis);

							if(antecedente.getInmunizacionesPediatricas() == null)
								antecedente.setInmunizacionesPediatricas(new ArrayList() );

							antecedente.getInmunizacionesPediatricas().add(inmunizacion);
							tiposInmunizacionStr += "," + codigoInmunizacion + ",";
//							request.removeAttribute("observacionesInmunizacionesPediatricas_" + keyArray[1]);
						}

						// Obteniendo la fecha del map
						fechaDosis = ( (String)antecedenteForm.getFechasDosisInmunizacionesPediatricas().get("fecha_" + keyArray[1] + "_" + keyArray[2]) ).trim();

						// Para distinguir si se trata de un refuerzo y no un numero de dosis normal
						laDosis = new Dosis(Integer.parseInt(keyArray[2]), fechaDosis, Integer.parseInt(keyArray[2]) < 0);

						// Agregando al arreglo de dosis
						inmunizacion.getDosis().add(laDosis);
					}
					//Si ya se ten�a la inmunizaci�n, se pueden agregar dosis, observaciones y si ya se tenia la dosis pero no su fecha, se puede poner la fecha
					else
					{
						// Mirando si cambiaron las dosis
						numDosis			= Integer.parseInt(keyArray[2]);
						dosisAntecedente	= antecedente.darDosisInmunizacionPediatrica(inmunizacionAntecedente, numDosis);
						fechaDosis			= ( (String)antecedenteForm.getFechasDosisInmunizacionesPediatricas().get("fecha_" + keyArray[1] + "_" + keyArray[2]) ).trim();

						//Si ya se tenia la dosis en el antecedente, agrego la fecha en caso de no tenerla antes
						if(dosisAntecedente != null)
						{
							if(dosisAntecedente.getFecha().equals("") )
								dosisAntecedente.setFecha(fechaDosis);
						}
						//Si la dosis es nueva
						else
						{
							laDosis = new Dosis(numDosis, fechaDosis, numDosis < 0);

							// Agregando la dosis a la inmunizaci�n que viene del antecedente bd
							if(inmunizacionAntecedente.getDosis() == null)
								inmunizacionAntecedente.setDosis(new ArrayList() );

							inmunizacionAntecedente.getDosis().add(laDosis);
						}

						// Se agrega la nueva observacion
						if(vacunaAnt != codigoInmunizacion)
						{
//							observacionesInmunizacion	= (request.getParameter("observacionesInmunizacionesPediatricas_" + keyArray[1]) == null) ? "": request.getParameter("observacionesInmunizacionesPediatricas_" + keyArray[1]).trim();
							observacionesInmunizacion	= ( (String)antecedenteForm.getObservacionesInmunizacionesPediatricas().get(codigoInmunizacion + "") ).trim();
							tiposInmunizacionStr		+= "," + codigoInmunizacion + ",";

							if(!observacionesInmunizacion.equals("") )
							{
								// Se debe ingresar con los datos de control y segun el formato
								observacionesInmunizacion = UtilidadCadena.cargarObservaciones(observacionesInmunizacion, "", usuarioMedico);

								if(inmunizacionAntecedente.getObservaciones().equals("") )
									inmunizacionAntecedente.setObservaciones(observacionesInmunizacion);
								// Si ya se tenia se debe concatenar con la anterior
								else
									inmunizacionAntecedente.setObservaciones(inmunizacionAntecedente.getObservaciones() + observacionesInmunizacion);

//								request.removeAttribute("observacionesInmunizacionesPediatricas_"+keyArray[1]);
							}
						}
					}

					vacunaAnt = codigoInmunizacion;
				}
			}

			// Falta incluir las observaciones de inmunizaciones que no tengan dosis seleccionada
			// OJO: arreglar
			// OJO:numero maximo de vacunas 26?
			// int numVacunas = new Integer(antecedenteForm.getMaxVacunas() ).intValue();

			iter = (c = (Collection)antecedenteForm.getObservacionesInmunizacionesPediatricas().keySet() ).iterator();

			while(iter.hasNext() )
			{
				codigoInmunizacion = Integer.parseInt(key = (String)iter.next() );

				//Si no se inicializo antes esta inmunizacion porque no tenia dosis seleccionada, se debe crear y aadir a la lista
				if(tiposInmunizacionStr.indexOf("," + new Integer(codigoInmunizacion).toString()+",") == -1)
				{
					observacionesInmunizacion = ( (String)antecedenteForm.getObservacionesInmunizacionesPediatricas().get(codigoInmunizacion + "") ).trim();

					if(!observacionesInmunizacion.equals("") )
					{
						observacionesInmunizacion	= UtilidadCadena.cargarObservaciones(observacionesInmunizacion, "", usuarioMedico);
						inmunizacionAntecedente		= antecedente.darInmunizacionPediatrica(codigoInmunizacion);

						//Si la inmunizacion no estaba antes en bd
						if(inmunizacionAntecedente == null)
						{
							inmunizacion = new InmunizacionesPediatricas(codigoInmunizacion, "", observacionesInmunizacion, null);

							if(antecedente.getInmunizacionesPediatricas() == null)
								antecedente.setInmunizacionesPediatricas(new ArrayList() );
							antecedente.getInmunizacionesPediatricas().add(inmunizacion);
						}
						//Si la inmunizacion ya estaba antes
						else
						{
							if(inmunizacionAntecedente.getObservaciones().equals("") )
								inmunizacionAntecedente.setObservaciones(observacionesInmunizacion);
							//Si ya se tenia se debe concatenar con la anterior
							else
								inmunizacionAntecedente.setObservaciones(inmunizacionAntecedente.getObservaciones()  + observacionesInmunizacion);
						}
					}
				}
			}
			*/

			// APGAR. Si no se tenia apgar se permite ingresar
			if(antecedente.getApgarMinuto1() == -1 && !antecedenteForm.getApgarMinuto1().equals("") )
			{
				riesgoApgarArray_cn = antecedenteForm.getRiesgoApgarMinuto1_cn().split("-");
				antecedente.setApgarMinuto1(Integer.parseInt(antecedenteForm.getApgarMinuto1() ) );
				antecedente.setRiesgoApgarMinuto1(new InfoDatos(riesgoApgarArray_cn[0], riesgoApgarArray_cn[1]) );
			}

			if(antecedente.getApgarMinuto5() == -1 && !antecedenteForm.getApgarMinuto5().equals("") )
			{
				riesgoApgarArray_cn = antecedenteForm.getRiesgoApgarMinuto5_cn().split("-");
				antecedente.setApgarMinuto5(Integer.parseInt(antecedenteForm.getApgarMinuto5() ) );
				antecedente.setRiesgoApgarMinuto5(new InfoDatos(riesgoApgarArray_cn[0], riesgoApgarArray_cn[1]) );
			}

			if(antecedente.getApgarMinuto10() == -1 && !antecedenteForm.getApgarMinuto10().equals("") )
			{
				riesgoApgarArray_cn = antecedenteForm.getRiesgoApgarMinuto10_cn().split("-");
				antecedente.setApgarMinuto10(Integer.parseInt(antecedenteForm.getApgarMinuto10() ) );
				antecedente.setRiesgoApgarMinuto10(new InfoDatos(riesgoApgarArray_cn[0], riesgoApgarArray_cn[1]) );
			}

			/* Secci�n de embarazos anteriores */
			antecedente.setEmbarazosAnterioresOtros(antecedenteForm.getEmbarazosAnterioresOtros() );
			antecedente.setIncompatibilidadABO(antecedenteForm.getIncompatibilidadABO() );
			antecedente.setIncompatibilidadRH(antecedenteForm.getIncompatibilidadRH() );
			antecedente.setMacrosomicos(antecedenteForm.getMacrosomicos() );
			antecedente.setMalformacionesCongenitas(antecedenteForm.getMalformacionesCongenitas() );
			antecedente.setMortinatos(antecedenteForm.getMortinatos() );
			antecedente.setMuertesFetalesTempranas(antecedenteForm.getMuertesFetalesTempranas() );
			antecedente.setPrematuros(antecedenteForm.getPrematuros() );

			/* Secci�n de embarazo actual */
			antecedente.setCodigoSerologia(antecedenteForm.getCodigoSerologia() );
			antecedente.setCodigoTestSullivan(antecedenteForm.getCodigoTestSullivan() );
			antecedente.setControlPrenatal(antecedenteForm.getControlPrenatal() );
			antecedente.setDescripcionSerologia(antecedenteForm.getDescripcionSerologia() );
			antecedente.setDescripcionTestSullivan(antecedenteForm.getDescripcionTestSullivan() );
			antecedente.setFrecuenciaControlPrenatal(antecedenteForm.getFrecuenciaControlPrenatal() );
			antecedente.setLugarControlPrenatal(antecedenteForm.getLugarControlPrenatal() );

			/* Secci�n de trabajo de parto */
			antecedente.setAmnionitis(antecedenteForm.getAmnionitis() );
			antecedente.setAmnionitisFactorAnormal(antecedenteForm.getAmnionitisFactorAnormal() );
			antecedente.setAnestesia(antecedenteForm.getAnestesia() );
			antecedente.setAnestesiaTipo(antecedenteForm.getAnestesiaTipo() );

			if(!antecedenteForm.getDuracionExpulsivoHoras().equals("") )
				antecedente.setDuracionExpulsivoHoras(Integer.parseInt(antecedenteForm.getDuracionExpulsivoHoras() ) );
			if(!antecedenteForm.getDuracionExpulsivoMinutos().equals("") )
				antecedente.setDuracionExpulsivoMinutos(Integer.parseInt(antecedenteForm.getDuracionExpulsivoMinutos() ) );

			antecedente.setNst(antecedenteForm.getNst() );
			antecedente.setNstDescripcion(antecedenteForm.getNstDescripcion() );
			antecedente.setOtrosExamenesTrabajoParto(antecedenteForm.getOtrosExamenesTrabajoParto() );
			antecedente.setPerfilBiofisico(antecedenteForm.getPerfilBiofisico() );
			antecedente.setPerfilBiofisicoDescripcion(antecedenteForm.getPerfilBiofisicoDescripcion() );
			antecedente.setPtc(antecedenteForm.getPtc() );
			antecedente.setPtcDescripcion(antecedenteForm.getPtcDescripcion() );

			/* Informaci�n de la secci�n atenci�n del parto */
			if(!antecedenteForm.getEdadGestacional().equals("") )
				antecedente.setEdadGestacional(Integer.parseInt(antecedenteForm.getEdadGestacional() ) );

			if(!antecedenteForm.getIntrauterinoAnormalidad().equals("") )
				antecedente.setIntrauterinoAnormalidad(Integer.parseInt(antecedenteForm.getIntrauterinoAnormalidad() ) );

			antecedente.setComplicacionesParto(antecedenteForm.getComplicacionesParto() );
			antecedente.setCordonUmbilicalCaracteristicas(antecedenteForm.getCordonUmbilicalCaracteristicas() );
			antecedente.setCordonUmbilicalDescripcion(antecedenteForm.getCordonUmbilicalDescripcion() );
			antecedente.setEdadGestacionalDescripcion(antecedenteForm.getEdadGestacionalDescripcion() );
			antecedente.setGemelo(antecedenteForm.getGemelo() );
			antecedente.setGemeloDescripcion(antecedenteForm.getGemeloDescripcion() );
			antecedente.setIntrauterinoPeg(antecedenteForm.getIntrauterinoPeg() );
			antecedente.setIntrauterinoPegCausa(antecedenteForm.getIntrauterinoPegCausa() );
			antecedente.setIntrauterinoAnormalidadCausa(antecedenteForm.getIntrauterinoAnormalidadCausa() );
			antecedente.setIntrauterinoArmonico(antecedenteForm.getIntrauterinoArmonico() );
			antecedente.setIntrauterinoArmonicoCausa(antecedenteForm.getIntrauterinoArmonicoCausa() );
			antecedente.setLiqAmnioticoClaro(antecedenteForm.getLiqAmnioticoClaro() );
			antecedente.setLiqAmnioticoMeconiado(antecedenteForm.getLiqAmnioticoMeconiado() );
			antecedente.setLiqAmnioticoMeconiadoGrado(antecedenteForm.getLiqAmnioticoMeconiadoGrado() );
			antecedente.setLiqAmnioticoSanguinolento(antecedenteForm.getLiqAmnioticoSanguinolento() );
			antecedente.setLiqAmnioticoFetido(antecedenteForm.getLiqAmnioticoFetido() );
			antecedente.setMuestraCordonUmbilical(antecedenteForm.getMuestraCordonUmbilical() );
			antecedente.setMuestraCordonUmbilicalDescripcion(antecedenteForm.getMuestraCordonUmbilicalDescripcion() );
			antecedente.setPlacentaCaracteristicas(antecedenteForm.getPlacentaCaracteristicas() );
			antecedente.setPlacentaCaracteristicasDescripcion(antecedenteForm.getPlacentaCaracteristicasDescripcion() );
			antecedente.setReanimacion(antecedenteForm.getReanimacion() );
			antecedente.setReanimacionAspiracion(antecedenteForm.getReanimacionAspiracion() );
			antecedente.setReanimacionMedicamentos(antecedenteForm.getReanimacionMedicamentos() );
			antecedente.setSano(antecedenteForm.getSano() );
			antecedente.setSanoDescripcion(antecedenteForm.getSanoDescripcion() );
			antecedente.setSexoDescripcion(antecedenteForm.getSexoDescripcion() );

			// Observaciones:
			// Formato para guardar las observaciones:
			// <fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>_&_<fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>
			if(!antecedenteForm.getObservacionesGenerales().trim().equals("") )
			{
				//La siguiente instrucci�n no hac�a nada, por eso se coment�
				//String cadenaObservaciones = fecha + "|" + horaAct + "|" + nombreMedico + especialidadesMedico + "|" + registroMedico + "|" + antecedenteForm.getObservacionesGenerales();
				//antecedente.setObservaciones(antecedente.getObservaciones(false) + "_&_" + cadenaObservaciones);
				antecedente.setObservaciones(UtilidadCadena.cargarObservaciones( antecedenteForm.getObservacionesGenerales(),antecedente.getObservaciones(false) ,usuarioMedico));
			}

			/* Informaci�n de opciones de embarazo */
			iter = (c = (Collection)antecedenteForm.getEmbarazoOpcionCampos().keySet() ).iterator();

			while(iter.hasNext() )
			{
				ls_valorOpcionEmbarazo = (String)antecedenteForm.getEmbarazoOpcionCampo(key = (String)iter.next() );

				if(ls_valorOpcionEmbarazo != null && !ls_valorOpcionEmbarazo.equals("") )
					antecedente.setEmbarazoOpcionCampo(new InfoDatos(key, ls_valorOpcionEmbarazo) );
			}

			con = null;

			try
			{
				//Abriendo conexi�n
				con = (DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ) ).getConnection();

				//modificando el antecedentePediatrico
				antecedente.modificar(con);

				// Cerrar la conexi�n
				if(con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);
			}
			catch(SQLException ex)
			{
				// Cerrar la conexi�n
				if(con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);

				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			// Si se debe salir del flujo normal de grabaci�n del antecedente para irse a otra p�gina
			if(request.getParameter("estado").equals("salir") )
			{
				String paginaSiguiente;

				if( (paginaSiguiente=request.getParameter("paginaSiguiente") ) != null)
					response.sendRedirect(paginaSiguiente);

				return null;
			}

			// Si sigue el flujo normal debe mostrar los datos grabados
			this.cargar(mapping, form, request, false);
			return mapping.findForward("ingresarAntecedentePediatrico");
		}

		request.setAttribute("descripcionError", "No se encontro un antecedente pediatrico previamente cargado");
		return mapping.findForward("paginaError");
	}
	private ActionForward resumenIngreso(
			ActionMapping		aam_mapping,
			ActionForm			aaf_form,
			HttpServletRequest	ahsr_request
		)throws Exception
		{
			ActionForward laf_af;

			laf_af = cargar(aam_mapping, aaf_form, ahsr_request, false);

			if(!(laf_af.getName() ).equals("paginaError") )
				return aam_mapping.findForward("resumenIngreso");
			else
				return laf_af;
		}
}