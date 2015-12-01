package com.princetonsa.action.glosas;

/*
 * @author: Juan Alejandro Cardona
 * date: octubre 2008
 */


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.glosas.ConceptosRespuestasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConceptosRespuestas;


public class ConceptosRespuestasAction extends Action {
	
	// Objeto para manejar los logs de esta clase
	Logger logger = Logger.getLogger(ConceptosRespuestasAction.class);

	
	/**	 * Método execute del Action	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

		Connection con = null;
		try{
			if (form instanceof ConceptosRespuestasForm) {



				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();

				if(con == null)	{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConceptosRespuestasForm conceptosForm = (ConceptosRespuestasForm) form;
				ConceptosRespuestas mundoConceptos = new ConceptosRespuestas();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = conceptosForm.getEstado();

				logger.info("===========");
				logger.warn("[ConceptosRespuestas]--->Estado: "+estado);

				if(estado == null) {
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar")) {
					conceptosForm.resetMsg();
					return accionEmpezar(con, mundoConceptos, conceptosForm, usuario, mapping);
				}


				else if (estado.equals("empezarRespuestaGlosas")) {
					conceptosForm.reset();	
					mundoConceptos.reset();

					// Se cargan los conceptos glosa
					mundoConceptos.cargarConceptosGlosa(con, conceptosForm, usuario);

					// Se cargan los conceptos ajuste
					mundoConceptos.cargarConceptosAjuste(con, conceptosForm, usuario);

					conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosRespuestasGlosas(con,false,usuario.getCodigoInstitucionInt()));
					conceptosForm.setMapConceptosBD((HashMap) conceptosForm.getMapConceptos().clone());

					logger.info("\n\nMAPA CONCEPTOS RESPUESTA ACTION----------->"+conceptosForm.getMapConceptos());

					activarEliminar(con, conceptosForm, mundoConceptos);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("ingresarModificarRespuestasGlosas");
				}			


				//////////////////////////////MANEJO DE LOS ESTADOS PARA CONCEPTOS RESPUESTAS GLOSAS///////////////////////////////
				else if(estado.equals("GuardarConceptoRespuestasGlosas")) {
					return this.salirGuardarRepuestasGlosas(con,conceptosForm, mundoConceptos, response,usuario);
				}

				else if(estado.equals("ingresarNuevoConceptoRespuestaGlosa")) {
					return this.ingresarNuevoConceptoRespuestaGlosa(con,conceptosForm,response,request); 
				}

				else if(estado.equals("eliminarConceptoRespuestaGlosa")) {
					return this.eliminarRespuestaGlosas(con,conceptosForm,response,request,mapping);
				}

				else if (estado.equals("busquedaAvanzadaRespuestasGlosas")) {
					conceptosForm.resetMsg();
					conceptosForm.setCodigoConcepto("");
					conceptosForm.setDescripcion("");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("ingresarModificarRespuestasGlosas");
				}

				else if (estado.equals("ejecutarBusquedaRespuestasGlosas")) {
					return this.busquedaAvanzadaRespuestasGlosas(con,conceptosForm,mapping,usuario);
				}

				else if (estado.equals("consultarRespuestasGlosas")) {
					conceptosForm.reset();	
					mundoConceptos.reset();
					conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosRespuestasGlosas(con,false,usuario.getCodigoInstitucionInt()));
					conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("consultarRespuestasGlosas");
				}

				else if (estado.equals("busquedaAvanzadaRespuestasGlosasConsulta")) {
					// Se cargan los conceptos glosa");
					mundoConceptos.cargarConceptosGlosa(con, conceptosForm, usuario);

					// Se cargan los conceptos ajuste");
					mundoConceptos.cargarConceptosAjuste(con, conceptosForm, usuario);

					conceptosForm.setCodigoConcepto("");
					conceptosForm.setDescripcion("");
					//conceptosForm.setCuentaDebito("");
					//conceptosForm.setCuentaCredito("");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("consultarRespuestasGlosas");
				}

				else if (estado.equals("ejecutarBusquedaRespuestasGlosasConsulta")) {
					return this.busquedaAvanzadaRespuestasGlosas(con,conceptosForm,mapping,usuario);  
				}		

				else if (estado.equals("ordenarRespuestasGlosas")) {
					conceptosForm.resetMsg();
					String[] indices={"codigoRespuestaConcepto_","descripcion_", "tipoConcepto_", "conceptoAjuste_", "descripAjuste_", "tipoCartera_", "nombreTipoCartera_"};
					return this.ordenarGlosasYRespuestasGlosas(con,conceptosForm,mapping,indices,"consultarRespuestasGlosas");  
				}
				////////////////////////////FIN MANEJO DE LOS ESTADOS PARA CONCEPTOS RESPUESTAS GLOSAS/////////////////////////////


				else {
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else {
				logger.error("El form no es compatible con el form de ConceptosRespuestas");
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


	private void activarEliminar(Connection con,
			ConceptosRespuestasForm conceptosForm,
			ConceptosRespuestas mundoConceptos) {
		for(int k=0;k< Utilidades.convertirAEntero(conceptosForm.getMapConceptos("numRegistros")+"");k++)
		{						
			if(mundoConceptos.consultarConceptosRespuesta(con, conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+k)+""))
			{
				conceptosForm.setMapConceptos("puedoeliminar_"+k, ConstantesBD.acronimoSi);
			}
			else
				conceptosForm.setMapConceptos("puedoeliminar_"+k, ConstantesBD.acronimoNo);
		}
	}

	
	/**	 * Metodo que carga los select con la informacion inicial y resetea la forma
	 * @param con, forma, usuario, mapping
	 * @return	 */
	private ActionForward accionEmpezar(Connection con, ConceptosRespuestas mundo, ConceptosRespuestasForm forma, UsuarioBasico usuario, ActionMapping mapping) {
/*		forma.resetMsg();

		// Se cargan los datos de la via de Ingreso
		forma.setFrmViaIngreso(Utilidades.obtenerViasIngresoTipoPaciente(con));
		//Cargamos los centros de Costo Inicialmente
		this.accionRecargar(con, mundo, forma, usuario, mapping);
*/
	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	
	/**
	 * @param con, conceptosForm, response, request
	 * @return	 */
	private ActionForward ingresarNuevoConceptoRespuestaGlosa(Connection con, ConceptosRespuestasForm conceptosForm, HttpServletResponse response, HttpServletRequest request) {
		conceptosForm.resetMsg();
		int posicion = Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");

		conceptosForm.setMapConceptos("codigoRespuestaConcepto_"+posicion,"");
		conceptosForm.setMapConceptos("descripcion_"+posicion,"");
		conceptosForm.setMapConceptos("tipoConcepto_"+posicion,"");
		conceptosForm.setMapConceptos("conceptoGlosa_"+posicion,"");
		conceptosForm.setMapConceptos("conceptoAjuste_"+posicion,"");
		conceptosForm.setMapConceptos("tipo_"+posicion,"MEM");
		conceptosForm.setMapConceptos("existeRelacion_"+posicion,"false");
		posicion ++;
		conceptosForm.setMapConceptos("numRegistros",posicion+"");

		return this.redireccion(con,conceptosForm,response,request,"ingresarModificarConceptosRespuestasGlosas.jsp");  	
     }
	
	

    /** Metodo implementado para posicionarse en la ultima pagina del pager.
     * @param con, Connection con la fuente de datos.
     * @param poolesForm ConceptosCarteraForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     * @param String enlace
     * @return null
     */
    public ActionForward redireccion(Connection con, ConceptosRespuestasForm conceptosForm, HttpServletResponse response, HttpServletRequest request, String enlace) {

    	conceptosForm.setOffset(((int)((Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"")-1)/conceptosForm.getMaxPageItems()))*conceptosForm.getMaxPageItems());

    	if(request.getParameter("ultimaPage")==null) {
    		if(conceptosForm.getNumRegistros() > (conceptosForm.getOffset()+conceptosForm.getMaxPageItems()))
    			conceptosForm.setOffset(((int)(conceptosForm.getNumRegistros()/conceptosForm.getMaxPageItems()))*conceptosForm.getMaxPageItems());
    		try {
    			response.sendRedirect(enlace+"?pager.offset="+conceptosForm.getOffset());
    		} 
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	}

    	else {    
    		String ultimaPagina=request.getParameter("ultimaPage");
    		int posOffSet=ultimaPagina.indexOf("offset=")+7;
            //conceptosForm.setOffset((Integer.parseInt(ultimaPagina.substring(posOffSet,ultimaPagina.length() ))));
            if(conceptosForm.getNumRegistros()>(conceptosForm.getOffset()+conceptosForm.getMaxPageItems()))
                conceptosForm.setOffset(conceptosForm.getOffset()+conceptosForm.getMaxPageItems());
             
            try {
            	response.sendRedirect(ultimaPagina.substring(0,posOffSet)+conceptosForm.getOffset());
            } 
            catch (IOException e) {
            	e.printStackTrace();
            }
    	}
    	UtilidadBD.closeConnection(con); // cerrarConexion(con);
    	return null;
    }
	
	

    /**
     * @param con, conceptosForm, response, usuario
     * @return  */
	private ActionForward salirGuardarRepuestasGlosas(Connection con, ConceptosRespuestasForm conceptosForm, ConceptosRespuestas mundoConceptos, HttpServletResponse response, UsuarioBasico usuario) {

		//conceptosForm.resetMsg();
		
		HashMap datosTmp = new HashMap();
		
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean enTransaccion=false;
		int numeroRegistros=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
		
		logger.info("=============");
		logger.info("====> Numero de Registros:" + numeroRegistros);
		
		//manejo de acceso a la BD en transaccion
		try {
			enTransaccion=myFactory.beginTransaction(con);
	        //ConceptosRespuestas mundoConceptos = new ConceptosRespuestas ();


	        //eliminar - que ¿?
	        int regEliminados=Integer.parseInt(conceptosForm.getMapConceptos("registrosEliminados")+"");

	        for(int i=0;i<regEliminados;i++) {
	        	if(enTransaccion) {
	        		enTransaccion = mundoConceptos.eliminarConceptoRespuestaGlosas(con,(conceptosForm.getMapConceptos("codigoRespuestaConceptoAntiguoEliminado_"+i)+""),usuario.getCodigoInstitucionInt());
	        		generarLogRespuestasGlosas(conceptosForm,usuario,true,i);
	        	}
	        	else {
	        		i=regEliminados;
	        	}
	        }

	        
	        //insertar modificar.
	        for(int k=0;k<numeroRegistros;k++) {

	        	if(enTransaccion) {

	        		//modificar
		        	if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("BD")&& enTransaccion) {
		        		if(registroModificadoConceptoRespuestaGlosa(conceptosForm,k)) {
		        			
		        			logger.info("Ingreso al Modificar <======= ");
		        			
			        		datosTmp.put("tipoConcepto", conceptosForm.getMapConceptos("tipoConcepto_"+k)+"");
			        		datosTmp.put("conceptoGlosa", conceptosForm.getMapConceptos("conceptoGlosa_"+k)+"");
			        		datosTmp.put("conceptoAjuste", conceptosForm.getMapConceptos("conceptoAjuste_"+k)+"");
			        		datosTmp.put("Usuario", usuario.getLoginUsuario());
		        			
		        			enTransaccion = mundoConceptos.modificarConceptoRespuestaGlosas(con,(conceptosForm.getMapConceptosBD("codigoRespuestaConceptoAntiguo_"+k)+""),usuario.getCodigoInstitucionInt(),(conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+k)+""),(conceptosForm.getMapConceptos("descripcion_"+k)+""), datosTmp);
		        			generarLogRespuestasGlosas(conceptosForm,usuario,false,k);
		        		}
		        	}

		        	//insertar
		        	if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("MEM")&& enTransaccion) {
		        		
	        			logger.info("Ingreso al Insertar <======= ");
		        		
		        		datosTmp.put("tipoConcepto", conceptosForm.getMapConceptos("tipoConcepto_"+k)+"");
		        		datosTmp.put("conceptoGlosa", conceptosForm.getMapConceptos("conceptoGlosa_"+k)+"");
		        		datosTmp.put("conceptoAjuste", conceptosForm.getMapConceptos("conceptoAjuste_"+k)+"");
		        		datosTmp.put("Usuario", usuario.getLoginUsuario());
		        		
		        		enTransaccion=mundoConceptos.insertarConceptoRespuestaGlosas(con, (conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+k)+""), usuario.getCodigoInstitucionInt(), (conceptosForm.getMapConceptos("descripcion_"+k)+""), datosTmp);
		        	}
	        	}
	        	else {
	        		k=numeroRegistros;
	        	}
	        }

	        if(enTransaccion) {
				conceptosForm.setMensaje(new ResultadoBoolean(true, "SE GUARDARON LOS CAMBIOS CON ÉXITO!!!"));
	        	myFactory.endTransaction(con);
	        }
	        else{
				conceptosForm.setMensaje(new ResultadoBoolean(true, "ERROR!. LOS CAMBIOS NO SE GUARDARON!!!"));
	        	myFactory.abortTransaction(con);
	        }
		}
		catch(SQLException e) {
			conceptosForm.setMensaje(new ResultadoBoolean(true, "ERROR!. LOS CAMBIOS NO SE GUARDARON!!!"));
			logger.error("Se presento error al guardar los datos "+e);
		}
		
		UtilidadBD.closeConnection(con); //.cerrarConexion(con);
		
		try {
			response.sendRedirect("conceptosRespuestas.do?estado=empezarRespuestaGlosas");
		}
		
		catch (IOException e1) {
			e1.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
        return null;
	}
    
    

	/**
	 * @param con, conceptosForm, response, request, mapping
	 * @return */
	private ActionForward eliminarRespuestaGlosas(Connection con, ConceptosRespuestasForm conceptosForm, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping) {
		conceptosForm.resetMsg();
		int pos=conceptosForm.getIndex();
		ActionErrors errores = new ActionErrors();
		int numReg=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(conceptosForm.getMapConceptos("registrosEliminados")+"");
		
		logger.info("puedo eliminar->"+conceptosForm.getMapConceptos("puedoeliminar_"+pos)+"");
		
		if((conceptosForm.getMapConceptos("puedoeliminar_"+pos)+"").equals(ConstantesBD.acronimoNo))	
		{
			errores.add("descripcion",new ActionMessage("prompt.generico","No es posible eliminar el registro. "));
		}
		
		if(!errores.isEmpty())
			saveErrors(request,errores);
		else
		{	
			if(!UtilidadTexto.getBoolean(conceptosForm.getMapConceptos("existeRelacion_"+pos)+"")) {
				if((conceptosForm.getMapConceptos("tipo_"+pos)+"").equals("BD")) {
					conceptosForm.setMapConceptos("codigoRespuestaConceptoEliminado_"+regEliminados,conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+pos));
					conceptosForm.setMapConceptos("codigoRespuestaConceptoAntiguoEliminado_"+regEliminados,conceptosForm.getMapConceptos("codigoRespuestaConceptoAntiguo_"+pos));
					conceptosForm.setMapConceptos("descripcionEliminado_"+regEliminados,conceptosForm.getMapConceptos("descripcion_"+pos));
			        conceptosForm.setMapConceptos("registrosEliminados",(regEliminados+1)+"");
				}
	
				for(int i=pos;i<ultimoRegistro;i++) {
					conceptosForm.setMapConceptos("codigoRespuestaConcepto_"+i,conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+(i+1)));
					conceptosForm.setMapConceptos("descripcion_"+i,conceptosForm.getMapConceptos("descripcion_"+(i+1)));
					conceptosForm.setMapConceptos("tipo_"+i,conceptosForm.getMapConceptos("tipo_"+(i+1)));
					conceptosForm.setMapConceptos("existeRelacion_"+i,conceptosForm.getMapConceptos("existeRelacion_"+(i+1)));
					conceptosForm.setMapConceptos("codigoRespuestaConceptoAntiguo_"+i,conceptosForm.getMapConceptos("codigoRespuestaConceptoAntiguo_"+(i+1)));
	
					//tambien los registros de la BD
					conceptosForm.setMapConceptosBD("codigoRespuestaConcepto_"+i,conceptosForm.getMapConceptosBD("codigoRespuestaConcepto_"+(i+1)));
					conceptosForm.setMapConceptosBD("descripcion_"+i,conceptosForm.getMapConceptosBD("descripcion_"+(i+1)));
					conceptosForm.setMapConceptosBD("tipo_"+i,conceptosForm.getMapConceptosBD("tipo_"+(i+1)));
					conceptosForm.setMapConceptosBD("existeRelacion_"+i,conceptosForm.getMapConceptosBD("existeRelacion_"+(i+1)));
					conceptosForm.setMapConceptosBD("codigoRespuestaConceptoAntiguo_"+i,conceptosForm.getMapConceptosBD("codigoRespuestaConceptoAntiguo_"+(i+1)));
				}
				conceptosForm.getMapConceptos().remove("codigoRespuestaConcepto_"+ultimoRegistro);
				conceptosForm.getMapConceptos().remove("descripcion_"+ultimoRegistro);
				conceptosForm.getMapConceptos().remove("tipo_"+ultimoRegistro);
				conceptosForm.getMapConceptos().remove("existeRelacion_"+ultimoRegistro);
				conceptosForm.getMapConceptos().remove("codigoRespuestaConceptoAntiguo_"+ultimoRegistro);
	
				//tambien los de la bd
				conceptosForm.getMapConceptosBD().remove("codigoRespuestaConcepto_"+ultimoRegistro);
				conceptosForm.getMapConceptosBD().remove("descripcion_"+ultimoRegistro);
				conceptosForm.getMapConceptosBD().remove("tipo_"+ultimoRegistro);
				conceptosForm.getMapConceptosBD().remove("existeRelacion_"+ultimoRegistro);
				conceptosForm.getMapConceptosBD().remove("codigoRespuestaConceptoAntiguo_"+ultimoRegistro);
				
				//actualizando numero de registros.
				conceptosForm.setMapConceptos("numRegistros",ultimoRegistro+"");
	
				if(conceptosForm.getIndex()==ultimoRegistro) {
					return this.redireccion(con,conceptosForm,response,request,"ingresarModificarConceptosRespuestasGlosas.jsp");
				}
			}
			else {
				//En caso de que toque mostrar la opcion eliminar para registros que no se pueden, aqui posiblemente
				//podría generar el mensaje.
			}
		}
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("ingresarModificarRespuestasGlosas");
	}
   
    
 
	/**
	 * @param conceptosForm, k
	 * @return	 */
	private boolean registroModificadoConceptoRespuestaGlosa(ConceptosRespuestasForm conceptosForm, int k) {

		return (
				!((conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+k)+"").equals(conceptosForm.getMapConceptosBD("codigoRespuestaConcepto_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("descripcion_"+k)+"").equals(conceptosForm.getMapConceptosBD("descripcion_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("tipoConcepto_"+k)+"").equals(conceptosForm.getMapConceptosBD("tipoConcepto_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("conceptoGlosa_"+k)+"").equals(conceptosForm.getMapConceptosBD("conceptoGlosa_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("conceptoAjuste_"+k)+"").equals(conceptosForm.getMapConceptosBD("conceptoAjuste_"+k)+""))
				);
	}
	
	
	/**
	 * @param conceptosForm, usuario, eliminacion, pos
	 */
	private void generarLogRespuestasGlosas(ConceptosRespuestasForm conceptosForm, UsuarioBasico usuario, boolean eliminacion, int pos) {
		String log = "";
		int tipoLog=0;

		if(eliminacion) {
			
			logger.info("\n\nMAPA CONCEPTOS RESPUESTA--->"+conceptosForm.getMapConceptos());
	            log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
		        			 "\n*  Código Respuesta Concepto [" +conceptosForm.getMapConceptos("codigoRespuestaConceptoEliminado_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcionEliminado_"+pos)+""+"] " +
		        			 "\n*  Tipo Concepto ["+conceptosForm.getMapConceptosBD("tipoConcepto_"+pos)+""+"] " +
		        			 "\n*  Concepto Ajuste ["+conceptosForm.getMapConceptosBD("descripAjuste_"+pos)+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
        }

		if(!eliminacion) {
			logger.info("\n\nMAPA CONCEPTOS RESPUESTA BD--->"+conceptosForm.getMapConceptosBD());
			
			logger.info("\n\nMAPA CONCEPTOS RESPUESTA--->"+conceptosForm.getMapConceptos());
			
	            log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
		        			 "\n*  Código Respuesta Concepto [" +conceptosForm.getMapConceptosBD("codigoRespuestaConceptoAntiguo_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcion_"+pos)+""+"] " +
		        			 "\n*  Tipo Concepto ["+conceptosForm.getMapConceptosBD("tipoConcepto_"+pos)+""+"] " +
		        			 "\n*  Concepto Ajuste ["+conceptosForm.getMapConceptosBD("descripAjuste_"+pos)+""+"] " +
		        			 "\n   ===========INFORMACION MODIFICADA========== 	" +
		        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+pos)+""+"] " +
		        			 "\n*  Tipo Concepto ["+conceptosForm.getMapConceptosBD("tipoConcepto_"+pos)+""+"] " +
		        			 "\n*  Concepto Ajuste ["+conceptosForm.getMapConceptosBD("descripAjuste_"+pos)+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogModificacion;
        }        
        LogsAxioma.enviarLog(ConstantesBD.logConceptosRespuestasGlosasCodigo,log,tipoLog,usuario.getLoginUsuario());		
        
        logger.info("\n\nSE GUARDO EL LOG EXITOSAMENTE");
     }
    
 

	/**
	 * @param con, conceptosForm, mapping, usuario
	 * @return	 */
	private ActionForward busquedaAvanzadaRespuestasGlosas(Connection con, ConceptosRespuestasForm conceptosForm, ActionMapping mapping, UsuarioBasico usuario) {
		ConceptosRespuestas mundoConceptos = new ConceptosRespuestas ();
		
		conceptosForm.setMapConceptos(new HashMap());
		mundoConceptos.reset();
		mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());
		mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
		conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosRespuestasGlosas(con,true,usuario.getCodigoInstitucionInt()));
		conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
        
		if(conceptosForm.getAccion().equals("busquedaAvanzadaRespuestasGlosasConsulta")) {
			conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            UtilidadBD.closeConnection(con);	//.cerrarConexion(con);        
            return mapping.findForward("consultarRespuestasGlosas");
        }

		else if(conceptosForm.getAccion().equals("busquedaAvanzadaRespuestasGlosas")) {
    		conceptosForm.setMapConceptosBD(new HashMap());
    		mundoConceptos.reset();
    		mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());
    		mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
    		conceptosForm.setMapConceptosBD((HashMap) conceptosForm.getMapConceptos().clone());
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            UtilidadBD.closeConnection(con); 	//.cerrarConexion(con);        
            return mapping.findForward("ingresarModificarRespuestasGlosas");
        }            

		else {
            logger.warn("accion no valida dentro de la busqueda avanzada");
            return null;
        }
	}
	
	
	private ActionForward ordenarGlosasYRespuestasGlosas(Connection con, ConceptosRespuestasForm conceptosForm, ActionMapping mapping, String[] indices, String forward) {
        conceptosForm.setMapConceptos(Listado.ordenarMapa(indices,
										                conceptosForm.getPatronOrdenar(),
										                conceptosForm.getUltimoPatron(),
										                conceptosForm.getMapConceptos(),
										                conceptosForm.getNumRegistros()));
        conceptosForm.setUltimoPatron(conceptosForm.getPatronOrdenar());
        UtilidadBD.closeConnection(con);	//.cerrarConexion(con);
        return mapping.findForward(forward);	
	}
	
	
}