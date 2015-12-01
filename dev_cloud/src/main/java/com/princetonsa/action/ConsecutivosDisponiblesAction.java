/*
 * @(#)ArticulosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

/**
 * @version 1.0, 29/06/2005
 * @author <a href="mailto:acardona@PrincetonSA.com">Angela Cardona</a>
 * @author [restructurada 1/12/2005] <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author [restructurada 22/01/2008] <a href="mailto:jearias@PrincetonSA.com">Jose Eduardo Arias Doncel</a>
 */
package com.princetonsa.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;

import com.princetonsa.actionform.ConsecutivosDisponiblesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.UsuarioBasico;

public class ConsecutivosDisponiblesAction extends Action {

	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Consecutivos disponibles
	 */
	private Logger logger = Logger.getLogger(ConsecutivosDisponiblesAction.class);
	private static final String CONSECUTIVO_POBLACION_CAPITADA = "consecutivo_autorizacion_poblacion_capitada";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Connection con = null; 
		try{
			if(form instanceof ConsecutivosDisponiblesForm )
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConsecutivosDisponiblesForm consecutivosForm = (ConsecutivosDisponiblesForm) form;
				HttpSession sesion = request.getSession();

				UsuarioBasico usuario = null;
				usuario = getUsuarioBasicoSesion(sesion);

				ConsecutivosDisponibles mundoConsecutivos = new ConsecutivosDisponibles ();
				String estado = consecutivosForm.getEstado();

				ActionErrors errores = new ActionErrors();

				consecutivosForm.setPager(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));

				logger.warn("[ConsecutivosDisponiblesAction] estado->"+consecutivosForm.getEstado());

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezarConsecutivo"))
				{					
					consecutivosForm.reset(true);	
					consecutivosForm.resetModulo();
					consecutivosForm.resetMapModulos();
					mundoConsecutivos.reset();
					mundoConsecutivos.setInstitucion(usuario.getCodigoInstitucionInt());
					consecutivosForm.setMapModulos(mundoConsecutivos.generarConsultaModulos(con));
					this.cerrarConexion(con);
					return mapping.findForward("ingresarModificarConsecutivo");
				}
				else if (estado.equals("generarFiltroModulo"))//empezar la modificaciï¿½n, inserciï¿½n ï¿½ busqueda avanzada
				{				    
					return this.generarFiltroModulo(con,consecutivosForm,mapping,usuario,mundoConsecutivos, errores, request);				    
				}	
				else if(estado.equals("generarFiltroAlmacen"))
				{
					return this.generarFiltroAlmacen(con,consecutivosForm,mapping,usuario,mundoConsecutivos);
				}
				else if(estado.equals("generarFiltroEmpresa"))
				{
					return this.generarFiltroEmpresa(con,consecutivosForm,mapping,usuario);
				}
				else if (estado.equals("salirGuardarConsecutivo"))
				{				  
					return this.salirGuardar(con,consecutivosForm,mapping,usuario,sesion,mundoConsecutivos, errores,request);  
				}
				else if(estado.equals("empezarConsultaConsecutivo"))
				{				    
					consecutivosForm.reset(true);	
					consecutivosForm.resetMapModulos();
					consecutivosForm.resetModulo();
					mundoConsecutivos.setInstitucion(usuario.getCodigoInstitucionInt());
					consecutivosForm.setMapModulos(mundoConsecutivos.generarConsultaModulos(con));
					this.cerrarConexion(con);
					return mapping.findForward("consultarConsecutivos");
				}			
				else if(estado.equals("ingresarNuevoConsecutivo"))//ingresar un registro nuevo
				{				   
					return this.ingresarNuevoConsecutivo(con,consecutivosForm,response,request); 
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsecutivosDisponiblesForm");
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
     * @param con
     * @param consecutivosForm
     * @param mapping
     * @param usuario
     * @param mundoConsecutivos
     * @return
     */
    private ActionForward generarFiltroAlmacen(Connection con, ConsecutivosDisponiblesForm consecutivosForm, ActionMapping mapping, UsuarioBasico usuario, ConsecutivosDisponibles mundoConsecutivos) 
    {        
        //para el caso especial de guardar consecutivos inventarios
        mundoConsecutivos.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoConsecutivos.setAlmacen(consecutivosForm.getAlmacen());
        consecutivosForm.setMapConsecInv(mundoConsecutivos.consultarConsecutivosInventarios(con,ConstantesBDInventarios.codigoConsecutivoTransXAlmacen,false,-1));        
        if(consecutivosForm.getAccion().equals("consulta"))
		{
		    this.cerrarConexion(con);
			return mapping.findForward("consultarConsecutivos");
		}
		else
		{
	        this.cerrarConexion(con);
			return mapping.findForward("ingresarModificarConsecutivo");
		}
    }
    
    
    /**
     * Filtra el Consecutivo de la factura por la empresa seleccionada
     * @param Connection con
     * @param ConsecutivosDisponiblesForm
     * @param ActionMapping mapping
     * @param UsuarioBasico usuario
     * */
    private ActionForward generarFiltroEmpresa(Connection con, ConsecutivosDisponiblesForm forma, ActionMapping mapping, UsuarioBasico usuario)
    {
    	forma.setMapConsecInv(ConsecutivosDisponibles.capturarInfoEmpresa(forma.getListMultiEmpresa(),forma.getEmpresa()));
    	UtilidadBD.closeConnection(con);
    	    	
    	if(forma.getAccion().equals("consulta")) 		 
 			return mapping.findForward("consultarConsecutivos"); 		
 		else 		 	     
 			return mapping.findForward("ingresarModificarConsecutivo"); 		
    }
    

    /**
     * @param con
     * @param consecutivosForm
     * @param mapping
     * @param usuario    
     * @param mundoConsecutivos
     * @return
     */
    private ActionForward generarFiltroModulo(Connection con, ConsecutivosDisponiblesForm consecutivosForm, ActionMapping mapping, UsuarioBasico usuario, ConsecutivosDisponibles mundoConsecutivos, ActionErrors errores,HttpServletRequest request) 
    {         
        ArrayList restricciones=new ArrayList();	    
	    mundoConsecutivos.setModulo(consecutivosForm.getModulo());
	    mundoConsecutivos.setInstitucion(usuario.getCodigoInstitucionInt());
	    ValoresPorDefecto.cargarValoresIniciales(con);
		String valor=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
		
		/*
		 * 	Sentencia comentada por la tarea 63852
		 *	consecutivosForm.setMapConsecInv(new HashMap());
		*/
		
		//para el caso especial de guardar consecutivos inventarios		
	    if(consecutivosForm.getModulo()==ConstantesBD.codigoModuloInventarios)
		{						
		    if(valor.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
		    {
		        restricciones.add(ConstantesBDInventarios.codigoConsecutivoTransUnicoSistema);	 
		        consecutivosForm.setCasoConsecInv(ConstantesBDInventarios.codigoConsecutivoTransXAlmacen);
		    }
		    else if(valor.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
		    {
		        restricciones.add(ConstantesBDInventarios.codigoConsecutivoTransXAlmacen); 
		        consecutivosForm.setMapConsecInv(mundoConsecutivos.consultarConsecutivosInventarios(con,ConstantesBDInventarios.codigoConsecutivoTransUnicoSistema,false,-1));
		        consecutivosForm.setCasoConsecInv(ConstantesBDInventarios.codigoConsecutivoTransUnicoSistema);
		        logger.info("Mapa ACTION ... "+consecutivosForm.getMapConsecInv());
		    }
		    else  
		    {
		        restricciones.add(ConstantesBDInventarios.codigoConsecutivoTransUnicoSistema); 
		        restricciones.add(ConstantesBDInventarios.codigoConsecutivoTransXAlmacen);
		    }						 
		}
	    
	    consecutivosForm.setEmpresa("");
	    
	    //Se evalua el flujo para el modulo de facturacion
	    if(consecutivosForm.getModulo() == ConstantesBD.codigoModuloFacturacion)
	    {	  	
	    	//Validación por anexo 959
	    	String manejaConsecutivoFacturaXCA=ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencion(usuario.getCodigoInstitucionInt());
	    	if (manejaConsecutivoFacturaXCA.equals(ConstantesBD.acronimoNo)) 
	    	{
		    	//se evalua si la institucion se constituye por instituciones asociadas
		    	if(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
		    	{
		    		//almacena la informacion de las empresas que pertenecen a la institucion    
		    		consecutivosForm.setListMultiEmpresa(Utilidades.obtenerEmpresasXInstitucion(con, usuario.getCodigoInstitucionInt()));	    	
		    	}
	    	}
	    	else if (manejaConsecutivoFacturaXCA.equals(ConstantesBD.acronimoSi))
	    	{
	    		errores.add("",	new ActionMessage("errors.notEspecific","Los consecutivos de Facturación se parametrizan en la funcionalidad de Centros de Atención "));
				saveErrors(request, errores);
	    	}
	    }
	    
	    //Anexo 959-Se hace una validación adicional para el modulo de tesoreria
	    if(consecutivosForm.getModulo() == ConstantesBD.codigoModuloTesoreria)
	    {	
	    	/* Anexo 63 -- Ya no se hace la validación
	    	String manejaConsecutivoTesoreriaXCA=ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(usuario.getCodigoInstitucionInt());
	    	if (manejaConsecutivoTesoreriaXCA.equals(ConstantesBD.acronimoSi)) 
	    	{
	    		errores.add("",	new ActionMessage("errors.notEspecific","Los consecutivos de Tesoreria se parametrizan en la funcionalidad de Centros de Atención "));
				saveErrors(request, errores);
	    	}*/	
	    }
	    
	    if (errores.isEmpty())
	    {
		    //Cambio Anexo 562 Validacion del tipo de consecutivo a manejar facturas varias
		    consecutivosForm.setModificaConsecutivoFacturasV(ConsecutivosDisponibles.consultaTipoConsecutivoFacturasVarias(con));
		    logger.info("\n\nVALOR DELLLLLLLLLL VALIDACION >>>>>>>>>>>>>>>>>>"+consecutivosForm.getModificaConsecutivoFacturasV()+"\n\n");
		    ///
		    
		    //Validación para determinar si se pueden modificar los consecutivos de notas pacientes
		    //según se el valor del parámetro Maneja Consecutivos Notas Paciente por centro atención
		    boolean manejaConsecutivosNotasPacienteXCentroAtencion = 
		    	(ValoresPorDefecto.getManejaConsecutivosNotasPacientesCentroAtencion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)) ? true : false;
		    consecutivosForm.setPermiteModificarConsecutivoNotasPaciente(!manejaConsecutivosNotasPacienteXCentroAtencion);
		    
		    consecutivosForm.setExistenNotasPaciente(Utilidades.existenNotasPaciente());
		    
			consecutivosForm.setMapConsecutivos(mundoConsecutivos.consultarConsecutivosXModulo(con,usuario.getCodigoInstitucionInt(),restricciones));		
		    //consecutivosForm.setMapConsecutivosBD(mundoConsecutivos.consultarConsecutivosXModulo(con,usuario.getCodigoInstitucionInt()));//copia de los registros de la BD, sin ninguna modificaciï¿½n
	    }
	    
		if(consecutivosForm.getAccion().equals("consulta")) 
		{           
            this.cerrarConexion(con);
			return mapping.findForward("consultarConsecutivos");
		}
		else
		{	       
            this.cerrarConexion(con);
			return mapping.findForward("ingresarModificarConsecutivo");
		}
    }

    /**
	 * Metodo para generar la actualizacion de 
	 * los consecutivos.
	 * @param con
	 * @param consecutivosForm
	 * @param mapping
	 * @param usuario
	 * @param sesion
	 * @param mundoConsecutivos
	 * @return
	 */
	private ActionForward salirGuardar(Connection con, ConsecutivosDisponiblesForm consecutivosForm, ActionMapping mapping, UsuarioBasico usuario, HttpSession sesion, ConsecutivosDisponibles mundoConsecutivos, ActionErrors errores,HttpServletRequest request) 
	{    
		String nombre="",valor="", anioVigencia="";		
		boolean resultado, checkbox;	                    
	    mundoConsecutivos.setInstitucion(usuario.getCodigoInstitucionInt());
	    mundoConsecutivos.setModulo(consecutivosForm.getModulo());	    
	    int numRegistros=Integer.parseInt(consecutivosForm.getMapConsecutivos("numRegistros")+"");
	    ActionErrors errorValidacionParametro=null;
	    
	    //Modificación Funcionalidad V 1.0 
	    //Se valida si los datos de un parámetro del map son válidos, si no es así no se realiza la transacción
        if(consecutivosForm.getModulo()==ConstantesBD.codigoModuloCapitacion){
        	errorValidacionParametro = 
        		validarConsecutivoAutorizacionPoblacionCapitada(con,mundoConsecutivos,consecutivosForm);
        }
        if(errorValidacionParametro!=null && !errorValidacionParametro.isEmpty()){
        	saveErrors(request, errorValidacionParametro);
        	consecutivosForm.setEstado("errorValidacionParametro");
        	return mapping.findForward("ingresarModificarConsecutivo");
        }
       
    	for(int k =0;k <numRegistros ;k++)
	    {	    	
	        nombre = consecutivosForm.getMapConsecutivos("nombre_"+k)+"";
	        logger.info("*********************VALOR NOMBRE CONSECUTIVO: "+nombre+"**************************************++");
	        valor = consecutivosForm.getMapConsecutivos("valor_"+k)+"";
	        anioVigencia = consecutivosForm.getMapConsecutivos("anio_vigencia_"+k)+"";
	        
	        checkbox=(consecutivosForm.getMapConsecutivos("checkBox_"+k)+"").equals("true")?true:false;	  
	        
	        HashMap map=new HashMap();
	    	
	    	map=mundoConsecutivos.consultarConsecutivo(con,nombre);  
	    	logger.info("mapa anteriores: "+map);
	    	logger.info("anioVigencia: "+anioVigencia);
	    	logger.info("valor: "+valor);
	    	    	
	    	int numReg=Integer.parseInt(map.get("numRegistros")+"");
	    	  
	    	
	    	if(numReg == 1)//existe el registro, entonces se modifica
	    	{
	    	    if(!valor.equals(map.get("valor_0")+"") || !anioVigencia.equals(map.get("anio_vigencia_0")+""))
	    	    {	    	        
		    	    resultado=mundoConsecutivos.insertarModificarTrans(con, nombre, valor, anioVigencia, usuario.getCodigoInstitucionInt(), checkbox,"modificar");
		    	    logger.info("resultado insercion modificacion: "+resultado);
			        if(!resultado)
		              logger.warn("No se pudo modificar el registro:"+nombre);
			        else 
			        {	
		              generarLog(consecutivosForm,k,usuario,sesion);
			        }
	    	    }
	    	}
	    	else//no existe el registro, entonces se inserta
	    	{
	    	    if(!valor.equals("") || !anioVigencia.equals(""))
	    	    {
		    	   resultado=mundoConsecutivos.insertarModificarTrans(con,nombre,valor,anioVigencia,usuario.getCodigoInstitucionInt(),checkbox,"insertar");
		            if(!resultado)
		                logger.warn("No se pudo insertar el registro:"+nombre);
		            else 
		            	logger.warn("insertï¿½ el registro");
	    	    }
	    	}	       
	    }
    	    
        
	    //para el caso especial de institucion multiEmpresa, Modulo Facturacion
	    if(consecutivosForm.getModulo() == ConstantesBD.codigoModuloFacturacion)
	    {	  	
	    	//se evalua si la institucion se constituye por instituciones asociadas
	    	if(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
	    	{
	    		if(!consecutivosForm.getEmpresa().equals(""))
	    		{
	    			resultado = guardarConsecutivoMultiEmpresa(con,mundoConsecutivos,consecutivosForm,usuario);	    	
	    			if(!resultado)
		                logger.warn("No se pudo Insertar/Actualizar el Consecutivo de MultiEmpresa ");
		            else 
		            	logger.warn("insertï¿½ el registro");
	    		}
	    	}
	    }
	    
	    //para el caso especial de guardar consecutivos inventarios
	    if(consecutivosForm.getCasoConsecInv().equals(ConstantesBDInventarios.codigoConsecutivoTransXAlmacen))	
	    {
		    if(consecutivosForm.getModulo()==ConstantesBD.codigoModuloInventarios && consecutivosForm.getAlmacen()!=ConstantesBD.codigoNuncaValido)			
		      if(!consecutivosForm.getMapConsecutivos().isEmpty())		      
		        guardarConsecutivoInventario(con,mundoConsecutivos,consecutivosForm,usuario);
	    }
	    else
	    {
	        if(consecutivosForm.getModulo()==ConstantesBD.codigoModuloInventarios)			
	            if(!consecutivosForm.getMapConsecutivos().isEmpty())		      
			        guardarConsecutivoInventario(con,mundoConsecutivos,consecutivosForm,usuario);   
	    } 
	    
	    consecutivosForm.reset(false);	     
	    ArrayList restricciones=new ArrayList();
	    consecutivosForm.setMapConsecutivos(mundoConsecutivos.consultarConsecutivosXModulo(con,usuario.getCodigoInstitucionInt(),restricciones));
	    //en el caso de consecutivo inventario por almacen	    
	    if(consecutivosForm.getCasoConsecInv().equals(ConstantesBDInventarios.codigoConsecutivoTransXAlmacen))
	    {
	        consecutivosForm.setMapConsecInv(mundoConsecutivos.consultarConsecutivosInventarios(con,ConstantesBDInventarios.codigoConsecutivoTransXAlmacen,false,-1));
	        consecutivosForm.setCasoConsecInv("");
	    }
	    
	    //mundoConsecutivos.reset();
	    //consecutivosForm.setMapConsecutivosBD(mundoConsecutivos.consultarConsecutivosXModulo(con,usuario.getCodigoInstitucionInt()));//copia de los registros de la BD, sin ninguna modificaciï¿½n    
	    return this.generarFiltroModulo(con,consecutivosForm,mapping,usuario,mundoConsecutivos, errores,request);
	}
	
	

	/**
	 * Guarda la informacion del consecutivo Multi empresa
	 * @param Connection con
	 * @param ConsecutivosDisponibles mundoConsecutivos
	 * @param ConsecutivosDisponiblesForm consecutivosForm
	 * @param UsuarioBasico usuario
	 * */
	private boolean guardarConsecutivoMultiEmpresa(
			Connection con,
			ConsecutivosDisponibles mundoConsecutivos,
			ConsecutivosDisponiblesForm forma, 
			UsuarioBasico usuario)
	{
		boolean checkbox=(forma.getMapConsecInv("check")+"").equals("true")?true:false;	  
		mundoConsecutivos.setMapConsecInv(forma.getMapConsecInv());
		mundoConsecutivos.setEmpresa(forma.getEmpresa());
		return mundoConsecutivos.insertarModificarTrans(con,"","","",usuario.getCodigoInstitucionInt(), checkbox,"insertarUpdateConsecMultiEmpresa");
	}
	
	
	private void guardarConsecutivoInventario(Connection con, ConsecutivosDisponibles mundoConsecutivos, ConsecutivosDisponiblesForm consecutivosForm, UsuarioBasico usuario)
	{
        mundoConsecutivos.setMapConsecInv(consecutivosForm.getMapConsecInv());
        mundoConsecutivos.setCasoConsecInv(consecutivosForm.getCasoConsecInv());
        mundoConsecutivos.setAlmacen(consecutivosForm.getAlmacen());
        mundoConsecutivos.insertarModificarTrans(con,"","","",usuario.getCodigoInstitucionInt(),false,"insertUpdateConsecInv");
	}
	/**
	 * @param con
	 * @param consecutivosForm
	 * @param response
	 * @param request
	 * @return
	 */
	private ActionForward ingresarNuevoConsecutivo(Connection con, ConsecutivosDisponiblesForm consecutivosForm, HttpServletResponse response, HttpServletRequest request) 
	{		
		int posicion = Integer.parseInt(consecutivosForm.getMapConsecutivos("numRegistros")+"");
        
		consecutivosForm.setMapConsecutivos("nombre_"+posicion,"");
		consecutivosForm.setMapConsecutivos("descripcion_"+posicion,"");
		consecutivosForm.setMapConsecutivos("valor_"+posicion,"");
		consecutivosForm.setMapConsecutivos("anio_vigencia_"+posicion,"");
		
		consecutivosForm.setMapConsecutivos("tipo_"+posicion,"MEM");//marcado como registro que pertenece a la memoria.
        posicion ++;
        consecutivosForm.setMapConsecutivos("numRegistros",posicion+"");
		
        //Verificar la ruta de la pï¿½gina que corresponda
        return this.redireccion(con,consecutivosForm,response,request,"ingresarModificarConsecutivosDispo.jsp");
	}
	
	/**
	 * Metodo implementado para posicionarse en la ultima
	 * pagina del pager.
	 * @param con, Connection con la fuente de datos.
	 * @param consecutivosForm ConsecutivosDisponiblesForm
	 * @param response HttpServletResponse
	 * @param request HttpServletRequest
	 * @param String enlace
	 * @return null
	 */
	public ActionForward redireccion (	Connection con,
	        											ConsecutivosDisponiblesForm consecutivosForm,
						            					HttpServletResponse response,
						            					HttpServletRequest request, String enlace)
	{
		consecutivosForm.setOffset(((int)((Integer.parseInt(consecutivosForm.getMapConsecutivos("numRegistros")+"")-1)/consecutivosForm.getMaxPageItems()))*consecutivosForm.getMaxPageItems());
	    if(request.getParameter("ultimaPage")==null)
	    {
	       if(consecutivosForm.getNumRegistros() > (consecutivosForm.getOffset()+consecutivosForm.getMaxPageItems()))
	           consecutivosForm.setOffset(((int)(consecutivosForm.getNumRegistros()/consecutivosForm.getMaxPageItems()))*consecutivosForm.getMaxPageItems());
	        try 
	        {
	            response.sendRedirect(enlace+"?pager.offset="+consecutivosForm.getOffset());
	        } 
	        catch (IOException e) 
	        {
	            
	            e.printStackTrace();
	        }
	    }
	    else
	    {    
	        String ultimaPagina=request.getParameter("ultimaPage");
	        
	        int posOffSet=ultimaPagina.indexOf("offset=")+7;
	        
	        if(consecutivosForm.getNumRegistros()>(consecutivosForm.getOffset()+consecutivosForm.getMaxPageItems()))
	            consecutivosForm.setOffset(consecutivosForm.getOffset()+consecutivosForm.getMaxPageItems());
	         
	        try 
	        {
	            
	            response.sendRedirect(ultimaPagina.substring(0,posOffSet)+consecutivosForm.getOffset());
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
	 * 
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		return con;
					
		try
		{
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
	 * Metodo para generar el log despues de la 
	 * modificaciï¿½n
	 * @param consecutivosForm ConsecutivosDisponiblesForm
	 * @param index int
	 */
	public void generarLog (ConsecutivosDisponiblesForm consecutivosForm,int index,UsuarioBasico usuario,HttpSession sesion)
	{
	    String log="";
	    usuario=(UsuarioBasico)sesion.getAttribute("usuarioBasico");
	    log=consecutivosForm.getLog() + 
	        							"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
	        							"\n*  Cï¿½digo [" +consecutivosForm.getMapConsecutivos("codigo_"+index)+""+"] "+
							 			"\n*  Descripciï¿½n ["+consecutivosForm.getMapConsecutivos("descripcion_"+index)+""+"] " +
							 			"\n*  Aï¿½o   ["+consecutivosForm.getMapConsecutivos("anio_vigencia_"+index)+""+"] "+
							 			"\n*  Consecutivo ["+consecutivosForm.getMapConsecutivos("valor_"+index)+""+"] "+
							 			"\n========================================================\n\n\n " ;       
	    LogsAxioma.enviarLog(ConstantesBD.logConsecutivosDisponiblesCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());  
	}
		 
	/**
	 * Mï¿½todo en que se cierra la conexiï¿½n (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexiï¿½n con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try
		{
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
	
	/**
	 * 
	 * Este método se encarga de validar para su registro el parámetro de Autorizaciones
	 * población capitada
	 * 
	 * @param ConsecutivosDisponibles mundoConsecutivos,
	 *		  ConsecutivosDisponiblesForm consecutivosForm 
	 * @return  ActionErrors
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionErrors validarConsecutivoAutorizacionPoblacionCapitada(Connection con,
			ConsecutivosDisponibles mundoConsecutivos, ConsecutivosDisponiblesForm consecutivosForm ){
		ActionErrors errores = new ActionErrors();
		int numRegistros=Integer.parseInt(consecutivosForm.getMapConsecutivos("numRegistros")+"");
		String nombreConsecutivo="";
		String valor="";
		String anioVigencia="";
		String descripcion="";
		MessageResources mensajes=MessageResources.getMessageResources(
				"com.servinte.mensajes.capitacion.ConsecutivosDisponiblesCapitacionForm");
		
		for(int k =0;k <numRegistros ;k++){	    	
	        nombreConsecutivo = consecutivosForm.getMapConsecutivos("nombre_"+k)+"";
	        
	        if(nombreConsecutivo.equals(CONSECUTIVO_POBLACION_CAPITADA)){
	        	valor = consecutivosForm.getMapConsecutivos("valor_"+k)+"";
		        anioVigencia = consecutivosForm.getMapConsecutivos("anio_vigencia_"+k)+"";
		        descripcion = consecutivosForm.getMapConsecutivos("descripcion_"+k)+"";
		        if(!UtilidadTexto.isEmpty(anioVigencia)){
		        	Calendar fechaActual = Calendar.getInstance();
		        	int diferenciaAnios = (fechaActual.get(Calendar.YEAR)+1) - (Integer.valueOf(anioVigencia));
		        	if(diferenciaAnios<0){
		        		errores.add("Registro No válido", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("consecutivosDisponiblesCapitacion.anioNoValido",descripcion)));
		        	}else{
		        		if(diferenciaAnios>1){
			    			errores.add("Registro No válido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("consecutivosDisponiblesCapitacion.anioNoValido",descripcion)));
			    		}
		        	}		    		
		        }else{
		        	if(UtilidadTexto.isEmpty(valor)){
		        		errores.add("Registro No válido", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("consecutivosDisponiblesCapitacion.valorAñioNulos",descripcion)));
		        	}
		        }
		        if(errores.isEmpty()){
		        	/*
		        	 * esta validacion no aplica, ya que no dejaria modificar los otros consecutivos.
		        	String usado = mundoConsecutivos.consultarConsecutivoUsado(con, nombreConsecutivo);		        	
		        	if(!(UtilidadTexto.isEmpty(usado)) && UtilidadTexto.getBoolean(usado)){
	            		errores.add("Registro No se puede modificar", new ActionMessage("errors.notEspecific", 
	    					mensajes.getMessage("consecutivosDisponiblesCapitacion.registroUsado",descripcion)));
		        	}
		        	*/
		        }
		        break;
	        }
	    }		
		return errores;
	 }
	
}