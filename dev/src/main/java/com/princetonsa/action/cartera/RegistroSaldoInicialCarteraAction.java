
/*
 * Creado   11/06/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.cartera;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cartera.RegistroSaldoInicialCarteraForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.CuentasCobro;
import com.princetonsa.mundo.cartera.RegistroSaldoInicialCartera;
import com.princetonsa.pdf.CuentasCobroPdf;

/**
 * Clase para manejar el registro de cuentas de cobro 
 * para el módulo de cartera que constituyen las 
 * cuentas de cobro de saldos iniciales y relacionar
 * a cada una de estas cuentas de cobro una selección
 * de facturas del sistema, permitiendo después de 
 * seleccionar realizar modificación en sus valores cartera.
 *
 * @version 1.0, 11/06/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class RegistroSaldoInicialCarteraAction extends Action 
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RegistroSaldoInicialCarteraAction.class);
	
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
	    if(form instanceof RegistroSaldoInicialCarteraForm )
	    {
	        
	        con = openDBConnection(con); 
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
	        RegistroSaldoInicialCarteraForm registroForm = (RegistroSaldoInicialCarteraForm)form; 
	        HttpSession sesion = request.getSession();			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			
			RegistroSaldoInicialCartera mundoRegistro = new RegistroSaldoInicialCartera(); 
			String estado = registroForm.getEstado();
			logger.warn("[RegistroSaldoInicialCarteraAction] estado->"+estado);
		    //intentamos abrir una conexion con la fuente de datos 
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de RegistroSaldoInicialCarteraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			if(ValoresPorDefecto.getTopeConsecutivoCxCSaldoI(usuario.getCodigoInstitucionInt()).trim().equals(""))
			{
				request.setAttribute("codigoDescripcionError", "error.noExisteTopeConsecutivoCuentaCobro");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			if(ValoresPorDefecto.getFechaCorteSaldoInicialC(usuario.getCodigoInstitucionInt()).trim().equals(""))
			{
				request.setAttribute("codigoDescripcionError", "error.noDefinidaFechaCorteSaldoInicial");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}			
			if(estado.equals("empezarRegistro"))
			{
			    registroForm.resetRegistro();
			    registroForm.reset(); 
			    registroForm.setInstitucion(usuario.getCodigoInstitucionInt());
			    registroForm.setAccion("registro");
			    this.cerrarConexion(con);
			    ValoresPorDefecto.cargarValoresIniciales(con);
				return mapping.findForward("paginaRegistro"); 
			}
			if(estado.equals("continuarGeneracion"))
			{
			    return this.continuarGeneracion (con,request,mundoRegistro,registroForm,mapping,usuario);			   
			}
			else if(estado.equals("seleccionarTodos"))
			{
				this.cerrarConexion(con);
				return accionSeleccionarTodasFacturas(response,registroForm);
			}
			else if(estado.equals("generar"))
			{
				return accionGenerar(con,response,mundoRegistro,registroForm,usuario,request,mapping);
			}
			else if(estado.equals("resumen"))
			{
				accionResumen(con,mundoRegistro,registroForm,usuario);
				this.cerrarConexion(con);
				return mapping.findForward("resumenIngreso");
			}
			else if(estado.equals("detalleResumen"))
			{
				cerrarConexion(con);
				return mapping.findForward("resumenDetalle");
			}
			else if(estado.equals("imprimir"))
			{
				return this.accionImprimir(mapping, request, con, mundoRegistro,registroForm, usuario);
			}	
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.cerrarConexion(con);
				response.sendRedirect(registroForm.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("nuevaFact"))
			{
				return this.agregarFactura(con,request, registroForm,response);
			}	
			else if(estado.equals("eliminarFact"))
			{
				return this.eliminarFactura(con,mapping,registroForm,request,response);
			}
			else if(estado.equals("volver"))
			{
			    this.cerrarConexion(con);
			    if(registroForm.getAccion().equals("activarCentinelaIngreso"))
			    	response.sendRedirect("registroSaldoInicialCartera.jsp");
			    else if(registroForm.getAccion().equals("activarCentinelaModificacion"))
			    	response.sendRedirect("modificacionSaldoInicial.jsp");
			    return null;
			    //return mapping.findForward("paginaRegistro");  
			}
			else if (estado.equals("empezarModificacion"))
			{
			    registroForm.resetRegistro();
			    registroForm.reset();
			    registroForm.setInstitucion(usuario.getCodigoInstitucionInt());
			    registroForm.setAccion("modificar");
			    this.cerrarConexion(con);
				return mapping.findForward("paginaModificacion"); 
			}
			else if(estado.equals("continuarModificacion"))
			{
			  registroForm.setNumCuentaCobro(Double.parseDouble(registroForm.getNumCuentaCobroStr()));//el numCxC empleado en Cuentas de cobro es double, 
			  																						//y en Registro Saldo Inicial es Str, por ello se pasa a double
			  return liberarRelacionarFacturas (con,mapping,registroForm,usuario);		
			}
			else if(estado.equals("actualizarPaginaModificacion"))
			{
			    this.cerrarConexion(con);
			    return mapping.findForward("paginaModificacion"); 
			}
			else if (estado.equals("modificarSaldoInicial"))
			{
			    return this.guardarSalirModificaciones (con,mapping,registroForm,usuario,request,mundoRegistro);
			}
	    }
	    else
		{
			logger.error("El form no es compatible con el form de RegistroSaldoInicialCartera");
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
	 * @param response
	 * @param registroForm
	 * @return
	 */
	private ActionForward accionSeleccionarTodasFacturas(HttpServletResponse response, RegistroSaldoInicialCarteraForm registroForm) 
	{
		try 
		{
		    double sumaTotalCartera = 0,totalAjustes=0;
			for(int i=0;i<Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"");i++)
			{				
				if(registroForm.getAccion().equals("registro"))
				    registroForm.setFacturasCxC("seleccionado_"+i,registroForm.isSelecTodas()+"");	
				else if(registroForm.getAccion().equals("modificar"))
				{
				    if(registroForm.isSelecTodas())
				        registroForm.setFacturasCxC("activarCheckBox_"+i,"on");
				    else
				        registroForm.setFacturasCxC("activarCheckBox_"+i,"off"); 
				    
				}
				
				sumaTotalCartera = sumaTotalCartera + Double.parseDouble(registroForm.getFacturaCxC("valor_cartera_"+i)+"");
				totalAjustes=totalAjustes + Double.parseDouble(registroForm.getFacturaCxC("valor_ajustes_"+i)+"");
			}
			if(registroForm.isSelecTodas())
			{	
				//registroForm.setValorTotalCartera(Double.parseDouble((registroForm.getFacturaCxC("valor_total_cartera")+"")!=null&&!(registroForm.getFacturaCxC("valor_total_cartera")+"").equals("null")?(registroForm.getFacturaCxC("valor_total_cartera")+""):"0"));
			    registroForm.setValorTotalCartera(sumaTotalCartera);
				//registroForm.setValorTotalAjustes(Double.parseDouble((registroForm.getFacturaCxC("valor_total_ajustes")+"")!=null&&!(registroForm.getFacturaCxC("valor_total_ajustes")+"").equals("null")?(registroForm.getFacturaCxC("valor_total_ajustes")+""):"0"));
			    registroForm.setValorTotalAjustes(totalAjustes);
				registroForm.setNumeroFacturasSeleccionadas(Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+""));
			}
			else
			{							    
				registroForm.setValorTotalAjustes(0);
				registroForm.setValorTotalCartera(0);
				registroForm.setNumeroFacturasSeleccionadas(0);
			}
			//evaluar si el url es generado por el pager sino eviarlo a detalleFacturas.jsp
			if(registroForm.getAccion().equals("registro"))
			    response.sendRedirect(registroForm.getLinkSiguiente().indexOf("pager")<0?"detalleFacturas.jsp":registroForm.getLinkSiguiente());
			else if(registroForm.getAccion().equals("modificar"))
			    response.sendRedirect(registroForm.getLinkSiguiente().indexOf("pager")<0?"detalleFacturasModificacion.jsp":registroForm.getLinkSiguiente());
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;	
	}

	
	/**
	 * Metodo para ingresar una nueva factura
	 * externa.
     * @param mapping ActionMapping
     * @param request HttpServletRequest     
     * @param registroForm RegistroSaldoInicialCarteraForm 
     * @author jarloc 
     * @return 
     */
    private ActionForward agregarFactura(Connection con,           											
													    HttpServletRequest request,													                        
													    RegistroSaldoInicialCarteraForm registroForm,
													    HttpServletResponse response) 
    {
       int pos = Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"");
       int numFactEx = registroForm.getNumFactExternas();
       int numFactSelect = registroForm.getNumeroFacturasSeleccionadas();
       
       registroForm.setFacturasCxC("codigo_"+pos,0+"");
       registroForm.setFacturasCxC("consecutivo_factura_"+pos,0+"");
       registroForm.setFacturasCxC("fecha_"+pos,"");
       registroForm.setFacturasCxC("via_ingreso_"+pos,-1+"");
       registroForm.setFacturasCxC("nombre_viaingreso_"+pos,"Seleccione");
       registroForm.setFacturasCxC("valor_cartera_"+pos,0+"");//valor cartera
       registroForm.setFacturasCxC("valor_ajustes_"+pos,0+"");
       if(registroForm.getAccion().equals("registro"))
           registroForm.setFacturasCxC("seleccionado_"+pos,"true");
       else if(registroForm.getAccion().equals("modificar"))
           registroForm.setFacturasCxC("activarCheckBox_"+pos,"on");
       registroForm.setFacturasCxC("tipo_"+pos,"MEM");
       
       pos ++;
       numFactEx ++;
       numFactSelect++;
       registroForm.setFacturasCxC("numFacturas",pos+"");
       registroForm.setNumFactExternas(numFactEx);
       registroForm.setNumeroFacturasSeleccionadas(numFactSelect);
       
       this.cerrarConexion(con);
       if(registroForm.getAccion().equals("registro"))
           return this.redireccion(con,registroForm,response,request,"detalleFacturas.jsp");
       else if(registroForm.getAccion().equals("modificar"))
           return this.redireccion(con,registroForm,response,request,"detalleFacturasModificacion.jsp");
       else
           return null;
    }

    /**
	 * Metodo para eliminar una factura, cuando es 
	 * ingresada como nueva y no pertenece al sistema.
     * @param con Connection, conexión con la fuente de datos
     * @param mapping ActionMapping
     * @param registroForm RegistroSaldoInicialCarteraForm
     * @param response
     * @param request
     * @return findForward ActionForward
     */
    private ActionForward eliminarFactura(Connection con, 
            ActionMapping mapping, 
            RegistroSaldoInicialCarteraForm registroForm, HttpServletRequest request, HttpServletResponse response) 
    {
        int nuevaPos=-1,posDel=-1;
        int esUltimo=Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"")-1;
        
        posDel = registroForm.getPosRegistro();        
        
        registroForm.getFacturasCxC().remove("codigo_"+posDel);
        registroForm.getFacturasCxC().remove("consecutivo_factura_"+posDel);
        registroForm.getFacturasCxC().remove("fecha_"+posDel);
        registroForm.getFacturasCxC().remove("via_ingreso_"+posDel);
        registroForm.getFacturasCxC().remove("nombre_viaingreso_"+posDel);
        registroForm.getFacturasCxC().remove("valor_cartera_"+posDel);
        registroForm.getFacturasCxC().remove("valor_ajustes_"+posDel);
        if(registroForm.getAccion().equals("registro"))
            registroForm.getFacturasCxC().remove("seleccionado_"+posDel);
        else if(registroForm.getAccion().equals("modificar"))
            registroForm.getFacturasCxC().remove("activarCheckBox_"+posDel);
        registroForm.getFacturasCxC().remove("tipo_"+posDel);
        nuevaPos = Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"");
        nuevaPos = nuevaPos-1;
        registroForm.setFacturasCxC("numFacturas",nuevaPos+"");
        if(posDel != esUltimo)//si no es el ultimo registro, se organiza el HashMap
        {               
         for(int k=posDel;k<Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"");k++)
         {
             registroForm.setFacturasCxC("codigo_"+k,registroForm.getFacturaCxC("codigo_"+(k+1))); 
             registroForm.setFacturasCxC("consecutivo_factura_"+k,registroForm.getFacturaCxC("consecutivo_factura_"+(k+1)));
             registroForm.setFacturasCxC("fecha_"+k,registroForm.getFacturaCxC("fecha_"+(k+1)));
             registroForm.setFacturasCxC("via_ingreso_"+k,registroForm.getFacturaCxC("via_ingreso_"+(k+1)));
             registroForm.setFacturasCxC("nombre_viaingreso_"+k,registroForm.getFacturaCxC("nombre_viaingreso_"+(k+1)));
             registroForm.setFacturasCxC("valor_cartera_"+k,registroForm.getFacturaCxC("valor_cartera_"+(k+1)));
             registroForm.setFacturasCxC("valor_ajustes_"+k,registroForm.getFacturaCxC("valor_ajustes_"+(k+1)));
             if(registroForm.getAccion().equals("registro"))
                 registroForm.setFacturasCxC("seleccionado_"+k,registroForm.getFacturaCxC("seleccionado_"+(k+1)));
             else if(registroForm.getAccion().equals("modificar"))
                 registroForm.setFacturasCxC("activarCheckBox_"+k,registroForm.getFacturaCxC("activarCheckBox_"+(k+1)));
             registroForm.setFacturasCxC("tipo_"+k,registroForm.getFacturaCxC("tipo_"+(k+1)));
         }
        }
		if(registroForm.getPosRegistro()==esUltimo)
		{
			if(registroForm.getAccion().equals("registro"))
				return this.redireccion(con,registroForm,response,request,"detalleFacturas.jsp");
			else if(registroForm.getAccion().equals("modificar"))
		       	return this.redireccion(con,registroForm,response,request,"detalleFacturasModificacion.jsp");
		}
        this.cerrarConexion(con);
        if(registroForm.getAccion().equals("registro"))
            return mapping.findForward("detalleFacturasConvenio");
        else if(registroForm.getAccion().equals("modificar"))
            return mapping.findForward("detalleFacturasModificar");
        else
            return null;	        
    }


    
    /**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param con, Connection con la fuente de datos.
     * @param poolesForm ConceptosCarteraForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     * @param String enlace
     * @return null
     */
    public ActionForward redireccion (	Connection con,
            											RegistroSaldoInicialCarteraForm registroForm,
						            					HttpServletResponse response,
						            					HttpServletRequest request, String enlace)
    {
        int numRegistros = Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"");
        registroForm.setOffset(((int)((Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"")-1)/registroForm.getMaxPageItems()))*registroForm.getMaxPageItems());
        if(request.getParameter("ultimaPage")==null)
        {
           if(numRegistros > (registroForm.getOffset()+registroForm.getMaxPageItems()))
               registroForm.setOffset(((int)(numRegistros/registroForm.getMaxPageItems()))*registroForm.getMaxPageItems());
            try 
            {
                this.cerrarConexion(con);
                response.sendRedirect(enlace+"?pager.offset="+registroForm.getOffset());
            } catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");
            
            int posOffSet=ultimaPagina.indexOf("offset=")+7;            
            if(numRegistros>(registroForm.getOffset()+registroForm.getMaxPageItems()))
                registroForm.setOffset(registroForm.getOffset()+registroForm.getMaxPageItems());
             
            try 
            {                
                this.cerrarConexion(con);
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+registroForm.getOffset());
            } 
            catch (IOException e) 
            {
                
                e.printStackTrace();
            }
     }
       this.cerrarConexion(con);
       return null;
    }

	/**
	 * @param con
	 * @param mundoRegistro
	 * @param registroForm
	 * @param usuario
	 */
	private void accionResumen(Connection con, RegistroSaldoInicialCartera mundoRegistro, RegistroSaldoInicialCarteraForm registroForm, UsuarioBasico usuario) 
	{
		mundoRegistro.reset();
		mundoRegistro.cargarCuentaCobroCompleta(con,registroForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt());
		registroForm.reset();
		registroForm.setNumCuentaCobro(mundoRegistro.getNumeroCuentaCobro());
		registroForm.setNumeroFacturasCxC(mundoRegistro.getNumeroFacturas());
		registroForm.setValorInicialCuenta(mundoRegistro.getValInicialCuenta());
		registroForm.setConvenio(mundoRegistro.getConvenio().getCodigo()+ConstantesBD.separadorSplit+mundoRegistro.getConvenio().getNombre());
		ArrayList vias=mundoRegistro.getViasIngreso();
		int i=0;
		for(i=0;i<vias.size();i++)
		{
			InfoDatosInt viasIngreso = (InfoDatosInt) vias.get(i);
			registroForm.setCuentasCobroMapa("codigoViaIngreso_"+i,viasIngreso.getCodigo()+"");
			registroForm.setCuentasCobroMapa("nombreViaIngreso_"+i,viasIngreso.getNombre()+"");
		}
		registroForm.setCuentasCobroMapa("numeroViasIngreso",i+"");
		registroForm.setFechaInicial(UtilidadFecha.conversionFormatoFechaAAp(mundoRegistro.getFechaInicial()));
		registroForm.setFechaFinal(UtilidadFecha.conversionFormatoFechaAAp(mundoRegistro.getFechaFinal()));
		registroForm.setObservaciones(mundoRegistro.getObservacionesGen());
		registroForm.setDetalleFacturas(mundoRegistro.getFacturas());
		registroForm.setFechaElaboracion(mundoRegistro.getFechaElaboracion());
		registroForm.setEstado("resumen");
		registroForm.setDetalleFacturas(mundoRegistro.getFacturas());
	}

	/**
     * @param con Connection,
     * @param request HttpServletRequest,
     * @param mundoRegistro
     * @param registroForm RegistroSaldoInicialCarteraForm,
     * @param mapping ActionMapping,
     * @param usuario UsuarioBasico,
     * @return findForward ActionForward,
     */ 
    private ActionForward continuarGeneracion(Connection con, 
													            HttpServletRequest request, 
													            RegistroSaldoInicialCartera mundoRegistro, 
																RegistroSaldoInicialCarteraForm registroForm, 
													            ActionMapping mapping,
													            UsuarioBasico usuario) 
    {
        registroForm.setMostrarMensaje(false);//si no se encontraron facturas, limpiar el atributo para no mostrar el mensaje nuevamente
        boolean existeCodigo=existeNumeroCuentaCobro(con,usuario.getCodigoInstitucionInt(),Double.parseDouble(registroForm.getNumCuentaCobroStr()));
        if(existeCodigo)
        {
            logger.warn("El número de la cuenta de cobro ya existe");            
            ActionErrors errores = new ActionErrors();
            String numCXC=registroForm.getNumCuentaCobro()+"";
            if(numCXC.indexOf(".")>0)
            	numCXC=numCXC.substring(0,numCXC.indexOf("."));
    	    errores.add("existeRegistroEnBD", new ActionMessage("error.existeNumeroCuentaCobroEnBD",numCXC));
    	    saveErrors(request, errores);  
    	    this.cerrarConexion(con);
    	    return mapping.findForward("paginaRegistro");
        }
        else
        {
        	mundoRegistro.resetRegistro();
    		registroForm.setNumeroFacturasCxC(0);
    		mundoRegistro.setConvenio(new InfoDatosInt(Integer.parseInt((registroForm.getConvenio().split(ConstantesBD.separadorSplit))[0])));
    		mundoRegistro.setFechaInicial(registroForm.getFechaInicial());
    		mundoRegistro.setFechaFinal(registroForm.getFechaFinal());
    		//todabia no tenemos cuenta de cobro la cargamos con codigonuncavalido(-1) para realizar la busqueda de las facturas.
    		mundoRegistro.setNumeroCuentaCobro(ConstantesBD.codigoNuncaValido);
    		int numeroViasIngreso=Integer.parseInt(registroForm.getCuentasCobroMapa("numeroViasIngreso")+"");
    		
    		/*for(int i=0;i<numeroViasIngreso;i++)
    		{
    			boolean seleccionada=UtilidadTexto.getBoolean(registroForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+"");
    			int numeroFacturaXViaIngreso=0;
    			if(seleccionada)
    			{
    				numeroFacturaXViaIngreso=registroForm.getNumeroFacturasCxC();
    				registroForm.setNumeroFacturasCxC(mundoRegistro.consultarFacturasViaIngreso(con,Integer.parseInt(registroForm.getCuentasCobroMapa("codigoViaIngreso_"+i)+""),usuario.getCodigoInstitucionInt(),registroForm.getNumeroFacturasCxC()));
    				numeroFacturaXViaIngreso=registroForm.getNumeroFacturasCxC()-numeroFacturaXViaIngreso;
    			}
    			registroForm.setCuentasCobroMapa("cantidadFacturasXViaIngreso_"+i,numeroFacturaXViaIngreso+"");
    		}*/
    		
    		String viasIngresoSeleccionadas = ConstantesBD.codigoNuncaValido+"";
    		for(int i=0;i<numeroViasIngreso;i++) {
    			if(UtilidadTexto.getBoolean(registroForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+""))
    				viasIngresoSeleccionadas+=", "+Integer.parseInt(registroForm.getCuentasCobroMapa("codigoViaIngreso_"+i)+"");
    		}
    		
    		registroForm.setNumeroFacturasCxC(mundoRegistro.consultarFacturasViaIngreso(con,viasIngresoSeleccionadas,usuario.getCodigoInstitucionInt(),registroForm.getNumeroFacturasCxC()));
    		
    		
    		registroForm.setNumeroFacturasSeleccionadas(registroForm.getNumeroFacturasCxC());
    		registroForm.setFacturasCxC(mundoRegistro.getFacturasCxC());
    		registroForm.setNumeroFacturasNoAptas(Integer.parseInt(registroForm.getFacturaCxC("numFacRechazadas")+""));
    		registroForm.setValorTotalCartera(Double.parseDouble((registroForm.getFacturaCxC("valor_total_cartera")+"")!=null&&!(registroForm.getFacturaCxC("valor_total_cartera")+"").equals("null")?(registroForm.getFacturaCxC("valor_total_cartera")+""):"0"));
    		registroForm.setValorTotalAjustes(Double.parseDouble((registroForm.getFacturaCxC("valor_total_ajustes")+"")!=null&&!(registroForm.getFacturaCxC("valor_total_ajustes")+"").equals("null")?(registroForm.getFacturaCxC("valor_total_ajustes")+""):"0"));
        }
		
		if(registroForm.getNumeroFacturasCxC()==0 && registroForm.getNumeroFacturasNoAptas()==0)
		{
			registroForm.setMostrarMensaje(true);	
			//si el N fE >0 insertarlas.
		}
		this.cerrarConexion(con);
		return mapping.findForward("detalleFacturasConvenio");
    }
  
	/**
	 * @param con
	 * @param mundoRegistro
	 * @param registroForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionGenerar(Connection con, 
            							HttpServletResponse response, 
										RegistroSaldoInicialCartera mundoRegistro, 
										RegistroSaldoInicialCarteraForm registroForm, 
										UsuarioBasico usuario,
										HttpServletRequest request,
										ActionMapping mapping) 
	{
		try 
		{
			boolean enTransaccion=true;			
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);			
			mundoRegistro.setNumeroCuentaCobro(registroForm.getNumCuentaCobro());
			cargarMundoCuentaCobroIngresar(mundoRegistro,registroForm);
			enTransaccion=mundoRegistro.insertarCuentaCobroPorRangos(con,usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt(),true);
			
			if(registroForm.getNumFactExternas()>0)
			{
				int estados=insertarFacturaExterna(con,request,registroForm,usuario,mundoRegistro);
				if(estados!=0)
				{
				    if(estados==1)//El consecutivo de la factura ya existe
				    {
				        enTransaccion = false;
				        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				        this.cerrarConexion(con);
				        return mapping.findForward("detalleFacturasConvenio");
				    }
				    else if(estados==2)//El consecutivo de la factura es superior a la primera factura generada por el sistema
				    {
				        enTransaccion = false;
				        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				        this.cerrarConexion(con);
				        return mapping.findForward("detalleFacturasConvenio");  
				    }
				}
			}

			
			if(registroForm.getNumFactExternas()>0)
				enTransaccion=insertarViasCXC(con,Double.parseDouble(registroForm.getNumCuentaCobroStr()),usuario.getCodigoInstitucionInt(),-1,-1,mundoRegistro,registroForm,true);//insertar las vias de ingreso de las facturas externas
			
			
			if(!enTransaccion )
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			}
			else 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			}
					
			this.cerrarConexion(con);
			
			try 
			{			    
			    response.sendRedirect("registroSaldoInicial.do?estado=resumen");
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			registroForm.setFacturasCxC(mundoRegistro.getFacturasCxC());
			return null;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		this.cerrarConexion(con);
		return null;
	}
    
   /**
    * Metodo implementado para insertar una factura externa
    * al sistema.
    * @param con Connection, 
    * @param request HttpServletRequest
    * @param registroForm RegistroSaldoInicialCarteraForm
    * @param usuario UsuarioBasico
    * @param mundoRegistro RegistroSaldoInicialCartera
    * @return int, codigo que representa el estado de la 
    * 			   transaccion
    */
	private int insertarFacturaExterna (Connection con,
													        HttpServletRequest request,															
													        RegistroSaldoInicialCarteraForm registroForm,
													        UsuarioBasico usuario,
													        RegistroSaldoInicialCartera mundoRegistro)
	{
	    int institucion=-1,estadoFact,estadoPac,consecutivo=-1,convenio,viaIngreso=-1,existeCodigo,consecutivoFact=-1;
		//int proxFact = 0;
		String logUsuario,fecha; 
		double vlrCartera=0,cuentaCobro=0;  
	    boolean enTransaccion = false;
	    int estados=0;
	    
	    for(int k = 0;k < Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"");k++)
		{

		    if((registroForm.getFacturaCxC("tipo_"+k)+"").equals("MEM"))
			{
			    vlrCartera = Double.parseDouble(registroForm.getFacturaCxC("valor_cartera_"+k)+"");
			    institucion = usuario.getCodigoInstitucionInt();
			    fecha = UtilidadFecha.conversionFormatoFechaABD(registroForm.getFacturaCxC("fecha_"+k)+"");
			    estadoFact = ConstantesBD.codigoEstadoFacturacionFacturada;
			    estadoPac = ConstantesBD.codigoEstadoFacturacionPacienteSinValorPaciente;
			    logUsuario = usuario.getLoginUsuario();
			    consecutivo = Integer.parseInt(registroForm.getFacturaCxC("consecutivo_factura_"+k)+"");
			    convenio = registroForm.getCodigoConvenio();
			    viaIngreso = Integer.parseInt(registroForm.getFacturaCxC("via_ingreso_"+k)+"");
			    int codigoCentroAtencion= Integer.parseInt(registroForm.getFacturaCxC("codigocentroatencion_"+k)+"");
			    
			    if(registroForm.getAccion().equals("registro"))//cuando es registro saldo inicial
			    {
			    	
			        if(UtilidadTexto.getBoolean(registroForm.getFacturaCxC("seleccionado_"+k)+""))
				        cuentaCobro = Double.parseDouble(registroForm.getNumCuentaCobroStr());												
				    else
				        cuentaCobro = -1;	//si la factura no es seleccionada, se marca la cuenta de cobro con -1, para que no pertenezca a la cuenta de cobro   
				}
	            else if(registroForm.getAccion().equals("modificar"))//cuando es modificación saldo inicial
	            {
	                if(UtilidadTexto.getBoolean(registroForm.getFacturaCxC("activarCheckBox_"+k)+""))
				        cuentaCobro = Double.parseDouble(registroForm.getNumCuentaCobroStr());
	                else
				        cuentaCobro = -1;	//si la factura no es seleccionada, se marca la cuenta de cobro con -1, para que no pertenezca a la cuenta de cobro
				}
			    consecutivoFact = mundoRegistro.primeraFacturaGeneradaSistema(con);
			    if(consecutivo < consecutivoFact || (consecutivoFact==ConstantesBD.codigoNuncaValido && consecutivo < Utilidades.convertirAEntero(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt()))))
			    {
				    existeCodigo = mundoRegistro.existeCodigoFactura(con,consecutivo);
				    if(existeCodigo == 0)
				    {
						int codTemp=mundoRegistro.insertarFacturaManual(con,
																		        cuentaCobro,
																		        false,
																		        vlrCartera,
																		        institucion,
																		        fecha,
																		        estadoFact,
																		        estadoPac,
																		        logUsuario,
																		        consecutivo,
																		        vlrCartera,
																		        vlrCartera,
																		        viaIngreso,
																		        convenio,
																		        codigoCentroAtencion
																		        );
						enTransaccion=codTemp>0;
						if(enTransaccion)
						{
						    estados=0;
						    //proxFact=mundoRegistro.proximoCodigoFactura(con, usuario);  
						    registroForm.setFacturasCxC("codigo_"+k,codTemp);
						   
						    if(registroForm.getAccion().equals("registro"))//cuando es registro saldo inicial
						    {
						        if((registroForm.getFacturaCxC("seleccionado_"+k)+"").equals("true"))
							    	insertarViasCXC(con,cuentaCobro,institucion,viaIngreso,consecutivo,mundoRegistro,registroForm,false);//para insertar en un ArrayList los codigos de las vias de ingreso de las facturas externas sin repetirlos
						    }
				            else if(registroForm.getAccion().equals("modificar"))//cuando es modificación saldo inicial
				            {
				                if((registroForm.getFacturaCxC("activarCheckBox_"+k)+"").equals("on"))
			 	                {
				                	insertarViasCXC(con,cuentaCobro,institucion,viaIngreso,consecutivo,mundoRegistro,registroForm,false);//para insertar en un ArrayList los codigos de las vias de ingreso de las facturas externas sin repetirlos
				                	registroForm.setFacturasSeleccionadasExternas(true);
				                }
							    	
				            }
						}
				    }
				    else if(existeCodigo != 0)
				    {				        
				        estados=1;
				        logger.warn("El consecutivo de la factura ya existe");            
			            ActionErrors errores = new ActionErrors();
			    	    errores.add("existeRegistroEnBD", new ActionMessage("error.existeFacturaEnBD",consecutivo+""));
			    	    saveErrors(request, errores);		    	    			    	    			    	    			    	    
				    }
			    }
			    else
			    {
			        estados=2;
			        enTransaccion = false;  
			        logger.warn("El código de la factura externa debe ser menor al de la primera factura generada del sistema");
			        ActionErrors errores = new ActionErrors();
		    	    errores.add("existeRegistroEnBD", new ActionMessage("error.consecutivoPrimeraFacturaSistema",consecutivo+""));
		    	    saveErrors(request, errores);		    	    		    	    		    	        	    
			    }
			}			
		}
	    
	    return estados;
	}
	
    /**Metodo para insertar las vias de ingreso
     * externas, para facturas externas al sistema
     * @param con
     * @param cuentaCobro
     * @param institucion
     * @param viaIngreso
     * @param mundoRegistro
     */
    private boolean insertarViasCXC(Connection con,	
            									double cuentaCobro,
									            int institucion, 
									            int viaIngreso, 
									            int consecutivo,
									            RegistroSaldoInicialCartera mundoRegistro,
									            RegistroSaldoInicialCarteraForm registroForm,
									            boolean esInsertar) 
    {       
        boolean insertoVia=false;		
		int existeViaBD=0;
		
		if(!esInsertar)
		{			
			int i=0;
			for(i=0;i<registroForm.getViasIngresoInsertadas().size();i++)
			{
				if(viaIngreso==Integer.parseInt((registroForm.getViasIngresoInsertadas(i)+"")))
				{
					i=registroForm.getViasIngresoInsertadas().size()+1;
				}
			}
			if(i==registroForm.getViasIngresoInsertadas().size())
			{
				 registroForm.setViasIngresoInsertadas(i,viaIngreso+"");
			}
			/*if(esPrimero)
	        {
	            if(cuentaCobro!=-1)//si la factura fue seleccionada, se hace la entrada a la vias_ingreso_cxc, con la cuenta de cobro respectiva
	            {
				    registroForm.setViasIngresoInsertadas(registroForm.getPosArray(),viaIngreso+"");            
		            esPrimero = false;
		            p++;
		            registroForm.setPosArray(p) ;
	            }
	        }
	        else
	        {
	            if(cuentaCobro!=-1)//si la factura fue seleccionada, se hace la entrada a la vias_ingreso_cxc, con la cuenta de cobro respectiva
	            {
		            for(int l=0;l<registroForm.getViasIngresoInsertadas().size();l++)
		            {	          
		                if((registroForm.getViasIngresoInsertadas(l)+"").equals(viaIngreso+""))
		                  existeVia=true;
		            }
		            if(!existeVia)
		            {
		                registroForm.setViasIngresoInsertadas(registroForm.getPosArray(),viaIngreso+"");                
		                p++;
			            registroForm.setPosArray(p) ;
		            }
	            }
	        }*/			
		}
		else
		{
		    for (int k=0;k<registroForm.getViasIngresoInsertadas().size();k++)
		    {
			    existeViaBD=mundoRegistro.existeViaIngresoCxCEnBD(con,institucion,Integer.parseInt(registroForm.getViasIngresoInsertadas(k)+""),cuentaCobro);//se verifica si la via de ingreso no se encuentre en la tabla
			    insertoVia=true;
			    if(existeViaBD==0)
			    {
			    	insertoVia=false;
			    	insertoVia=mundoRegistro.insertarCxCAViasIngresoCxC(con,cuentaCobro,institucion,Integer.parseInt(registroForm.getViasIngresoInsertadas(k)+""));//se inserta la nueva entrada de la cuenta de cobro en la tabla vias_ingreso_cxc, con la nueva via de ingreso
			        if(!insertoVia)
			            logger.warn("No se inserto la via de ingreso de la factura externa "+consecutivo+" con via de ingreso "+registroForm.getViasIngresoInsertadas(k));
			    }
		    }
		}
		
		return insertoVia;
    }

    /**
	 * @param mundoRegistro
	 * @param registroForm
	 */
	private void cargarMundoCuentaCobroIngresar(RegistroSaldoInicialCartera mundoRegistro, RegistroSaldoInicialCarteraForm registroForm) 
	{
		mundoRegistro.setConvenio(new InfoDatosInt(Integer.parseInt((registroForm.getConvenio().split(ConstantesBD.separadorSplit))[0])));
		mundoRegistro.setFechaInicial(registroForm.getFechaInicial());
		mundoRegistro.setFechaFinal(registroForm.getFechaFinal());
		mundoRegistro.setObservacionesGen(registroForm.getObservaciones());
		mundoRegistro.setFechaElaboracion(registroForm.getFechaElaboracion());
		mundoRegistro.setHoraElaboracion(UtilidadFecha.getHoraActual());
		ArrayList viasIngreso=new ArrayList();
		mundoRegistro.setViasIngreso(new ArrayList());
		for(int i=0;i<Integer.parseInt(registroForm.getCuentasCobroMapa("numeroViasIngreso")+"");i++)
		{
			//int facViaIngreso=Integer.parseInt(registroForm.getCuentasCobroMapa("cantidadFacturasXViaIngreso_"+i)+"");
			//if(facViaIngreso>0&&UtilidadTexto.getBoolean(registroForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+""))
			if(UtilidadTexto.getBoolean(registroForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+""))			
				viasIngreso.add(registroForm.getCuentasCobroMapa("codigoViaIngreso_"+i));
		}
		
		mundoRegistro.setViasIngreso(viasIngreso);
		int nF=0;
		double valorTotal=0,saldoTotal=0,ajustesTotal=0;
		mundoRegistro.getFacturasCxC().clear();
		//for(int f=0;f<registroForm.getNumeroFacturasCxC();f++)
		
		for(int f=0;f<Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"");f++)
		{
			if(UtilidadTexto.getBoolean(registroForm.getFacturaCxC("seleccionado_"+f)+""))
			{
				mundoRegistro.setFacturaCxC("codigo_"+nF,registroForm.getFacturaCxC("codigo_"+f));
				valorTotal=valorTotal+Double.parseDouble(registroForm.getFacturaCxC("valor_cartera_"+f)+"");
				mundoRegistro.setFacturaCxC("valor_cartera_"+nF,registroForm.getFacturaCxC("valor_cartera_"+f));
				ajustesTotal=ajustesTotal+Double.parseDouble(registroForm.getFacturaCxC("valor_ajustes_"+f)+"");
				saldoTotal=saldoTotal+(Double.parseDouble(registroForm.getFacturaCxC("valor_cartera_"+f)+"")-Double.parseDouble(registroForm.getFacturaCxC("valor_ajustes_"+f)+""));
				nF++;
			}
		}
		registroForm.setValorTotalFacturas(valorTotal);
		registroForm.setValorTotalAjustes(ajustesTotal);
		registroForm.setSaldoTotalCuenta(saldoTotal);
		//eñ valor inicial de la cuenta es el mismo que el saldo
		mundoRegistro.setValInicialCuenta(registroForm.getSaldoTotalCuenta());
		mundoRegistro.setSaldoCuenta(registroForm.getSaldoTotalCuenta());
		//en el momento de generar la cuenta de cobro el saldo es igual al valor
		mundoRegistro.setFacturaCxC("cantidadFacturas",nF+"");	
	}

	
	
	private ActionForward accionImprimir(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			RegistroSaldoInicialCartera mundoRegistro,
			RegistroSaldoInicialCarteraForm registroForm, UsuarioBasico usuario) {
		
		
		
		
		
		
		registroForm.setMapMovimientos("codigoCXC", registroForm
				.getNumCuentaCobroStr());
		String nombreArchivo;
		String nombreArchivo2;
		Random r = new Random();
		nombreArchivo = "/aBorrarDetallado" + r.nextInt() + ".pdf";
		nombreArchivo2 = "/aBorrarResumido" + r.nextInt() + ".pdf";

		
		accionResumen(con, mundoRegistro, registroForm, usuario);
		
		CuentasCobroPdf.imprimir(registroForm, usuario, con, mundoRegistro,
				ValoresPorDefecto.getFilePath() + nombreArchivo,
				ValoresPorDefecto.getFilePath() + nombreArchivo2, request);

		this.cerrarConexion(con);

		if (registroForm.isImpresionAnexos()) {
			request.setAttribute("nombreArchivo1", nombreArchivo);
			request.setAttribute("nombreVentana1", "Cuentas de Cobro");
			request.setAttribute("nombreArchivo2", nombreArchivo2);
			request.setAttribute("nombreVentana2", "Cuentas de Cobro Resumido");
			return mapping.findForward("abrir2Pdf");
		} else {
			request.setAttribute("nombreArchivo", nombreArchivo);
			request.setAttribute("nombreVentana", "Cuentas de Cobro");
			return mapping.findForward("abrirPdf");
		}

	}
	
	/**
	 * metodo para validar si una cuenta de cobro existe 
	 * en la BD.
	 * @param con Connection, conexión con la fuente de datos.
	 * @param institucion int, código de la institución.
	 * @param numeroCuentaCobro double, número de la cuenta de cobro
	 * @return boolean, true si existe
	 */
	private boolean existeNumeroCuentaCobro (Connection con,int institucion,double numeroCuentaCobro)
	{
	    RegistroSaldoInicialCartera mundoRegistro = new RegistroSaldoInicialCartera ();
	    int existeCodigo = mundoRegistro.existeCodigoCunetaDeCobro(con,numeroCuentaCobro,institucion);
	    if(existeCodigo > 0)
	        return true;
	    else
	        return false;   
	}
	
	/**
	 * Metodo implementado para liberar las facturas
	 * correspondientes a una cuenta de cobro especifica,
	 * y relacionarlas nuevamente segun los parametros
	 * que han sido seleccionados, por Vía de Ingreso,
	 * por Fecha Inicial-Fecha Final.
	 * 
     * @param con Connection con la fuente de datos
     * @param mapping ActionMapping
     * @param cxcForm RegistroSaldoInicialCarteraForm
	 * @param usuario UsuarioBasico
     * @return findForward ActionForward
     */
    private ActionForward liberarRelacionarFacturas(Connection con, 
														            ActionMapping mapping,														            
														            RegistroSaldoInicialCarteraForm registroForm, 
														            UsuarioBasico usuario) 
    {
        	registroForm.setMostrarMensaje(false);//si no se encontraron facturas, limpiar el atributo para no mostrar el mensaje nuevamente
	        RegistroSaldoInicialCartera mundoReg = new RegistroSaldoInicialCartera ();
	        mundoReg.getMapMovimientos().clear();//limpiar el mapa del mundo
	        registroForm.getFacturasCxC().clear();//limpiar el mapa de facturas
	        registroForm.setNumeroFacturasCxC(0);//inicializar el numero de facturas
	        int viasIngresoConFacturas = 0;
	        
	        String codigoFactRelacionAntigua="",codigoFactRelacionNueva="";
	        
	        ordenarValidarHashMap(registroForm);
	        mundoReg.consultarFactALiberar(con,Double.parseDouble(registroForm.getNumCuentaCobroStr()),usuario.getCodigoInstitucionInt());
	        mundoReg.setConvenio(new InfoDatosInt(registroForm.getCodigoConvenio()));
	        mundoReg.setFechaInicial(registroForm.getFechaInicial());
	        mundoReg.setFechaFinal(registroForm.getFechaFinal());    
	        mundoReg.setObservacionesGen(registroForm.getObservacionMovimiento());
	        mundoReg.setNumeroCuentaCobro(registroForm.getNumCuentaCobro());
	       
	        for(int k=0; k < Integer.parseInt(registroForm.getMapMovimientos("numViasIngreso")+"") ; k++)
	        {
	            if((registroForm.getMapMovimientos("esCheckActivo_"+k)+"").equals("on"))
	            {                
	                registroForm.setNumeroFacturasCxC(mundoReg.consultarFacturasViaIngreso(con,registroForm.getMapMovimientos("codViaIngreso_"+k)+"",usuario.getCodigoInstitucionInt(),registroForm.getNumeroFacturasCxC()));	                
	                //validar que la via de ingreso tenga facturas, si tiene true, de lo contrario false
	                //para no guardar vias de ingreso que no posean relacion con facturas
	                if(viasIngresoConFacturas == registroForm.getNumeroFacturasCxC())
	                    registroForm.setMapMovimientos("viaIngresoSinFacturas_"+k,"true");
	                else
	                    registroForm.setMapMovimientos("viaIngresoSinFacturas_"+k,"false");
	                
	                viasIngresoConFacturas = registroForm.getNumeroFacturasCxC();
	            }
	        }
	        registroForm.setFacturasCxC(mundoReg.getFacturasCxC());        
	        registroForm.getFacturasCxC().putAll(mundoReg.getMapMovimientos());           
	        registroForm.setNumeroFacturasSeleccionadas(registroForm.getNumeroFacturasCxC());	        		
			if(registroForm.getNumeroFacturasCxC()==0 && registroForm.getNumeroFacturasNoAptas()==0)
			{
				registroForm.setMostrarMensaje(true);		
			}
			
	        /*para validar que facturas tenian relacion antes con la CXC y seleccionarlas
	         * en los checkBox de la vista, y adicionar las nuevas facturas mostrandolas
	         * deseleccionadas en los checkbox*/
	        
	        if(Integer.parseInt(registroForm.getFacturaCxC("numeroFactConRelacionCXC")+"") != 0)
	        {
	        	double valorTotalCartera=0,valorTotalAjustes=0,valor=0;
		        for(int k =0; k<Integer.parseInt(registroForm.getFacturaCxC("numeroFactConRelacionCXC")+""); k++)
		        {	
		            /*el index <codigoFactConRelacionCXC_> proviene del metodo del mundo <consultarFactALiberar>
		             * que es donde se consultan las facturas que estaban relacionadas a la CXC, antes
		             * de ser liberadas con el codigo de la cuenta de cobro en null*/
		            codigoFactRelacionAntigua = (registroForm.getFacturaCxC("codigoFactConRelacionCXC_"+k)+"");
		            
		            /*el index <numFacturas> proviene del metodo del mundo <consultarFacturasViaIngreso>
		             *que es cuando se consultan las facturas segun el rango de busqueda en la CXC*/
			        for(int i=0; i<Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+""); i++)
			        {
			          codigoFactRelacionNueva = (registroForm.getFacturaCxC("codigo_"+i)+"");		          
			          if(codigoFactRelacionAntigua.equals(codigoFactRelacionNueva))
			          {
			              registroForm.setFacturasCxC("activarCheckBox_"+i,"on");
			              valorTotalCartera=valorTotalCartera+Double.parseDouble(registroForm.getFacturaCxC("valor_cartera_"+i)+"");
			              valorTotalAjustes=valorTotalAjustes+Double.parseDouble(registroForm.getFacturaCxC("valor_ajustes_"+i)+"");
			              valor=valor+Double.parseDouble(registroForm.getFacturaCxC("valor_"+i)+"");
			          }
			          else
			          {
			            if(!(registroForm.getFacturaCxC("activarCheckBox_"+i)+"").equals("on"))
			            {
			                registroForm.setFacturasCxC("activarCheckBox_"+i,"off");			         
			            }
			          }
			        }   
		        }
		        registroForm.setFacturasCxC("valor_total_cartera",valorTotalCartera+"");
		        registroForm.setFacturasCxC("valor_total_ajustes",valorTotalAjustes+"");
		        registroForm.setFacturasCxC("valor_",valor+"");
	        }
	        else if(Integer.parseInt(registroForm.getFacturaCxC("numeroFactConRelacionCXC")+"") == 0)
	        {
	            /*el index <numFacturas> proviene del metodo del mundo <consultarFacturasViaIngreso>
	             *que es cuando se consultan las facturas segun el rango de busqueda en la CXC*/
	          for(int i=0; i<Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+""); i++)
		      {
	              registroForm.setFacturasCxC("activarCheckBox_"+i,"off"); 
		      }
	        }  
		registroForm.setNumeroFacturasNoAptas(Integer.parseInt(registroForm.getFacturaCxC("numFacRechazadas")+""));
		registroForm.setValorTotalCartera(Double.parseDouble((registroForm.getFacturaCxC("valor_total_cartera")+"")!=null&&!(registroForm.getFacturaCxC("valor_total_cartera")+"").equals("null")?(registroForm.getFacturaCxC("valor_total_cartera")+""):"0"));
		registroForm.setValorTotalAjustes(Double.parseDouble((registroForm.getFacturaCxC("valor_total_ajustes")+"")!=null&&!(registroForm.getFacturaCxC("valor_total_ajustes")+"").equals("null")?(registroForm.getFacturaCxC("valor_total_ajustes")+""):"0"));
        this.cerrarConexion(con);
		return mapping.findForward("detalleFacturasModificar");
    }
        
    /**
	 * Metodo implementado para ordenar los registros del HashMap,
	 * que provienen de la página de modificaciónCuentaCobro y
	 * de busquedaCuentaCobro.
	 * 
	 * @param cxcForm CuentasCobroForm
	 */
	private boolean ordenarValidarHashMap (RegistroSaldoInicialCarteraForm registroForm)
	{
	    
	    boolean eliminarRelacion = false,adicionarRelacion=false,existeModificacion=false;
	    int posHashMap=0;//para regular la secuencia de los indices del HashMap de forma ordenada.
	    int posHashMap2=0;
	    
	    //imprimirDatosMovimientos(cxcForm);
	    
	       for(int k = 0; k < Integer.parseInt(registroForm.getMapMovimientos("ultimoIndex")+"") ; k++)
	        {
	         /*esta comparación porque no se conocen los indices de las llaves en el mapa, entonces se busca
	          *la llave iterando los elementos del mapa, hasta la ultima posicion que se inserto desde
	          *la pagina de busquedaCuentaCobro, la propiedad containsKey retornara true cuando la encuentre.
	          *Como existe la propiedad pero para algunas posiciones los valores son null, 
              *porque asi se producen en la pagina, evitar errores */
	           if(registroForm.getMapMovimientos().containsKey("codViaIngresoTemp_"+k) 
	                    && registroForm.getMapMovimientos().containsKey("esCheckActivoTemp_"+k)
	                    && !registroForm.getMapMovimientos("codViaIngresoTemp_"+k).equals(null))
	            {               
		          /*Si se modifico una via de ingreso que estaba relacionada a la cuenta de cobro, 
		           * y por lo tanto a las facturas,y ya no debe de haber relacion */
		          if( registroForm.getMapMovimientos("existeViaIngresoCXCTemp_"+k).equals("on") 
		                     && registroForm.getMapMovimientos("esCheckActivoTemp_"+k).equals("off") )
		          {
		             registroForm.setMapMovimientos("codViaIngresoEliminarRelacionVI_"+posHashMap2,registroForm.getMapMovimientos("codViaIngresoTemp_"+k));	                    
		             eliminarRelacion = true;
		             registroForm.setMapMovimientos("esEliminarRelacionVI_"+posHashMap2,eliminarRelacion+"");
		          }
		                
		          /*Si se modifico una via de ingreso que NO estaba relacionada a la cuenta de cobro, 
		           * y por lo tanto a las facturas,y debe de haber relacion */
		          if( registroForm.getMapMovimientos("existeViaIngresoCXCTemp_"+k).equals("off") 
		                     && registroForm.getMapMovimientos("esCheckActivoTemp_"+k).equals("on") )
		          {
		            registroForm.setMapMovimientos("codViaIngresoAdicionarRelacionVI_"+posHashMap2,registroForm.getMapMovimientos("codViaIngresoTemp_"+k));
		            adicionarRelacion=true;
		            registroForm.setMapMovimientos("esAdicionarRelacionVI_"+posHashMap2,adicionarRelacion+"");
		          }
	              
		          /*para ordenar el mapa, con un orden de indices secuencial, puesto que antes contenia
	               * indices en desorden(ej.11,8,0, ...), y queda(ej. 0,1,2, ...)*/
		          registroForm.setMapMovimientos("codViaIngreso_"+posHashMap,registroForm.getMapMovimientos("codViaIngresoTemp_"+k));
		          registroForm.setMapMovimientos("esCheckActivo_"+posHashMap,registroForm.getMapMovimientos("esCheckActivoTemp_"+k));
		          registroForm.setMapMovimientos("existeViaIngresoCXC_"+posHashMap,registroForm.getMapMovimientos("existeViaIngresoCXCTemp_"+k));	                
		          registroForm.getMapMovimientos().remove("codViaIngresoTemp_"+k);
		          registroForm.getMapMovimientos().remove("esCheckActivoTemp_"+k);
		          registroForm.getMapMovimientos().remove("existeViaIngresoCXCTemp_"+k);		          
		          registroForm.setMapMovimientos("numViasIngreso",(posHashMap+1)+"");
		          posHashMap ++;
		            
	              if ( eliminarRelacion )                   
		          {
	                existeModificacion = true;
	                eliminarRelacion = false;                
	                registroForm.setMapMovimientos("numViasIngresoMod",(posHashMap2+1)+"");
	                posHashMap2 ++;
		          }	              
	              if( adicionarRelacion )
	              {
		            existeModificacion = true;
		            adicionarRelacion=false;
		            registroForm.setMapMovimientos("numViasIngresoMod",(posHashMap2+1)+"");
		            posHashMap2 ++;	              }
	            }
	        }          
	    return existeModificacion;
	}
	
	/**
	 * Metodo implementado para guardar los cambios realizados 
	 * en el saldo inicial, facturas adicionadas o
	 * retiradas de las ya existentes.
	 * 
     * @param con Connection con la fuente de datos 
     * @param mapping ActionMapping
     * @param registroForm RegistroSaldoInicialCarteraForm
     * @return findForward
     */
    private ActionForward guardarSalirModificaciones(Connection con,
															            ActionMapping mapping, 
															            RegistroSaldoInicialCarteraForm registroForm,
															            UsuarioBasico usuario,
															            HttpServletRequest request,
															            RegistroSaldoInicialCartera mundoRegistro) 
    {
        boolean existeModificacion=verificarModificaciones(con, registroForm,usuario);
        boolean continuarTrasaccion=true;
        
        if(existeModificacion)
        {            
            RegistroSaldoInicialCartera mundoCXC = new RegistroSaldoInicialCartera(); 
	        ArrayList vectorViasIngresoAdc=new ArrayList(),vectorViasIngresoMod=new ArrayList();
	        int viaIngresoMod=0,pos1=0,pos2=0;
	        double sumaValInicial = 0.0;       
	        
	        mundoCXC.getMapMovimientos().clear();
	        Utilidades.imprimirMapa(registroForm.getFacturasCxC());
	        mundoCXC.setMapMovimientos(registroForm.getFacturasCxC());
	        mundoCXC.setFechaInicial(registroForm.getFechaInicial());
	        mundoCXC.setFechaFinal(registroForm.getFechaFinal());
	        mundoCXC.setFechaElaboracion(registroForm.getFechaElaboracion()); 
	        mundoCXC.setObservacionesGen(registroForm.getObservacionMovimiento());
	        if(!(registroForm.getMapMovimientos("numViasIngresoMod")+"").equals("null"))//en caso de que no se hallan modificado vias de ingreso, evitar errores
	        {
		        /*se valida que vias de ingreso fueron adicionadas o eliminadas
		         *para realizar la respectiva transaccion sobre la tabla vias_ingreso_cxc,
		         *estos index provienen del metodo del action <ordenarValidarHashMap>*/
		        for(int k=0; k < Integer.parseInt(registroForm.getMapMovimientos("numViasIngresoMod")+"") ; k++)
		        {
		          for(int i=0; i<Integer.parseInt(registroForm.getMapMovimientos("numViasIngreso")+""); i++)
			  	  {
			        int viaIngreso = Integer.parseInt(registroForm.getMapMovimientos("codViaIngreso_"+i)+"");
			        //para que solo se ingresen las vias de ingreso que tienen facturas, index <viaIngresoSinFacturas_> proviene del metodo liberarRelacionarFacturas
			        //if(!UtilidadTexto.getBoolean(registroForm.getMapMovimientos("viaIngresoSinFacturas_"+i)+""))
			        {
				        if((registroForm.getMapMovimientos("esEliminarRelacionVI_"+k)+"").equals(true+"") )
					    {
				          viaIngresoMod=Integer.parseInt(registroForm.getMapMovimientos("codViaIngresoEliminarRelacionVI_"+k)+"");
					      if(viaIngresoMod == viaIngreso)
					      {   
					        vectorViasIngresoMod.add(pos1,viaIngresoMod+"");			       
					        pos1++;			        
					      }
					    }
				        
				        if((registroForm.getMapMovimientos("esAdicionarRelacionVI_"+k)+"").equals(true+""))
					    {
				          viaIngresoMod=Integer.parseInt(registroForm.getMapMovimientos("codViaIngresoAdicionarRelacionVI_"+k)+"");
				          if(viaIngresoMod == viaIngreso)
					      {   
					        vectorViasIngresoAdc.add(pos2,viaIngresoMod+"");			        
					        pos2++;			        
					      }
					    }
			        }
		           } 
		        }
	        }
	        /*Se suman todos los valores de las facturas que estan seleccionadas, para guardar el 
	         * valor inicial y saldo de la CXC*/
	        double valorTotal=0,ajustesTotal=0;
	        for(int i=0; i<Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+""); i++)
	        {  
	          if( (registroForm.getFacturaCxC("activarCheckBox_"+i)+"").equals("on") )
	          {   
	          	//sumValInicial es el mismo SaldoTotal
	              sumaValInicial = sumaValInicial + (Double.parseDouble(registroForm.getFacturaCxC("valor_cartera_"+i)+"")-(Double.parseDouble(registroForm.getFacturaCxC("valor_ajustes_"+i)+"")));
	              valorTotal=valorTotal+Double.parseDouble(registroForm.getFacturaCxC("valor_cartera_"+i)+"");
	              ajustesTotal=ajustesTotal+Double.parseDouble(registroForm.getFacturaCxC("valor_ajustes_"+i)+"");
	          }
	        } 
	     	registroForm.setValorTotalFacturas(valorTotal);
			registroForm.setValorTotalAjustes(ajustesTotal);
			registroForm.setSaldoTotalCuenta(sumaValInicial);

			try 
			{
				boolean enTransaccion=true;			
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);			
			
	
				if(registroForm.getNumFactExternas()>0)
				{
					registroForm.setNumCuentaCobroStr(registroForm.getNumCuentaCobro()+"");
			        int estados=insertarFacturaExterna(con,request,registroForm,usuario,mundoCXC);
					if(estados!=0)
					{
					    if(estados==1)//El consecutivo de la factura ya existe
					    {
					        continuarTrasaccion = false;
					        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
					        this.cerrarConexion(con);
					        return mapping.findForward("detalleFacturasModificar");
					    }
					    else if(estados==2)//El consecutivo de la factura es superior a la primera factura generada por el sistema
					    {
					        continuarTrasaccion = false;
					        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
					        this.cerrarConexion(con);
					        return mapping.findForward("detalleFacturasModificar");  
					    }
					}
					else
					    continuarTrasaccion = true; //No existe error en la insercion de la factura externa
				}	

				if(continuarTrasaccion)
				{	
			        boolean noExisteError=mundoCXC.liberarRelacionarFacturas(con,
																	                registroForm.getNumCuentaCobro(),
																					vectorViasIngresoMod,
																					vectorViasIngresoAdc,true,true,true,
																					ConstantesBD.continuarTransaccion,
																					usuario.getLoginUsuario(),
																					sumaValInicial,
																					usuario.getCodigoInstitucionInt(),
																					true);
			        if(!noExisteError)
			        {
			            continuarTrasaccion = false;			            
			        } 
			        else
			        {
			        	if(registroForm.getNumFactExternas()>0&&registroForm.getNumeroFacturasSeleccionadas()>0&&registroForm.isFacturasSeleccionadasExternas())
			        			enTransaccion=insertarViasCXC(con,Double.parseDouble(registroForm.getNumCuentaCobroStr()),usuario.getCodigoInstitucionInt(),  -1,-1,mundoRegistro,registroForm,true);//insertar las vias de ingreso de las facturas externas	
			        	generarLog(registroForm,usuario.getLoginUsuario(),registroForm.getNumCuentaCobro());
			        	if(!enTransaccion )
						{			            	
			                continuarTrasaccion = false;
			                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
						}
						else 
						{							
						    cargarResumen(con,registroForm,usuario);
						    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
						}
			        }
				}
				else
				{					
				    continuarTrasaccion = false;  
				}
			}
			catch (SQLException e) 
			{
			     e.printStackTrace();
			}
        }
        
        this.cerrarConexion(con);
        if(!continuarTrasaccion)
        {
            logger.warn("No se grabo información de facturas externas");            
			ActionErrors errores = new ActionErrors();
            errores.add("anular Saldo Inicial Cartera", new ActionMessage("prompt.noSeGraboInformacion"));            
            saveErrors(request, errores);    
            return mapping.findForward("detalleFacturasModificar");
        }
        else if (continuarTrasaccion)
        {
        	//cargar las observaciones para la impresion
        	registroForm.setObservaciones(registroForm.getObservacionMovimiento());
        	
        	//cargar el valor inicial para la impresion
        	String valorStr=registroForm.getMapValidaciones("valor")+"";
        	registroForm.setValorInicialCuenta(Double.parseDouble(valorStr.trim().equals("")?"0":valorStr));
        	return mapping.findForward("resumenModificacion");
        }
        
        else 
            return null;
			     
    }
	
	/**
     *Metodo implementado para generar el resumen 
     *despues de las modificaciones, ó ates de ser
     *realizadas por motivos de validaciones de las
     *modificaciones realizadas.
     * @param con Connection, conexión con la fuente de datos
     * @param registroForm RegistroSaldoInicialCarteraForm
     * @param usuario UsuarioBasico    
     */
    private void cargarResumen (Connection con,
									            RegistroSaldoInicialCarteraForm registroForm,
									            UsuarioBasico usuario)
    {
        CuentasCobro mundoCXC = new CuentasCobro ();
        Iterator it;
        DtoFactura fact;
        int i=0;
        
        mundoCXC.cargarCuentaCobroCompleta(con,Double.parseDouble(registroForm.getNumCuentaCobroStr()),usuario.getCodigoInstitucionInt());
        registroForm.setMapValidaciones("numCXC",registroForm.getNumCuentaCobroStr()+"");
        registroForm.setMapValidaciones("numFact",mundoCXC.getNumeroFacturas()+"");
        registroForm.setMapValidaciones("valor",mundoCXC.getValInicialCuenta()+"");	
        registroForm.setMapValidaciones("convenio",mundoCXC.getConvenio().getNombre()+"");
        registroForm.setMapValidaciones("codConvenio",mundoCXC.getConvenio().getCodigo()+"");
		for(int k=0;k<mundoCXC.getViasIngreso().size();k++)
		{
		    registroForm.setMapValidaciones("viaIngreso_"+k,((InfoDatosInt)(mundoCXC.getViasIngreso().get(k))).getNombre()+"");
		    registroForm.setMapValidaciones("codViaIngreso_"+k,((InfoDatosInt)(mundoCXC.getViasIngreso().get(k))).getCodigo()+"");
		    registroForm.setMapValidaciones("numViasIngreso",k+"");
		}
		
		it=mundoCXC.getFacturas().iterator();
		while(it.hasNext())
		{		
			fact = (DtoFactura) it.next();
		    registroForm.setMapValidaciones("codFact_"+i,fact.getCodigo()+"");
		    i++;
		}
		registroForm.setMapValidaciones("fechaInicial",UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaInicial()+""));
		registroForm.setMapValidaciones("fechaFinal",UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaFinal()+""));
		registroForm.setMapValidaciones("fechaElaboracion",UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaElaboracion()+""));
		registroForm.setMapValidaciones("observaciones",mundoCXC.getObservacionesGen()+"");      
        
    }
	
    /**
     * metodo para validar si hubo modificaciones en la 
     * cuenta de cobro ó en las facturas.
     * @param con Connection, conexión con la fuente de datos
     * @param registroForm RegistroSaldoInicialCarteraForm
     * @param usuario
     * @return boolean, true si hay modificaciones
     */
    private boolean verificarModificaciones (Connection con,RegistroSaldoInicialCarteraForm registroForm, UsuarioBasico usuario)
    {
        cargarResumen(con,registroForm,usuario);
        boolean existeModificacion = false,codViaIngresoDif = false,codFacturaDif=false;
        int codVia1 = -1,codVia2 = -1,codVia3 = -1;
        int codFact1 = -1,codFact2 = -1,codFact3 = -1;
        
        if(!registroForm.getFechaInicial().equals(registroForm.getMapValidaciones("fechaInicial")+""))
        {
            existeModificacion = true;
        }
        if(!registroForm.getFechaFinal().equals(registroForm.getMapValidaciones("fechaFinal")+""))
        {
            existeModificacion = true;
        }
        if(!registroForm.getFechaElaboracion().equals(registroForm.getMapValidaciones("fechaElaboracion")+""))
        {
            existeModificacion = true;
        }
        if(!registroForm.getObservacionMovimiento().equals(registroForm.getMapValidaciones("observaciones")+""))
        {
            existeModificacion = true;
        }
        
        for(int k=0; k < Integer.parseInt(registroForm.getMapMovimientos("numViasIngreso")+"") ; k++)
        {
            if((registroForm.getMapMovimientos("esCheckActivo_"+k)+"").equals("off"))
                codVia1=Integer.parseInt(registroForm.getMapMovimientos("codViaIngreso_"+k)+"");
                
            if((registroForm.getMapMovimientos("esCheckActivo_"+k)+"").equals("on"))
            {
                codVia2=Integer.parseInt(registroForm.getMapMovimientos("codViaIngreso_"+k)+"");                
            }
            
            for(int i=0; i <= Integer.parseInt(registroForm.getMapValidaciones("numViasIngreso")+"") ; i++)
            {
                codVia3=Integer.parseInt(registroForm.getMapValidaciones("codViaIngreso_"+i)+"");
                
                if(codVia1!=-1)
	                if(codVia1 == codVia3)
	                  existeModificacion = true;	                    
	                
	            if(codVia2!=-1)
	                if(codVia2 != codVia3)
	                  codViaIngresoDif = true;
	                	                
                if(!codViaIngresoDif && i == Integer.parseInt(registroForm.getMapValidaciones("numViasIngreso")+""))
                    existeModificacion = true;
                
                if(codViaIngresoDif && i == Integer.parseInt(registroForm.getMapValidaciones("numViasIngreso")+""))
                    existeModificacion = true;
            }
            codVia1 = -1;
            codVia2 = -1;
        }
                
        for(int k=0; k < Integer.parseInt(registroForm.getFacturaCxC("numFacturas")+"") ; k++)
        {
            if((registroForm.getFacturaCxC("activarCheckBox_"+k)+"").equals("off"))
            {
                codFact1= Integer.parseInt(registroForm.getFacturaCxC("codigo_"+k)+"");
                if(Integer.parseInt(registroForm.getMapValidaciones("numFact")+"") == 0)
                    existeModificacion = true;//si no se encuentra seleccionada ninguna fact antes, y selecciona una              
            }
            
            if((registroForm.getFacturaCxC("activarCheckBox_"+k)+"").equals("on"))
               codFact2=Integer.parseInt(registroForm.getFacturaCxC("codigo_"+k)+"");  
                
            for(int i=0; i < Integer.parseInt(registroForm.getMapValidaciones("numFact")+"") ; i++)
            {
                codFact3=Integer.parseInt(registroForm.getMapValidaciones("codFact_"+i)+"");
                
                if(codFact1!=-1)
	                if(codFact1 == codFact3)
	                  existeModificacion = true;        
	                
	            if(codFact2!=-1)
	                if(codFact2 != codFact3)
	                    codFacturaDif = true;
	              	                
                if(!codFacturaDif && i == Integer.parseInt(registroForm.getMapValidaciones("numFact")+""))
                    existeModificacion = true;
            }
            codFact1 = -1;
            codFact2 = -1;
        }
        
        
        return existeModificacion;  
    }
    
    /**
	 * Metodo para generar el log tipo archivo de
	 * la cuenta de cobro modificada
	 * @param registroForm RegistroSaldoInicialCarteraForm
	 * @param usuario, usuario que modifico
	 * 
	 */
	private void generarLog(RegistroSaldoInicialCarteraForm registroForm,String usuario,double numCxc)
	    {
        
	    	registroForm.setLog(
		            					"\n============INFORMACION CUENTA DE COBRO MODIFICADA===== " +
		            					"\n*  Usuario [" +usuario+"] "+
		            					"\n*  Fecha ["+UtilidadFecha.getFechaActual()+"] " +
		            					"\n*  Hora  ["+UtilidadFecha.getHoraActual()+"] "+
		            					"\n*  Número Cuenta Cobro  ["+numCxc+"] "+	
										"\n========================================================\n\n\n ") ;
	        
	        LogsAxioma.enviarLog(ConstantesBD.logRegistroSaldoInicialCodigo,registroForm.getLog(),ConstantesBD.tipoRegistroLogModificacion,usuario);  
	     	        
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
					logger.warn("Problemas con la base de datos al abrir la conexion");
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
						logger.error("Error al tratar de cerrar la conexion con la fuente de datos MedicosXPoolAction. \n Excepcion: " +e);
					}
			}	
	
}
