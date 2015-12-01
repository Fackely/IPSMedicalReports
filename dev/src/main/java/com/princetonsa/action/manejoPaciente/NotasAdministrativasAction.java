package com.princetonsa.action.manejoPaciente;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.actionform.manejoPaciente.NotasAdministrativasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.DetNotasAdministrativas;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.IngresosHome;
import com.servinte.axioma.orm.NotasAdministrativas;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.administracion.CuentasDelegate;
import com.servinte.axioma.orm.delegate.administracion.DetNotasAdministrativasDelegate;
import com.servinte.axioma.orm.delegate.administracion.NotasAdministrativasDelegate;







/**
 * 
 * @author Alejoo
 *
 */
public class NotasAdministrativasAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(NotasAdministrativasAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (form instanceof NotasAdministrativasForm) 
		{
			
			NotasAdministrativasForm forma = (NotasAdministrativasForm) form;
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");

			String estado = forma.getEstado();

			logger.info("estado--> " + estado);

			if (estado == null) 
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}

			// empezar
			else if (estado.equals("empezar") || estado.equals("resumen")  || estado.equals("inicio")) 
			{

				return accionEmpezar(mapping, forma, usuario, paciente,request);
			}
			// nueva nota
			else if (estado.equals("notaNueva")) {
				return accionNotaNueva(mapping, forma, usuario);
			}
			
			// nuevo comentario
			else if (estado.equals("detallenuevo")) {
				return accionDetalleNuevo(mapping, forma, usuario);
			}
						
			// ingreso en DB de la nota
			else if(estado.equals("guardar"))
			{
				return accionGuardar(mapping, forma, usuario, request, paciente);
			}

			// nuevo comentario ingreso a DB
			else if (estado.equals("guardarComentario")) {
				return accionGuardarComentario(mapping, forma, usuario, request, paciente);
			}
			
			// ver detalle nota 
			else if(estado.equals("verdetalle")) 
			{ 
				return	accionVerDetalle(mapping,forma, usuario, request); 
			}
			
			else if(estado.equals("imprimir"))
			{
				return accionGenerarReporte(mapping, request, forma, usuario);
			}
			// volver
			else if (estado.equals("volver")) 
			{
				return accionEmpezar(mapping, forma, usuario, paciente,request);
			}

			else if(estado.equals("consultar"))
			{
				return accionConsultar(mapping, forma, usuario, paciente,request);
			}
			else if(estado.equals("cargarIngresoConsulta"))
			{
				return accionDetalleIngreso(mapping, forma, usuario, paciente,request);
			}
			else if(estado.equals("ordenarIngresos"))
			{
				return ordenarIngresos(forma,mapping);
			}
		}
		return null;
	}
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public ActionForward ordenarIngresos(NotasAdministrativasForm forma, ActionMapping mapping){
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente()!=null&&forma.getEsDescendente().equals(forma.getPropiedadOrdenar()))
		{
			forma.setEsDescendente(forma.getPropiedadOrdenar()+"descendente") ;
		}else{
			forma.setEsDescendente(forma.getPropiedadOrdenar());
		}	
		
		logger.info("patron ORDENAR-> " + forma.getPropiedadOrdenar());
		logger.info("DESCENDENTE --> " + forma.getEsDescendente() );
		
		if(forma.getEsDescendente().equals(forma.getPropiedadOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPropiedadOrdenar(),ordenamiento);
		Collections.sort(forma.getIngresos(),sortG);
		return mapping.findForward("listado");
	}

	private ActionForward accionDetalleIngreso(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request) 
	{
		
		HibernateUtil.beginTransaction();
		
		ArrayList<NotasAdministrativas> listaNotas = new NotasAdministrativasDelegate().listarNotas(forma.getIngresos().get(forma.getIndiceIngresoSeleccionado()).getIngreso()); 
		

		for(NotasAdministrativas nota : listaNotas){
			nota.getUsuarios().getLogin();
		}

		forma.setListaDto(listaNotas);	

		
		HibernateUtil.endTransaction();
		return mapping.findForward("principal");	
	}

	private ActionForward accionConsultar(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request) 
	{
		if(paciente==null || paciente.getNumeroIdentificacionPersona().equals(""))
		{
			mostrarErrorPacienteNoCargado(forma, request,"consultar");
			forma.reset();
			forma.setEstado("inicio");
			
		}
		else
		{
			forma.setIngresos(DetNotasAdministrativasDelegate.consultarIngresosConNotasAdministrativas(paciente.getCodigoPersona()));
			if(forma.getIngresos().size()<=0)
			{
				ActionErrors errores = new ActionErrors();
				errores.add("error no_paciente_cargado", new ActionMessage("error.errorEnBlanco","Paciente sin registro de Notas Administrativas. Por favor verifique."));
				saveErrors(request, errores);
				forma.reset();
				forma.setEstado("inicio");
			}
		}
			
		
		return mapping.findForward("listado");
	}
	
	/**
	 * @param mapping
	 * @param forma.
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request) {
		
		if(paciente==null || paciente.getNumeroIdentificacionPersona().equals(""))
		{
			mostrarErrorPacienteNoCargado(forma, request,"empezar");
			forma.reset();
			forma.setEstado("inicio");
		}
		else
		{
			if(paciente.getCodigoIngreso()<=0)
			{
				mostrarErrorNoIngreso(forma, request, paciente);
				
			}else
			{
			  if(paciente.getCodigoCuenta()<=0)
			   {
				mostrarErroresCuenta(forma, request, paciente);
			   }
			}
			//logger.info("identificacion >> "+paciente.getNumeroIdentificacionPersona());
			if(paciente!=null && !paciente.getNumeroIdentificacionPersona().equals("") && paciente.getCodigoIngreso()>0)
			{
				forma.reset();
				
				HibernateUtil.beginTransaction();
				
				ArrayList<NotasAdministrativas> listaNotas = new NotasAdministrativasDelegate().listarNotas(paciente.getCodigoIngreso()); 
				

				for(NotasAdministrativas nota : listaNotas){
					nota.getUsuarios().getLogin();
				}

		forma.setListaDto(listaNotas);	

				
				HibernateUtil.endTransaction();
				return mapping.findForward("principal");	
			}
			
		}
			
		
		return mapping.findForward("principal");
	}

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario,HttpServletRequest request, 
			PersonaBasica paciente) {
		
	
		HibernateUtil.beginTransaction();
				
		forma.getDto().setIngresos(new IngresosHome().findById(paciente.getCodigoIngreso()));
		forma.getDto().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		
		
		new NotasAdministrativasDelegate().persist(forma.getDto());
		HibernateUtil.endTransaction();
		forma.reset();
		ArrayList<NotasAdministrativas> listaNotas = new NotasAdministrativasDelegate().listarNotas(paciente.getCodigoIngreso()); 
		forma.setListaDto(listaNotas);	
		forma.setEstado("empezar");
		return mapping.findForward("principal");
	}
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarComentario(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario,HttpServletRequest request, 
			PersonaBasica persona) {
		
		
		HibernateUtil.cerrarSession();
		HibernateUtil.beginTransaction();
		
				
		forma.getDtoDet().setNotasAdministrativas(forma.getDto());
		forma.getDtoDet().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
	
		new DetNotasAdministrativasDelegate().persist(forma.getDtoDet());
		HibernateUtil.endTransaction();
		
		
		
		int codigoNotaAdministrativa = forma.getDto().getCodigoPk(); 
		ArrayList<DetNotasAdministrativas> listaDetalles = new DetNotasAdministrativasDelegate()
		.listarDetallesDeNotaAdministrativa(codigoNotaAdministrativa);
		forma.resetDos();
		
		
		forma.setListaDetDto(listaDetalles);
		NotasAdministrativas x = new NotasAdministrativas();
		x.setCodigoPk(codigoNotaAdministrativa);
		forma.getDtoDet().setNotasAdministrativas(x);
			
		forma.setEstado("verdetalle");
		return mapping.findForward("principal");
	}

	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private void inicializarDto(NotasAdministrativasForm forma, UsuarioBasico usuario)
	{
		forma.getDto().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getDto().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDto().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private void inicializarDtoDet(NotasAdministrativasForm forma, UsuarioBasico usuario)
	{
		forma.getDtoDet().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getDtoDet().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoDet().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
	}
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNotaNueva(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario) {
		//forma.reset();
		inicializarDto(forma, usuario);
		forma.setEstado("empezar");
		forma.setMostrarFormularioIngreso(true);
		return mapping.findForward("principal");
	}

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleNuevo(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario) {
		//forma.reset();
		inicializarDtoDet(forma, usuario);
		forma.setMostrarFormularioIngresoDetalle(true);
		forma.setEstado("verdetalle");
		return mapping.findForward("principal");
	}
	
	
	private ActionForward accionImprimir(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario,
			HttpServletRequest request){
		
		int codigo= forma.getDto().getCodigoPk();
		//consultarNotasPorCodigo(codigo);
		
		
		return mapping.findForward("principal");
	}
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionVerDetalle(ActionMapping mapping,
			NotasAdministrativasForm forma, UsuarioBasico usuario,
			HttpServletRequest request) {
		
		
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		
		
		int codigoNotaAdministrativa = forma.getDto().getCodigoPk(); 
		ArrayList<DetNotasAdministrativas> listaDetalles = new DetNotasAdministrativasDelegate()
			.listarDetallesDeNotaAdministrativa(codigoNotaAdministrativa);

		
		
		HibernateUtil.beginTransaction();
		
		for(DetNotasAdministrativas detNota : listaDetalles){
			detNota.getNotasAdministrativas().getCodigoPk();
		}

		forma.setListaDetDto(listaDetalles);	

		HibernateUtil.endTransaction();
		
		forma.setEstado("verdetalle");
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Muestra un mensaje indicando que no hay paciente cargado
	 * @param forma
	 * @param request
	 * @param string 
	 */
	private void mostrarErrorPacienteNoCargado(NotasAdministrativasForm forma, HttpServletRequest request, String estado) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_paciente_cargado", new ActionMessage("errors.paciente.noCargado"));
		saveErrors(request, errores);
		forma.setEstado(estado);
	}
	
	
	
	
	/**
	 * ESTE METODO HACE:
	 * 
	 *  1. CREA EL PDF DE LA CONSULTA.
	 *  2. GUARDA EL LOG DE LA CONSULTA
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @param institucionBasica
	 * @return
	 */
	private ActionForward accionGenerarReporte(ActionMapping mapping,HttpServletRequest request, NotasAdministrativasForm forma, UsuarioBasico usuario) 
	{
		/*
		String tmpRutaImpresion ="";
		
		
						Vector v = new Vector();
				
						HashMap result = new HashMap();
				
						String nombreRptDesign = "prueba.rptdesign";
				
						// ***************** INFORMACIÓN DEL CABEZOTE
						

						DesignEngineApi comp = new DesignEngineApi("/home/axioma/contextos/axioma/web/WEB-INF/reports/designs/","prueba.rptdesign");
						
						//tmpRutaImpresion=ParamsBirtApplication.getReportsPath()+ "odontologia/"+nombreRptDesign;
						
						tmpRutaImpresion="/home/axioma/contextos/axioma/web/WEB-INF/reports/designs/prueba.rptdesign";
						// Logo
						Connection con = UtilidadBD.abrirConexion();
						comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
						logger.info("xxxxxxxxxx--->"+ ins.getLogoReportes());
						comp.insertGridHeaderOfMasterPage(0, 1, 1, 4);
						v.add(ins.getRazonSocial());
						
						if (Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
							v.add(Utilidades.getDescripcionTipoIdentificacion(con, ins.getTipoIdentificacion())+ ". "+ ins.getNit()+ " - "
											+ ins.getDigitoVerificacion());
						else
							v.add(Utilidades.getDescripcionTipoIdentificacion(con, ins.getTipoIdentificacion())	+ ". " + ins.getNit());
							
							v.add(ins.getDireccion());
							v.add("Tels. " + ins.getTelefono());
							comp.insertLabelInGridOfMasterPage(0, 1, v);
							UtilidadBD.cerrarObjetosPersistencia(null, null, con);
					
							// Información del reporte
							comp.insertLabelInGridPpalOfHeader(1, 0, "Notas Administrativas");
							// Información del reporte
							//comp.insertLabelInGridPpalOfHeader(	2, 0,"Fecha de Generación Inicial:  "+ forma.getDtoPromocion().getFechaGeneracionInicial() +" -  Fecha de Generación Final: "+forma.getDtoPromocion().getFechaGeneracionFinal() );
							comp.insertarUsuarioImprimio(usuario.getNombreUsuario());
						
							String newquery =ConsultasBirt.consultarNotasPorCodigo(forma.getDto().getCodigoPk());
							logger.info("CONSULTA DEL REPORTE DEL BIRT newquery birt------>"+newquery+"\n\n\n\n");
							
						
							comp.obtenerComponentesDataSet("notas_administrativas");
							comp.modificarQueryDataSet(newquery);
						
				
						// debemos arreglar los alias para que funcione oracle, debido a que
						// este los convierte a MAYUSCULAS
							comp.lowerAliasDataSet();
							String newPathReport = comp.saveReport1(false);
							comp.updateJDBCParameters(newPathReport);
						
						
						ConsultasBirt.consultarNotasPorCodigo(obtenerCodigoNota(forma));
				
						result.put("descripcion", newPathReport);
						result.put("resultado", true);
						result.put("urlArchivoPlano", "");
						result.put("pathArchivoPlano", "");
						
						 
				        if(!newPathReport.equals(""))
				        {
				        	request.setAttribute("isOpenReport", "true");
							request.setAttribute("newPathReport",result.get("descripcion").toString());
				        }
							
		*/
		return mapping.findForward("principal");
	}
 
	
	/**
	 
	 * @param forma
	 * @return
	 */
	private  int obtenerCodigoNota(NotasAdministrativasForm forma){
		
		return 	forma.getListaDto().get(forma.getPosArray()).getCodigoPk();
	}
	
	/**
	 * validacion de cuenta activa, asociada o facturada parcial.
	 * @param forma
	 * @param request
	 * @param paciente
	 */
	private void mostrarErroresCuenta(NotasAdministrativasForm forma, HttpServletRequest request, PersonaBasica paciente) 
	{
		int cuentaPaciente =paciente.getCodigoCuenta();
		
		Cuentas cuenta = new CuentasDelegate().findById(cuentaPaciente);
		
		
		int estado=1;
		try {
			estado = cuenta.getEstadosCuenta().getCodigo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(estado != 0 && estado != 3 && estado != 6)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("error paciente_estado_invalido", new ActionMessage("error.paciente.estadoInvalido","activa, asociada o facturada parcial"));
			saveErrors(request, errores);
			forma.setEstado("inicio");
						
		}
	}
	
	/**
	 * validacion de cuenta abierta
	 * 
	 * @param forma
	 * @param request
	 * @param paciente
	 */
	
	private void mostrarErrorNoIngreso(NotasAdministrativasForm forma, HttpServletRequest request, PersonaBasica paciente) 
	{
		logger.info("INGRESOOO >> "+paciente.getCodigoIngreso());
		if(paciente.getCodigoIngreso()<=0)
		{
			forma.reset();
			ActionErrors errores = new ActionErrors();
			errores.add("error no_paciente_cargado", new ActionMessage("errors.paciente.noIngreso"));
			saveErrors(request, errores);
			forma.setEstado("inicio");
			
		}else
		{
			int IngresoPaciente =paciente.getCodigoIngreso();
			
			Ingresos ingreso = new IngresosHome().findById(IngresoPaciente);
			
			if(!ingreso.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{   
				forma.reset();
				ActionErrors errores = new ActionErrors();
				errores.add("error no_paciente_cargado", new ActionMessage("errors.paciente.noIngreso"));
				saveErrors(request, errores);
				forma.setEstado("inicio");
							
			}
		}
		
	}

	

}
