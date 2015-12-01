package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.odontologia.ParametricaClasificacionPacienteOdoForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.ClasOdoMotivosNoConfir;
import com.servinte.axioma.orm.ClasOdoMotivosNoConfirHome;
import com.servinte.axioma.orm.ClasOdoSecEstCita;
import com.servinte.axioma.orm.ClasOdoSecEstCitaHome;
import com.servinte.axioma.orm.ClasOdoSecEstadosPres;
import com.servinte.axioma.orm.ClasOdoSecEstadosPresHome;
import com.servinte.axioma.orm.ClasOdoSecIndConf;
import com.servinte.axioma.orm.ClasOdoSecIndConfHome;
import com.servinte.axioma.orm.ClasOdoSecSalDispHome;
import com.servinte.axioma.orm.ClasOdoSecTiposCita;
import com.servinte.axioma.orm.ClasOdoSecTiposCitaHome;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.FuncionalidadesRestriccion;
import com.servinte.axioma.orm.MotivosCita;
import com.servinte.axioma.orm.delegate.ClasificaPacientesOdoDelegate;
import com.servinte.axioma.orm.delegate.FuncionalidadesRestriccionDelegate;
import com.servinte.axioma.orm.delegate.MotivosCitaDelegate;
import com.servinte.axioma.orm.delegate.administracion.EspecialidadesDelegate;

public class ParametricaClasificacionPacienteOdoAction  extends Action {
	
	

	Logger logger =Logger.getLogger(ParametricaClasificacionPacienteOdoAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			
		
	throws Exception
	{
		Connection con =null;
		try{
			if (response == null);
			if (form instanceof ParametricaClasificacionPacienteOdoForm) 
			{

				ParametricaClasificacionPacienteOdoForm forma = (ParametricaClasificacionPacienteOdoForm) form;
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
				String estado = forma.getEstado();
				logger.info("************ estado  "+estado+" ***********\n");


				if (estado==null)
				{
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					forma.reset();
					return mapping.findForward("principal");

				}

				else if (estado.equals("nuevo"))
				{ 


					forma.reset();
					//Muestra los motivos de no atencion y genera el check de captura *******
					HibernateUtil.beginTransaction();// inicializa transaccion para poder consultar hibernate
					MotivosCitaDelegate motivosCitaDelegate=new MotivosCitaDelegate();// metodo que va a consultar se inicializa
					List<MotivosCita> listadoMotivosCita=motivosCitaDelegate.listarMotivos(); // llama desde la bd los motivos
					List<String> motivosCheck=new ArrayList<String>();// se inicializa una lista de checks para ingresar datos en la jsp
					int cantMotivos=listadoMotivosCita.size(); // cuenta cuantos elementos hay en la lista para iterar la lista de motivos
					for(int i=0; i<cantMotivos; i++)// itera la lista de motivos
					{
						motivosCheck.add("");// inicializa la lista de check en blanco para poder usarla en la jsp
					}
					forma.setMotivosCheck(motivosCheck); // actualiza la forma con la lista de checks inicializada
					forma.setListadoMotivosCita(listadoMotivosCita); // actualiza la forma con los motivos a mostrar en la jsp.
					HibernateUtil.endTransaction(); // finaliza transaccion
					//************************************************************************




					// muestra las etiquetas integridad dominio **************
					String[] listaConstantesStr = {   // se genere un vector con las etiquetas a mostrar
							ConstantesIntegridadDominio.acronimoEntre,
							ConstantesIntegridadDominio.acronimoMayorQue,
							ConstantesIntegridadDominio.acronimoMenorQue,
							ConstantesIntegridadDominio.acronimoIgualA};
					con = UtilidadBD.abrirConexion(); // se hace una coneccion para la utilidad de integridad dominio
					List<DtoIntegridadDominio> listaConstantes=Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesStr, false);
					UtilidadBD.cerrarConexion(con);//  se cierra la coneccion
					forma.setListaConstantes(listaConstantes); // se actualiza la forma.
					//********************************************************


					//Seccion presupuesto acronimos****************************
					String[] listaConstantesPresStr = {   // se genere un vector con las etiquetas a mostrar
							ConstantesIntegridadDominio.acronimoSinPresupuesto,
							ConstantesIntegridadDominio.acronimoEstadoActivo,
							ConstantesIntegridadDominio.acronimoInactivo,
							ConstantesIntegridadDominio.acronimoPrecontratado};
					UtilidadBD.abrirConexion(); // se hace una coneccion para la utilidad de integridad dominio
					List<DtoIntegridadDominio> listaConstantesPres=Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesPresStr, false);
					listaConstantesPresStr = new String[]{   // se genere un vector unicamente con contratado
							ConstantesIntegridadDominio.acronimoContratado};
					listaConstantesPres.addAll(Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesPresStr, false));
					UtilidadBD.cerrarConexion(con);//  se cierra la coneccion
					forma.setListaConstantesPres(listaConstantesPres); // se actualiza la forma.
					// inicializacon del check
					List<String> presupuCheck=new ArrayList<String>();
					int cantConstantesPres=listaConstantesPres.size();
					for(int i=0;i<cantConstantesPres;i++)
					{
						presupuCheck.add("");
					}
					forma.setPresupuChecK(presupuCheck);
					//************************************************************


					//seccion presupuesto acronimos indicativos.
					String[] listaConstantesPresIndStr= {
							ConstantesIntegridadDominio.acronimoContratadoContratado,
							ConstantesIntegridadDominio.acronimoContratadoTerminado,
							ConstantesIntegridadDominio.acronimoContratadoCancelado,
							ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente};
					UtilidadBD.abrirConexion();
					List<DtoIntegridadDominio> listaConstantesPresInd=Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesPresIndStr, false);
					UtilidadBD.cerrarConexion(con);
					forma.setListaConstantesPresInd(listaConstantesPresInd);
					// inicializacion del check
					List<String> presIndCheck=new ArrayList<String>();
					int cantContantesPresInd=listaConstantesPresInd.size();
					for (int i=0;i<cantContantesPresInd;i++)
					{
						presIndCheck.add("");
					}
					forma.setPresIndCheck(presIndCheck);
					//*****************************************************************


					//seccion Tipos Cita
					String[] listaConstantesTipoCStr= {
							ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,
							ConstantesIntegridadDominio.acronimoPrioritaria,
							ConstantesIntegridadDominio.acronimoTratamiento,
							ConstantesIntegridadDominio.acronimoControlCitaOdon,
							ConstantesIntegridadDominio.acronimoRevaloracion,
							ConstantesIntegridadDominio.acronimoRemisionInterconsulta,
							ConstantesIntegridadDominio.acronimoAuditoria};
					UtilidadBD.abrirConexion();
					List<DtoIntegridadDominio> listaConstantesTipoC=Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesTipoCStr, false);
					UtilidadBD.cerrarConexion(con);
					forma.setListaConstantesTipoC(listaConstantesTipoC);
					// inicializacion del check
					List<String> tipoCCheck=new ArrayList<String>();
					int cantContantesTipoC=listaConstantesTipoC.size();
					for (int i=0;i<cantContantesTipoC;i++)
					{
						tipoCCheck.add("");
					}
					forma.setTipoCCheck(tipoCCheck);
					//*****************************************************************



					//seccion Estados Cita
					String[] listaConstantesEstadosCStr= {
							ConstantesIntegridadDominio.acronimoProgramado,
							ConstantesIntegridadDominio.acronimoReservado,
							ConstantesIntegridadDominio.acronimoAsignado,
							ConstantesIntegridadDominio.acronimoAtendida,
							ConstantesIntegridadDominio.acronimoAreprogramar,
							ConstantesIntegridadDominio.acronimoCancelada,
							ConstantesIntegridadDominio.acronimoNoAsistio,
							ConstantesIntegridadDominio.acronimoNoAtencion};
					UtilidadBD.abrirConexion();
					List<DtoIntegridadDominio> listaConstantesEstadosC=Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesEstadosCStr, false);
					UtilidadBD.cerrarConexion(con);
					forma.setListaConstantesEstadosC(listaConstantesEstadosC);
					// inicializacion del check
					List<String> estadosCCheck=new ArrayList<String>();
					int cantContantesEstadosC=listaConstantesEstadosC.size();
					for (int i=0;i<cantContantesEstadosC;i++)
					{
						estadosCCheck.add("");
					}
					forma.setEstadosCCheck(estadosCCheck);
					//*****************************************************************





					//Muestra las especialidades  *******
					HibernateUtil.beginTransaction();// inicializa transaccion para poder consultar hibernate
					EspecialidadesDelegate especialidaesDelegate=new EspecialidadesDelegate();// metodo que va a consultar se inicializa
					List<Especialidades> listaEspecialidades=especialidaesDelegate.listarEspecialidades(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica); // llama desde la bd las especialidades
					forma.setListaEspecialidades(listaEspecialidades); // actualiza la forma con los motivos a mostrar en la jsp.
					HibernateUtil.endTransaction(); // finaliza transaccion
					//************************************************************************


					//Muestra los motivos de no atencion y genera el check de captura *******
					HibernateUtil.beginTransaction();// inicializa transaccion para poder consultar hibernate
					logger.info("********************** trasaccion funcionalidades");
					FuncionalidadesRestriccionDelegate funcionalidaRestriccionDelegate=new FuncionalidadesRestriccionDelegate();// metodo que va a consultar se inicializa
					List<FuncionalidadesRestriccion> listaFuncionalidadesRestriccion=funcionalidaRestriccionDelegate.listarTodas();
					forma.setListaFuncionalidadesRestriccion(listaFuncionalidadesRestriccion);
					//inicia check				
					List<String> funcionalidadesCheck=new ArrayList<String>();// se inicializa una lista de checks para ingresar datos en la jsp
					int cantfuncionalidades=listaFuncionalidadesRestriccion.size(); // cuenta cuantos elementos hay en la lista para iterar la lista de motivos
					logger.info("********************** Tamaño de la tabla "+cantfuncionalidades);
					for(int i=0; i<cantfuncionalidades; i++)// itera la lista de motivos
					{
						funcionalidadesCheck.add("");// inicializa la lista de check en blanco para poder usarla en la jsp
					}
					forma.setFuncionalidadesCheck(funcionalidadesCheck); // actualiza la forma con la lista de checks inicializada
					// actualiza la forma con los motivos a mostrar en la jsp.
					HibernateUtil.endTransaction(); // finaliza transaccion
					//************************************************************************






					return mapping.findForward("detalle"); //llama la pagina donde se capturan los check y se muestran las listas.
				}
				else if (estado.equals("modificar"))
				{
					//ClasificaPacientesOdo clasificaPacientesOdo=new ClasificaPacientesOdoDelegate().findById(forma.getClasificaPacientesOdo().getCodigoPk());// trae todos los elementos que estan guardados en la bd para la tabla clasifica_pacientes_odo

					//forma.setClasificaPacientesOdo(clasificaPacientesOdo);// los elementros traidos actualiza la forma
					return mapping.findForward("detalle");
				}
				else if (estado.equals("listarClasificaciones"))
				{
					return mapping.findForward("principal");

				}
				else if (estado.equals("guardar"))
				{

					return accionGuardar(usuario,forma,request,mapping);


				}


			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("detalle");

	}



	/**
	 * 
	 * @param usuario 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardar(UsuarioBasico usuario, ParametricaClasificacionPacienteOdoForm forma,HttpServletRequest request, ActionMapping mapping) 
	{
		logger.info("*****************entra guardar********");
		HibernateUtil.beginTransaction();
		
		//guardar encabezado.
		ClasificaPacientesOdoDelegate daoCPO=new ClasificaPacientesOdoDelegate();
		forma.getClasificaPacientesOdo().getUsuarios().setLogin(usuario.getLoginUsuario());
		forma.getClasificaPacientesOdo().getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
		forma.getClasificaPacientesOdo().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getClasificaPacientesOdo().setHoraModifica(UtilidadFecha.getHoraActual().substring(0,5));
		daoCPO.persist(forma.getClasificaPacientesOdo());
		//****************************
		
		// Guarda la seccion Saldo Disponible
		if(UtilidadTexto.getBoolean(forma.getSecSaldopacCheck()))
		{
			ClasOdoSecSalDispHome daoCSSDH=new ClasOdoSecSalDispHome();
			forma.getClasOdoSecSalDisp().setClasificaPacientesOdo(forma.getClasificaPacientesOdo());
			daoCSSDH.persist(forma.getClasOdoSecSalDisp());
		}//*************************************
		
		
		
		//Guarda la Seccion Estados del presupuesto
		if(UtilidadTexto.getBoolean(forma.getSecEstadosPresCheck()))
		{
			
	
			for(int i=0;i<forma.getPresupuChecK().size();i++)
			{
				String estadoTempo=forma.getPresupuChecK().get(i);
				if (!estadoTempo.equals(ConstantesIntegridadDominio.acronimoContratado))
				{	
						if(!estadoTempo.equals(""))
						{
							logger.info("*****************Estado presupuesto ********:"+estadoTempo);
							ClasOdoSecEstadosPres estado=new ClasOdoSecEstadosPres();
							estado.setEstado(estadoTempo);
							estado.setClasificaPacientesOdo(forma.getClasificaPacientesOdo());
							ClasOdoSecEstadosPresHome coseph=new ClasOdoSecEstadosPresHome();
							coseph.persist(estado);
							forma.getClasificaPacientesOdo().getClasOdoSecEstadosPreses().add(estado);
									
						}
				}else// Guarda los indices de los estados del presupuesto
				{
					for(int j=0;j<forma.getPresIndCheck().size();j++)
					{
						String estadoIndTempo=forma.getPresIndCheck().get(j);
						if(!estadoIndTempo.equals(""))
						{
							logger.info("*****************Indicativo Presupuesto ********:"+estadoIndTempo);
							ClasOdoSecEstadosPres estado=new ClasOdoSecEstadosPres();
							estado.setEstado(estadoIndTempo);
							estado.setClasificaPacientesOdo(forma.getClasificaPacientesOdo());
							ClasOdoSecEstadosPresHome coseph=new ClasOdoSecEstadosPresHome();
							coseph.persist(estado);
							forma.getClasificaPacientesOdo().getClasOdoSecEstadosPreses().add(estado);
						}
						
					}
				}//***************************************************************
			}
			
		}/////////********************************************************************
		
		//Guarda la Seccion Tipos Cita
		if(UtilidadTexto.getBoolean(forma.getSecTiposCitaCheck()))
		{
			
			for (int i=1;i<forma.getTipoCCheck().size();i++)
				{
					String tipoTempo=forma.getTipoCCheck().get(i)   ;
					if (!tipoTempo.equals(""))
					{
						logger.info("*****************Estado presupuesto ********:"+tipoTempo);
						ClasOdoSecTiposCita tipoCita= new ClasOdoSecTiposCita();
						tipoCita.setTipoCita(tipoTempo);
						tipoCita.setClasificaPacientesOdo(forma.getClasificaPacientesOdo());
						ClasOdoSecTiposCitaHome COSTCH= new ClasOdoSecTiposCitaHome();
						COSTCH.persist(tipoCita);
						forma.getClasificaPacientesOdo().getClasOdoSecTiposCitas().add(tipoCita);
						
					}
					
				}
		}//// FIN GUARDA LA SECCION TIPOS CITA
		
		
		//Guarda la Seccion Estados Cita
		
		logger.info("*****************chec de cita ********:"+forma.getSecEstadosCitaCheck());
		if(UtilidadTexto.getBoolean(forma.getSecEstadosCitaCheck()))
		{
			
			for (int i=1;i<forma.getEstadosCCheck().size();i++)
				{
					String estadosTempo=forma.getEstadosCCheck().get(i)   ;
					String citasmigradasTempo= forma.getMigraCheck();
					
					if (!estadosTempo.equals(""))
					{
						logger.info("*****************Estado cita ********:"+estadosTempo);
						logger.info("*****************citas migradas ********:"+citasmigradasTempo);
						ClasOdoSecEstCita estadosCita= new ClasOdoSecEstCita();
						estadosCita.setEstadoCita((estadosTempo));
					    estadosCita.setClasificaPacientesOdo(forma.getClasificaPacientesOdo());
					    estadosCita.setCitasMigradas(citasmigradasTempo);
						ClasOdoSecEstCitaHome COSECH= new ClasOdoSecEstCitaHome();
						COSECH.persist(estadosCita);
						forma.getClasificaPacientesOdo().getClasOdoSecEstCitas().add(estadosCita);
						
					}
					
				}
		}//// FIN GUARDA LA SECCION TIPOS CITA
		
		
		
		
		
		
		
		
		
		//Guarda la Seccion indicativos confirmacion
		if(UtilidadTexto.getBoolean(forma.getSecIndConfCheck()))
		{
			
		
			
				String IndConfTempo=forma.getConfCheck();
				String citasmigradasTempo= forma.getMigraCheckn();
				logger.info("*****************nombre del indicativo  ********:"+IndConfTempo);
				if (!IndConfTempo.equals(""))
					{
						
							
								
									
											logger.info("*****************motivo confirmado ********:"+IndConfTempo);
											ClasOdoSecIndConf indicativo=new ClasOdoSecIndConf();
											indicativo.setConfirmada(IndConfTempo);
											indicativo.setClasificaPacientesOdo(forma.getClasificaPacientesOdo());
											indicativo.setCitasMigradas(citasmigradasTempo);
											ClasOdoSecIndConfHome COSICH= new ClasOdoSecIndConfHome();
											COSICH.persist(indicativo);
											forma.getClasificaPacientesOdo().getClasOdoSecIndConfs().add(indicativo);
											
											
											
							
								
					}
				
				
				
				
				String motivosnTempo=forma.getNcConfCheck();
				if (!motivosnTempo.equals(""))
				{
					
							
								
										logger.info("*****************motivo no confirmado ********:"+motivosnTempo);
										ClasOdoSecIndConf indicativo=new ClasOdoSecIndConf();
										indicativo.setConfirmada(motivosnTempo);
										indicativo.setClasificaPacientesOdo(forma.getClasificaPacientesOdo());
										indicativo.setCitasMigradas(ConstantesBD.acronimoNo);
										ClasOdoSecIndConfHome COSICH= new ClasOdoSecIndConfHome();
										COSICH.persist(indicativo);
										forma.getClasificaPacientesOdo().getClasOdoSecIndConfs().add(indicativo);
							
										

										for(int i=0;i<forma.getMotivosCheck().size();i++)
										{
											String motivoTempo=forma.getMotivosCheck().get(i);
											if (!motivoTempo.equals(""))
												{
													logger.info("***************** cuando no confirmado ********:"+motivoTempo);
													ClasOdoMotivosNoConfir motivo=new ClasOdoMotivosNoConfir();
													motivo.getMotivosCita().setTipoMotivo(motivoTempo);
													motivo.setClasOdoSecIndConf(indicativo);
													ClasOdoMotivosNoConfirHome COSMCH= new ClasOdoMotivosNoConfirHome();
													COSMCH.persist(motivo);
												    forma.getClasificaPacientesOdo().getClasOdoSecIndConfs().add(indicativo);
													
												}
										}		
								
						
								
								
										
										
										
										
										
										
										
										
							
			
				
				
				
				
				
					
						
						
						
						
					
					
					}
					
					
		}	
			
		
		HibernateUtil.endTransaction();
		

		
		return mapping.findForward("detalle");
		}
	}
		
	
