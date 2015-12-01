package com.princetonsa.action.facturacion;

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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.ImpresionSoportesFacturasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ImpresionSoportesFacturas;
import com.princetonsa.mundo.facturacion.SoportesFacturas;

/**
 * Clase para el manejo de la parametrizacion 
 * de impresion de soportes de facturas
 * Date: 2009-02-09
 * @author jfhernandez@princetonsa.com
 */
public class ImpresionSoportesFacturasAction extends Action 
{
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ImpresionSoportesFacturasAction.class);
	
	/**
	 * Metodo execute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof ImpresionSoportesFacturasForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			ImpresionSoportesFacturasForm forma = (ImpresionSoportesFacturasForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la impresion de soportes de facturas (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("paginaError");
    			}
    			/*------------------------------
    			 * 		ESTADO > empezar
    			 *-------------------------------*/
    			if(estado.equals("empezar"))
    				return accionEmpezar(con, mapping, forma, request, usuario);

    			/*------------------------------
    			 * 		ESTADO > listarFacturas
    			 *-------------------------------*/
    			if(estado.equals("listarFacturas"))
    				return accionListarFacturas(con, mapping, forma, request, usuario);

    			/*------------------------------
    			 * 		ESTADO > listarTiposSoportes
    			 *-------------------------------*/
    			if(estado.equals("listarTiposSoportes"))
    				return accionListarTiposSoportes(con, mapping, forma, request, usuario);

    			/*------------------------------
    			 * 		ESTADO > imprimir
    			 *-------------------------------*/
    			if(estado.equals("imprimir"))
    				return accionImprimir(con, mapping, forma, request, usuario,paciente);

    			/*------------------------------
    			 * 		ESTADO > ordenar
    			 *-------------------------------*/
    			if(estado.equals("ordenar"))
    				return accionOrdenar(con, mapping, forma, request, usuario);

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
     * Ordenar mapa del listado de facturas
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionOrdenar(Connection con, ActionMapping mapping,ImpresionSoportesFacturasForm forma, HttpServletRequest request, UsuarioBasico usuario) {
    	
    	int numReg = Integer.parseInt(forma.getListadoFacturasMap("numRegistros")+"");
    	String[] indicesListadoFacturasMap = {"consecutivo_","fecha_","codviaingreso_","nomviaingreso_","codconvenio_","nomconvenio_","codpaciente_","hora_",""};
    	forma.setListadoFacturasMap(Listado.ordenarMapaRompimiento(indicesListadoFacturasMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoFacturasMap(), "codconvenio_"));
    	forma.setUltimoPatron(forma.getPatronOrdenar());
    	forma.setListadoFacturasMap("numRegistros",numReg+"");
    
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("listarFacturas");
	}
    /**
     * Imprimir los tipos de soportes seleccionados
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimir(Connection con, ActionMapping mapping, ImpresionSoportesFacturasForm forma, HttpServletRequest request, UsuarioBasico usuario,PersonaBasica paciente) {
    	
    	//Cargar las solicitudes asociadas a la factura
    	forma.setSolicitudesFacturaMap(Utilidades.obtenerSolicitudesFacturas(con, Utilidades.convertirAEntero(forma.getListadoFacturasMap("codfactura_"+forma.getPosMap()).toString())));
    	Utilidades.imprimirMapa(forma.getSolicitudesFacturaMap());
    	
    	
    	// Cargar paciente en sesión
    	paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getListadoFacturasMap("codpaciente_"+forma.getPosMap()).toString())); 
    	UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
    	
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoTiposSoportes");
	}

	/**
     * Listar los tipos de reportes
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionListarTiposSoportes(Connection con, ActionMapping mapping, ImpresionSoportesFacturasForm forma, HttpServletRequest request, UsuarioBasico usuario) {
    	forma.setTiposSoportesMap(organizarTiposSoportesMap(SoportesFacturas.obtenerTiposSoporteXConvenio(con, usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(forma.getListadoFacturasMap().get("codviaingreso_"+forma.getPosMap())+""), Utilidades.convertirAEntero(forma.getListadoFacturasMap().get("codconvenio_"+forma.getPosMap())+""))));
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoTiposSoportes");
	}

    /**
     * Metodo para organizar las llaves del mapa
     * @param obtenerTiposSoporteXConvenio
     * @return
     */
	private HashMap<String, Object> organizarTiposSoportesMap(HashMap<String, Object> tiposSoporteXConvenio) {
		
		for(int i=0; i<Utilidades.convertirAEntero(tiposSoporteXConvenio.get("numRegistros").toString()); i++){
			int tipoSoporteHijo = ConstantesBD.codigoNuncaValido; 
			if (!tiposSoporteXConvenio.get("codpadre_"+i).toString().equals("")){
				tipoSoporteHijo = obtenerTipoSoporteHijo(tiposSoporteXConvenio, tiposSoporteXConvenio.get("codpadre_"+i).toString());
				if(tipoSoporteHijo==ConstantesBD.codigoNuncaValido){
					tiposSoporteXConvenio.put("mostraropcioneshijas_"+tiposSoporteXConvenio.get("codpadre_"+i), false);
					tiposSoporteXConvenio.put("activohijo_"+tiposSoporteXConvenio.get("codpadre_"+i), "");
				} else {
					tiposSoporteXConvenio.put("mostraropcioneshijas_"+tiposSoporteXConvenio.get("codpadre_"+i), true);
					tiposSoporteXConvenio.put("activohijo_"+tiposSoporteXConvenio.get("codpadre_"+i), tipoSoporteHijo);
				}
			}
		}
		
		return tiposSoporteXConvenio;
	}

	/**
	 * Método para obtener el hijo activo de un tipo de soporte padre
	 * @param tiposSoporteXConvenio
	 * @param string
	 * @return
	 */
	private int obtenerTipoSoporteHijo(HashMap<String, Object> tiposSoporteXConvenio, String codPadre) {
		int hijoActivo = ConstantesBD.codigoNuncaValido;
		
		for(int i=0; i<Utilidades.convertirAEntero(tiposSoporteXConvenio.get("numRegistros").toString()); i++){
			if (!tiposSoporteXConvenio.get("codpadre_"+i).toString().equals("") && tiposSoporteXConvenio.get("codpadre_"+i).toString().equals(codPadre) && UtilidadTexto.getBoolean(tiposSoporteXConvenio.get("activo_"+i)+"")){
				hijoActivo = Utilidades.convertirAEntero(tiposSoporteXConvenio.get("codtiposoporte_"+i).toString());
			}
		}	
		
		return hijoActivo;
	}



	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, ImpresionSoportesFacturasForm forma, HttpServletRequest request, UsuarioBasico usuario) {
		forma.reset();
		
		// Cargar el contenido de los campos de selección de las vias de ingreso y los convenios respectivamente
		forma.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoAmbulatorios+","+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
    
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionListarFacturas (Connection con, ActionMapping mapping, ImpresionSoportesFacturasForm forma, HttpServletRequest request, UsuarioBasico usuario) {
		Utilidades.imprimirMapa(forma.getFiltrosMap());
		forma.setListadoFacturasMap(ImpresionSoportesFacturas.listarImprimir(con,forma.getFiltrosMap(), usuario.getCodigoInstitucionInt(), usuario.getCodigoInstitucion()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listarFacturas");
	}
}