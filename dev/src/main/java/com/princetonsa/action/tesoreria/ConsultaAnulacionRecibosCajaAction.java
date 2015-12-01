
/*
 * Creado   24/03/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.tesoreria.ConsultaAnulacionRecibosCajaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.AnulacionRecibosCaja;
import com.princetonsa.mundo.tesoreria.ConsultaRecibosCaja;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.servicio.impl.administracion.CentroAtencionServicio;
 

/**
 * 
 * Manejo del work flow
 *
 * @version 1.0, 24/03/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaAnulacionRecibosCajaAction extends Action 
{
    /**
     * Para hacer logs de debug / warn / error de esta funcionalidad.
     */
    private Logger logger = Logger.getLogger(ConsultaAnulacionRecibosCajaAction.class);
    /**
     * Método execute del action
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward execute(   ActionMapping mapping,  
                                                            ActionForm form, 
                                                            HttpServletRequest request, 
                                                            HttpServletResponse response) throws Exception
                                                            {

    	Connection con = null;
    	try{
    		if( form instanceof ConsultaAnulacionRecibosCajaForm )
    		{

    			//intentamos abrir una conexion con la fuente de datos 
    			con = UtilidadBD.abrirConexion(); 
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			} 
    			ConsultaAnulacionRecibosCajaForm forma=(ConsultaAnulacionRecibosCajaForm)form;
    			ConsultaRecibosCaja mundo = new ConsultaRecibosCaja();
    			HttpSession sesion = request.getSession();      
    			UsuarioBasico usuario = null;
    			usuario = Utilidades.getUsuarioBasicoSesion(sesion);
    			String estado = forma.getEstado();
    			logger.warn("estado [ConsultaReciboCajaAction.java]-->"+estado);
    			if(estado == null)
    			{
    				logger.warn("Estado no valido dentro del flujo de ConsultaAnulacionRecibosCajaAction (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if (estado.equals("empezar"))
    			{
    				forma.reset();
    				forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
    				mundo.reset();
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaPrincipal");
    			}
    			else if (estado.equals("realizarBusqueda"))
    			{
    				HashMap vo=new HashMap();
    				vo.put("numero_anulacion_inicial",forma.getNumAnulacionInicial());
    				vo.put("numero_anulacion_final",forma.getNumAnulacionFinal());
    				vo.put("fecha_anulacion_inicial",forma.getFechaAnulacionInicial());
    				vo.put("fecha_anulacion_final",forma.getFechaAnulacionFinal());
    				vo.put("motivo_anulacion",forma.getMotivoAnulacion());
    				vo.put("usuario_anulacion",forma.getUsuarioAnulacion());
    				vo.put("numero_recibo_caja",forma.getNumReciboCaja());
    				vo.put("institucion",usuario.getCodigoInstitucionInt());
    				vo.put("codigo_centro_atencion", forma.getCodigoCentroAtencion());
    				forma.setMapaAnulacionRC(mundo.ejecutarBusquedaAvanzadaAnulacionesRC(con, vo));

    				if(forma.getMapaAnulacionRC().containsKey("numRegistros"))
    				{	
    					//para poder realizar bien el ordenamiento entonces uni fecha - hora en un solo atributo
    					for(int w=0; w<Integer.parseInt(forma.getMapaAnulacionRC("numRegistros").toString()); w++)
    					{	
    						forma.setMapaAnulacionRC("fechaHora_"+w, UtilidadFecha.conversionFormatoFechaAAp(forma.getMapaAnulacionRC("fecha_"+w).toString()) +" - "+forma.getMapaAnulacionRC("hora_"+w));
    						forma.setMapaAnulacionRC("fechaHoraFormatoBD_"+w, forma.getMapaAnulacionRC("fecha_"+w).toString() +" - "+forma.getMapaAnulacionRC("hora_"+w));
    					}
    				}    
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("listadoAnulacionRecibosCaja");
    			}
    			else if (estado.equals("cargarDetalle"))
    			{
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("detalleAnulacionRecibosCaja");
    			}
    			else if (estado.equals("ordenarListado"))
    			{
    				this.accionOredenar(forma);
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("listadoAnulacionRecibosCaja");
    			}            
    			else if (estado.equals("generarReporteListado"))
    			{       
    				DesignEngineApi comp;
    				InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    				comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"tesoreria/","ConsultaAnulacionRecibosCaja.rptdesign");
    				comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
    				comp.insertGridHeaderOfMasterPage(0,1,1,4);
    				Vector v=new Vector();
    				v.add(ins.getRazonSocial());
    				v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
    				if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
    					v.add("Actividad Económica: "+ins.getActividadEconomica());
    				v.add(ins.getDireccion()+"          "+ins.getTelefono());
    				comp.insertLabelInGridOfMasterPage(0,1,v);
    				String param="";
    				param+=!forma.getNumAnulacionInicial().equals("")?" No. Anulación Inicial: "+forma.getNumAnulacionInicial():"";
    				param+=!forma.getNumAnulacionFinal().equals("")?" No. Anulación Final: "+forma.getNumAnulacionFinal():"";
    				param+=!forma.getFechaAnulacionInicial().equals("")?" Fecha Anulación Inicial: "+forma.getFechaAnulacionInicial():"";
    				param+=!forma.getFechaAnulacionFinal().equals("")?" Fecha Anulación Final: "+forma.getFechaAnulacionFinal():"";
    				param+=!forma.getNumReciboCaja().equals("")?" No. Recibo Caja: "+forma.getNumReciboCaja():"";
    				param+=!forma.getDescMotivoAnulacion().equals("")?" Motivo Anulación: "+forma.getDescMotivoAnulacion():"";
    				param+=!forma.getDescUsuarioAnulacion().equals("")?" Usuario Anulación: "+forma.getDescUsuarioAnulacion():"";
    				comp.insertLabelInGridPpalOfHeader(1,0,param);
    				comp.obtenerComponentesDataSet("listadoAnulacionRC");            
    				String oldQuery=comp.obtenerQueryDataSet();     
    				forma.getMapaAnulacionRC("consultaWhere");                
    				String newQuery=oldQuery+forma.getMapaAnulacionRC("consultaWhere")+"";
    				comp.modificarQueryDataSet(newQuery);                
    				//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
    				comp.lowerAliasDataSet();
    				String newPathReport = comp.saveReport1(false);
    				if(!newPathReport.equals(""))
    				{
    					request.setAttribute("isOpenReport", "true");
    					request.setAttribute("newPathReport", newPathReport);
    				}            
    				//por ultimo se modifica la conexion a BD
    				comp.updateJDBCParameters(newPathReport);
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("listadoAnulacionRecibosCaja");
    			} 
    			else if (estado.equals("generarReporteDetalle"))
    			{
    				try{
    					HibernateUtil.beginTransaction();
	    				DesignEngineApi comp;  
	    		        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
	    		        
	    		        //ENCABEZADO------------------------
	    		        Vector v=new Vector();
	    		        v.add(ins.getRazonSocial());
	    		        if (ins.getTipoIdentificacion().equals("NI")) {
	    		        	v.add("NIT: "+ins.getNit());
	    		        } else {
	    		        	v.add(ins.getTipoIdentificacion()+": "+ins.getNit());
	    		        }  
	    		        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
	    		            v.add("Actividad Económica: "+ins.getActividadEconomica());
	    		        CentroAtencionServicio centroAtencionServicio = new CentroAtencionServicio();
	    				CentroAtencion centroAtencion = new CentroAtencion();
	    				centroAtencion = centroAtencionServicio.buscarPorCodigoPK(usuario.getCodigoCentroAtencion());
	    		        //v.add(ins.getDireccion()+"              "+ins.getTelefono());
	    				v.add(centroAtencion.getCodigo().trim()+ " - "+centroAtencion.getDescripcion());
	    				v.add(centroAtencion.getDireccion()+(centroAtencion.getTelefono()!=null?" Tels: "+centroAtencion.getTelefono():""));
	    		        //-----------------------------------
	    		        
	    				comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"tesoreria/","ConsultaAnulacionReciboCajaFormatoResumen.rptdesign");
	    		        comp.obtenerComponentesDataSet("listadoAnulacionRC");
	    				
	    				if (ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) 
	    				{
	    					comp.insertImageHeaderOfMasterPageCenter(0, 2, ins.getLogoReportes());
	    				} else if (ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
	    					comp.insertImageHeaderOfMasterPageCenter(0, 0,ins.getLogoReportes());
	    				}
	    				
	    				comp.insertGridHeaderOfMasterPage(0,1,1,5);
	    			        
	    		        comp.insertLabelInGridOfMasterPage(0,1,v);
	    		        for(int t = 0 ; t < v.size() ; t++)
	    				{
	    		        	comp.fontWeightCellGridHeaderOfMasterPage(0,1,t,0,DesignChoiceConstants.FONT_WEIGHT_BOLD);
	    				}
	    			
	    				UsuariosDelegate usu= new UsuariosDelegate();
	    				Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
	    				comp.insertLabelInGridPpalOfFooter(0, 1, usuarioCompleto.getPersonas().getPrimerNombre()
	    						+" "+usuarioCompleto.getPersonas().getPrimerApellido()
	    						+" ("+usuarioCompleto.getLogin()+")"
	    						);
	    		        
	    		        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	    		      	comp.lowerAliasDataSet();
	    				String newPathReport = comp.saveReport1(false);
	    		        if(!newPathReport.equals(""))
	    		        {
	    		            request.setAttribute("isOpenReport", "true");
	    		            request.setAttribute("newPathReport", newPathReport);
	    		        }            
	//    		          por ultimo se modifica la conexion a BD
	    		        comp.updateJDBCParameters(newPathReport);
	    				HibernateUtil.endTransaction();
    				}
    				catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
    				finally{
    					UtilidadBD.cerrarConexion(con);
    				}
    				return mapping.findForward("detalleAnulacionRecibosCaja");
    			}
    			else if (estado.equals("volverListado"))
    			{                 
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("listadoAnulacionRecibosCaja");
    			}
    		}
    		else
    		{
    			logger.error("El form no es compatible con el form de ConsultaAnulacionRecibosCajaForm");
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

    /**
     * @param forma
     */
    private void accionOredenar(ConsultaAnulacionRecibosCajaForm forma)
    {
        String[] indices={
                            "numero_recibo_caja_",
                            "numeroanulacion_",
                            "loginusuario_",
                            "nomusuario_",
                            "fecha_",
                            "hora_",
                            "codmotivo_",
                            "descmotivoanulacion_",
                            "observaciones_",
                            "valortotalrc_",
                            "fechaelaboracionrc_",
                            "horaelaboracionrc_",
                            "recibido_de_",
                            "fechaHora_",
                            "fechaHoraFormatoBD_",
                            "nombrecentroatencion_"
                         };
        if(forma.getMapaAnulacionRC().containsKey("numRegistros"))
        {
	        int numReg=Integer.parseInt(forma.getMapaAnulacionRC("numRegistros")+"");
	        String consultaWhere=forma.getMapaAnulacionRC("consultaWhere").toString();
	        forma.setMapaAnulacionRC(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaAnulacionRC(),numReg));
	        /*if(forma.getPatronOrdenar().equals("fecha_"))
	        {
	            forma.setPatronOrdenar("hora_");
	            forma.setMapaAnulacionRC(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaAnulacionRC(),numReg));
	        }*/
	        forma.setMapaAnulacionRC("numRegistros", numReg+"");
	        forma.setMapaAnulacionRC("consultaWhere", consultaWhere);
	        forma.setUltimoPatron(forma.getPatronOrdenar());
        }    
    }
}
