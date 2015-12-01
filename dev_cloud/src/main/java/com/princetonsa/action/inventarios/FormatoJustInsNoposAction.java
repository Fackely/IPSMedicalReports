package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.ConstantesBDHistoriaClinica;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.historiaClinica.UtilidadesJustificacionNoPos;

import com.princetonsa.actionform.inventarios.FormatoJustInsNoposForm;
import com.princetonsa.dto.historiaClinica.DtoParamCamposJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamOpcionesCamposJusNoPos;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.inventarios.FormatoJustInsNopos;
import com.servinte.axioma.servicio.formatoJust.ComportamientoCampo;

/**
 * @author Giovanny Arias
 */
public class FormatoJustInsNoposAction extends Action {

	/**
	 * logger 
	 * */
	static Logger logger = Logger.getLogger(FormatoJustInsNoposAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{

    	Connection con = null;
    	try{

    		if(response==null);
    		if(form instanceof FormatoJustInsNoposForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("paginaError");
    			}

    			FormatoJustInsNoposForm forma = (FormatoJustInsNoposForm) form;
    			FormatoJustInsNopos mundo = new FormatoJustInsNopos();

    			String estado = forma.getEstado();
    			ActionErrors errores = new ActionErrors();
    			logger.info("\n\n\n ESTADO (Formato Justificacion Insumos No Pos) > "+estado+"\n");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			UsuarioBasico usuario  = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(paciente==null)
    				errores.add("Paciente", new ActionMessage("errors.required","Paciente"));

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado no valido dentro del Flujo del Formato Justificacion Insumos No Pos (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("paginaError");
    			}
    			else 
    				/*------------------------------
				ESTADO > empezar
			-------------------------------*/
    				if(estado.equals("empezar"))		
    					return this.accionEmpezar(forma, con, mapping, usuario, paciente, request, response, mundo);
    				else if(estado.equals("empezarotravez")){
        				return this.accionEmpezarOtraVez(request, forma, mapping, usuario, con);
        			}
    					/*------------------------------
				ESTADO > guardar
			-------------------------------*/
    					if(estado.equals("guardar"))		
    						return this.accionGuardar(forma, con, mapping, usuario, paciente, request, response, mundo);
    					else
    						/*------------------------------
				ESTADO > consultar
			-------------------------------*/
    						if(estado.equals("consultar"))		
    							return this.accionConsultar(forma, con, mapping, usuario, paciente, request, response, mundo);
    						else
    							/*------------------------------
				ESTADO > modificar
			-------------------------------*/
    							if(estado.equals("modificar"))		
    								return this.accionModificar(forma, con, mapping, usuario, paciente, request, response, mundo);
    							else
    								/*------------------------------
				ESTADO > guardarModificacion
			-------------------------------*/
    								if(estado.equals("guardarModificacion"))		
    									return this.accionGuardarModificacion(forma, con, mapping, usuario, paciente, request, response, mundo);
    								else
    									/*------------------------------
				ESTADO > guardarDeUna
			-------------------------------*/
    									if(estado.equals("guardarDeUna"))		
    										return this.accionGuardarDeUna(forma, con, mapping, usuario, paciente, request, response, mundo);
					    					if(estado.equals("accionCampo")){
												ActionForward retorno=ComportamientoCampo.accionCampo(con, forma, mapping, request);
												forma.setEstado(forma.getEstadoAnterior());
												return retorno;
											}

    			UtilidadBD.closeConnection(con);
    		}
    	}catch (Exception e) {
    		Log4JManager.error(e);
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    	return null;
}
	
    /**
     * Metodo para guardar la justificaci�n No Pos
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @param mundo
     * @return
     */
    private ActionForward accionGuardarDeUna(FormatoJustInsNoposForm forma,
			Connection con, ActionMapping mapping, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request,
			HttpServletResponse response, FormatoJustInsNopos mundo) {
		
    	UtilidadesJustificacionNoPos.guardarJustificacion(con, forma.getDtoParam(), ConstantesIntegridadDominio.acronimoInsumo, usuario);
    	UtilidadJustificacionPendienteArtServ.eliminarJusNoposPendiente(con, Utilidades.convertirAEntero(forma.getDtoParam().getSolicitud()),Utilidades.convertirAEntero(forma.getDtoParam().getCodigoArticulo()), true);
    	
    	// Pendiente ingresar mas responsables si los tiene
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("formato");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @param mundo
     * @return
     */
    private ActionForward accionGuardarModificacion(
			FormatoJustInsNoposForm forma, Connection con,
			ActionMapping mapping, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request,
			HttpServletResponse response, FormatoJustInsNopos mundo) {
    	
    	UtilidadesJustificacionNoPos.actualizarJustificacion(con, forma.getDtoParam(), ConstantesIntegridadDominio.acronimoInsumo, usuario);
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("formato");
		
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @param mundo
     * @return
     */
    private ActionForward accionModificar(FormatoJustInsNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response, FormatoJustInsNopos mundo) 
    {
    	// Cargar Parametrizaci�n
		forma.setDtoParam(UtilidadesJustificacionNoPos.cargarParametrizacion(con, usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoInsumo));
    	
    	// Tomar parametros del request
		tomarParametrosDelRequest(forma, request);
		
		// Consultar Justificaci�n
		forma.setDtoParam(UtilidadesJustificacionNoPos.consultarJustificacion(con, ConstantesIntegridadDominio.acronimoInsumo, forma.getDtoParam(), paciente));
		
		
		DtoParamJusNoPos tmpDtoparam= forma.getDtoParam();
		
		for (int i = 0; i < tmpDtoparam.getSecciones().size(); i++) {
			
			Boolean hayDatos=false;
			
			for (int j = 0; j <tmpDtoparam.getSecciones().get(i).getCampos().size() && !hayDatos; j++) {
				
				if(tmpDtoparam.getSecciones().get(i).getCampos().get(j).getValor()!=null && !String.valueOf(tmpDtoparam.getSecciones().get(i).getCampos().get(j).getValor()).equals("") &&
						tmpDtoparam.getSecciones().get(i).getCampos().get(j).getFijo().equals("N")
						)
						{
					tmpDtoparam.getSecciones().get(i).getCampos().get(j).setTieneDatos(true);
					hayDatos=true;
				}
				
				
			}
			
			
			
		}
		
    
		try{
			int[]valores=ComportamientoCampo.buscarIdentificadoresCampoInsumo(forma.getDtoParam().getSecciones(), ConstantesBDHistoriaClinica.codigoCampoJusNoPosExisteSustituto);
			if(valores!=null){
				DtoParamCamposJusNoPos campo=forma.getDtoParam().getSecciones().get(valores[0]).getCampos().get(valores[1]);
				campo.setDisabled(true);
			}
			
			ComportamientoCampo.cargarValoresDefectoAccionesCampo(con, forma, mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("formato");
	}

	/**
     *
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @param mundo
     * @return
     */
    private ActionForward accionConsultar(FormatoJustInsNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response, FormatoJustInsNopos mundo) 
    {
    	// Cargar Parametrizaci�n
		forma.setDtoParam(UtilidadesJustificacionNoPos.cargarParametrizacion(con, usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoInsumo));
    	
    	// Tomar parametros del request
		tomarParametrosDelRequest(forma, request);
		
		// Consultar Justificaci�n
		forma.setDtoParam(UtilidadesJustificacionNoPos.consultarJustificacion(con, ConstantesIntegridadDominio.acronimoInsumo, forma.getDtoParam(), paciente));
    
		try{
			int[]valores=ComportamientoCampo.buscarIdentificadoresCampoInsumo(forma.getDtoParam().getSecciones(), ConstantesBDHistoriaClinica.codigoCampoJusNoPosExisteSustituto);
			if(valores!=null){
				DtoParamCamposJusNoPos campo=forma.getDtoParam().getSecciones().get(valores[0]).getCampos().get(valores[1]);
				campo.setDisabled(true);
			}
			
			ComportamientoCampo.cargarValoresDefectoAccionesCampo(con, forma, mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("imprimir");
	}

	/**
     * M�todo encargado de guardar en memoria la justificaci�n no pos
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @param mundo
     * @return
     */
    private ActionForward accionGuardar(FormatoJustInsNoposForm forma,Connection con, ActionMapping mapping, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request,HttpServletResponse response, FormatoJustInsNopos mundo) 
    {
    	if(request.getSession().getAttribute("JUSTIFICACION")==null){
    		ArrayList<DtoParamJusNoPos> justificacion = new ArrayList<DtoParamJusNoPos>();
    		justificacion.add(forma.getDtoParam());
    		request.getSession().setAttribute("JUSTIFICACION", justificacion);
    	} else {
    		boolean justificacionGuardada = false;
    		ArrayList<DtoParamJusNoPos> justificacion = new ArrayList<DtoParamJusNoPos>();
    		justificacion = (ArrayList<DtoParamJusNoPos>)request.getSession().getAttribute("JUSTIFICACION");
    		    		
    		
    		// Buscamos si la justificaci�n para este articulo ya se ha realizado
    		for(int i=0; i<justificacion.size(); i++){
    			if(justificacion.get(i).getCodigoArticulo().equals(forma.getDtoParam().getCodigoArticulo())){
    				justificacion.set(i, forma.getDtoParam());
    				justificacionGuardada = true;
    			}
    		}
    		
    		// Ingresamos una nueva justificaci�n si esta no existe
    		if(!justificacionGuardada){
    			justificacion.add(forma.getDtoParam());
    			justificacionGuardada = true;
    		}
    		
    	}
    	UtilidadesJustificacionNoPos.imprimirCamposDtoJustificacion(forma.getDtoParam());
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("formato");
	}

	/**
     * M�todo encargado de realizar la acci�n de empezar
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @param mundo
     * @return
     * @throws SQLException
     */
	private ActionForward accionEmpezar(FormatoJustInsNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response, FormatoJustInsNopos mundo) throws SQLException 
	{
		forma.reset();
		
		if(forma.isRecordar()){
			
			// Cargar el Dto de la justificaci�n anteriormente guardada en sesion
			cargarDtoParam(forma, request);
			
		} else {
			
			// Cargar Parametrizaci�n 
			forma.setDtoParam(UtilidadesJustificacionNoPos.cargarParametrizacion(con, usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoInsumo));
			
			// Inicializar justificaci�n cuando se va a ingresar
			inicializarDatos(forma, con, usuario, paciente, request);
			
			// Realizar Validaciones
			if(!validarIngresoJustificacion(forma, con, usuario, paciente, request)){
				UtilidadBD.closeConnection(con);
				return mapping.findForward("noJustificacion");
			}
			
		}
		
		try{
			ComportamientoCampo.cargarValoresDefectoAccionesCampo(con, forma, mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("formato");
	}

	public ActionForward accionEmpezarOtraVez(HttpServletRequest request, FormatoJustInsNoposForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con){
		
		ArrayList<DtoParamJusNoPos> justificacion=(ArrayList<DtoParamJusNoPos>) request.getSession().getAttribute("JUSTIFICACION");
		//boolean ingresado=false;
		if(justificacion!=null){
			for(int i=0; i<justificacion.size(); i++){
				if(justificacion.get(i).getCodigoArticulo().equals(forma.getArticulo())){
					forma.setDtoParam(justificacion.get(i));
				}
			}
			//ingresado=true;
		}
				
		return mapping.findForward("formato");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param request
	 */
	private boolean validarIngresoJustificacion(FormatoJustInsNoposForm forma, Connection con, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) {
		
		boolean noJustificacion = true;
		
		//Evaluamos si el convenio que cubre el servicio requiere de justificaci�n de articulo
		if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio(), Utilidades.convertirAEntero(forma.getDtoParam().getCodigoArticulo())) )
		{
			if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)){
				logger.info("JUSTIFICACION NO SE REQUIERE X VALIDACION PROFESIONAL DE LA SALUD");
				forma.setNoJustificacion("1");
				noJustificacion = false;
			}
		}
		else
		{
			logger.info("EL CONVENIO NO REQUIERE JUSTIFICACION");
			forma.setNoJustificacion("2");
			noJustificacion = false;
		}
		
		return noJustificacion;
	}

	/**
	 * 
	 * @param forma
	 * @param request
	 */
	private void cargarDtoParam(FormatoJustInsNoposForm forma,HttpServletRequest request) {
		
		String articulo="", cantidad="";
		if(!request.getParameter("articulo").isEmpty())
			articulo = request.getParameter("articulo");
		if(!request.getParameter("cantidad").isEmpty())
			cantidad=request.getParameter("cantidad");
		
		ArrayList<DtoParamJusNoPos> justificacion = new ArrayList<DtoParamJusNoPos>();
		justificacion = (ArrayList<DtoParamJusNoPos>)request.getSession().getAttribute("JUSTIFICACION");
		for(int i=0; i<justificacion.size(); i++){
			if(justificacion.get(i).getCodigoArticulo().equals(articulo))
				forma.setDtoParam(justificacion.get(i));
		}
		
		for(int i=0; i<forma.getDtoParam().getSecciones().size(); i++){
			for(int j=0; j<forma.getDtoParam().getSecciones().get(i).getCampos().size(); j++){
				DtoParamCamposJusNoPos campo = new DtoParamCamposJusNoPos();
				campo = forma.getDtoParam().getSecciones().get(i).getCampos().get(j);
				if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCantidadArtNoPos))
					campo.setValor(cantidad);
			}
		}
	}

	/**
	 * Inicializar datos
	 * @param forma
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param reques
	 */
	private void inicializarDatos(FormatoJustInsNoposForm forma, Connection con, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request)
	{
		// Tomar parametros del request
		tomarParametrosDelRequest(forma, request);
		
		// Inicializamos Variables generales
		FormatoJustArtNopos fjan = new FormatoJustArtNopos();
		fjan.setEmisor("solicitud");
		fjan.setMedicamentoNoPos(forma.getDtoParam().getCodigoArticulo());
		String[] subCuentaConvenio = new String [2];
		String tipoUsuario = "";
		ArrayList<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
		HashMap sustitutosNoPos = new HashMap();
		
		// Cargar subcuenta y convenio
		subCuentaConvenio = fjan.requiereJustificacionArticulo(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoCuenta()+"");
		forma.getDtoParam().setSubCuenta(subCuentaConvenio[0]+"");
		
		// Obtenemos el nombre del tipo de regimen segun el codigo del convenio
		tipoUsuario = UtilidadesFacturacion.consultarNombreTipoRegimen(con, Utilidades.convertirAEntero(subCuentaConvenio[1]));
		
		// Obtenemos los Diagnosticos
		diagnosticos = UtilidadesHistoriaClinica.obtenerUltimosDiagnosticoIngreso(con, paciente.getCodigoIngreso(), true);
		
		// Obtenemos los sustitutos No Pos
		sustitutosNoPos = fjan.cargarSustitutosNoPos(con,fjan,paciente);
		
		// LLenar Campos Conocidos
		for(int i=0; i<forma.getDtoParam().getSecciones().size(); i++){
			
			for(int j=0; j<forma.getDtoParam().getSecciones().get(i).getCampos().size(); j++){
				
				DtoParamCamposJusNoPos campo = new DtoParamCamposJusNoPos();
				campo = forma.getDtoParam().getSecciones().get(i).getCampos().get(j);
				
				if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosInsNombrePaciente))
					campo.setValor(paciente.getNombrePersona(true));
									
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosInsIdPaciente))
					campo.setValor(paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEntidad)){
					//campo.setValor(subCuentaConvenio[1]);
					if(campo.getValor()==null||campo.getValor().toString().trim().equals("")){
						campo.setEtiquetaValor(Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(subCuentaConvenio[1])));
					}else{
						campo.setEtiquetaValor(Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(campo.getValor())));
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosIngreso))
					campo.setValor(paciente.getConsecutivoIngreso());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosFecha))
					campo.setValor(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosTipoUsuario))
					campo.setValor(tipoUsuario);
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEdad))
					campo.setValor(paciente.getEdadDetallada()+"");
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEstado)){
					campo.setValor(ConstantesIntegridadDominio.acronimoJustificado);
					campo.setEtiquetaValor("Justificado");
				}	
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCentroCosto)){
					if(campo.getValor()==null||campo.getValor().toString().trim().equals("")){
						if(!UtilidadTexto.isEmpty(request.getParameter("provieneOrdenAmbulatoria"))&&UtilidadTexto.getBoolean(request.getParameter("provieneOrdenAmbulatoria"))){
							campo.setValor("Orden Ambulatoria");							
						}else{
							Cuenta cuenta=new Cuenta();
							String[] valores=null;
							try {
								valores = cuenta.consultarAreaYViaIngreso(con, paciente.getCodigoCuenta());
								if(valores!=null){
									campo.setValor(valores[3]+" - "+valores[1]);	
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosConsecutivo))
					campo.setValor("");
			
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosTipoRiesgoSoportado)){
					campo.setValor(ConstantesBD.acronimoSi);
					for(int op=0; op<campo.getOpciones().size(); op++){
						if (campo.getOpciones().get(op).getValor().equals(campo.getValor())){
							campo.getOpciones().get(op).setSeleccionado(ConstantesBD.acronimoSi);
						}	
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxComplicacion)){
					for(int d=0; d<diagnosticos.size(); d++){
						if(diagnosticos.get(d).isComplicacion()){
							campo.setEtiquetaValor(diagnosticos.get(d).getAcronimo()+" - "+diagnosticos.get(d).getTipoCIE()+" "+diagnosticos.get(d).getNombre());
							campo.setValor(diagnosticos.get(d).getAcronimo()+ConstantesBD.separadorSplit+diagnosticos.get(d).getTipoCIE()+ConstantesBD.separadorSplit+diagnosticos.get(d).getNombre());
						}
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxPrincipal)){
					for(int d=0; d<diagnosticos.size(); d++){
						if(diagnosticos.get(d).isPrincipal()){
							campo.setEtiquetaValor(diagnosticos.get(d).getAcronimo()+" - "+diagnosticos.get(d).getTipoCIE()+" "+diagnosticos.get(d).getNombre());
							campo.setValor(diagnosticos.get(d).getAcronimo()+ConstantesBD.separadorSplit+diagnosticos.get(d).getTipoCIE()+ConstantesBD.separadorSplit+diagnosticos.get(d).getNombre());
						}
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxRelacionado)){
					HashMap<String, Object> dxRelacionadosMap = new HashMap<String, Object>();
					int numDx=0;
					String dxSeleccionados="'"+ConstantesBD.codigoNuncaValido+"'";
					for(int d=0; d<diagnosticos.size(); d++){
						if(!diagnosticos.get(d).isPrincipal() && !diagnosticos.get(d).isComplicacion()){
							
							dxRelacionadosMap.put("relacionado_"+numDx, diagnosticos.get(d).getAcronimo()+ConstantesBD.separadorSplit+diagnosticos.get(d).getTipoCIE()+ConstantesBD.separadorSplit+diagnosticos.get(d).getNombre());
							dxRelacionadosMap.put("checkbox_"+numDx, true);
							dxSeleccionados += ", '"+diagnosticos.get(d).getAcronimo()+"'";
							numDx++;
							
						}
					}
					dxRelacionadosMap.put("numDx", numDx);
					dxRelacionadosMap.put("dxSeleccionados", dxSeleccionados);
					forma.getDtoParam().setDxRelacionadosMap(dxRelacionadosMap);
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosPacienteInformado)){
					campo.setValor(ConstantesBD.acronimoSi);
					for(int op=0; op<campo.getOpciones().size(); op++){
						if (campo.getOpciones().get(op).getValor().equals(campo.getValor())){
							campo.getOpciones().get(op).setSeleccionado(ConstantesBD.acronimoSi);
						}	
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosNombreArtNoPos)){
					campo.setValor(Utilidades.obtenerNombreArticulo(con, Utilidades.convertirAEntero(forma.getDtoParam().getCodigoArticulo())));
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCantidadArtNoPos)){
					campo.setValor(forma.getDtoParam().getCantidadArticulo());
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosExisteSustituto)){
					campo.setDisabled(true);
					if (Utilidades.convertirAEntero(sustitutosNoPos.get("numRegistros").toString()) > 0)
						campo.setValor(ConstantesBD.acronimoSi);
					else
						campo.setValor(ConstantesBD.acronimoNo);
					
					for(int op=0; op<campo.getOpciones().size(); op++){
						if (campo.getOpciones().get(op).getValor().equals(campo.getValor()))
							campo.getOpciones().get(op).setSeleccionado(ConstantesBD.acronimoSi);
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosArticuloSustituye)){
					for(int s=0; s<Utilidades.convertirAEntero(sustitutosNoPos.get("numRegistros")+""); s++){
						if(campo.getValor()==null||campo.getValor().isEmpty()){
							campo.setValor(sustitutosNoPos.get("codigo_"+s)+ConstantesBD.separadorSplit+sustitutosNoPos.get("numdossisequiv_"+s));
						}
						
						DtoParamOpcionesCamposJusNoPos opcion = new DtoParamOpcionesCamposJusNoPos();
						opcion.setValor(sustitutosNoPos.get("codigo_"+s)+ConstantesBD.separadorSplit+sustitutosNoPos.get("numdossisequiv_"+s));
						opcion.setOpcion(sustitutosNoPos.get("nombre_"+s).toString());
						campo.getOpciones().add(opcion);
					}
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosCantidadSustituye)){
					if (Utilidades.convertirAEntero(sustitutosNoPos.get("numRegistros").toString()) > 0)
						campo.setValor(sustitutosNoPos.get("numdossisequiv_0").toString());
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosMedico)){
					campo.setValor(usuario.getCodigoPersona()+"");
					campo.setEtiquetaValor(usuario.getNombreUsuario());
				}
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosEspecialidadMedico))
					campo.setValor(usuario.getEspecialidadesMedico()+"");
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosIdMedico))
					campo.setValor(usuario.getCodigoTipoIdentificacion()+" "+usuario.getNumeroIdentificacion());
			
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosRegistroMedico))
					campo.setValor(usuario.getNombreRegistroMedico());
				
				else if (campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosFirmaSelloMedico))
					campo.setValor(usuario.getFirmaDigital());
				
			}
		}
	}
	
	/**
	 * Tomar parametros del request
	 * @param forma
	 * @param request
	 */
	private void tomarParametrosDelRequest(FormatoJustInsNoposForm forma, HttpServletRequest request){	
		
		if(!UtilidadTexto.isEmpty(request.getParameter("articulo")))
			forma.getDtoParam().setCodigoArticulo(request.getParameter("articulo"));
		
		if(!UtilidadTexto.isEmpty(request.getParameter("cantidad")))
			forma.getDtoParam().setCantidadArticulo(request.getParameter("cantidad"));
		
		if(!UtilidadTexto.isEmpty(request.getParameter("solicitud")))
			forma.getDtoParam().setSolicitud(request.getParameter("solicitud"));
		
		if(!UtilidadTexto.isEmpty(request.getParameter("subcuenta")))
			forma.getDtoParam().setSubCuenta(request.getParameter("subcuenta"));		
		
		if(!UtilidadTexto.isEmpty(request.getParameter("ordenAmbulatoria")))
			forma.getDtoParam().setOrdenAmbulatoria(request.getParameter("ordenAmbulatoria"));
	}

}