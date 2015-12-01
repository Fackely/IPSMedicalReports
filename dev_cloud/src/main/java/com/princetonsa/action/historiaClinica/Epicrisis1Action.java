package com.princetonsa.action.historiaClinica;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
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

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.InfoDatosInt;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.Epicrisis1Form;
import com.princetonsa.dto.epicrisis.DtoCirugiaEpicrisis;
import com.princetonsa.dto.epicrisis.DtoEpicrisis1;
import com.princetonsa.dto.epicrisis.DtoEventoAdversoEpicrisis;
import com.princetonsa.dto.epicrisis.DtoEvolucionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoMedicamentosAdminEpicrisis;
import com.princetonsa.dto.epicrisis.DtoNotasAclaratoriasEpicrisis;
import com.princetonsa.dto.epicrisis.DtoProcedimientosEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionHospitalizacionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionUrgenciasEpicrisis;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.historiaClinica.Epicrisis1Antecedentes;
import com.princetonsa.mundo.historiaClinica.Epicrisis1CuadroTexto;


/**
 * 
 * @author wilson
 *
 */
public class Epicrisis1Action extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(Epicrisis1Action.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{

    	Connection con = null;
    	try {
    		if (response==null); 
    		if(form instanceof Epicrisis1Form)
    		{

    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			Epicrisis1Form forma =(Epicrisis1Form)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en EPICRISIS es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			ActionForward validacionesGenerales = this.validacionesAccesoUsuario(con, paciente,  mapping, request, forma, usuario);
    			if (validacionesGenerales != null)
    			{
    				UtilidadBD.cerrarConexion(con);
    				return validacionesGenerales ;
    			}

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()); 
    				logger.warn("Estado no valido dentro del flujo de EPICRISIS (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{
    				return this.accionEmpezar(forma,request, mapping, con, usuario, paciente);
    			}
    			else if(estado.equals("listadoIngresos"))
    			{
    				return this.accionListadoIngreso(forma, request, mapping, con, usuario, paciente);
    			}
    			else if(estado.equals("detalleAtencion"))
    			{
    				return this.accionDetalleAtencion(mapping, con, forma);
    			}
    			else if(estado.equals("definirEpicrisis"))
    			{
    				return this.accionDefinirEpicrisis(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("continuarDefinirEpicrisis"))
    			{
    				return this.accionContinuarDefinirEpicrisis(mapping, con);
    			}
    			else if(estado.equals("busquedaAvanzada"))
    			{
    				return this.accionBusquedaAvanzada(forma,mapping, con, usuario, response, paciente, false);
    			}
    			else if (estado.equals("redireccion"))
    			{
    				return this.accionRedireccion(con, forma, response, usuario, paciente);
    			}
    			else if(estado.equals("enviarAEpicrisis"))
    			{
    				return this.accionEnviarAEpicrisis(forma, request, mapping, con, usuario, paciente);
    			}
    			else if(estado.equals("guardar") || estado.equals("finalizar"))
    			{
    				return this.accionGuardarEpicrisis(forma, request, mapping, con, usuario, response, paciente);
    			}
    			else if(estado.equals("empezarNotasAclaratorias"))
    			{
    				return this.accionEmpezarNotasAclaratorias(forma, request, mapping, con, usuario);
    			}
    			else if(estado.equals("nuevaNotaAclaratoria"))
    			{
    				return this.accionNuevaNotaAclaratoria(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarNuevaNotaAclaratoria"))
    			{
    				return this.accionGuardarNuevaNotaAclaratoria(forma, mapping, con, usuario, request);
    			}
    			else if(estado.equals("modificarNotaAclaratoria"))
    			{
    				return this.accionModificarNotaAclaratoria(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarModificarNotaAclaratoria"))
    			{
    				return this.accionGuardarModificarNotaAclaratoria(forma, mapping, con, usuario, request);
    			}
    			else if(estado.equals("eliminarNotaAclaratoria"))
    			{
    				return this.accionEliminarNotaAclaratoria(forma, mapping, con, usuario, request);
    			}
    			else if(estado.equals("empezarConsultaEpicrisis"))
    			{
    				return this.accionEmpezarConsultaEpicrisis(forma, request, mapping, con, usuario, paciente);
    			}
    			else if(estado.equals("busquedaConsultaEpicrisis"))
    			{
    				return this.accionBusquedaConsultaEpicrisis(forma, mapping, con);
    			}
    			else if(estado.equals("continuarConsultaEpicrisis"))
    			{
    				return this.accionContinuarConsultaEpicrisis(mapping, con);
    			}
    			else if(estado.equals("consultarImprimirEpicrisis"))
    			{
    				return this.accionConsultarImprimirEpicrisis(forma, request, mapping, con, usuario, paciente);
    			}
    			else
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()); 
    				logger.warn("Estado no valido dentro del flujo de EPICRISIS");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
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

    /**
     * 
     * @param forma
     * @param request
     * @param mapping
     * @param con
     * @param usuario
     * @param paciente
     * @return
     */
	private ActionForward accionConsultarImprimirEpicrisis(Epicrisis1Form forma, HttpServletRequest request,ActionMapping mapping, Connection con, UsuarioBasico usuario,PersonaBasica paciente) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setImprimirAutomaticamente(true);
		
		HashMap<Object, Object> mapaRestricciones= new HashMap<Object, Object>();
		mapaRestricciones.put("codigopaciente", paciente.getCodigoPersona());
		mapaRestricciones.put("centroatencion",forma.getCentroAtencion());
		forma.setIngresosMap(IngresoGeneral.cargarListadoIngresos(con, mapaRestricciones));
		
		if(request.getParameter("isImpresionDummy")!=null && 
				request.getParameter("isImpresionDummy").toString().equals(ConstantesBD.acronimoSi))
		{
			boolean flag = false;
			for(int i = 0; i < Utilidades.convertirAEntero(forma.getIngresosMap("numRegistros").toString()) && !flag; i++)
			{
				if(forma.getIngresosMap("ingreso_"+i).toString().equals(paciente.getCodigoIngreso()+""))
				{
					forma.setIndice(i);
					flag  = true;
				}
			}
		}
		
		if(forma.getIndice()>=0)			
			consultarEpicrisis(forma, request, mapping, con, usuario, paciente);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultaEpicrisis");
	}

	/**
     * 
     * @param con
     * @param paciente
     * @param mapping
     * @param request
     * @param forma
     * @param usuario
     * @return
     */
    private ActionForward validacionesAccesoUsuario(Connection con, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, Epicrisis1Form forma, UsuarioBasico usuario) 
    {
    	if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
    		logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		if(ValoresPorDefecto.getMaxPageItemsIntEpicrisis(usuario.getCodigoInstitucionInt())<1)
		{
			ActionErrors errores=new ActionErrors();
            errores.add("error.parametrosGenerales.faltaDefinirParametro", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "No. Lineas Pager Epicrisis (Historia Clinica)"));
            logger.warn("entra al error de [error.parametrosGenerales.faltaDefinirParametro] ");
            saveErrors(request, errores);
            return mapping.findForward("paginaErroresActionErrors");
		}
		if( UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirConsultarEpicrisisSoloProfesionales(usuario.getCodigoInstitucionInt())))
		{
			if(!UtilidadTexto.isEmpty(UtilidadValidacion.esMedico(usuario)))
			{
				ActionErrors errores=new ActionErrors();
	            errores.add("errors.usuario.noAutorizado", new ActionMessage("errors.usuario.noAutorizado"));
	            logger.warn("entra al error de [errors.usuario.noAutorizado] ");
	            saveErrors(request, errores);
	            return mapping.findForward("paginaErroresActionErrors");
			}
		}	
		//Validación de autoatencion
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);
		
		
    	return null;
	}

	/**
     * 
     * @param forma
     * @param request
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(Epicrisis1Form forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		return accionListadoIngreso(forma,request, mapping, con, usuario, paciente);
	}

	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionListadoIngreso(Epicrisis1Form forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		HashMap<Object, Object> mapaRestricciones= new HashMap<Object, Object>();
		mapaRestricciones.put("codigopaciente", paciente.getCodigoPersona());
		mapaRestricciones.put("centroatencion", forma.getCentroAtencion());
		forma.setIngresosMap(IngresoGeneral.cargarListadoIngresos(con, mapaRestricciones));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoIngresos");
	}
    
	/**
     * 
     * @param mapping
     * @param con
     * @return
     */
    private ActionForward accionDetalleAtencion(ActionMapping mapping, Connection con, Epicrisis1Form forma) 
    {
    	forma.setFinalizada(Epicrisis1.existeFinalizacionEpicrisis(con, Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")));
    	if(!UtilidadTexto.isEmpty(forma.getIngresosMap("fechaegreso_"+forma.getIndice())+""))
    		forma.setEgresoMedico(true);
    	else
    		forma.setEgresoMedico(false);
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAtencion");
	}
    
    /**
     * 
     * @param forma
     * @param request
     * @param mapping
     * @param con
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionDefinirEpicrisis(Epicrisis1Form forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
    {
    	forma.resetDetalleBusqueda();
    	forma.resetCuadroTexto();
    	cargarCuentasIngreso(con, forma);
		llenarCriteriosBusqueda(forma, con, usuario, true);
		
		//cargamos los cuadros de texto
		forma.setCuadroTextoMap(Epicrisis1.cargarCuadroTexto(con, forma.getCuentasIngreso(), forma.getCriteriosBusquedaMap(), Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), true, true));
		
		//cargamos el cuadro texto del encabezado (siempre se muestra)
		forma.setContenidoEncabezado(Epicrisis1.cargarEpicrisis1(Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")).getContenidoEncabezado().toString());
		
		Utilidades.imprimirMapa(forma.getCriteriosBusquedaMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("definirEpicrisis");
    }
    
    /**
	 * 
	 * @param con
	 * @param forma
	 */
	private void cargarCuentasIngreso(Connection con, Epicrisis1Form forma)
	{
		//setiamos las cuentas pertenecientes a ese ingreso
		forma.setCuentasIngreso(Cuenta.obtenerCuentasIngreso(con, Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), true));
	}
    
    /**
     * 
     * @param forma
     * @param con
     */
	private void llenarCriteriosBusqueda(Epicrisis1Form forma, Connection con, UsuarioBasico usuario, boolean esFlujoDefinirEpicrisis)
	{
		//lo primero es llenar los criterios de busqueda, obtenemos los tipos de evolucion
		ArrayList<InfoDatosInt> viasIngresoTiposPaciente= new ArrayList<InfoDatosInt>();
		if(!UtilidadTexto.getBoolean(forma.getIngresosMap("escuentaasocio_"+forma.getIndice())+""))
		{	
			viasIngresoTiposPaciente.add(new InfoDatosInt(Integer.parseInt(forma.getIngresosMap("viaingreso_"+forma.getIndice())+""), forma.getIngresosMap("tipopaciente_"+forma.getIndice())+""));
		}
		else
		{
			HashMap<Object, Object> mapaViasIngresoTiposPaciente= Cuenta.obtenerViasIngresoTipoPacDadoIngreso(con, Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), ConstantesBD.acronimoSi);
			for(int w=0;w<Integer.parseInt(mapaViasIngresoTiposPaciente.get("numRegistros")+""); w++)
			{	
				viasIngresoTiposPaciente.add(new InfoDatosInt(Integer.parseInt(mapaViasIngresoTiposPaciente.get("viaingreso_"+w)+""), mapaViasIngresoTiposPaciente.get("tipopaciente_"+w)+""));
			}	
		}
		
		forma.setCriteriosBusquedaMap(Epicrisis1.obtenerTiposEvolucion(con, viasIngresoTiposPaciente));
		//luego le adicionamos los key de fecha inicial y fecha final, y los key de la presentacion de la info por fecha y hora o por tipo de evolucion
		forma.setCriteriosBusquedaMap("fechainicial", forma.getIngresosMap("fechaingreso_"+forma.getIndice()));
		forma.setCriteriosBusquedaMap("fechafinal", forma.getIngresosMap("fechaegreso_"+forma.getIndice()));
		if(UtilidadTexto.isEmpty(forma.getCriteriosBusquedaMap("fechafinal")+""))
		{
			forma.setCriteriosBusquedaMap("fechafinal", UtilidadFecha.getFechaActual());
		}
		//es excluyente con la otra persentacion
		forma.setCriteriosBusquedaMap("presentacionxfechahora", ConstantesBD.acronimoSi);
		forma.setCriteriosBusquedaMap("institucion", usuario.getCodigoInstitucion());
		
		if(esFlujoDefinirEpicrisis)
		{
			forma.setValoracionesInicialesAmostrar(Epicrisis1.cargarValoracionesIniciales(con, forma.getCuentasIngreso()));
		}
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionContinuarDefinirEpicrisis(ActionMapping mapping, Connection con) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("definirEpicrisis");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionBusquedaAvanzada(Epicrisis1Form forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletResponse response, PersonaBasica paciente, boolean esResumen) 
	{
		forma.resetDetalleBusqueda();
		
		boolean seleccionoValInicial=false;
    	for (int w=0; w<Integer.parseInt(forma.getValoracionesInicialesAmostrar("numRegistros").toString()); w++) 
    	{
    		if(UtilidadTexto.getBoolean(forma.getValoracionesInicialesAmostrar("seleccionadaval_"+w)+""))
    			seleccionoValInicial=true;
		}
		
		boolean seleccionaronEvol=false;
		for(int w=0; w<Integer.parseInt(forma.getCriteriosBusquedaMap("numRegistros")+"");w++)
		{
			if(UtilidadTexto.getBoolean(forma.getCriteriosBusquedaMap("seleccionado_"+w)+""))
			{
				seleccionaronEvol=true;
			}
		}
		
		logger.info("\n\n*********************************************************************");
		logger.info("sel evol-->"+seleccionaronEvol+" sel val-->"+seleccionoValInicial);
		logger.info("*********************************************************************\n\n");
		
		if(seleccionaronEvol || seleccionoValInicial)
			forma.setFechasHorasMap(Epicrisis1.obtenerFechaHoraSolicitudes(con, forma.getCuentasIngreso(), forma.getCriteriosBusquedaMap(), Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), forma.getValoracionesInicialesAmostrar()));
		//lo primero es cargar desde el indice 0 al maxPage de la epicrisis
		forma.setDetalleMap(new HashMap<Object, Object>());
		logger.info("POS INICIAL 0, POS FINAL-->"+ValoresPorDefecto.getMaxPageItemsIntEpicrisis(usuario.getCodigoInstitucionInt()));
		cargarMapaDetalle(con, forma, 0 /*indiceInicial*/, ValoresPorDefecto.getMaxPageItemsIntEpicrisis(usuario.getCodigoInstitucionInt()) /*indiceFinal*/, usuario, paciente);
		//forma.setCuadroTextoMap(Epicrisis1.cargarCuadroTexto(con, forma.getCuentasIngreso(), forma.getCriteriosBusquedaMap(), Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")));
		
		if(!UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
		{
			int posOffSet=forma.getLinkSiguiente().indexOf("offset=")+7;
		    String temp=forma.getLinkSiguiente().substring(0, posOffSet);
		    logger.info("temp---->"+temp);
		    forma.setLinkSiguiente(temp+"0");
		    return accionRedireccion(con, forma, response, usuario, paciente);
		}
		
		if(esResumen)
		{
			forma.setCuadroTextoMap(Epicrisis1.cargarCuadroTexto(con, forma.getCuentasIngreso(), forma.getCriteriosBusquedaMap(), Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), true, true));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("definirEpicrisis");
	}

	/**
     * 
     * @param con
     * @param forma
     * @param response
     * @return
     */
    private ActionForward accionRedireccion(Connection con, Epicrisis1Form forma, HttpServletResponse response, UsuarioBasico usuario, PersonaBasica paciente) 
    {
		logger.info("link Siguiente-->"+forma.getLinkSiguiente());
		//ESTE ES EL REULTADO, ENTONCES TOMAMOS EL OFFSET PARA SABER EL REGISTRO INICIAL Y FINAL 
		//link Siguiente-->/axioma/epicrisis1/definirEpicrisis.jsp?pager.offset=2
		
		int posOffSet=forma.getLinkSiguiente().indexOf("offset=")+7;
	    int posInicial=Integer.parseInt(forma.getLinkSiguiente().substring(posOffSet, forma.getLinkSiguiente().length()));
		int posFinal= posInicial+ValoresPorDefecto.getMaxPageItemsIntEpicrisis(usuario.getCodigoInstitucionInt());
		
		logger.info("POS INICIAL->"+posInicial+" POS FINAL->"+posFinal);
		logger.info("MAPA DETALLE ANTES-->"+forma.getDetalleMap());
		
		//EN CASO DE QUE NO EXISTA EL KEY ENTONCES SE CARGA EN EL MAPA, DE LO CONTRARIO YA SE TIENE EN MEM
		if(!forma.getDetalleMap().containsKey("DETALLE_"+posInicial))
		{
			cargarMapaDetalle(con, forma, posInicial, posFinal, usuario, paciente);
		}
		logger.info("MAPA DETALLE DESPUES-->"+forma.getDetalleMap());
		UtilidadBD.closeConnection(con);
		try 
		{
			response.sendRedirect(forma.getLinkSiguiente());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * 
     * @param con
     * @param forma
     * @param indiceInicial
     * @param indiceFinal
     */
    private void cargarMapaDetalle(Connection con, Epicrisis1Form forma, int indiceInicial, int indiceFinal, UsuarioBasico usuario, PersonaBasica paciente)
    {
    	HashMap<Object, Object> mapaTemporal= Epicrisis1.cargarDetalleEpicrisis(con, forma.getFechasHorasMap(), indiceInicial, indiceFinal, usuario, paciente);
    	for(int w=indiceInicial; (w<indiceFinal && w<Integer.parseInt(forma.getFechasHorasMap("numRegistros")+"")); w++)
		{
			forma.setDetalleMap("DETALLE_"+w, mapaTemporal.get("DETALLE_"+w));
		}
    }
    
    
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionEnviarAEpicrisis(Epicrisis1Form forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		String contenido="";
		boolean selEnviarEpicrisis= false;
		//hacemos un recorrido de todas las secciones que fueron enviadas a la epicrisis y les colocamos el indicativo de ya enviadas
		for(int w=0; w<Integer.parseInt(forma.getFechasHorasMap("numRegistros")+""); w++)
		{
			if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos
				|| Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales)
			{
				if(UtilidadTexto.getBoolean(forma.getFechasHorasMap("enviarepicrisis_"+w)+""))
				{	
					contenido= Epicrisis1CuadroTexto.armarContenidoEventosAdversos((DtoEventoAdversoEpicrisis)forma.getDetalleMap("DETALLE_"+w));
					armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
					forma.setFechasHorasMap("enviarepicrisis_"+w, ConstantesBD.acronimoNo);
					forma.setFechasHorasMap("yaenviadoepicrisis_"+w, ConstantesBD.acronimoSi);
					selEnviarEpicrisis=true;
				}
			}
			else if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos)
			{
				if(UtilidadTexto.getBoolean(forma.getFechasHorasMap("enviarepicrisis_"+w)+""))
				{	
					contenido= Epicrisis1CuadroTexto.armarContenidoProcedimientos((DtoProcedimientosEpicrisis)forma.getDetalleMap("DETALLE_"+w));
					armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
					forma.setFechasHorasMap("enviarepicrisis_"+w, ConstantesBD.acronimoNo);
					forma.setFechasHorasMap("yaenviadoepicrisis_"+w, ConstantesBD.acronimoSi);
					selEnviarEpicrisis=true;
				}
			}
			else if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos)
			{
				if(UtilidadTexto.getBoolean(forma.getFechasHorasMap("enviarepicrisis_"+w)+""))
				{	
					contenido= Epicrisis1CuadroTexto.armarContenidoAdminMed((DtoMedicamentosAdminEpicrisis)forma.getDetalleMap("DETALLE_"+w));
					armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
					forma.setFechasHorasMap("enviarepicrisis_"+w, ConstantesBD.acronimoNo);
					forma.setFechasHorasMap("yaenviadoepicrisis_"+w, ConstantesBD.acronimoSi);
					selEnviarEpicrisis=true;
				}
			}
			else if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia)
			{
				DtoCirugiaEpicrisis dto= (DtoCirugiaEpicrisis)forma.getDetalleMap("DETALLE_"+w);
				
				if(dto!=null)
				{	
					for(int x=0; x<dto.getServicios().size(); x++)
					{	
						if(UtilidadTexto.getBoolean(forma.getFechasHorasMap("enviarserviciocxepicrisis_"+dto.getNumeroSolicitud()+"_"+dto.getServicios().get(x).getCodigoServicio())+""))
						{	
							contenido= Epicrisis1CuadroTexto.armarContenidoCxHojaQx( dto.getServicios().get(x));
							armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
							forma.setFechasHorasMap("enviarserviciocxepicrisis_"+dto.getNumeroSolicitud()+"_"+dto.getServicios().get(x).getCodigoServicio(), ConstantesBD.acronimoNo);
							forma.setFechasHorasMap("yaenviadoserviciocxepicrisis_"+dto.getNumeroSolicitud()+"_"+dto.getServicios().get(x).getCodigoServicio(), ConstantesBD.acronimoSi);
							selEnviarEpicrisis=true;
						}
					}
					if(UtilidadTexto.getBoolean(forma.getFechasHorasMap("enviarsalidasala_"+dto.getNumeroSolicitud())+""))
					{
						contenido= Epicrisis1CuadroTexto.armarContenidoCxSalidaSalaPaciente(dto);
						armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
						forma.setFechasHorasMap("enviarsalidasala_"+dto.getNumeroSolicitud(), ConstantesBD.acronimoNo);
						forma.setFechasHorasMap("yaenviadasalidasala_"+dto.getNumeroSolicitud(), ConstantesBD.acronimoSi);
						selEnviarEpicrisis=true;
					}
					for(int x=0; x<dto.getNotasEnfermeria().size(); x++)
					{	
						if(UtilidadTexto.getBoolean(forma.getFechasHorasMap("enviarnotaenfermeria_"+dto.getNumeroSolicitud()+"_"+dto.getNotasEnfermeria().get(x).getCodigo())+""))
						{
							contenido= Epicrisis1CuadroTexto.armarContenidoNotasEnfermeria(dto.getNotasEnfermeria().get(x));
							armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
							forma.setFechasHorasMap("enviarnotaenfermeria_"+dto.getNumeroSolicitud()+"_"+dto.getNotasEnfermeria().get(x).getCodigo(), ConstantesBD.acronimoNo);
							logger.info("\n\nNOTAS DE ENFERMERIA->"+"yaenviadanotaenfermeria_"+dto.getNumeroSolicitud()+"_"+dto.getNotasEnfermeria().get(x).getCodigo());
							forma.setFechasHorasMap("yaenviadanotaenfermeria_"+dto.getNumeroSolicitud()+"_"+dto.getNotasEnfermeria().get(x).getCodigo(), ConstantesBD.acronimoSi);
							selEnviarEpicrisis=true;
						}
					}
					for(int x=0; x<dto.getNotasRecuperacion().size(); x++)
					{	
						if(UtilidadTexto.getBoolean(forma.getFechasHorasMap("enviarnotarecuperacion_"+dto.getNumeroSolicitud()+"_"+x)+""))
						{
							contenido= Epicrisis1CuadroTexto.armarContenidoNotasRecuperacion(dto.getNotasRecuperacion().get(x));
							armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
							forma.setFechasHorasMap("enviarnotarecuperacion_"+dto.getNumeroSolicitud()+"_"+x, ConstantesBD.acronimoNo);
							forma.setFechasHorasMap("yaenviadanotarecuperacion_"+dto.getNumeroSolicitud()+"_"+x, ConstantesBD.acronimoSi);
							selEnviarEpicrisis=true;
						}
					}
				}
			}
			else if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta)
			{
				String indiceCheckEnviar="";
				DtoValoracionEpicrisis dto= (DtoValoracionEpicrisis)forma.getDetalleMap("DETALLE_"+w);
				if(dto!=null)
				{
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						logger.info("\n enviaron seccion->"+indiceCheckEnviar+" codigoSeccion->"+seccionFija.getCodigoSeccion()+" \n");
						
						if(UtilidadTexto.getBoolean(forma.getFechasHorasMap().get("enviarseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							logger.info("SI");
							if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaInformacionGeneral)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultaInformacionGeneral(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaRevisionSistemas)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultaRevisionSistemas(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaCausaExterna)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultaCausaExterna(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaFinalidadConsulta)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultaFinalidadConsulta(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaDiagnosticos)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultasDx(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaConceptoConsulta)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultasConceptoConsulta(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaIncapacidadFuncional)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesIncapacidadFuncional(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaObservaciones)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultasObservaciones(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaFechaProximoControl)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoInterconsultasFechaProximoControl(seccionFija, dto.getDtoValoracion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							
							contenido= Epicrisis1CuadroTexto.armarContenidoSeccionesParametrizablesInterconsultas(seccionFija, dto.getDtoValoracion());
							if(!UtilidadTexto.isEmpty(contenido))
							{	
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}	
							
						}
					}
					
					if(UtilidadTexto.getBoolean(forma.getFechasHorasMap().get("enviarinterpretacion_"+w)+""))
					{
						contenido= Epicrisis1CuadroTexto.armarContenidoInterpretacion(dto.getDtoInterpretacion());
						armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
						forma.setFechasHorasMap("enviarinterpretacion_"+w, ConstantesBD.acronimoNo);
						forma.setFechasHorasMap("yaenviadainterpretacion_"+w, ConstantesBD.acronimoSi);
						selEnviarEpicrisis=true;
					}
				}
			}
			else if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
					|| Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
			{
				String indiceCheckEnviar="";
				int tipoEvolucion= Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"");
				
				DtoValoracionHospitalizacionEpicrisis dto= (DtoValoracionHospitalizacionEpicrisis)forma.getDetalleMap("DETALLE_"+w);
				if(dto!=null)
				{
					String infoAutomatica="";
					//cargamos el contenido de la información que se envía automática
					if(tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
					{
						for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
						{
							//primero verificamos si envio la informacion general
							DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
							infoAutomatica= Epicrisis1CuadroTexto.armarInfoAutomaticaValoraciones(seccionFija, dto.getDtoValHospitalizacion());
							if(!UtilidadTexto.isEmpty(infoAutomatica))
							{
								infoAutomatica= forma.getFechasHorasMap("nombretipoevolucion_"+w)+"<br>"+infoAutomatica;
								infoAutomatica=infoAutomatica.toUpperCase();
								logger.info("\n\n\n SE ENVIA LA INFO AUTOMATICA \n\n\n\n");
								contenido="";
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								selEnviarEpicrisis=true;
							}
						}
					}	
					infoAutomatica=infoAutomatica.toUpperCase();
					
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						if(UtilidadTexto.getBoolean(forma.getFechasHorasMap().get("enviarseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaInformacionGeneral)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionInformacionGeneral(seccionFija, dto.getDtoValHospitalizacion(), tipoEvolucion);
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaRevisionSistemas)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionRevisionSistemas(seccionFija, dto.getDtoValHospitalizacion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaCausaExterna)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesCausaExterna(seccionFija, dto.getDtoValHospitalizacion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaFinalidadConsulta)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesFinalidadConsulta(seccionFija, dto.getDtoValHospitalizacion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							//para las valoraciones iniciales esta informacion se envia automaticamente a la consulta de epicrisis y no se debe permitir modificar en el cuadro de texto
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaDiagnosticos && tipoEvolucion!=ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesDx(seccionFija, dto.getDtoValHospitalizacion(), "\n");
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaObservaciones)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesObservaciones(seccionFija, dto.getDtoValHospitalizacion(), tipoEvolucion);
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							
							contenido= Epicrisis1CuadroTexto.armarContenidoSeccionesParametrizablesValoraciones(seccionFija, dto.getDtoValHospitalizacion());
							if(!UtilidadTexto.isEmpty(contenido))
							{	
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}	
							
						}
					}
				}
			}
			
			else if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
			{
				String indiceCheckEnviar="";
				DtoEvolucionEpicrisis dto= (DtoEvolucionEpicrisis)forma.getDetalleMap("DETALLE_"+w);
				if(dto!=null)
				{
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						if(UtilidadTexto.getBoolean(forma.getFechasHorasMap().get("enviarseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaInformacionGeneral)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionInformacionGeneral(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaDiagnosticos)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionesDx(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaDatosSubjetivos)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionesDatosSubjetivos(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionfijaHallazgosImportantes)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionesHallazgosImportantes(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaAnalisis)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionesAnalisis(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaPlanManejo)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionesPlanManejo(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaComentariosGenerales)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionesComentariosGenerales(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaConductaSeguir)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoEvolucionesConductaSeguir(seccionFija, dto.getDtoEvolucion());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							
							contenido= Epicrisis1CuadroTexto.armarContenidoSeccionesParametrizablesEvoluciones(seccionFija, dto.getDtoEvolucion());
							if(!UtilidadTexto.isEmpty(contenido))
							{	
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, "");
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
						}	
					}
				}
			}
			else if(Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
			{
				String indiceCheckEnviar="";
				DtoValoracionUrgenciasEpicrisis dto= (DtoValoracionUrgenciasEpicrisis)forma.getDetalleMap("DETALLE_"+w);
				if(dto!=null)
				{
					String infoAutomatica="";
					//cargamos el contenido de la información que se envía automática
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						infoAutomatica= Epicrisis1CuadroTexto.armarInfoAutomaticaValoracionesUrgencias(seccionFija, dto.getDtoValUrgencias());
						if(!UtilidadTexto.isEmpty(infoAutomatica))
						{
							infoAutomatica= forma.getFechasHorasMap("nombretipoevolucion_"+w)+"<br>"+infoAutomatica;
							infoAutomatica=infoAutomatica.toUpperCase();
							logger.info("\n\n\n SE ENVIA LA INFO AUTOMATICA \n\n\n\n");
							contenido="";
							armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
							selEnviarEpicrisis=true;
						}
					}	
					infoAutomatica=infoAutomatica.toUpperCase();
					
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						if(UtilidadTexto.getBoolean(forma.getFechasHorasMap().get("enviarseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaInformacionGeneral)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionUrgInformacionGeneral(seccionFija, dto.getDtoValUrgencias());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaRevisionSistemas)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionUrgRevisionSistemas(seccionFija, dto.getDtoValUrgencias());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaExamenFisico)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionUrgExamenFisico(seccionFija, dto.getDtoValUrgencias());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaCausaExterna)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesUrgCausaExterna(seccionFija, dto.getDtoValUrgencias());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaFinalidadConsulta)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesUrgFinalidadConsulta(seccionFija, dto.getDtoValUrgencias());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaConductaSeguir)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesUrgConductaSeguir(seccionFija, dto.getDtoValUrgencias());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaObservaciones)
							{
								contenido= Epicrisis1CuadroTexto.armarContenidoValoracionesUrgObservaciones(seccionFija, dto.getDtoValUrgencias());
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}
							contenido= Epicrisis1CuadroTexto.armarContenidoSeccionesParametrizablesValoracionesUrg(seccionFija, dto.getDtoValUrgencias());
							if(!UtilidadTexto.isEmpty(contenido))
							{	
								armarCuadroTexto(forma, forma.getFechasHorasMap("codigopk_"+w)+"", forma.getFechasHorasMap("fecha_"+w)+"", forma.getFechasHorasMap("hora_"+w)+"", Integer.parseInt(forma.getFechasHorasMap("codigotipoevolucion_"+w)+""), contenido, infoAutomatica);
								forma.setFechasHorasMap("enviarseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoNo);
								forma.setFechasHorasMap("yaenviadaseccion_"+w+"_"+indiceCheckEnviar, ConstantesBD.acronimoSi);
								selEnviarEpicrisis=true;
							}	
							
						}
					}
				}
			}
		}
		
		if(!selEnviarEpicrisis)
		{
			UtilidadBD.closeConnection(con);
			forma.setEstado("continuarDefinirEpicrisis");
			ActionErrors errores=new ActionErrors();
			errores.add("", new ActionMessage("errors.notEspecific", "No ha seleccionado ningun dato para enviar a epicrisis. Por favor revisar"));
            saveErrors(request, errores);
            return mapping.findForward("definirEpicrisis");
		}
    		
    	//le cambio el estado para el caso de un f5 sobre la pagina
		forma.setEstado("continuarDefinirEpicrisis");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("definirEpicrisis");
	}

	/**
	 * 
	 * @param forma
	 * @param codigoPk
	 * @param fechaApp
	 * @param hora
	 * @param codigoTipoEvolucion
	 * @param contenido
	 * @param infoAutomatica
	 */
	private void armarCuadroTexto(Epicrisis1Form forma, String codigoPk, String fechaApp, String hora, int codigoTipoEvolucion, String contenido, String infoAutomatica )
	{
		contenido=contenido.toUpperCase();
		boolean asignado=false;
		for(int w=0; w<forma.getNumRegCuadroTexto(); w++)
		{
			if((forma.getCuadroTextoMap("codigopk_"+w)+"").equals(codigoPk) && Integer.parseInt(forma.getCuadroTextoMap("codigotipoevolucion_"+w)+"")==codigoTipoEvolucion)
			{
				forma.setCuadroTextoMap("contenido_"+w, forma.getCuadroTextoMap("contenido_"+w)+contenido);
				forma.setCuadroTextoMap("resaltar_"+w, ConstantesBD.acronimoSi);
				asignado=true;
			}
		}
		if(!asignado)
		{
			forma.setCuadroTextoMap("codigopk_"+forma.getNumRegCuadroTexto(), codigoPk);
			forma.setCuadroTextoMap("codigotipoevolucion_"+forma.getNumRegCuadroTexto(), codigoTipoEvolucion);
			forma.setCuadroTextoMap("fecha_"+forma.getNumRegCuadroTexto(), fechaApp);
			forma.setCuadroTextoMap("fechabd_"+forma.getNumRegCuadroTexto(), UtilidadFecha.conversionFormatoFechaABD(fechaApp));
			forma.setCuadroTextoMap("hora_"+forma.getNumRegCuadroTexto(), hora);
			forma.setCuadroTextoMap("contenido_"+forma.getNumRegCuadroTexto(), contenido);
			forma.setCuadroTextoMap("resaltar_"+forma.getNumRegCuadroTexto(), ConstantesBD.acronimoSi);
			forma.setCuadroTextoMap("infoautomatica_"+forma.getNumRegCuadroTexto(), infoAutomatica);
			forma.setCuadroTextoMap("numRegistros", forma.getNumRegCuadroTexto()+1);
		}
		
		//tenemos que hacer un ordenamiento por fecha
		String[] indices= {"codigopk_", "codigotipoevolucion_", "fecha_", "fechabd_", "hora_", "contenido_", "resaltar_", "infoautomatica_"};
		int numReg=forma.getNumRegCuadroTexto();
		forma.setCuadroTextoMap(Listado.ordenarMapa(indices, "hora_", "", forma.getCuadroTextoMap(),numReg));
		forma.setCuadroTextoMap(Listado.ordenarMapa(indices, "fechabd_", "", forma.getCuadroTextoMap(),numReg));
		forma.setCuadroTextoMap("numRegistros",numReg+"");	
	}

	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param response 
	 * @return
	 */
	private ActionForward accionGuardarEpicrisis(Epicrisis1Form forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletResponse response, PersonaBasica paciente) 
	{
		//iniciamos la transaccion
		UtilidadBD.iniciarTransaccion(con);
		if(!Epicrisis1.insertarEpicrisis(con, forma.getCuadroTextoMap(), usuario.getLoginUsuario()))
		{
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			ActionErrors errores=new ActionErrors();
			errores.add("", new ActionMessage("errors.notEspecific", "No actualizo la info de la epicrisis. Por favor revisar"));
            saveErrors(request, errores);
            return mapping.findForward("definirEpicrisis");
		}
		Epicrisis1.actualizarIndicativosEnviadosEpicrisis(con, forma.getFechasHorasMap(), forma.getDetalleMap(), Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), usuario);
		
		boolean finalizar=false;
		if(forma.getEstado().equals("finalizar"))
		{
			forma.setFinalizada(true);
			finalizar=true;
		}	
		
		DtoEpicrisis1 dto = new DtoEpicrisis1();
		dto.setFinalizada(finalizar);
		dto.setIngreso(Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""));
		dto.setUsuarioModifica(usuario.getLoginUsuario());
		dto.setContenidoEncabezado(new StringBuilder(forma.getContenidoEncabezado()));
		dto.setFechaContenido(forma.getIngresosMap("fechaingreso_"+forma.getIndice())+"");
		dto.setHoraContenido(forma.getIngresosMap("horaingreso_"+forma.getIndice())+"");
		
		if(Epicrisis1.existeEncabezadoEpicrisis(con, Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")))
		{
			Epicrisis1.actualizarEpicrisis(con, dto);
		}
		else
		{	
			Epicrisis1.insertarEncabezadoEpicrisis(con, dto);
		}	
		
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("guardarExitoso");
		
		if(finalizar)
			return accionEmpezarConsultaEpicrisis(forma, request, mapping, con, usuario, paciente);
		return this.accionBusquedaAvanzada(forma, mapping, con, usuario, response, paciente, true);
	}
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarNotasAclaratorias(Epicrisis1Form forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.resetNotasAclaratorias();
		forma.setNotasAclaratorias( Epicrisis1.cargarNotasAclaratorias(con, Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("notasAclaratorias");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionNuevaNotaAclaratoria(Epicrisis1Form forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		DtoNotasAclaratoriasEpicrisis dto= new DtoNotasAclaratoriasEpicrisis();
		dto.setCodigo(ConstantesBD.codigoNuncaValidoDoubleNegativo);
		dto.setFecha(UtilidadFecha.getFechaActual()+"");
		dto.setHora(UtilidadFecha.getHoraActual());
		dto.setLoginUsuario(usuario.getLoginUsuario());
		dto.setNombreUsuario(usuario.getNombreUsuario());
		dto.setIngreso(Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""));
		dto.setNota("");
		forma.setDtoNotaAclaratoria(dto);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("notasAclaratorias");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionGuardarNuevaNotaAclaratoria(Epicrisis1Form forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadBD.iniciarTransaccion(con);
		if(Epicrisis1.insertarNotaAclaratoria(con, forma.getDtoNotaAclaratoria())>0)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return accionEmpezarNotasAclaratorias(forma, request, mapping, con, usuario);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarNotaAclaratoria(Epicrisis1Form forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		DtoNotasAclaratoriasEpicrisis dto= new DtoNotasAclaratoriasEpicrisis();
		dto.setCodigo(Double.parseDouble(forma.getNotasAclaratorias("codigo_"+forma.getIndiceNotaAclaratoria())+""));
		dto.setFecha(forma.getNotasAclaratorias("fecha_"+forma.getIndiceNotaAclaratoria())+"");
		dto.setHora(forma.getNotasAclaratorias("hora_"+forma.getIndiceNotaAclaratoria())+"");
		dto.setLoginUsuario(forma.getNotasAclaratorias("loginusuario_"+forma.getIndiceNotaAclaratoria())+"");
		dto.setNombreUsuario(forma.getNotasAclaratorias("nombremedico_"+forma.getIndiceNotaAclaratoria())+"");
		dto.setIngreso(Integer.parseInt(forma.getNotasAclaratorias("ingreso_"+forma.getIndiceNotaAclaratoria())+""));
		dto.setNota(forma.getNotasAclaratorias("nota_"+forma.getIndiceNotaAclaratoria())+"");
		forma.setDtoNotaAclaratoria(dto);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("notasAclaratorias");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarModificarNotaAclaratoria(Epicrisis1Form forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadBD.iniciarTransaccion(con);
		if(Epicrisis1.modificarNotaAclaratoria(con, forma.getDtoNotaAclaratoria()))
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return accionEmpezarNotasAclaratorias(forma, request, mapping, con, usuario);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminarNotaAclaratoria(Epicrisis1Form forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadBD.iniciarTransaccion(con);
		if(Epicrisis1.eliminarNotaAclaratoria(con, Double.parseDouble(forma.getNotasAclaratorias("codigo_"+forma.getIndiceNotaAclaratoria())+"")))
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return accionEmpezarNotasAclaratorias(forma, request, mapping, con, usuario);
	}
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarConsultaEpicrisis(Epicrisis1Form forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		consultarEpicrisis(forma, request, mapping, con, usuario, paciente);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultaEpicrisis");
	}
	

	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private void consultarEpicrisis(Epicrisis1Form forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario, PersonaBasica paciente){
		cargarCuentasIngreso(con, forma);
		llenarCriteriosBusqueda(forma, con, usuario, false);
		
		//se cargan la informacion de las valoraciones iniciales para cargarlas via frame
		forma.setValoracionesInicialesAmostrar(Epicrisis1.cargarValoracionesIniciales(con, forma.getCuentasIngreso()));
		
		//se carga la informacion de egreso 
		forma.setDetalleMap(Epicrisis1.cargarUltimaEvolucion(con, forma.getCuentasIngreso(), usuario));
		
		//se verifica el parametro general de cargar la parametrizacion de antecedentes epicrisis
		Vector<String> vectorAntecedentesAMostrar= new Vector<String>();
		
		logger.info("\n\n AMOSTRAR ANTCEDENTES PARAMETRIZADOS EPICRISIS-->"+ValoresPorDefecto.getMostrarAntecedentesParametrizadosEpicrisis(usuario.getCodigoInstitucionInt()));
		
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getMostrarAntecedentesParametrizadosEpicrisis(usuario.getCodigoInstitucionInt())))
		{
			vectorAntecedentesAMostrar=Epicrisis1.obtenerAntecedentesParametrizadosEpicrisis(con, usuario.getCodigoInstitucionInt());
		}
		else
		{
			//lo cargamos con todos los antecedentes
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesAlergias+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesFamiliares+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesFamiliaresOculares+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesGinecoObstetricos+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesMedicamentos+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesMedicosYQx+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesOdontologicos+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesPediatricos+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesPersonalesOculares+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesToxicos+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesTransafuncionales+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesVacunas+"");
			vectorAntecedentesAMostrar.add(ConstantesBD.codigoFuncionalidadAntecentesVarios+"");
		}
		
		forma.setAntecedentesAmostrar(vectorAntecedentesAMostrar);
		
		for(int w=0; w<vectorAntecedentesAMostrar.size(); w++)
		{	
			if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesAlergias)
			{	
				forma.setAntecedentesAlergias(Epicrisis1Antecedentes.cargarAntecedentesAlergias(paciente.getCodigoPersona(), "<br>"));
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesFamiliares)
			{	
				forma.setAntecedentesFamiliares(Epicrisis1Antecedentes.cargarAntecedentesFamiliares(paciente.getCodigoPersona(), "<br>"));
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesFamiliaresOculares)
			{	
				forma.setAntecedentesFamiliaresOculares(Epicrisis1Antecedentes.cargarAntecedentesFamiliaresOculares(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt()));
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesPediatricos)
			{
				//este se carga en la forma de antecedentes y se muestra con un include
			}
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesGinecoObstetricos)
			{
				forma.setAntecedentesGineco(Epicrisis1Antecedentes.cargarAntecedentesGinecoObstetricos(con, paciente, usuario.getCodigoInstitucionInt(), request));
				forma.setHistoricosGineco((ArrayList<Object>)forma.getAntecedentesGineco("historicos"));
			}
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesMedicamentos)
			{	
				forma.setAntecedentesMedicamentos(Epicrisis1Antecedentes.cargarAntecedentesMedicamentos(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt()));
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesMedicosYQx)
			{	
				String antecedenteMedicoYqx=Epicrisis1Antecedentes.cargarAntecedentesMedicos(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt());
				antecedenteMedicoYqx+=Epicrisis1Antecedentes.cargarAntecedentesQx(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt());
				forma.setAntecedentesMedicosYQx(antecedenteMedicoYqx);
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesOdontologicos)
			{	
				String antecedenteOdon=Epicrisis1Antecedentes.cargarAntecedentesOdontologicos(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt());
				antecedenteOdon+= Epicrisis1Antecedentes.cargarAntecedentesTraumatismoYfracturasDentales(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt());
				antecedenteOdon+= Epicrisis1Antecedentes.cargarAntecedentesTratamientosOdontologicosPrevios(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt());
				forma.setAntecedentesOdontologicos(antecedenteOdon);
				
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesPersonalesOculares)
			{
				String antecedentePocular=Epicrisis1Antecedentes.cargarAntecedentesPersonalesOculares(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt());
				antecedentePocular+= Epicrisis1Antecedentes.cargarAntecedentesPersonalesOcularesQx(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt());
				forma.setAntecedentesPersonalesOculares(antecedentePocular);
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesToxicos)
			{	
				forma.setAntecedentesToxicos(Epicrisis1Antecedentes.cargarAntecedentesToxicos(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt()));
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesTransafuncionales)
			{	
				forma.setAntecedentesTransfuncionales(Epicrisis1Antecedentes.cargarAntecedentesTransfuncionales(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt()));
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesVarios)
			{	
				forma.setAntecedentesVarios(Epicrisis1Antecedentes.cargarAntecedentesOtros(paciente.getCodigoPersona(), "<br>", usuario.getCodigoInstitucionInt()));
			}	
			else if(Integer.parseInt(vectorAntecedentesAMostrar.get(w)+"")==ConstantesBD.codigoFuncionalidadAntecentesVacunas)
			{	
				forma.setAntecedentesVacunas(Epicrisis1Antecedentes.cargarAntecedentesVacunas(paciente.getCodigoPersona()));
			}
		}
		
		forma.resetMedicoElaboraEpicrisis();
		forma.setMapaMedicoElaboraEpicrisis(Epicrisis1.cargarMedicoElaboraEpicrisis(con, Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")));
		
		forma.resetNotasAclaratorias();
		forma.setNotasAclaratorias( Epicrisis1.cargarNotasAclaratorias(con, Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")));
		
		forma.setCuadroTextoMap(Epicrisis1.cargarCuadroTexto(con, forma.getCuentasIngreso(), forma.getCriteriosBusquedaMap(), Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), true, true));
		
		//cargamos el cuadro texto del encabezado (siempre se muestra)
		forma.setContenidoEncabezado(Epicrisis1.cargarEpicrisis1(Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+"")).getContenidoEncabezado().toString());
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaConsultaEpicrisis(Epicrisis1Form forma, ActionMapping mapping, Connection con)
	{
		forma.setCuadroTextoMap(Epicrisis1.cargarCuadroTexto(con, forma.getCuentasIngreso(), forma.getCriteriosBusquedaMap(), Integer.parseInt(forma.getIngresosMap("ingreso_"+forma.getIndice())+""), true, true));
		forma.setAbrirSecciones("evolucion", ConstantesBD.acronimoSi);
		UtilidadBD.closeConnection(con);
		logger.info("llega 11111111111");
		return mapping.findForward("consultaEpicrisis");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionContinuarConsultaEpicrisis(ActionMapping mapping, Connection con) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultaEpicrisis");
	}
}
