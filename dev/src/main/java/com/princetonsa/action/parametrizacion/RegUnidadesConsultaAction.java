/*
 * Creado  17/08/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.parametrizacion.RegUnidadesConsultaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.BusquedaServiciosGenerica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.RegistroUnidadesConsulta;
import com.princetonsa.pdf.UnidadConsultaPdf;

/**
 * Clase para manejar
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class RegUnidadesConsultaAction extends Action
{
    private Logger logger=Logger.getLogger(RegUnidadesConsultaAction.class);
    RegUnidadesConsultaForm regUniConForm;
    ResultSetDecorator rs;
   
       
    /**
     * Cierre de Conexion  
     * */    
    private void closeConnection(Connection con)
    {
	   try{
			if (con!=null && !con.isClosed())
				UtilidadBD.closeConnection(con);
			
			}
	   catch(SQLException e){
			return;
	   }
	}
    
   
	/**
	 * Mï¿½todo encargado de el flujo y control de la funcionalidad	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	 public ActionForward execute (ActionMapping mapping,
								   ActionForm form,  
								   HttpServletRequest request,
								   HttpServletResponse response) throws Exception
								   {

		 RegistroUnidadesConsulta mundoUnidadesCon=new RegistroUnidadesConsulta();         
		 ActionErrors errores = new ActionErrors();
		 Connection con=null;


		 try{
			 con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
		 }
		 catch(SQLException e)
		 {
			 logger.warn("No se pudo abrir la conexiï¿½n"+e.toString());           
		 }

		 try {
			 if(form instanceof RegUnidadesConsultaForm)
			 {
				 regUniConForm=(RegUnidadesConsultaForm) form;
				 String estado=regUniConForm.getEstado();
				 HttpSession session=request.getSession();
				 UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

				 //borrar esta salida
				 logger.warn("estado="+estado);


				 if(estado.equals("empezar"))  
				 {
					 mundoUnidadesCon.clean();
					 regUniConForm.clean();
					 regUniConForm.cleanChecks();
					 regUniConForm.setEspecialidadesMap(Utilidades.obtenerEspecialidades());
					 //regUniConForm.setCheckEstado("");
					 UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
					 int institucion = usuario.getCodigoInstitucionInt();
					 String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion);
					 regUniConForm.setUtilizaProgramasOdonto(utilizaProgramasOdonto);
					 this.closeConnection(con);
					 return mapping.findForward("principal");			
				 }

				 if(estado.equals("volver"))
				 {
					 HashMap nuevo= new HashMap();
					 regUniConForm.setServicios(nuevo);
					 regUniConForm.setServicios("numeroServicios", "0");
					 this.closeConnection(con);
					 return mapping.findForward("principal");			
				 }

				 if(estado.equals("insertarServicio"))  
				 {
					 this.closeConnection(con);
					 return validarServiciosBusquedaGenerica(regUniConForm, mapping);

				 }
				 if(estado.equals("eliminarServicio"))  
				 {
					 this.closeConnection(con);
					 return eliminarServicio(regUniConForm,mapping,"principal");			
				 }
				 if(estado.equals("eliminarServicioModificacion"))  
				 {
					 this.closeConnection(con);
					 return eliminarServicio(regUniConForm,mapping,"modificarEleccion");			
				 }
				 if(estado.equals("guardar"))
				 {
					 llenarMundo(mundoUnidadesCon,regUniConForm);

					 if(mundoUnidadesCon.insertar(con))
						 regUniConForm.setEstado("operacionTrue");
					 else
						 regUniConForm.setEstado("empezar");

					 regUniConForm.setNomEspecialidadUniAgenda(mundoUnidadesCon.obtenerEspecialidad(con, 
							 Utilidades.convertirAEntero(regUniConForm.getEspecialidadUniAgenda())));

					 regUniConForm.clean();
					 regUniConForm.setCheckEstado("");	           
					 regUniConForm.setCheckTemp("");

					 //Llamado al Resumen de la Unidad de Consulta
					 regUniConForm.setCodigoT(mundoUnidadesCon.getCodigoT());
					 regUniConForm.setTiposAtencion(mundoUnidadesCon.getTiposAtencion());
					 regUniConForm.setColorFondo(mundoUnidadesCon.getColorFondo());
					 mundoUnidadesCon.clean();
					 Consulta(con,regUniConForm, mundoUnidadesCon);
					 regUniConForm.cleanChecks();
					 regUniConForm.setEstado("empezar");		       
					 regUniConForm.setIndicadorOperaciones(ConstantesBD.acronimoNo);		       
					 this.closeConnection(con);
					 return mapping.findForward("resumenUnidadC");
				 }
				 if(estado.equals("modificar"))
				 {
					 mundoUnidadesCon.clean();
					 regUniConForm.clean();
					 regUniConForm.cleanChecks();
					 regUniConForm.setCheckEstado("");
					 this.closeConnection(con);
					 return mapping.findForward("modificacion");
				 }
				 if(estado.equals("consultaAvanzada"))
				 {
					 mundoUnidadesCon.clean();
					 regUniConForm.clean();
					 regUniConForm.cleanChecks();
					 regUniConForm.setCheckEstado("");
					 this.closeConnection(con);
					 return mapping.findForward("modificacion");
				 }
				 if(estado.equals("especialiadades"))
				 {
					 regUniConForm.setEspecialidadesMap(Utilidades.obtenerEspecialidades());
					 this.closeConnection(con);
					 return mapping.findForward("especialidades");
				 }
				 if(estado.equals("busquedaAvanzada"))
				 {
					 consultaAvanzada(con,regUniConForm,mundoUnidadesCon);
					 mundoUnidadesCon.clean();
					 regUniConForm.clean();
					 regUniConForm.cleanChecks();
					 regUniConForm.setCheckEstado("");
					 regUniConForm.setEstado("consultarImprimir");
					 this.closeConnection(con);
					 return mapping.findForward("resumenConsultaAvanzada");
				 } 
				 if(estado.equals("consultar"))
				 {
					 consultaAvanzada(con,regUniConForm,mundoUnidadesCon);
					 mundoUnidadesCon.clean();
					 regUniConForm.clean();
					 regUniConForm.cleanChecks();
					 regUniConForm.setCheckEstado("");
					 regUniConForm.setEstado("modificar");
					 this.closeConnection(con);
					 return mapping.findForward("resumenConsultaAvanzada");
				 } 
				 if(estado.equals("modificarEleccion"))
				 {
					 return modificarEleccion(mapping, mundoUnidadesCon, con, request);
				 }
				 if(estado.equals("guardarModificacion"))
				 {
					 errores = validacionesGuardarUnidad(regUniConForm, errores);

					 if(!errores.isEmpty())
					 {
						 saveErrors(request,errores);	
						 regUniConForm.setIndicadorOperaciones(ConstantesBD.acronimoNo);
						 UtilidadBD.closeConnection(con);			
						 return mapping.findForward("modificarEleccion");
					 }	
					 else	    		   
						 return this.accionGuardarModificacion(con,regUniConForm,mapping, medico);	          
				 }
				 if(estado.equals("eliminarEleccion"))
				 {
					 Consulta(con,regUniConForm, mundoUnidadesCon);
					 //generar cabecera del log
					 empezarTransaccion(con);
					 int validar=mundoUnidadesCon.eliminar(con,regUniConForm.getCodigoT());
					 if(validar <= 0)
					 {
						 abortarTransaccion(con);
						 logger.warn("No se pudo eliminar el registro de la Unidad de Consulta: "+regUniConForm.getCodigoT());		        	
						 errores.add("Eliminando Unidad Consulta", new ActionMessage("error.unidadCon.eliminarUnidad"));
						 regUniConForm.setIndicadorOperaciones(ConstantesBD.acronimoNo);
						 saveErrors(request, errores);
					 }
					 else
					 {
						 //generar log
						 terminarTransaccion(con);
						 regUniConForm.setIndicadorOperaciones(ConstantesBD.acronimoSi);
						 consultaAvanzada(con,regUniConForm,mundoUnidadesCon);
					 }
					 regUniConForm.setEstado("modificar");	 
					 this.closeConnection(con);
					 return mapping.findForward("resumenConsultaAvanzada");		        
				 }
				 if(estado.equals("consultarImprimir"))
				 {
					 regUniConForm.setColeccion(mundoUnidadesCon.consultarTodo(con));
					 mundoUnidadesCon.clean();
					 regUniConForm.clean();
					 this.closeConnection(con);
					 regUniConForm.setIndicadorOperaciones(ConstantesBD.acronimoNo);
					 return mapping.findForward("resumenConsultaAvanzada");
				 }
				 if (estado.equals("imprimir"))
				 {
					 String nombreArchivo;
					 Random r=new Random();
					 nombreArchivo="/Relacion de Unidad de Consulta" + r.nextInt()  +".pdf";
					 UnidadConsultaPdf.pdfUnidadConsulta(ValoresPorDefecto.getFilePath() + nombreArchivo, regUniConForm, medico, request);
					 UtilidadBD.cerrarConexion(con);
					 request.setAttribute("nombreArchivo", nombreArchivo);
					 request.setAttribute("nombreVentana", "Unidad Consulta");
					 return mapping.findForward("abrirPdf");	           
				 }
				 UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				 if(estado.equals("adicionarPrograma")){

					 accionBuscarServiciosPorPrograma(regUniConForm, usuario, con);	    	   
					 return mapping.findForward("principal");																	
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

	 


	private ActionForward modificarEleccion(ActionMapping mapping,
			RegistroUnidadesConsulta mundoUnidadesCon, Connection con, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		if(RegistroUnidadesConsulta.verificarUniAgenAsoHorarioAten(con, regUniConForm.getCodigoT())>0)
		{
			regUniConForm.setTieneHorarioAsignado(true);
		}
		else{
			regUniConForm.setTieneHorarioAsignado(false);
		}
			regUniConForm.setEspecialidadesMap(Utilidades.obtenerEspecialidades());
			Consulta(con,regUniConForm, mundoUnidadesCon); 	        
			//mundoUnidadesCon.clean();
			regUniConForm.cleanChecks();
			regUniConForm.setCheckEstado("");
			this.closeConnection(con);
			regUniConForm.setIndicadorOperaciones(ConstantesBD.acronimoNo);
			return mapping.findForward("modificarEleccion");
	}
	 
	 /**
	 * @param con
	 * @param regUniConForm2
	 * @param mapping
	 * @param strMapping
	 * @return
	 */
	private ActionForward eliminarServicio(RegUnidadesConsultaForm regUniConForm, ActionMapping mapping, String strMapping) 
	{
		int pos=regUniConForm.getIndex();
		int numReg=Integer.parseInt(regUniConForm.getServicios("numeroServicios")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=regUniConForm.getServicios("registrosEliminados")!=null?Integer.parseInt(regUniConForm.getServicios("registrosEliminados")+""):0;
		if((regUniConForm.getServicios("tipo_"+pos)+"").equals("BD"))
		{
			regUniConForm.setServicios("codigoEliminado_"+regEliminados,regUniConForm.getServicios("codigo_"+pos));
			regUniConForm.setServicios("descripcionEliminado_"+regEliminados,regUniConForm.getServicios("descripcion_"+pos));
			regUniConForm.setServicios("registrosEliminados",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			regUniConForm.setServicios("codigo_"+i,regUniConForm.getServicios("codigo_"+(i+1)));
			regUniConForm.setServicios("codigocups_"+i,regUniConForm.getServicios("codigocups_"+(i+1)));
			regUniConForm.setServicios("descripcion_"+i,regUniConForm.getServicios("descripcion_"+(i+1)));
			regUniConForm.setServicios("tipo_"+i,regUniConForm.getServicios("tipo_"+(i+1)));
			regUniConForm.setServicios("tiposervicio_"+i,regUniConForm.getServicios("tiposervicio_"+(i+1)));
			regUniConForm.setServicios("especialidad_"+i,regUniConForm.getServicios("especialidad_"+(i+1)));
			regUniConForm.setServicios("descripcionespecialidad_"+i,regUniConForm.getServicios("descripcionespecialidad_"+(i+1)));
			
			
			regUniConForm.setServiciosBD("codigo_"+i,regUniConForm.getServiciosBD("codigo_"+(i+1)));
			regUniConForm.setServiciosBD("codigocups_"+i,regUniConForm.getServiciosBD("codigocups_"+(i+1)));
			regUniConForm.setServiciosBD("descripcion_"+i,regUniConForm.getServiciosBD("descripcion_"+(i+1)));
			regUniConForm.setServiciosBD("tiposervicio_"+i,regUniConForm.getServiciosBD("tiposervicio_"+(i+1)));
			regUniConForm.setServiciosBD("tipo_"+i,regUniConForm.getServiciosBD("tipo_"+(i+1)));
			regUniConForm.setServiciosBD("especialidad_"+i,regUniConForm.getServiciosBD("especialidad_"+(i+1)));
			regUniConForm.setServiciosBD("descripcionespecialidad_"+i,regUniConForm.getServiciosBD("descripcionespecialidad_"+(i+1)));
		}
		regUniConForm.getServicios().remove("codigo_"+ultimoRegistro);
		regUniConForm.getServicios().remove("codigocups_"+ultimoRegistro);
		regUniConForm.getServicios().remove("descripcion_"+ultimoRegistro);
		regUniConForm.getServicios().remove("tiposervicio_"+ultimoRegistro);
		regUniConForm.getServicios().remove("tipo_"+ultimoRegistro);
		regUniConForm.getServicios().remove("especialidad_"+ultimoRegistro);
		regUniConForm.getServicios().remove("descripcionespecialidad_"+ultimoRegistro);
		
		regUniConForm.getServiciosBD().remove("codigo_"+ultimoRegistro);
		regUniConForm.getServiciosBD().remove("codigocups_"+ultimoRegistro);
		regUniConForm.getServiciosBD().remove("descripcion_"+ultimoRegistro);
		regUniConForm.getServiciosBD().remove("tiposervicio_"+ultimoRegistro);
		regUniConForm.getServiciosBD().remove("tipo_"+ultimoRegistro);
		regUniConForm.getServiciosBD().remove("especialidad_"+ultimoRegistro);
		regUniConForm.getServiciosBD().remove("descripcionespecialidad_"+ultimoRegistro);
		
			//actualizando numero de registros.
		regUniConForm.setServicios("numeroServicios",ultimoRegistro+"");
		return mapping.findForward(strMapping);
	}

	
	
	/**
	 * Realiza validadiones de Unidades de Agenda
	 * @param RegUnidadesConsultaForm regUniConForm
	 * @param ActionErrors errores
	 **/
	private ActionErrors validacionesGuardarUnidad(RegUnidadesConsultaForm regUniConForm, ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(regUniConForm.getServicios("numeroServicios").toString());
		
		if(regUniConForm.getDescripcion().equals(""))
        {
            errores.add("Campo Descripcion vacio", new ActionMessage("errors.required","El campo Descripcion"));
        }
    	
		
		if(numRegistros <= 0)
        {
            errores.add("Campo Servicio vacio", new ActionMessage("errors.required","El servicio"));
        }   
		
    	/*for(int i = 0; i < numRegistros; i++)
        {                	
        	logger.info("valor del tipo del servicio >> "+regUniConForm.getServicios("tiposervicio_"+i).toString()+" >> "+regUniConForm.getServicios("especialidad_"+i).toString());
        		
        	if(regUniConForm.getServicios("tiposervicio_"+i).toString().equals(ConstantesBD.codigoServicioCargosConsultaExterna+"") 
        			&& (regUniConForm.getServicios("especialidad_"+i).toString().equals("") || regUniConForm.getServicios("especialidad_"+i).toString().equals("0")))
        	{
        		errores.add("Campo especialidad", new ActionMessage("errors.required","El campo Especialidad No. "+i ));                		
        	}
        }*/     
    	
		return errores;
	}
	
	
	/**
     * @param con
     * @param regUniConForm
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionGuardarModificacion(Connection con,RegUnidadesConsultaForm regUniConForm,ActionMapping mapping, UsuarioBasico usuario) 
    {    
    	
        RegistroUnidadesConsulta mundoUnidadesCon = new RegistroUnidadesConsulta();
        mundoUnidadesCon.consultaModificar(con,regUniConForm.getCodigoT());
        if(regUniConForm.getCheckEstado().equals("on"))
            regUniConForm.setActivo(true);
        else
        {
          if(regUniConForm.getCheckEstado().equals(""))
              regUniConForm.setActivo(false);
                              
        }
        boolean modificadoPrincipal = verificarModificacion(regUniConForm,mundoUnidadesCon);
        boolean modificadoServicios = verificarModificacionServicios(regUniConForm,mundoUnidadesCon);    
        
        if (modificadoPrincipal||modificadoServicios)
        {
        	if(modificadoPrincipal)
        		mundoUnidadesCon.modificar(con,regUniConForm.getCodigoT(),regUniConForm.getDescripcion(),regUniConForm.getActivo(),Utilidades.convertirAEntero(regUniConForm.getEspecialidadUniAgenda()), regUniConForm.getColorFondo());
        	if(modificadoServicios)
        	{
        		eliminarServicios(con,regUniConForm,mundoUnidadesCon);
        		if(regUniConForm.getRegistrosNuevos()>0)
            		insertarServicios(con,regUniConForm,mundoUnidadesCon);
        	}
        	/*
        	 *GENERAR LOS LOGS. 
        	*/
        	String logAnt="\n\n ====== INFORMACION ORIGINAL UNIDADES DE CONSULTA ======= ";
        	String logDes="\n ======= INFORMACION NUEVA UNIDADES DE CONSULTA ========= ";
        	
        	logAnt = logAnt +  "\n* DESCRIPCION ["+mundoUnidadesCon.getDescripcion() +"] ";		
			logDes = logDes +  "\n* DESCRIPCION ["+regUniConForm.getDescripcion()+"] ";
			
			if (mundoUnidadesCon.getActivo())
				{
					logAnt = logAnt +  "\n* ACTIVO [SI] ";	
				}
			else
				{
					logAnt = logAnt +  "\n* ACTIVO [NO] ";
				}
			
			if (regUniConForm.getActivo())
				{
				 logDes = logDes +  "\n* ACTIVO [SI] ";
				}
			else
				{
				logDes = logDes +  "\n* ACTIVO [NO] ";
				}
			
			//-----------------------------Servicios Anteriores de la Unidad de Consulta--------------------------------------
			int numServiciosAnt=Integer.parseInt(mundoUnidadesCon.getServicios("numeroServicios")+"");
			for(int i=0; i<numServiciosAnt; i++)
			{
				if(i==0)
				{
					logAnt = logAnt +  "\n                     SERVICIOS  ANTERIORES                      \n";
				}
				logAnt = logAnt +  "\n* CODIGO SERVICIO ["+mundoUnidadesCon.getServicios("codigo_"+i) +"] ";
				logAnt = logAnt +  "\n* NOMBRE SERVICIO ["+mundoUnidadesCon.getServicios("descripcion_"+i) +"] \n";		
				logAnt = logAnt +  "\n* NOMBRE ESPECIALIDAD ["+mundoUnidadesCon.getServicios("descripcionespecialidad_"+i) +"] \n";
			}
			
			//-----------------------------Servicios Nuevos de la Unidad de Consulta--------------------------------------
			int numServiciosDes=Integer.parseInt(regUniConForm.getServicios("numeroServicios")+"");
			for(int j=0; j<numServiciosDes; j++)
			{
				if(j==0)
				{
					logDes = logDes +  "\n                  SERVICIOS NUEVOS                          \n";
				}
				logDes = logDes +  "\n* CODIGO SERVICIO ["+regUniConForm.getServicios("codigo_"+j) +"] ";
				logDes = logDes +  "\n* NOMBRE SERVICIO ["+regUniConForm.getServicios("descripcion_"+j) +"] \n";		
				logDes = logDes +  "\n* NOMBRE ESPECIALIDAD ["+regUniConForm.getServicios("descripcionespecialidad_"+j) +"] \n";
			}
			
			logAnt = logAnt + "\n" + logDes;
			LogsAxioma.enviarLog(ConstantesBD.logModificacionUnidadCodigo, logAnt , ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
					
        }        
        consultaAvanzada(con,regUniConForm,mundoUnidadesCon);
        mundoUnidadesCon.clean();
        regUniConForm.clean();
        regUniConForm.setEstado("modificar");
        regUniConForm.setIndicadorOperaciones(ConstantesBD.acronimoSi);
        this.closeConnection(con);
	    return mapping.findForward("resumenConsultaAvanzada"); 
       
    }
    
    /**
	 * @param con
	 * @param regUniConForm2
	 * @param mundoUnidadesCon
	 */
	private void insertarServicios(Connection con, RegUnidadesConsultaForm regUniConForm, RegistroUnidadesConsulta mundoUnidadesCon)
	{
		int numeroRegistros=Integer.parseInt(regUniConForm.getServicios("numeroServicios")+"");
		int especialidad = 0;
		
		for(int i=0;i<numeroRegistros;i++)
		{
			if((regUniConForm.getServicios("tipo_"+i)+"").equals("MEM"))
			{
				if(!regUniConForm.getServicios("especialidad_"+i).toString().equals(""))
					especialidad = Integer.parseInt(regUniConForm.getServicios("especialidad_"+i).toString());
				else
					especialidad = ConstantesBD.codigoNuncaValido;
	
				mundoUnidadesCon.insertarDetalle(con,regUniConForm.getCodigoT(),Utilidades.convertirAEntero(regUniConForm.getServicios("codigo_"+i)+""),especialidad);
			}
		}
	}

	/**
	 * @param con
     * @param mundoUnidadesCon
	 * @param regUniConForm2
	 */
	private void eliminarServicios(Connection con, RegUnidadesConsultaForm regUniConForm, RegistroUnidadesConsulta mundoUnidadesCon) 
	{
		int numEliminados=regUniConForm.getServicios("registrosEliminados")!=null?Integer.parseInt(regUniConForm.getServicios("registrosEliminados")+""):0;
			for(int i=0;i<numEliminados;i++)
			{
				if((regUniConForm.getServicios("codigoEliminado_"+i)+"").contains("-"))
					mundoUnidadesCon.eliminarServicio(con,regUniConForm.getCodigoT(),Integer.parseInt((regUniConForm.getServicios("codigoEliminado_"+i)+"").split("-")[1]));
				else
					mundoUnidadesCon.eliminarServicio(con,regUniConForm.getCodigoT(),Integer.parseInt((regUniConForm.getServicios("codigoEliminado_"+i)+"")));
			}
	}

	/**
	 * @param regUniConForm2
	 * @param mundoUnidadesCon
	 * @return
	 */
	private boolean verificarModificacionServicios(RegUnidadesConsultaForm regUniConForm, RegistroUnidadesConsulta mundoUnidadesCon) 
	{
		if(Integer.parseInt(regUniConForm.getServicios("numeroServicios")+"")!=Integer.parseInt(mundoUnidadesCon.getServicios("numeroServicios")+""))
			return true;//si hay modificaciï¿½n y no se necesita mï¿½s comprobaciones.
		if(regUniConForm.getRegistrosNuevos()>0)
			return true;
		return false;
	}

	/**
     * metodo para  verificar si existen modificaciones
     * @param regUniConForm
     * @return hayModificado, boolean true si hay modificaciones presentes.
     */
    private boolean verificarModificacion(RegUnidadesConsultaForm regUniConForm,RegistroUnidadesConsulta mundoUnidadesCon)
    {
        boolean hayModificado=false;
        
        if(!mundoUnidadesCon.getDescripcion().equals(regUniConForm.getDescripcion()) ) 
            hayModificado=true;
        if( mundoUnidadesCon.getActivo() != regUniConForm.getActivo() )
            hayModificado=true;
        if(mundoUnidadesCon.getCodigoEspecialidad()!=regUniConForm.getCodigoEspecialidad())
        	hayModificado=true;
        if(!mundoUnidadesCon.getEspecialidadUniAgen().equals(regUniConForm.getEspecialidadUniAgenda()) )
        	hayModificado=true;
        if(!mundoUnidadesCon.getColorFondo().equals(regUniConForm.getColorFondo()) )
        	hayModificado=true;
        return hayModificado;
    }

    /**
	  * Metodo para llenar el mundo con todos los datos provenientes del
	  * Form.
	  * @param mundoUnidadesCon, instancia del mundo RegistroUnidadesConsulta.
	  * @param regUniConForm, instancia del Form RegUnidadesConsultaForm.
	  */
	 private void llenarMundo(RegistroUnidadesConsulta mundoUnidadesCon, RegUnidadesConsultaForm regUniConForm)
	 {
	     mundoUnidadesCon.setDescripcion(regUniConForm.getDescripcion());
	     mundoUnidadesCon.setCodServicio(regUniConForm.getCodServicio());	     
	     mundoUnidadesCon.setServicios(regUniConForm.getServicios());
	     mundoUnidadesCon.setEspecialidadUniAgen(regUniConForm.getEspecialidadUniAgenda());
	     mundoUnidadesCon.setNomEspecialidadUniAgen(regUniConForm.getNomEspecialidadUniAgenda());
	     mundoUnidadesCon.setTiposAtencion(regUniConForm.getTiposAtencion());
	     mundoUnidadesCon.setColorFondo(regUniConForm.getColorFondo());
	     if(regUniConForm.getCheckEstado().equals("on"))
	         mundoUnidadesCon.setActivo(true);
	 }
	 
	 
	 
	 /**
	  * Metodo para realizar la busqueda avanzada, dependiendo del parametro
	  * a consultar dado en el Form.
	  * @param con, Connection con la BD.
	  * @param regUniConForm, instancia del Form RegUnidadesConsultaForm.
	  * @param mundoUnidadesCon, instancia del mundo RegistroUnidadesConsulta.
	  */
	 public void consultaAvanzada(Connection con, RegUnidadesConsultaForm regUniConForm, RegistroUnidadesConsulta mundoUnidadesCon)
	 {
	     String parametros[]={""};
	     int parametrosInt[]={0,0,0};
	     boolean activo=false;
	     	
	     /*
	      * cambio de la filosofia de la busqueda
	     if(regUniConForm.getCheckCodigo().equals("on"))
	        parametrosInt[0]=regUniConForm.getCodigoT();
	     else
	     {
	         regUniConForm.setCodigoT(-1);
	         parametrosInt[0]= regUniConForm.getCodigoT();
	     }
	         

	     if(regUniConForm.getCheckDescripcion().equals("on"))
	        parametros[0]=regUniConForm.getDescripcion();
	     if(regUniConForm.getCheckActivo().equals("on"))
	     {
	       if(regUniConForm.getCheckAct().equals("on"))
	      {   
	          activo=true;
	          regUniConForm.setTemp("ok");    
	      }   
	      if(regUniConForm.getCheckIna().equals("on"))
	      {  
	        activo=false;
	      	regUniConForm.setTemp("ok");
	      }	
	     }
	        
	    if(regUniConForm.getCheckServicio().equals("on"))
	     {      
	      parametrosInt[1]=regUniConForm.getCodServicio();
	     } 
	    
	    if(regUniConForm.getCheckEspecialidad().equals("on"))
	        parametrosInt[2]=regUniConForm.getCodigoEspecialidad();
	     else
	     {
	         regUniConForm.setCodigoEspecialidad(-2);
	         parametrosInt[2]=regUniConForm.getCodigoEspecialidad();
	     }
     */
	    
	    	if(regUniConForm.getCodigoT()>0)
	    	{
		        parametrosInt[0]=regUniConForm.getCodigoT();
	    	}
		    else
		    {
		        regUniConForm.setCodigoT(-1);
		        parametrosInt[0]= regUniConForm.getCodigoT();
		    }
		         

	    	if(!UtilidadTexto.isEmpty(regUniConForm.getDescripcion()))
	    	{
	    		parametros[0]=regUniConForm.getDescripcion();
	    	}
	    	if(regUniConForm.getCheckAct().equals("on"))
		    {   
	    		activo=true;
		        regUniConForm.setTemp("ok");    
		    }   
		    if(regUniConForm.getCheckIna().equals("on"))
		    {  
		    	activo=false;
		      	regUniConForm.setTemp("ok");
		    }	
		        
		    if(regUniConForm.getCodServicio()>0)
		    {      
		      parametrosInt[1]=regUniConForm.getCodServicio();
		    } 
		    
		    if(regUniConForm.getCodigoEspecialidad()>0)
		    {
		        parametrosInt[2]=regUniConForm.getCodigoEspecialidad();
		    }
		    else
		    {
		        regUniConForm.setCodigoEspecialidad(-2);
		        parametrosInt[2]=regUniConForm.getCodigoEspecialidad();
		    }
	     
	    regUniConForm.setColeccion(mundoUnidadesCon.consultaAvanzada(con,
	             							  parametrosInt[0],
	             							  parametros[0],
	             							  parametrosInt[1],
	             							  activo,
	             							  parametrosInt[2],
	             							  regUniConForm.getTemp()));
	       
	 }
	 
	 
	 
	 /**
	  * Metodo para Consultar todos los datos correspondientes a un 
	  * codigo especifico, tomado del Form.
	  * @param con, Connection con la BD.
	  * @param regUniConForm, Instancia del Form RegUnidadesConsultaForm.
	  * @param mundoUnidadesCon, instancia del mundo RegistroUnidadesConsulta.
	  */
	 public void Consulta(Connection con, RegUnidadesConsultaForm regUniConForm, RegistroUnidadesConsulta mundoUnidadesCon)
	 {
	     
	     mundoUnidadesCon.consultaModificar(con,regUniConForm.getCodigoT());
	     regUniConForm.setCodigoT(mundoUnidadesCon.getCodigoT());
	     regUniConForm.setDescripcion(mundoUnidadesCon.getDescripcion());
	     regUniConForm.setDescripcionAntiguo(mundoUnidadesCon.getDescripcion());
	     regUniConForm.setServicios(mundoUnidadesCon.getServicios());
	     regUniConForm.setActivo(mundoUnidadesCon.getActivo());
	     regUniConForm.setCodigoEspecialidad(mundoUnidadesCon.getCodigoEspecialidad());
	     regUniConForm.setColorFondo(mundoUnidadesCon.getColorFondo());
	     regUniConForm.setTiposAtencion(mundoUnidadesCon.getTiposAtencion());
	     regUniConForm.setEspecialidadUniAgenda(mundoUnidadesCon.getEspecialidadUniAgen());
	     mundoUnidadesCon.clean();
	     mundoUnidadesCon.consultaModificar(con,regUniConForm.getCodigoT());
	     regUniConForm.setServiciosBD(mundoUnidadesCon.getServicios());
	 }
	 
	 
	 /**
	  * Metodo para cargar el Log con los datos actuales, antes de la 
	  * modificaciï¿½n.
	  * @param regUniConForm, Instancia del Form RegUnidadesConsultaForm.
	  */
	 private void llenarloghistorial(RegistroUnidadesConsulta mundoUnidadesCon)
	    {
	     String cadena="";
	     cadena+=mundoUnidadesCon.getCodServicio()+"-"+mundoUnidadesCon.getCodigoEspecialidad();
	     
	     regUniConForm.setLog("\n            ====INFORMACION ORIGINAL===== " +
	        		"\n*  Cï¿½digo Unidad de Consulta [" +mundoUnidadesCon.getCodigoT()+"] "+
	        		"\n*  Descripcion ["+mundoUnidadesCon.getDescripcion()+"] " +
	        		"\n*  Servicio ["+cadena+"] "+
	        		"\n*  Estado ["+mundoUnidadesCon.getActivo()+"] " );
	        
	    }
	 
	 
	 
	 /**
	  * Metodo para generar el Log, y aï¿½adir los cambios realizados.
	  * @param regUniConForm, Instancia del Form RegUnidadesConsultaForm.
	  * @param session, Session para obtener el usuario.
	  */
	 private void generarLog(RegUnidadesConsultaForm regUniConForm, HttpSession session)
	    {
	        UsuarioBasico usuario;
	        String cadena="";
		    cadena+=regUniConForm.getCodServicio()+"-"+regUniConForm.getCodigoEspecialidad();
	        String log=regUniConForm.getLog() +
			"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
			"\n*  Cï¿½digo Unidad de Consulta [" +regUniConForm.getCodigoT()+"] "+
			"\n*  Descripcion ["+regUniConForm.getDescripcion()+"] " +
			"\n*  Servicio ["+cadena+"] "+
			"\n*  Estado ["+regUniConForm.getActivo()+"] "+
			"\n========================================================\n\n\n " ;
	        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
	        LogsAxioma.enviarLog(ConstantesBD.logModificacionUnidadCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
	        
	    }

	 private boolean empezarTransaccion(Connection con)
	 {
	     try {
            return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
        } catch (SQLException e) {
            logger.error("Error iniciando la transaccion "+e);
            return false;
        }
	 }

	 private void terminarTransaccion(Connection con)
	 {
	     try {
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
        } catch (SQLException e) {
            logger.error("Error iniciando la transaccion "+e);
        }
	 }

	 private void abortarTransaccion(Connection con)
	 {
	     try {
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
        } catch (SQLException e) {
            logger.error("Error iniciando la transaccion "+e);
        }
	 }	 
	 
	 /**
	  * 
	  * Este mètodo se encarga de postular los servicios asociados al programa seleccionado.
	  *
	  * @param regUniConForm2
	  * @param usuario
	  * @param con
	  *
	  * @autor Yennifer Guerrero
	  */
	private void accionBuscarServiciosPorPrograma(
			RegUnidadesConsultaForm regUniConForm, UsuarioBasico usuario,
			Connection con) {
		
		BusquedaServiciosGenerica mundo= new BusquedaServiciosGenerica();
		
		Collection<HashMap<String, Object>> col = null;
		
		String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		
		col =  mundo.busquedaAvanzadaServiciosXDescripcion(con, "", 
				"", 
				0, 
				regUniConForm.getCodigosServiciosInsertados(), 
				"",
				0, 
				codigoTarifario,
				false, 
				"",
				0,
				usuario,	
				0,
				0,
				"",
				false,
				"", 
				"regUniConForm",
				regUniConForm.getCodigoPrograma(),"");
		
		
		validarServiciosExistentes(col, regUniConForm);
		
		regUniConForm.setRegistrosNuevos(regUniConForm.getRegistrosNuevos()+col.size());
		HashMap<String, Object> servicios=regUniConForm.getServicios();
		int numeroServicios=Utilidades.convertirAEntero(servicios.get("numeroServicios")+"");
		if(numeroServicios==ConstantesBD.codigoNuncaValido)
		{
			numeroServicios=col.size();
		}
		int numServicio=numeroServicios;
		numeroServicios+=col.size();
		servicios.put("numeroServicios", numeroServicios);
		
		
		
		for (HashMap<String, Object> serv : col) {
			
			String descripcionServicio = serv.get("codigopropietario").toString() + " " + serv.get("descripcion").toString() ;
			
			Utilidades.imprimirMapa(serv);
			servicios.put("codigo_"+numServicio, serv.get("codigoaxioma"));
			servicios.put("descripcion_"+numServicio, descripcionServicio);
			servicios.put("codigocups_"+numServicio, serv.get("codigopropietario"));
			servicios.put("tiposervicio_"+numServicio, serv.get("tiposervicio"));
			servicios.put("atencionOdon_"+numServicio, serv.get("atencionodontologica"));
			servicios.put("especialidad_"+numServicio, serv.get("codigoespecialidad"));
			servicios.put("descripcionespecialidad_"+numServicio, serv.get("nombreespecialidad"));
			servicios.put("tipo_"+numServicio, "MEM");			
			servicios.put("guardar_"+numServicio, "");
			
			numServicio ++;	
			
		}
		
		regUniConForm.setServicios(servicios);
		
	}

	/**
	 * @param col
	 * @param regUniConForm2
	 * @param usuario
	 * @param con
	 */
	private void validarServiciosExistentes(
			Collection<HashMap<String, Object>> col, RegUnidadesConsultaForm regUniConForm2) {

		HashMap<String, Object> servExistentes =regUniConForm.getServicios();
		int numeroServicios=Utilidades.convertirAEntero(servExistentes.get("numeroServicios")+"");
		
		if (numeroServicios > 0 ) {
			
			for (int i = 0; i < numeroServicios; i++) {
				
				for (HashMap<String, Object> servicioNuevo : col) {
					
					String codigoServicioExistente = servExistentes.get("codigo_"+i).toString();
					String codigoServicioNuevo = servicioNuevo.get("codigoaxioma").toString();
					
					if ( codigoServicioExistente.trim().equals(codigoServicioNuevo)) {
						col.remove(servicioNuevo);
						break;
					}
					
				}
			}
		}
		
	}
	
	public ActionForward validarServiciosBusquedaGenerica(RegUnidadesConsultaForm regUniConForm, ActionMapping mapping){
		
		
		int numReg=Integer.parseInt(regUniConForm.getServicios("numeroServicios")+"");
		int ultimoRegistro=numReg-1;
		HashMap<String, Object> servicios =regUniConForm.getServicios();
		
		for(int i=0;i<ultimoRegistro;i++){
			
			String codigoServicio = servicios.get("codigo_"+i).toString();
			String codigoServicioAux = servicios.get("codigo_"+ultimoRegistro).toString();
			
			if ( codigoServicio.trim().equals(codigoServicioAux)) {
				regUniConForm.setIndex(ultimoRegistro);
				regUniConForm.setServicios("especialidad_"+ultimoRegistro,regUniConForm.getServicios("especialidad_"+(i)));
				regUniConForm.setServicios("descripcionespecialidad_"+ultimoRegistro,regUniConForm.getServicios("descripcionespecialidad_"+(i)));
				
				return eliminarServicio(regUniConForm, mapping, "principal");
				
			}
		}
		regUniConForm.setRegistrosNuevos(regUniConForm.getRegistrosNuevos()+1);
        return mapping.findForward("principal");
	}
}