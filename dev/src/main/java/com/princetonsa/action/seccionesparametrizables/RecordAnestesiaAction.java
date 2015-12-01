package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.seccionesparametrizables.RecordAnestesiaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.Eventos;
import com.princetonsa.mundo.parametrizacion.GasesHojaAnestesia;
import com.princetonsa.mundo.parametrizacion.InfoGeneralHA;
import com.princetonsa.mundo.parametrizacion.SignosVitales;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class RecordAnestesiaAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(RecordAnestesiaAction.class);
    
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try {
    		if (response==null); 
    		if(form instanceof RecordAnestesiaForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			RecordAnestesiaForm forma =(RecordAnestesiaForm)form;
    			String estado=forma.getEstado();
    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en RECORD ANESTESIA es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Recor Anestesia (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("graficaRecordAnestesia"))
    			{
    				return this.accionGraficaRecordAnestesia(forma,request, mapping, con, usuario);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Recor Anestesia");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
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
    
    
    /**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionGraficaRecordAnestesia(RecordAnestesiaForm forma, HttpServletRequest request,ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		ActionErrors errores = new ActionErrors(); 
		
		if(request != null)
			inicializarParametrosRequest(forma,request);
		
		//EL RANGO DE TIEMPO A GRAFICAR ESTA DADO DENTRO DE LA FECHA/HORA INICIAL CX - FECHA/HORA FINAL CX
		SolicitudesCx solicitudCx = new SolicitudesCx();		
		solicitudCx.cargarEncabezadoSolicitudCx(forma.getNumeroSolicitud()+"");
		
		logger.info("\n\nsolicitudCx.getFechaIngresoSala()-->"+solicitudCx.getFechaIngresoSala()+" solicitudCx.getFechaSalidaSala()-->"+solicitudCx.getFechaSalidaSala()+ " solicitudCx.getHoraIngresoSala->"+solicitudCx.getHoraIngresoSala()+" solicitudCx.getHoraSalidaSala()->"+solicitudCx.getHoraSalidaSala()+"\n\n");
		
		if(	UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala()) 
			|| UtilidadTexto.isEmpty(solicitudCx.getHoraIngresoSala()))
		{	
			errores.add("", new ActionMessage("errors.required", "La fecha/hora ingreso sala"));
        	saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("graficaRecordAnestesia");
		}
		else
		{
			//si la fecha y hora es vacia entonces consultamos la ultima fecha y hora insertada de las secciones graficables
			if(UtilidadTexto.isEmpty(solicitudCx.getFechaSalidaSala())
				|| UtilidadTexto.isEmpty(solicitudCx.getHoraSalidaSala()))
			{
				String[] fechaHoraSecciones= InfoGeneralHA.obtenerUltimaFechaHoraGraficarRecord(con, forma.getNumeroSolicitud());
				
				//en caso de que no exista ninguna otra parametrizacion entonces no se grafica
				if(UtilidadTexto.isEmpty(fechaHoraSecciones[0]) 
					|| UtilidadTexto.isEmpty(fechaHoraSecciones[1]))
				{
					//errores.add("", new ActionMessage("errors.required", "La(s) fecha(s)/hora(s) de las secciones para graficar "));
		        	//saveErrors(request, errores);
		            UtilidadBD.closeConnection(con);
		            return mapping.findForward("graficaRecordAnestesia");
				}
				else
				{
					forma.setFechaInicial(solicitudCx.getFechaIngresoSala());
					forma.setHoraInicial(solicitudCx.getHoraIngresoSala());
					forma.setFechaFinal( fechaHoraSecciones[0] );
					forma.setHoraFinal(fechaHoraSecciones[1]);
				}
			}
			else
			{
				forma.setFechaInicial(solicitudCx.getFechaIngresoSala());
				forma.setHoraInicial(solicitudCx.getHoraIngresoSala());
				forma.setFechaFinal(solicitudCx.getFechaSalidaSala());
				forma.setHoraFinal(solicitudCx.getHoraSalidaSala());
			}
			
			//en este punto ya deben estar seteadas tanto la fecha/hora inicial y fecha/hora final, entonces evaluamos que este rango de fechas
			//no supere las 72 horas, en caso de que sea mayor a ese rango de tiempo entonces debemos setear 72 horas como maximo
			
			int numDiasDiferencia= UtilidadFecha.numeroDiasEntreFechas(forma.getFechaInicial(), forma.getFechaFinal());
			if(numDiasDiferencia==3)
			{
				if(UtilidadFecha.esHoraMenorIgualQueOtraReferencia(forma.getHoraInicial(), forma.getHoraFinal()))
				{
					forma.setHoraFinal(forma.getHoraInicial());
				}
			}
			else if(numDiasDiferencia>3)
			{
				forma.setFechaFinal( UtilidadFecha.incrementarDiasAFecha(forma.getFechaInicial(), 3, false));
				forma.setHoraFinal(forma.getHoraInicial());
			}
			
			if(forma.getHoraInicial().trim().length()==4)
				forma.setHoraInicial("0"+forma.getHoraInicial().trim());
			if(forma.getHoraFinal().trim().length()==4)
				forma.setHoraFinal("0"+forma.getHoraFinal().trim());
			
			logger.info("Fecha inicio grafica: "+forma.getFechaInicial()+" Hora Inicio grafica: "+forma.getHoraInicial()+" Fecha Final grafica: "+forma.getFechaFinal()+" Hora final grafica: "+forma.getHoraFinal());
			
			forma.setMinutos(UtilidadFecha.numeroMinutosEntreFechas(forma.getFechaInicial() , forma.getHoraInicial(), forma.getFechaFinal(),forma.getHoraFinal()));
			
		}
		
		logger.info("------------------------------------------------------------------------------------------------------------------------------------");
		logger.info("------------------------------------------------------------------------------------------------------------------------------------");
		logger.info("Fecha inicio grafica: "+forma.getFechaInicial()+" Hora Inicio grafica: "+forma.getHoraInicial()+" Fecha Final grafica: "+forma.getFechaFinal()+" Hora final grafica: "+forma.getHoraFinal());
		logger.info("------------------------------------------------------------------------------------------------------------------------------------");
		logger.info("------------------------------------------------------------------------------------------------------------------------------------");
		
		errores=this.cargarSignosVitales(forma, con, usuario, errores);
		errores=this.cargarEventos(forma, con, usuario, errores);
		errores=this.cargarGases(forma, con, usuario, errores);
		errores=this.cargarMedicamentosLiquidos(forma, con, usuario, errores);
		errores=this.cargarInfusiones(forma, con, usuario, errores);
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("graficaRecordAnestesia");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("graficaRecordAnestesia");
	}	
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario
	 */
	private ActionErrors cargarSignosVitales(RecordAnestesiaForm forma, Connection con, UsuarioBasico usuario, ActionErrors errores)
	{
		forma.setMapaSignosVitales(SignosVitales.obtenerSignosVitalesHojaAnestesia(con, forma.getNumeroSolicitud(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoSi));
		
		logger.info("\n**********************************************************************************************************");
		logger.info("MAPA SIGNOS VITALES TITULO-->"+forma.getMapaTitulosSignosVitales());
		logger.info("\n**********************************************************************************************************\n");
		
		//OBTENEMOS EL NUMERO DE MINUTOS DE DIFERENCIA ENTRE LAS FECHAS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaCamposSignosVitales().get("numRegistros")+""); w++)
		{
			//72 horas
			int numMin=(3*24*60);
			if(!UtilidadFecha.betweenFechas(forma.getMapaCamposSignosVitales().get("fecha_"+w).toString(), forma.getMapaCamposSignosVitales().get("hora_"+w).toString(), forma.getFechaInicial(), forma.getHoraInicial(), forma.getFechaFinal(), forma.getHoraFinal()))
			{
				//error.recordAnestesia.seccion=La/el {0} con fecha/hora {1} esta por fuera del rango de la fecha/hora inicio cx {2} - fecha/hora final cx {3}, por favor verificar para poder graficar el record de anestesia.. [mh-01]
				errores.add("", new ActionMessage("error.recordAnestesia.seccion", 
													"Signo Vital", 
													forma.getMapaCamposSignosVitales().get("fecha_"+w).toString()+" - "+forma.getMapaCamposSignosVitales().get("hora_"+w).toString(),
													forma.getFechaInicial()+" "+forma.getHoraInicial(),
													forma.getFechaFinal()+" "+forma.getHoraFinal()));
			}
			else
			{	
				numMin=UtilidadFecha.numeroMinutosEntreFechas(forma.getFechaInicial(), forma.getHoraInicial(), forma.getMapaCamposSignosVitales().get("fecha_"+w).toString(),forma.getMapaCamposSignosVitales().get("hora_"+w).toString());
			}	
			forma.setMapaCamposSignosVitales("numeroMinutos_"+w, numMin);
			logger.info("\n numMin->"+numMin);
		}
		
		logger.info("\n**********************************************************************************************************");
		logger.info("MAPA SIGNOS VITALES CAMPOS-->"+forma.getMapaCamposSignosVitales());
		logger.info("\n**********************************************************************************************************\n");
		
		return errores;
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario
	 */
	private ActionErrors cargarEventos(RecordAnestesiaForm forma, Connection con, UsuarioBasico usuario, ActionErrors errores) 
	{
		forma.setMapaEventos(Eventos.cargarEventosHojaAnestesia(con, forma.getNumeroSolicitud(), ConstantesBD.acronimoSi));
		logger.info("\n**********************************************************************************************************");
		logger.info("MAPA EVENTOS-->"+forma.getMapaEventos());
		logger.info("\n**********************************************************************************************************\n");
		
		//OBTENEMOS EL NUMERO DE MINUTOS DE DIFERENCIA ENTRE LAS FECHAS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaEventos("numRegistros")+""); w++)
		{
			int numMin=ConstantesBD.codigoNuncaValido;
			boolean errorEvento= false;
			if(UtilidadTexto.isEmpty(forma.getMapaEventos().get("fecha_"+w).toString()) || UtilidadTexto.isEmpty(forma.getMapaEventos().get("hora_"+w).toString()))
			{
				/*errores.add("", new ActionMessage("errors.required", 
						"La Fecha/Hora final de la anestesia "));*/
				
				//error.recordAnestesia.seccion=La/el {0} con fecha/hora {1} esta por fuera del rango de la fecha/hora inicio cx {2} - fecha/hora final cx {3}, por favor verificar para poder graficar el record de anestesia.. [mh-01]
				/*errores.add("", new ActionMessage("error.recordAnestesia.seccion", 
													"Evento "+forma.getMapaEventos("nomevento_"+w), 
													forma.getMapaEventos().get("fecha_"+w).toString()+" - "+forma.getMapaEventos().get("hora_"+w).toString(),
													forma.getFechaInicial()+" "+forma.getHoraInicial(),
													forma.getFechaFinal()+" "+forma.getHoraFinal()));*/
				
				//forma.setMapaEventos("fecha_"+w, forma.getFechaInicial());
				//forma.setMapaEventos("hora_"+w, forma.getHoraInicial());
				errorEvento=true;
				
			}
			else if(!UtilidadFecha.betweenFechas(forma.getMapaEventos().get("fecha_"+w).toString(), forma.getMapaEventos().get("hora_"+w).toString(), forma.getFechaInicial(), forma.getHoraInicial(), forma.getFechaFinal(), forma.getHoraFinal()))
			{
				//error.recordAnestesia.seccion=La/el {0} con fecha/hora {1} esta por fuera del rango de la fecha/hora inicio cx {2} - fecha/hora final cx {3}, por favor verificar para poder graficar el record de anestesia.. [mh-01]
				errores.add("", new ActionMessage("error.recordAnestesia.seccion", 
													"Evento "+forma.getMapaEventos("nomevento_"+w), 
													forma.getMapaEventos().get("fecha_"+w).toString()+" - "+forma.getMapaEventos().get("hora_"+w).toString(),
													forma.getFechaInicial()+" "+forma.getHoraInicial(),
													forma.getFechaFinal()+" "+forma.getHoraFinal()));
				errorEvento=true;
			}
			if(!errorEvento)
			{	
				numMin=UtilidadFecha.numeroMinutosEntreFechas(forma.getFechaInicial(), forma.getHoraInicial(), forma.getMapaEventos().get("fecha_"+w).toString(),forma.getMapaEventos().get("hora_"+w).toString());
				if(numMin==0)
					numMin=1;
			}	
			forma.setMapaEventos("numeroMinutos_"+w, numMin);
			logger.info("\n numMin->"+numMin);
		}
		return errores;
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario
	 */
	private ActionErrors cargarGases(RecordAnestesiaForm forma, Connection con, UsuarioBasico usuario, ActionErrors errores) 
	{
		forma.setMapaGases(GasesHojaAnestesia.cargarGasesHojaAnestesia(con, forma.getNumeroSolicitud(), ConstantesBD.acronimoSi));
		logger.info("\n**********************************************************************************************************");
		logger.info("MAPA GASES-->"+forma.getMapaGases());
		logger.info("\n**********************************************************************************************************\n");
		
		//OBTENEMOS EL NUMERO DE MINUTOS DE DIFERENCIA ENTRE LAS FECHAS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaGases("numRegistros")+""); w++)
		{
			int numMin= ConstantesBD.codigoNuncaValido;
			if(!UtilidadFecha.betweenFechas(forma.getMapaGases().get("fecha_"+w).toString(), forma.getMapaGases().get("hora_"+w).toString(), forma.getFechaInicial(), forma.getHoraInicial(), forma.getFechaFinal(), forma.getHoraFinal()))
			{
				//error.recordAnestesia.seccion=La/el {0} con fecha/hora {1} esta por fuera del rango de la fecha/hora inicio cx {2} - fecha/hora final cx {3}, por favor verificar para poder graficar el record de anestesia.. [mh-01]
				errores.add("", new ActionMessage("error.recordAnestesia.seccion", 
													"Gases", 
													forma.getMapaGases().get("fecha_"+w).toString()+" - "+forma.getMapaGases().get("hora_"+w).toString(),
													forma.getFechaInicial()+" "+forma.getHoraInicial(),
													forma.getFechaFinal()+" "+forma.getHoraFinal()));
			}
			else
			{	
				numMin=UtilidadFecha.numeroMinutosEntreFechas(forma.getFechaInicial(), forma.getHoraInicial(), forma.getMapaGases().get("fecha_"+w).toString(),forma.getMapaGases().get("hora_"+w).toString());
				if(numMin==0)
					numMin=1;
			}	
			forma.setMapaGases("numeroMinutos_"+w, numMin);
			logger.info("\n numMin->"+numMin);
		}
		return errores;
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario
	 */
	private ActionErrors cargarMedicamentosLiquidos(RecordAnestesiaForm forma, Connection con, UsuarioBasico usuario, ActionErrors errores) 
	{
		forma.setMapaMedicamentos(HojaAnestesia.cargarGraficaHojaAnestesia(con, forma.getNumeroSolicitud(), ConstantesBD.acronimoSi, false));
		forma.setMapaLiquidos(HojaAnestesia.cargarGraficaHojaAnestesia(con, forma.getNumeroSolicitud(), ConstantesBD.acronimoSi, true));
		logger.info("\n**********************************************************************************************************");
		logger.info("MAPA MED -->"+forma.getMapaMedicamentos());
		logger.info("MAPA LIQUIDOS -->"+forma.getMapaLiquidos());
		logger.info("\n**********************************************************************************************************\n");
		
		//OBTENEMOS EL NUMERO DE MINUTOS DE DIFERENCIA ENTRE LAS FECHAS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaLiquidos("numRegistros")+""); w++)
		{
			int numMin= ConstantesBD.codigoNuncaValido;
			if(!UtilidadFecha.betweenFechas(forma.getMapaLiquidos().get("fecha_"+w).toString(), forma.getMapaLiquidos().get("hora_"+w).toString(), forma.getFechaInicial(), forma.getHoraInicial(), forma.getFechaFinal(), forma.getHoraFinal()))
			{
				//error.recordAnestesia.seccion=La/el {0} con fecha/hora {1} esta por fuera del rango de la fecha/hora inicio cx {2} - fecha/hora final cx {3}, por favor verificar para poder graficar el record de anestesia.. [mh-01]
				errores.add("", new ActionMessage("error.recordAnestesia.seccion", 
													"Liquidos", 
													forma.getMapaLiquidos().get("fecha_"+w).toString()+" - "+forma.getMapaLiquidos().get("hora_"+w).toString(),
													forma.getFechaInicial()+" "+forma.getHoraInicial(),
													forma.getFechaFinal()+" "+forma.getHoraFinal()));
			}
			else
			{	
				numMin=UtilidadFecha.numeroMinutosEntreFechas(forma.getFechaInicial(), forma.getHoraInicial(), forma.getMapaLiquidos().get("fecha_"+w).toString(),forma.getMapaLiquidos().get("hora_"+w).toString());
				if(numMin==0)
					numMin=1;
			}
			forma.setMapaLiquidos("numeroMinutos_"+w, numMin);
			logger.info("\n numMin->"+numMin);
		}
		//OBTENEMOS EL NUMERO DE MINUTOS DE DIFERENCIA ENTRE LAS FECHAS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaMedicamentos("numRegistros")+""); w++)
		{
			int numMin= ConstantesBD.codigoNuncaValido;
			if(!UtilidadFecha.betweenFechas(forma.getMapaMedicamentos().get("fecha_"+w).toString(), forma.getMapaMedicamentos().get("hora_"+w).toString(), forma.getFechaInicial(), forma.getHoraInicial(), forma.getFechaFinal(), forma.getHoraFinal()))
			{
				//error.recordAnestesia.seccion=La/el {0} con fecha/hora {1} esta por fuera del rango de la fecha/hora inicio cx {2} - fecha/hora final cx {3}, por favor verificar para poder graficar el record de anestesia.. [mh-01]
				errores.add("", new ActionMessage("error.recordAnestesia.seccion", 
													"Anestesicos/Medicamentos", 
													forma.getMapaMedicamentos().get("fecha_"+w).toString()+" - "+forma.getMapaMedicamentos().get("hora_"+w).toString(),
													forma.getFechaInicial()+" "+forma.getHoraInicial(),
													forma.getFechaFinal()+" "+forma.getHoraFinal()));
			}
			else
			{	
				numMin=UtilidadFecha.numeroMinutosEntreFechas(forma.getFechaInicial(), forma.getHoraInicial(), forma.getMapaMedicamentos().get("fecha_"+w).toString(),forma.getMapaMedicamentos().get("hora_"+w).toString());
				if(numMin==0)
					numMin=1;
			}	
			forma.setMapaMedicamentos("numeroMinutos_"+w, numMin);
			logger.info("\n numMin->"+numMin);
		}
		return errores;
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario
	 */
	private ActionErrors cargarInfusiones(RecordAnestesiaForm forma, Connection con, UsuarioBasico usuario, ActionErrors errores) 
	{
		forma.setMapaInfusiones(HojaAnestesia.cargarGraficaInfusionesHA(con, forma.getNumeroSolicitud(), ConstantesBD.acronimoSi));
		logger.info("\n**********************************************************************************************************");
		logger.info("MAPA INFUSIONES-->"+forma.getMapaInfusiones());
		logger.info("\n**********************************************************************************************************\n");
		
		//OBTENEMOS EL NUMERO DE MINUTOS DE DIFERENCIA ENTRE LAS FECHAS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaInfusiones("numRegistros")+""); w++)
		{
			int numMin= ConstantesBD.codigoNuncaValido;
			if(!UtilidadFecha.betweenFechas(forma.getMapaInfusiones().get("fecha_"+w).toString(), forma.getMapaInfusiones().get("hora_"+w).toString(), forma.getFechaInicial(), forma.getHoraInicial(), forma.getFechaFinal(), forma.getHoraFinal()))
			{
				//error.recordAnestesia.seccion=La/el {0} con fecha/hora {1} esta por fuera del rango de la fecha/hora inicio cx {2} - fecha/hora final cx {3}, por favor verificar para poder graficar el record de anestesia.. [mh-01]
				errores.add("", new ActionMessage("error.recordAnestesia.seccion", 
													"Infusiones", 
													forma.getMapaInfusiones().get("fecha_"+w).toString()+" - "+forma.getMapaInfusiones().get("hora_"+w).toString(),
													forma.getFechaInicial()+" "+forma.getHoraInicial(),
													forma.getFechaFinal()+" "+forma.getHoraFinal()));
			}
			else
			{	
				numMin=UtilidadFecha.numeroMinutosEntreFechas(forma.getFechaInicial(), forma.getHoraInicial(), forma.getMapaInfusiones().get("fecha_"+w).toString(),forma.getMapaInfusiones().get("hora_"+w).toString());
				if(numMin==0)
					numMin=1;
			}
			forma.setMapaInfusiones("numeroMinutos_"+w, numMin);
			logger.info("\n numMin->"+numMin);
		}
		return errores;
	}
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param RecordAnestesiaForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(RecordAnestesiaForm forma,HttpServletRequest request)
	{
		if (!(request.getParameter("esSinEncabezado")+"").equals("") && !(request.getParameter("esSinEncabezado")+"").equals("null"))
			forma.setEsSinEncabezado(request.getParameter("esSinEncabezado")+"");
		else
			forma.setEsSinEncabezado(ConstantesBD.acronimoNo);
		
		if (!(request.getParameter("mostrarDatosInfoActivo")+"").equals("") && !(request.getParameter("mostrarDatosInfoActivo")+"").equals("null"))
			forma.setMostrarDatosInfoActivo(UtilidadTexto.getBoolean(request.getParameter("mostrarDatosInfoActivo")+""));
		else
			forma.setMostrarDatosInfoActivo(false);
		
		if (!(request.getParameter("ocultarMenu")+"").equals("") && !(request.getParameter("ocultarMenu")+"").equals("null"))
			forma.setOcultarMenu(UtilidadTexto.getBoolean(request.getParameter("ocultarMenu")+""));
		else
			forma.setOcultarMenu(false);
	}
}