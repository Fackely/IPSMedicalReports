/*
 * @(#)AdmisionUrgenciasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 * 
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.Encoder;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.AdmisionUrgenciasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * <code>Action</code> para el ingreso y modificacin de datos de
 * <code>AdmisionUrgencias</code>, toma los atributos de un objeto
 * <code>AdmisionUrgenciasForm</code> y los transforma en el formato apropiado
 * para <code>AdmisionUrgencias</code>.
 *
 * @version 1.0, Mar 10, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class AdmisionUrgenciasAction extends Action {

	/**
	 * Para hacer los logs de la aplicación
	 */
	protected Logger logger = Logger.getLogger(AdmisionUrgenciasAction.class);

	/**
	 * Ejecuta alguna de las acciones correspondientes a una Admision por
	 * Urgencias: insertar, modificar.
	 * @param mapping el mapeado usado para elegir esta instancia
	 * @param form objeto con los datos provenientes del formulario
	 * @param request el <i>servlet request</i> que est?siendo procesado en este momento
	 * @param response el <i>servlet response</i> resultado de procesar este request
	 * @return un <code>ActionForward</code> indicando la siguiente p?ina dentro de la navegacin
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (response == null); // NOP para evitar el warning "The argument response is never read"
		String accion = request.getParameter("accion");
		accion = (accion != null) ? accion.trim().toLowerCase() : "";
		
		logger.warn("\n\n\t\t\t\t [AdmisionesUrgenciasAction] estado ["+accion+"]\n\n");
		
		
		if (accion.equals("modificar")) 
		{
			
			return modificar(mapping, form, request);
		}
		else 
		{
			request.setAttribute("error", Encoder.encode("Acción desconocida : " + accion));
			return mapping.findForward("errorIngresarAdmisionUrgencias");
		}		

	}

	

	/**
	 * Método privado para modificar un ingreso de admisiones por urgencias ya
	 * existente. Maneja el cambio de estado de las camas de urgencias en caso
	 * de asignarse, liberarse o modificarse
	 * @param mapping el mapeado usado para elegir esta instancia
	 * @param form objeto con los datos provenientes del formulario
	 * @param request el <i>servlet request</i> que estásiendo procesado en este momento
	 * @return un <code>ActionForward</code> indicando la siguiente página dentro de la navegacin
	 */
	private ActionForward modificar (ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {

		// Obtenemos la session asociada a este request, hacemos el cast del form al objeto correspondiente, declaramos flags y variables.
		boolean isObservado = false;
		HttpSession session = request.getSession();
		AdmisionUrgenciasForm admisionForm = (AdmisionUrgenciasForm) form;
		int codigo = 0;		
		String codigoCausaExternaNoDefinida = "0-";	
		// Separamos parejas codigo-nombre que vienen en el form, para pasarlos al mundo
		
		String [] origenArray_cn = UtilidadTexto.separarNombresDeCodigos(admisionForm.getOrigen_cn(), 1);
		String [] causaExternaArray_cn = null;
		if(admisionForm.getCausaExterna_cn()!= null && admisionForm.getCausaExterna_cn().indexOf(codigoCausaExternaNoDefinida) != -1)
		   causaExternaArray_cn = new String []{codigoCausaExternaNoDefinida.substring(0, codigoCausaExternaNoDefinida.length()-1), ""};
		else if (admisionForm.getCausaExterna_cn()== null) causaExternaArray_cn = new String []{codigoCausaExternaNoDefinida.substring(0, codigoCausaExternaNoDefinida.length()-1), ""};
		else causaExternaArray_cn = UtilidadTexto.separarNombresDeCodigos(admisionForm.getCausaExterna_cn(), 1);

		// Obteniendo la admision
		AdmisionUrgencias admision = (AdmisionUrgencias)session.getAttribute("admision");
		
		/* VALIDANDO datos no requeridos */

		//***********SE REVISA SI EL PACIENTE ESTABA DESDE ANTES EN OBSERVACION**************************************************
		if (admisionForm.getFechaObservacion() != null && admisionForm.getHoraObservacion() != null && admisionForm.getCama_cn() != null && !admisionForm.getCama_cn().equals("")) 
		{
			isObservado = true;									
		}

		/* PROCESANDO... */
		//Se almacena temporalmente la fecha/hora observacion de la admisión
		String fechaObservacionAnterior = admision.getFechaObservacion();
		String horaObservacionAnterior = admision.getHoraObservacion();
		
		String horaEgresoObservacion=admision.getHoraEgresoObservacion();
		String fechaEgresoObservacion=admision.getFechaEgresoObservacion();
		
		PropertyUtils.copyProperties(admision, admisionForm);
		
		admision.setHoraEgresoObservacion(horaEgresoObservacion);
		admision.setFechaEgresoObservacion(fechaEgresoObservacion);
		
		admision.setCodigoOrigen(origenArray_cn[0]);		
		admision.setOrigen(origenArray_cn[1]);		
		admision.setCodigoCausaExterna(causaExternaArray_cn[0]);
		admision.setCausaExterna(causaExternaArray_cn[1]);
		admision.setLoginUsuario((UsuarioBasico) session.getAttribute("usuarioBasico"));		
		//admision.setNumeroAutorizacion(admisionForm.getNumeroAutorizacion());


		//Abriendo conexion
		
		//1)***************SE REALIZA BLOQUEO DE LA ADMISION DE URGENCIAS************************************************************
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")); 
		Connection con = myFactory.getConnection();		
		boolean isBegin = myFactory.beginTransaction(con);
		ArrayList filtro = new ArrayList();
		filtro.add(admision.getCodigo()+"");
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearAdmisionUrgencias,filtro);
		
		//2) *************SE VERIFICA QUE LA CAMA A ASIGNAR NO SE HAYA OCUPADO********************************************************
		if(!admisionForm.getCama_cn().equals("asignada")&&
			!admisionForm.getCama_cn().equals("")&&
			Integer.parseInt(admisionForm.getCama_cn())!=admision.getCodigoCama())//en caso de modificacion esta validacion se debe haces si y solo si la cama cambio.
		{
			if(Utilidades.obtenerCodigoEstadoCama(con,Integer.parseInt(admisionForm.getCama_cn()))!=ConstantesBD.codigoEstadoCamaDisponible)
			{
				ActionErrors errores = new ActionErrors();  
	            errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
	            saveErrors(request, errores);
	            UtilidadBD.abortarTransaccion(con);
	            UtilidadBD.closeConnection(con);            
	            return mapping.findForward("paginaErroresActionErrors");  
			}
		}
		//***********************************************************************************************
		try
		{		
			if(isBegin) 
			{
				//************CASO EN LA QUE EL PACIENTE NO HABÍA ESTADO EN OBSERVACION************************
				if(!isObservado) 
				{	
					//Si tenía cama se pasa su estado a disponible
					if (admision.getCodigoCama()!= -1)
					{
					    Camas1 camas1 = new Camas1();
					    camas1.setCodigo(admision.getCodigoCama());
					    camas1.detalleCama1(con);
					    camas1.setEstadoCama(new InfoDatosInt(ConstantesBD.codigoEstadoCamaDisponible));
					    camas1.modificarCamas1Transaccional(con, ConstantesBD.continuarTransaccion);
					}
					admision.setCodigoCama("-1");			
					admision.setNombreCama("");	
					admision.setTipoUsuarioCama("");
					admision.setTipoHabitacionCama("");
					admision.setPisoCama("");
					admision.setDescripcionCama("");
					admision.setHabitacionCamaStr("");
					admision.setNombreCentroCostoCama("");
				}
				//************************************************************************************************
				//*********CASO EN EL QUE EL PACIENTE ESTABA EN OBSERVACION Y SE LE ASIGNÓ CAMA********************
				if(isObservado && !admisionForm.getCama_cn().equals("asignada") && !admisionForm.getCama_cn().equals("")) 
				{
					Camas1 camas1 = new Camas1();
					
					    
					
					//Se habilita la cama anterior
					if(admision.getCodigoCama() == -1){
						String[] datosCama = admision.getUltimaCama(con, admision.getCodigo());
						if(datosCama[3]!=null && !datosCama[3].equals("") && datosCama[4]!=null && !datosCama[4].equals("")){
							admision.setCodigoCama(datosCama[2]);
						}
					}
					if(admision.getCodigoCama() != -1){
					    camas1.setCodigo(admision.getCodigoCama());
					    camas1.detalleCama1(con);
					    camas1.setEstadoCama(new InfoDatosInt(ConstantesBD.codigoEstadoCamaDisponible));
					    camas1.modificarCamas1Transaccional(con, ConstantesBD.continuarTransaccion);
						
					}
					
					//Se cambia la nueva cama
					camas1.setCodigo(Integer.parseInt(admisionForm.getCama_cn()));
					camas1.detalleCama1(con);
					
					admision.setCodigoCama(admisionForm.getCama_cn());
					
					admision.setPisoCama(camas1.getNombrePiso());
					admision.setTipoHabitacionCama(camas1.getTipoHabitacion());
					admision.setNombreCama(camas1.getNumeroCama());			
					admision.setTipoUsuarioCama(camas1.getTipoUsuarioCama().getNombre());
					admision.setHabitacionCamaStr(camas1.getNombreHabitacion());
					admision.setNombreCentroCostoCama(camas1.getCentroCosto().getNombre());
				}	
				/**else
				{
					//Ajuste hecho por tarea 64699 de Xplanner 3
					admision.setFechaObservacion(fechaObservacionAnterior);
					admision.setHoraObservacion(horaObservacionAnterior);
				}**/
			
				// Insertando la admisión
				codigo = admision.modificarTransaccional(con, "finalizar");
			}

		}	
		catch (SQLException ex)
		{
			logger.info(" ERROR EXCEPCION!!!   "+ex.getSQLState());
		    return ComunAction.accionSalirCasoError(mapping,request, con, logger, "Base de datos. " + ex.getMessage() , "Base de datos. " + ex.getMessage(), false);
		}

		// Cerramos la conexión antes de retornar
		UtilidadBD.cerrarConexion(con);
		con = null;

		// Si se pudo hacer la inserción de la admisión
		if (codigo != 0) {
			return mapping.findForward("paginaResumen");
		}

		else {
			session.removeAttribute("codigoTipoIdentificacionPaciente");
			session.removeAttribute("numeroIdentificacionPaciente");
			
			return ComunAction.accionSalirCasoError(mapping,request, con, logger, "Error modificando la admisión de urgencias con código: "+admision.getCodigo(), "Error modificando la admisión de urgencias con código: "+admision.getCodigo(), false);
		}

	}

}