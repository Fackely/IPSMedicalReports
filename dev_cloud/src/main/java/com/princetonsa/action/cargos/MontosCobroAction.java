/*
 * Creado en 2/07/2004
 *
 * Juan David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.action.cargos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cargos.MontosCobroForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseMontosCobroDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.MontosCobro;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Juan David Ramírez
 *
 */
public class MontosCobroAction extends Action
{
	/**
	 * Manejo de estados
	 */
	private String estado="";
	
	/**
	 * Forma de montos de cobro
	 */
	private MontosCobroForm montosForm=null;
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(MontosCobroAction.class);
	
	/**
	 * Mundo de montos de cobro
	 */
	private MontosCobro montosCobroMundo=null;
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		
		Connection con=null;
		try{
		ActionErrors errores=null;
		if(form instanceof MontosCobroForm)
		{
			montosForm=(MontosCobroForm)form;

			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			//PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			estado=montosForm.getEstado();
			logger.warn("estado->"+estado);
			
			try
			{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				//No se cierra conexión porque si llega aca ocurrió un
				//error al abrirla
				logger.error("Problemas abriendo la conexión en ServiciosAction");
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			
			if(estado!=null)
			{
				if(estado.equals("empezar"))
				{
					montosForm.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresar");
				}
				if(estado.equals("adicionar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresar");
				}
				if(estado.equals("modificar"))
				{
					UtilidadBD.closeConnection(con);
					montosForm.resetBusqueda();
					return mapping.findForward("buscarModificar");
				}
				if(estado.equals("resultadoModificar"))
				{
					consultar(con, false);
					UtilidadBD.closeConnection(con);
					montosForm.setEstado("resultadoBusqueda");
					return mapping.findForward("modificar");
				}
				if(estado.equals("detalleModificar"))
				{
					return accionDetalleModificar(con,mapping);
				}
				if(estado.equals("guardarModificacion"))
				{
					modificar(con, usuario.getLoginUsuario());
					///Se instancia mundo de MontosCobro
					montosCobroMundo = new MontosCobro();
					
					//Se asigna código del monto de cobro que se va a cargar
					montosCobroMundo.setCodigo(montosForm.getCodigo());
					montosForm.setConsulta(montosCobroMundo.bucarMontos(con,false));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenModificar");
				}
				if(estado.equals("consultar"))
				{
					montosForm.resetBusqueda();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("consultar");
				}
				if(estado.equals("buscarOtro"))
				{
					//montosForm.resetChecks();
					montosForm.setEstado("consultar");	
					UtilidadBD.closeConnection(con);
					return mapping.findForward("consultar");
				}
				if(estado.equals("guardar"))
				{
					guardar(con, montosForm.getEliminados());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumen");
				}
				if(estado.equals("salir"))
				{
					montosForm.setNumeroIngresos(montosForm.getNumeroIngresos()+1);
					guardar(con, montosForm.getEliminados());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumen");
				}
				if(estado.equals("resultadoBusqueda"))
				{
					consultar(con, false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("consultar");
				}
				if(estado.equals("ordenar"))
				{
					UtilidadBD.closeConnection(con);
					return accionOrdenar(mapping);
				}
				if(estado.equals("ordenarResumen"))
				{
					UtilidadBD.closeConnection(con);
					return accionOrdenarResumen(mapping);
				}
				if(estado.equals("obtenerFechaXViaIngreso"))
				{
					UtilidadBD.closeConnection(con);
				}
				
				if (estado.equals("imprimir"))
				{
					String nombreRptDesign = "ListadoMontosCobro.rptdesign";
					InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
					Vector v;
					String parametros=""+montosCobroMundo.getFecha();
					logger.info("LA FECHA DE LA CONSULTA------>"+parametros);
					
					//***************** INFORMACIÓN DEL CABEZOTE
				    DesignEngineApi comp; 
				    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
				     
				    // Logo
				    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
				    
				    // Nombre Institución, titulo y rango de fechas
				    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
				    
				    v=new Vector();
				    v.add(ins.getRazonSocial());
				    v.add("NIT. "+ins.getNit());
				    v.add("\nIMPRESIÓN MONTOS COBRO");
				    comp.insertLabelInGridOfMasterPage(0,1,v);
				    
				    // Parametros de Generación
				    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
				    v=new Vector();

				    //Parámetros de búsqueda
				
				    v.add(SqlBaseMontosCobroDao.getParamsReporte()); 
				    
			    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
				    
				    // Fecha hora de proceso y usuario
				    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
				    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
				    
				    //****************** FIN INFORMACIÓN DEL CABEZOTE
					
				    comp.obtenerComponentesDataSet("dataSet");
				    
				    //***************** NUEVA CONSULTA DEL REPORTE
				    logger.info("Consulta > "+SqlBaseMontosCobroDao.getConsultaSql());
				    comp.modificarQueryDataSet(SqlBaseMontosCobroDao.getConsultaSql());
				    //*****************
				   
				    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
				    comp.updateJDBCParameters(newPathReport);
				   
				    if(!newPathReport.equals(""))
				    {
				    	request.setAttribute("isOpenReport", "true");
				    	request.setAttribute("newPathReport", newPathReport);
				    }
				    	    
				    consultar(con, false);
				    UtilidadBD.closeConnection(con);
				    return mapping.findForward("consultar");
				}

				UtilidadBD.closeConnection(con);
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("descripcionError");
			}
			else
			{
				UtilidadBD.closeConnection(con);
				errores.add("noForm", new ActionMessage("errors.form", "MontosCobroForm"));
				return mapping.findForward("paginaError");
			}

		}
		else
		{
			errores.add("noForm", new ActionMessage("errors.form", "MontosCobroForm"));
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
	 * Método implementado para cargar el detalle del registro que que va a modificar
	 * @param con
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleModificar(Connection con, ActionMapping mapping) 
	{
		//Se instancia mundo de MontosCobro
		montosCobroMundo = new MontosCobro();
		
		//Se asigna código del monto de cobro que se va a cargar
		montosCobroMundo.setCodigo(montosForm.getCodigo());
		montosForm.setConsulta(new ArrayList());
		montosForm.setConsulta(montosCobroMundo.bucarMontos(con,true));
		//se inicializa mapa
		llenarMap();
		
		this.cerrarConexion(con);
		return mapping.findForward("detalle");
	}

	/**
	 * @param mapping
	 * @param montosForm2
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping)
	{
		montosForm.setEstado("resultadoBusqueda");
		
		try
		{
			montosForm.setConsulta(Listado.ordenarColumna(new ArrayList(montosForm.getConsulta()), montosForm.getColumnaAnterior(), montosForm.getColumna()));
			montosForm.setColumnaAnterior(montosForm.getColumna());
		}
		catch (IllegalAccessException e)
		{
			logger.error("Error convirtiendo la colección a arraylist "+e);
		}
		return mapping.findForward("consultar");
	}

	/**
	 * Accion ordenar para el listado despues de guardar la modificacion
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarResumen(ActionMapping mapping)
	{
		montosForm.setEstado("resultadoBusqueda");
		
		try
		{
			montosForm.setConsulta(Listado.ordenarColumna(new ArrayList(montosForm.getConsulta()), montosForm.getColumnaAnterior(), montosForm.getColumna()));
			montosForm.setColumnaAnterior(montosForm.getColumna());
		}
		catch (IllegalAccessException e)
		{
			logger.error("Error convirtiendo la colección a arraylist "+e);
		}
		return mapping.findForward("modificar");
	}
	
	private boolean modificar(Connection con, String usuario)
	{
		montosCobroMundo=new MontosCobro();
		HashMap ingreso=montosForm.getMapPropiedades();
		for(int i=0; i<montosForm.getNumeroIngresos(); i++)
		{
			montosCobroMundo.setCodigo(Integer.parseInt(""+ingreso.get("codigo_"+i)));
			montosCobroMundo.setConvenio(Integer.parseInt(""+ingreso.get("convenio_"+i)));
			montosCobroMundo.setEstratoSocial(Integer.parseInt(""+ingreso.get("estratoSocial_"+i)));
			if(!(""+ingreso.get("porcentaje_"+i)).equals(""))
			{
				montosCobroMundo.setPorcentaje(Float.parseFloat(""+ingreso.get("porcentaje_"+i)));
			}
			else
			{
				montosCobroMundo.setPorcentaje(ConstantesBD.codigoNuncaValido);
			}
			montosCobroMundo.setTipoAfiliado(String.valueOf(ingreso.get("tipoAfiliado_"+i)));
			montosCobroMundo.setTipoMonto(Integer.parseInt(""+ingreso.get("tipoMonto_"+i)));
			if(!(""+ingreso.get("valor_"+i)).equals(""))
			{
				montosCobroMundo.setValor(Float.parseFloat(""+ingreso.get("valor_"+i)));
			}
			else
			{
				montosCobroMundo.setValor(ConstantesBD.codigoNuncaValido);
			}
			montosCobroMundo.setViaIngreso(Integer.parseInt(""+ingreso.get("viaIngreso_"+i)));
			montosCobroMundo.setActivo(UtilidadTexto.getBoolean(""+ingreso.get("activo_"+i)));
			montosCobroMundo.setFecha(UtilidadFecha.conversionFormatoFechaABD( ingreso.get("fecha_"+i)+""));
			
			MontosCobro montoCobroBD=new MontosCobro();
			montoCobroBD.setCodigo(montosCobroMundo.getCodigo());
			montoCobroBD.cargar(con);
			
			if(sonMontosIguales(montosCobroMundo,montoCobroBD))
			{
				if(!montosCobroMundo.modificarMonto(con))
				{
					logger.error("error modificando el registro número "+i+1);
				}
				else
				{
					String modificacion=
						"\n          ===========INFORMACION ORIGINAL========= " +
						"\n*	Código Monto [" +montoCobroBD.getCodigo() +"] "+
						"\n*	Convenio [" +montoCobroBD.getConvenio() +"] "+
						"\n* 	Clasificación Socioeconómica [" +montoCobroBD.getEstratoSocial() +"] "+
						"\n*	Porcentaje [" +montoCobroBD.getPorcentaje() +"] "+
						"\n*	Tipo de Afiliado [" +montoCobroBD.getTipoAfiliado() +"] "+
						"\n*	Tipo de monto [" +montoCobroBD.getTipoMonto() +"] "+
						"\n*	Valor [" +montoCobroBD.getValor() +"] "+
						"\n*	Activo [" +montoCobroBD.getActivo() +"] "+
						"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
						"\n*	Código Monto [" +montosCobroMundo.getCodigo() +"] "+
						"\n*	Convenio [" +montosCobroMundo.getConvenio() +"] "+
						"\n* 	Clasificación Socioeconómica [" +montosCobroMundo.getEstratoSocial() +"] "+
						"\n*	Porcentaje [" +montosCobroMundo.getPorcentaje() +"] "+
						"\n*	Tipo de Afiliado [" +montosCobroMundo.getTipoAfiliado() +"] "+
						"\n*	Tipo de monto [" +montosCobroMundo.getTipoMonto() +"] "+
						"\n*	Valor [" +montosCobroMundo.getValor() +"] "+
						"\n*	Activo [" +montosCobroMundo.getActivo() +"] "+
						"\n*	Vigencia Inicial [" +montosCobroMundo.getFecha() +"] ";
	
					LogsAxioma.enviarLog(ConstantesBD.logMontosCobroCodigo, modificacion, 2, usuario);
	
				}
			}
		}
		return false;
	}
	
	/**
	 * Método para comparar 2 montos de cobro
	 * @param montosCobroMundo
	 * @param montoCobroBD
	 * @return true si son iguales
	 */
	private boolean sonMontosIguales(MontosCobro montosCobroMundo, MontosCobro montoCobroBD)
	{
		return !(
					montosCobroMundo.getCodigo()==montoCobroBD.getCodigo() &&
					montosCobroMundo.getConvenio()==montoCobroBD.getConvenio() &&
					montosCobroMundo.getEstratoSocial()==montoCobroBD.getEstratoSocial() &&
					montosCobroMundo.getPorcentaje()==montoCobroBD.getPorcentaje() &&
					montosCobroMundo.getTipoAfiliado().equals(montoCobroBD.getTipoAfiliado()) &&
					montosCobroMundo.getTipoMonto()==montoCobroBD.getTipoMonto() &&
					montosCobroMundo.getValor()==montoCobroBD.getValor() &&
					montosCobroMundo.getViaIngreso()==montoCobroBD.getViaIngreso() &&
					montosCobroMundo.getActivo()==montoCobroBD.getActivo() &&
					montosCobroMundo.getFecha()==montoCobroBD.getFecha()
					
				);
	}

	private boolean guardar(Connection con, String eliminados)
	{
		montosCobroMundo=new MontosCobro();
		HashMap ingreso=montosForm.getMapPropiedades();
		String[] eliminado=new String[1];
		String[] temp4 = new String[1];
		if(!eliminados.equals(""))
		{
			if(eliminado.length>1)
				eliminado=eliminados.split("-");
			else
				eliminado[0]=eliminados;
			
			temp4 = eliminado[0].split("-");
		}
		else			
		{
			eliminado[0]=new String("-1");
			temp4[0]=new String("-1");
		}
		
		logger.info("valor de i >> "+montosForm.getNumeroIngresos());
		Utilidades.imprimirMapa(ingreso);
		
		
		for(int i=0; i<montosForm.getNumeroIngresos(); i++)
		{
			boolean puedoValidar=true;
			for(int j=0;j<temp4.length;j++)
			{
				if(i==Integer.parseInt(temp4[j]))
				{
					puedoValidar=false;
				}
			}
			if(puedoValidar)
			{
				montosCobroMundo.setConvenio(Integer.parseInt(""+ingreso.get("convenio_"+i)));
				montosCobroMundo.setEstratoSocial(Integer.parseInt(""+ingreso.get("estratoSocial_"+i)));
				montosCobroMundo.setFecha((""+ingreso.get("fecha_"+i)));
				String tempo=ingreso.get("porcentaje_"+i)+"";
				if(tempo!=null && !tempo.equals(""))
					montosCobroMundo.setPorcentaje(Float.parseFloat(""+ingreso.get("porcentaje_"+i)));
				else
					montosCobroMundo.setPorcentaje(-1);
				montosCobroMundo.setTipoAfiliado(String.valueOf(ingreso.get("tipoAfiliado_"+i)));
				montosCobroMundo.setTipoMonto(Integer.parseInt(""+ingreso.get("tipoMonto_"+i)));
				tempo=ingreso.get("valor_"+i)+"";
				if(tempo!=null && !tempo.equals(""))
					montosCobroMundo.setValor(Float.parseFloat(""+ingreso.get("valor_"+i)));
				else
					montosCobroMundo.setValor(-1);
				montosCobroMundo.setViaIngreso(Integer.parseInt(""+ingreso.get("viaIngreso_"+i)));
				
				//tempo=ingreso.get("activo_"+i)+"";
				//if(tempo.equals("null") || tempo.equals("off"))
				//{
					//montosCobroMundo.setActivo(false);
				//}
				//else if(tempo.equals("on"))
				//{
				montosCobroMundo.setActivo(true);
				//}
				
				if(montosCobroMundo.getViaIngreso()==-1)
				{
					montosCobroMundo.setViaIngreso(ConstantesBD.codigoViaIngresoAmbulatorios);
					if(!montosCobroMundo.insertarMonto(con))
					{
						logger.error("No se insertó el monto número "+i+" para la via de ingreso \"Ambulatorios\"");
					}
					montosCobroMundo.setViaIngreso(ConstantesBD.codigoViaIngresoConsultaExterna);
					if(!montosCobroMundo.insertarMonto(con))
					{
						logger.error("No se insertó el monto número "+i+" para la via de ingreso \"ConsultaExterna\"");
					}
					montosCobroMundo.setViaIngreso(ConstantesBD.codigoViaIngresoHospitalizacion);
					if(!montosCobroMundo.insertarMonto(con))
					{
						logger.error("No se insertó el monto número "+i+" para la via de ingreso \"Hospitalizacion\"");
					}
					montosCobroMundo.setViaIngreso(ConstantesBD.codigoViaIngresoUrgencias);
					if(!montosCobroMundo.insertarMonto(con))
					{
						logger.error("No se insertó el monto número "+i+" para la via de ingreso \"Urgencias\"");
					}
				}
				else if(!montosCobroMundo.insertarMonto(con))
				{
					logger.error("No se insertó el monto número "+i);
				}
			}
		}
		return true;
	}
	
	/**
	 * Método para consultar los montos de cobro
	 * @param con
	 * @param esModificar
	 * @return true si la cionsulta se hizo correctamente
	 */
	private boolean consultar(Connection con, boolean esModificar)
	{
		HashMap datosConsulta=montosForm.getMapPropiedades();
		montosCobroMundo=new MontosCobro();
		
		if(!datosConsulta.get("codigo").equals(""))
		{
			montosCobroMundo.setCodigo(Integer.parseInt(datosConsulta.get("codigo")+""));
		}
		else
		{
			montosCobroMundo.setCodigo(0);
		}
		if(!datosConsulta.get("convenio").equals(""))
		{
			montosCobroMundo.setConvenio(Integer.parseInt(datosConsulta.get("convenio")+""));
		}
		else
		{
			montosCobroMundo.setConvenio(0);
		}
		if(!datosConsulta.get("viaIngreso").equals(""))
		{
			montosCobroMundo.setViaIngreso(Integer.parseInt(datosConsulta.get("viaIngreso")+""));
		}
		else
		{
			montosCobroMundo.setViaIngreso(0);
		}
		if(!datosConsulta.get("tipoAfiliado").equals(""))
		{
			montosCobroMundo.setTipoAfiliado(datosConsulta.get("tipoAfiliado")+"");
		}
		else
		{
			montosCobroMundo.setTipoAfiliado("");
		}
		if(!datosConsulta.get("estratoSocial").equals(""))
		{
			montosCobroMundo.setEstratoSocial(Integer.parseInt((datosConsulta.get("estratoSocial")+"").trim()));
		}
		else
		{
			montosCobroMundo.setEstratoSocial(0);
		}
		if(!datosConsulta.get("tipoMonto").equals(""))
		{
			montosCobroMundo.setTipoMonto(Integer.parseInt(datosConsulta.get("tipoMonto")+""));
		}
		else
		{
			montosCobroMundo.setTipoMonto(0);
		}
		if(!datosConsulta.get("valor").equals(""))
		{
			montosCobroMundo.setValor(Float.parseFloat(datosConsulta.get("valor")+""));
		}
		else
		{
			montosCobroMundo.setValor(-1);
		}
		if(!datosConsulta.get("porcentaje").equals(""))
		{
			montosCobroMundo.setPorcentaje(Float.parseFloat(datosConsulta.get("porcentaje")+""));
		}
		else
		{
			montosCobroMundo.setPorcentaje(-1);
		}
		if(!datosConsulta.get("vigenciaInicial").equals(""))
		{
			montosCobroMundo.setFecha(UtilidadFecha.conversionFormatoFechaABD(datosConsulta.get("vigenciaInicial")+""));
		}
		else
		{
			montosCobroMundo.setFecha("");
		}
		Utilidades.imprimirMapa(datosConsulta);
		datosConsulta.put("activo","");
		datosConsulta.put("activo","0");
		if(!datosConsulta.get("activo").equals("")&&!datosConsulta.get("activo").equals("0"))
		{
			String activoStr=(String)datosConsulta.get("activo");
			if(UtilidadTexto.getBoolean(activoStr))
			{
				activoStr=ValoresPorDefecto.getValorTrueParaConsultas();
			}
			else 
			{
				activoStr=ValoresPorDefecto.getValorFalseParaConsultas();
			}
			montosCobroMundo.setActivo(UtilidadTexto.getBoolean(activoStr));
			montosCobroMundo.setActivoString(activoStr);
		}
		else
		{
			montosCobroMundo.setActivoString("");
		}
		montosForm.setConsulta(new ArrayList());
		montosForm.setConsulta(montosCobroMundo.bucarMontos(con, esModificar));
		return true;
	}
	
	private void llenarMap()
	{
		Collection col=montosForm.getConsulta();
		if(!col.isEmpty())
		{
			Iterator iterador=col.iterator();
			HashMap propiedades=montosForm.getMapPropiedades();
			for(int i=0; i<col.size(); i++)
			{
				HashMap fila=(HashMap)iterador.next();
				propiedades.put("codigo_"+i,fila.get("codigo")+"");
				propiedades.put("convenio_"+i,fila.get("convenio")+"");
				propiedades.put("estratoSocial_"+i,fila.get("estratosocial")+"");
				String tempo=fila.get("porcentaje")+"";
				if(!tempo.equals("null"))
					propiedades.put("porcentaje_"+i,tempo);
				else
					propiedades.put("porcentaje_"+i,"");
				propiedades.put("tipoAfiliado_"+i,fila.get("tipoafiliado")+"");
				propiedades.put("tipoMonto_"+i,fila.get("tipomonto")+"");
				tempo=fila.get("valor")+"";
				if(!tempo.equals("null"))
				{
					int valor = (int)Utilidades.convertirAFloat(tempo);
					if (valor<0)
						propiedades.put("valor_"+i,"");
					else
						propiedades.put("valor_"+i,valor+"");
				}
				else
					propiedades.put("valor_"+i,"");
				propiedades.put("viaIngreso_"+i,fila.get("viaingreso")+"");
				propiedades.put("activo_"+i,fila.get("activo")+"");
				propiedades.put("fecha_"+i,UtilidadFecha.conversionFormatoFechaAAp(fila.get("fecha")+""));
			}
			montosForm.setMapPropiedades(propiedades);
		}
		
	}
		
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
}
