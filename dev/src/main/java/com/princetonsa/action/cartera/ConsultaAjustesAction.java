
/*
 * Creado   9/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cartera.ConsultaAjustesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.ConsultaAjustes;
import com.princetonsa.pdf.AjustesPdf;

/**
 * Clase para manejar el workflow de 
 * consulta de ajustes empresa
 * @version 1.0, 9/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaAjustesAction extends Action 
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaAjustesAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
		Connection con = null;		
		try{
	    if(form instanceof ConsultaAjustesForm)
	    {
	            
		    //intentamos abrir una conexion con la fuente de datos 
			con = openDBConnection(con);
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			ConsultaAjustesForm conForm = (ConsultaAjustesForm)form;
			ConsultaAjustes mundoCon=	new ConsultaAjustes();	
			HttpSession sesion = request.getSession();			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			String estado=conForm.getEstado();
			logger.warn("estado->"+estado);
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConsultaAjustesAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezarConsulta") )
			{			    
				 conForm.reset();				 
				 mundoCon.reset();				 
				 this.cerrarConexion(con);
				 return mapping.findForward("paginaPrincipal");
			}	
			else if(estado.equals("generarConsulta"))
			{
			 return this.generarConsultaAjustes(con,conForm,mundoCon,mapping,usuario);   
			}
			else if(estado.equals("imprimirAjustes"))
			{
			 return this.imprimirAjustes(con,conForm,mundoCon,mapping,request,usuario);   
			}
			else if(estado.equals("volverConsulta"))
			{
			    this.cerrarConexion(con);
				return mapping.findForward("paginaPrincipal");   
			}
			else if(estado.equals("detalleAjuste"))
			{			    
			    return this.enviarADetalleFactura(con,mundoCon,conForm,mapping,usuario);
			}
			else if(estado.equals("imprimirDetalleAjuste"))
			{
				return this.imprimirDetalleAjuste(con,conForm,mundoCon,mapping,request,usuario);   
			}
			else if(estado.equals("detalleFacturas"))
			{
			    mundoCon.setRegSel(conForm.getRegSel());
			    mundoCon.setRegSelFact(conForm.getRegSelFact());
			    mundoCon.setMapaAjustes(conForm.getMapAjustes());
			    mundoCon.setInstitucion(usuario.getCodigoInstitucionInt());
			    if((conForm.getMapAjustes("esPorCxC_"+conForm.getRegSel())+"").equals("false"))
			    {
			        mundoCon.realizarConsultaDetalleAjustes(con,Double.parseDouble(conForm.getMapAjustes("codigo_ajuste_"+conForm.getRegSel())+""));
			        conForm.setMapAjustes(mundoCon.getMapaAjustes());
			    }		    
			    mundoCon.realizarConsultaDetalleFacturas(con);
			    conForm.setMapAjustes(mundoCon.getMapaAjustes());
			    this.cerrarConexion(con);
				return mapping.findForward("paginaDetalleFactura");
			}
			else if(estado.equals("volverDetalleAjuste"))
			{
			    this.cerrarConexion(con);
				return mapping.findForward("paginaDetalleAjuste");   
			}
			else if(estado.equals("volverAjustes"))
			{
			    this.cerrarConexion(con);
				return mapping.findForward("paginaAjustes");   
			}
			else if(estado.equals("ordenarColumna"))
			{
			    return this.accionOrdenarColumna(con,conForm,mapping);
			}
            else if(estado.equals("cargarListadoDetalleAsocios"))
            {
                mundoCon.setRegSel(conForm.getRegSel());
                mundoCon.setRegSelFact(conForm.getRegSelFact());
                mundoCon.setRegSelDetFact(conForm.getRegSelDetFact());
                mundoCon.setMapaAjustes(conForm.getMapAjustes());
                mundoCon.realizarConsultaDetalleFacturasAsocios(con);                
                conForm.setMapAjustes(mundoCon.getMapaAjustes());
                this.cerrarConexion(con);
                return mapping.findForward("paginaDetalleFacturaAsocio"); 
            }
            else if(estado.equals("volverDetalleFacturas"))
            {
                this.cerrarConexion(con);
                return mapping.findForward("paginaDetalleFactura");   
            }
            else if(estado.equals("ordenarDetalleFacturasAsocio"))
            {
                this.accionOrdenarMapaDetalleFacturasAsocio(conForm);
                this.cerrarConexion(con);
                return mapping.findForward("paginaDetalleFacturaAsocio");   
            }
	    }
	    else
		{
			logger.error("El form no es compatible con el form de ConsultaAjustesForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}	
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
        return null;	
	}
	
	private ActionForward imprimirDetalleAjuste(Connection con, ConsultaAjustesForm forma, ConsultaAjustes mundoCon, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		AjustesPdf.imprimirDetalleAjusteConsulta(con,ValoresPorDefecto.getFilePath()+ nombreArchivo,  forma.getMapAjustes().get("codigo_ajuste_"+forma.getRegSel())+"", forma.getMapAjustes(), usuario, request);
		UtilidadBD.closeConnection(con);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Consulta Ajustes");
		return mapping.findForward("abrirPdf");
	}

	/**
	 * 
	 * @param con
	 * @param conForm
	 * @param mundoCon
	 * @param mapping
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward imprimirAjustes(Connection con, ConsultaAjustesForm forma, ConsultaAjustes mundoCon, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		HashMap camposBusqueda=new HashMap();
		camposBusqueda.put("estado", forma.getCodEstado());
		camposBusqueda.put("tipoAjuste",forma.getCodTipoAjuste());
		camposBusqueda.put("ajusteInicial",forma.getNumAjusteI());
		camposBusqueda.put("ajusteFinal",forma.getNumAjusteF());
		camposBusqueda.put("fechaInicial",forma.getFechaInicial());
		camposBusqueda.put("fechaFinal",forma.getFechaFinal());
		camposBusqueda.put("concepto", forma.getCodConceptoAjuste());
		camposBusqueda.put("factura", forma.getNumFactura());
		camposBusqueda.put("convenio", forma.getCodConvenio());
		String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		AjustesPdf.imprimirListadoAjustes(con,ValoresPorDefecto.getFilePath()+ nombreArchivo, forma.getMapAjustes(), usuario,camposBusqueda, request);
		UtilidadBD.closeConnection(con);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Consulta Ajustes");
		return mapping.findForward("abrirPdf");
	}

	/**
	 * Metodo para realizar la consulta del detalle
	 * de el ajuste, y cuando el ajuste es por factura
	 * mostrar el detalle de la factura
     * @param con
     * @param mundoCon
     * @param conForm
     * @param mapping
     * @return
     */
    private ActionForward enviarADetalleFactura(Connection con, 
													            ConsultaAjustes mundoCon, 
													            ConsultaAjustesForm conForm, 
													            ActionMapping mapping,
													            UsuarioBasico usuario) 
    {
        mundoCon.setRegSel(conForm.getRegSel());
	    mundoCon.setMapaAjustes(conForm.getMapAjustes());
	    mundoCon.setInstitucion(usuario.getCodigoInstitucionInt());
	    mundoCon.realizarConsultaDetalleAjustes(con,Double.parseDouble(conForm.getMapAjustes("codigo_ajuste_"+conForm.getRegSel())+""));
	    conForm.setMapAjustes(mundoCon.getMapaAjustes());
	   	this.cerrarConexion(con);
	    return mapping.findForward("paginaDetalleAjuste");	     
    }

    /**
     * metodo para realizar la consulta de ajustes
     * @param con
     * @param conForm
     * @param mundoCon
     * @param mapping
     * @param request
     * @return
     */
    private ActionForward generarConsultaAjustes(Connection con, 
													            ConsultaAjustesForm conForm, 
													            ConsultaAjustes mundoCon, 
													            ActionMapping mapping,													            
													            UsuarioBasico usuario) 
    {
        mundoCon.setInstitucion(usuario.getCodigoInstitucionInt());
        if(!conForm.getCodEstado().equals(""))
            mundoCon.setEstado(Integer.parseInt(conForm.getCodEstado()));
        if(!conForm.getCodTipoAjuste().equals(""))
            mundoCon.setCodTipoAjuste(Integer.parseInt(conForm.getCodTipoAjuste()));
        mundoCon.setAjusteInicial(conForm.getNumAjusteI());
        mundoCon.setAjusteFinal(conForm.getNumAjusteF());
        mundoCon.setFechaInicial(UtilidadFecha.conversionFormatoFechaABD(conForm.getFechaInicial()));
        mundoCon.setFechaFinal(UtilidadFecha.conversionFormatoFechaABD(conForm.getFechaFinal()));
        mundoCon.setConcepto(conForm.getCodConceptoAjuste());        
        if(!conForm.getNumFactura().equals(""))
            mundoCon.setFactura(Integer.parseInt(conForm.getNumFactura()));
        if(!conForm.getCodConvenio().equals(""))
            mundoCon.setConvenio(Integer.parseInt(conForm.getCodConvenio()));
        mundoCon.realizarConsultaAjustes(con);
        conForm.setMapAjustes(mundoCon.getMapaAjustes());         
        this.cerrarConexion(con);
		return mapping.findForward("paginaAjustes");
    }
    
    /**
	 * Metodo para ordenar el hashmap que 
	 * contiene el resultado de la consulta.
     * @param con
     * @param ConsultaAjustesForm
     * @param mapping 
     * @return
     */
    private ActionForward accionOrdenarColumna(Connection con, 
            											ConsultaAjustesForm conForm, 
											            ActionMapping mapping) 
    {         
        int numRegTemp=0;
        if(conForm.getTipoOrdenamiento().equals("ajustes"))
        {
	        numRegTemp=Integer.parseInt(conForm.getMapAjustes("numReg")+"");        
	        String[] indices={
					            "codigo_ajuste_", 
					            "tipo_ajuste_", 
					            "consecutivo_ajuste_", 
					            "reversado_",
					            "nombre_estado_",
					            "fecha_elaboracion_",
					            "hora_elaboracion_",
					            "valor_ajuste_",
					            "cuenta_cobro_",
					            "usuario_elaboracion_",
					            "castigo_cartera_",
					            "observaciones_",
					            "nombre_tipo_ajuste_corto_",
					            "fecha_aprobacion_",
					            "usuario_aprobacion_",
					            "nombre_convenio_",
					            "consecutivo_factura_",
					            "esPorCxC_",
					            "detalleAjuste_",
					            "detalleFactura_",
					            "nombreempresa_",
					            "codigo_estado_"
		            		};
                
	        conForm.setMapAjustes(Listado.ordenarMapa(indices, 
                											conForm.getPatronOrdenar(),
                											conForm.getUltimoPatron(),
                											conForm.getMapAjustes(),
                											Integer.parseInt(conForm.getMapAjustes("numReg")+"")));
	        conForm.setMapAjustes("numReg",numRegTemp+"");
	        conForm.setUltimoPatron(conForm.getPatronOrdenar());
	        this.cerrarConexion(con);
			return mapping.findForward("paginaAjustes");  
        }
        else if(conForm.getTipoOrdenamiento().equals("detAjustes"))
        {
            HashMap facturas=new HashMap();
		    facturas=(HashMap)(conForm.getMapAjustes("detalleAjuste_"+conForm.getRegSel()));
		    if(!facturas.isEmpty())
		    {
		        numRegTemp=Integer.parseInt(facturas.get("numReg")+""); 
		        String[] indices={
			            "factura_", 
			            "consecutivo_factura_", 
			            "nombre_convenio_", 
			            "metodo_ajuste_",
			            "nombre_metodo_ajuste_",
			            "valor_ajuste_",
			            "concepto_ajuste_",
			            "tipo_factura_sistema_",
			            "codigocentroatencion_",
			            "nombrecentroatencion_"
            		};  
		        facturas.putAll(Listado.ordenarMapa(indices,
															conForm.getPatronOrdenar(),
															conForm.getUltimoPatron(),
															facturas,
															Integer.parseInt(facturas.get("numReg")+"")));
		        
		        facturas.put("numReg",numRegTemp+"");
		        conForm.setMapAjustes("detalleAjuste_"+conForm.getRegSel(),facturas);		        
		        conForm.setUltimoPatron(conForm.getPatronOrdenar());
		        this.cerrarConexion(con);
			    return mapping.findForward("paginaDetalleAjuste");	   
		    }
        } 
        else if(conForm.getTipoOrdenamiento().equals("detFactura"))
        {
            int reg=0;
            HashMap detFacturas=new HashMap();
            if((conForm.getMapAjustes("esPorCxC_"+conForm.getRegSel())+"").equals("true"))            
                reg=conForm.getRegSelFact();
            detFacturas=(HashMap)conForm.getMapAjustes("detalleFactura_"+reg);
            
		    if(!detFacturas.isEmpty())
		    {
		        numRegTemp=Integer.parseInt(detFacturas.get("numReg")+""); 
		        String[] indices={
			            "solicitud_",
			            "consolicitud_",
			            "nombre_servicio_articulo_", 
			            "nombre_medico_", 
			            "nombre_pool_",
			            "valor_ajuste_",
			            "nombre_concepto_ajuste_",
			            "esServicio_",
			            "centro_costo_solicitante_"
            		};  
		        detFacturas.putAll(Listado.ordenarMapa(indices,
																conForm.getPatronOrdenar(),
																conForm.getUltimoPatron(),
																detFacturas,
																Integer.parseInt(detFacturas.get("numReg")+"")));
		        detFacturas.put("numReg",numRegTemp+"");
		        conForm.setMapAjustes("detalleFactura_"+reg,detFacturas);		        
		        conForm.setUltimoPatron(conForm.getPatronOrdenar());
		        this.cerrarConexion(con);
			    return mapping.findForward("paginaDetalleFactura");
		    }
        }
        return null;
    }
    /**
     * metodo para ordenar el detalle de 
     * facturas asocio
     * @param con
     * @param forma
     * @param mapping
     * @return     
     */ 
    private void accionOrdenarMapaDetalleFacturasAsocio(ConsultaAjustesForm forma) 
    {         
        HashMap detFacturas=new HashMap();      
        detFacturas=(HashMap)forma.getMapAjustes("detalleFacturaAsocio_"+forma.getRegSelDetFact());
        Object temp=detFacturas.get("numRegistros");
                
        String[] indices={
                            "codigo_", 
                            "asocios_", 
                            "nom_almacen_despacha_", 
                            "medico_",
                            "pool_",   
                            "valor_ajuste_",    
                            "concepto_",
                            "nombre_concepto_ajuste_"
                          };

        detFacturas=(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),detFacturas,Integer.parseInt(temp+"")));        
        detFacturas.put("numRegistros",temp);
        forma.setMapAjustes("detalleFacturaAsocio_"+forma.getRegSelDetFact(),detFacturas);
        forma.setUltimoPatron(forma.getPatronOrdenar());        
    }
    /**
	 * 
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{

		if(con != null)
		return con;
					
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
			}
			catch(Exception e)
			{
			    logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
				return null;
			}
					
			return con;
	}
		 
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try{
	        if (con!=null&&!con.isClosed())
	        {
	        	UtilidadBD.closeConnection(con);
	        }
	    }
	    catch(Exception e){
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
