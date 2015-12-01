/*
 * Creado en Dec 15, 2005
 */
package com.princetonsa.action.presupuesto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

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
import util.ElementoApResource;
import util.RespuestaInsercionPersona;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoTarifaVigente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.presupuesto.ConsultaPresupuestoForm;
import com.princetonsa.actionform.presupuesto.PresupuestoPacienteForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.presupuesto.FormatoImpresionPresupuesto;
import com.princetonsa.mundo.presupuesto.PresupuestoPaciente;
import com.princetonsa.pdf.PresupuestoPdf;


/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 * 
 * Modificado por: Andrés Eugenio Silva Monsalve
 * Fecha: Julio 
 */
public class PresupuestoPacienteAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(PresupuestoPacienteAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
															ActionForm form,
															HttpServletRequest request,
															HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (response==null); //Para evitar que salga el warning
		
		if(form instanceof PresupuestoPacienteForm)
		{
						
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//HttpSession session=request.getSession();	
				//PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				PresupuestoPacienteForm forma = (PresupuestoPacienteForm) form;
				String estado=forma.getEstado();
			  	ConsultaPresupuestoForm formaConsulta=new ConsultaPresupuestoForm();
			  	PresupuestoPaciente mundo=new PresupuestoPaciente();

				
			  	logger.warn("Estado PresupuestoPacienteAction  [" + estado + "]");
			
			 //******************ESTADOS INICIALES**********************************
			if(estado.equals("empezar"))
			{
				
				return accionValidacionesIniciales(con,mapping,request,usuario,forma);
				
			}
			else if(estado.equals("decisionIngresoPacienteSistema"))
			{
				return accionIngresarPresupuesto(forma, mapping, request, con, usuario);
			}
			else if(estado.equals("guardar"))
			{
				forma.getResultadoProceso().put("procesoExitoso", ConstantesBD.acronimoNo);
				return accionGuardar(con, forma, mapping, usuario, request);
			}
			//*************************************************************
			//**************ESTADOS VINCULADOS CON LOS SERVICIOS*********************************
			else if(estado.equals("eliminarServicio"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("empezarContinuarServicio"))
			{
				int indice=(Utilidades.convertirAEntero(forma.getMapaServicios().get("numeroFilasMapaServicios")+"")-1);
				int servicio=Utilidades.convertirAEntero(forma.getMapaServicios().get("codigoServicio_"+indice)+"");
				int grupoServicio=Utilidades.obtenerGrupoServicio(con,servicio);
				int esquemaTarifario=obtenerEsquemaTarifario(grupoServicio+"", forma, true);
				int tipoTarifario=Utilidades.obtenertipoTarifarioEsquema(con,esquemaTarifario);
				
				InfoTarifaVigente infoTarifaVigentecargo= Cargos.obtenerTarifaBaseServicio(con, tipoTarifario, servicio, esquemaTarifario, "" /*fechaVigencia*/);
				boolean error=false;
				
				if(infoTarifaVigentecargo.getTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatGrupo || infoTarifaVigentecargo.getTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUvr)
				{
					forma.getMapaServicios().put("fueEliminadoServicio_"+indice, "true");
					forma.getMapaServicios().put("error_"+indice,"La tarifa actual del servicio tiene tipo de liquidación grupo o uvr, por tal motivo no puede ser seleccionado.");
					error=true;
				}
				else
				{
					if(infoTarifaVigentecargo.isLiquidarAsocios())
					{
						forma.getMapaServicios().put("fueEliminadoServicio_"+indice, "true");
						forma.getMapaServicios().put("error_"+indice,"La tarifa del servicio es Base de Asocios, por tal motivo no puede ser seleccionado.");
						error=true;
					}
					else if(!infoTarifaVigentecargo.isExiste())
					{
						forma.getMapaServicios().put("fueEliminadoServicio_"+indice, "true");
						forma.getMapaServicios().put("error_"+indice,"No existe tarifa válida para el servicio, por tal motivo no puede ser seleccionado.");
						error=true;
					}
				}	
				if(!error)
				{	
					forma.getMapaServicios().put("valor_unitario_"+indice,infoTarifaVigentecargo.getValorTarifa());
				}	
				forma.getMapaServicios().put("esquematarifario_"+indice,esquemaTarifario+"");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaPrincipal");
			}
			  else if(estado.equals("empezarContinuarIntervencion"))
				{
				  	if(Utilidades.convertirAEntero(forma.getMapaServiciosIntervencion().get("numeroFilasMapaServicios")+"")>0)
				  	{
				  		String servicios="'-1'";
				  		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaServiciosIntervencion().get("numeroFilasMapaServicios")+"");i++)
				  		{
				  			servicios=servicios+",'"+forma.getMapaServiciosIntervencion().get("codigoServicio_"+i)+"'";
				  		}
				  		forma.setPaquetesValidos(PresupuestoPaciente.obtenerPaquetesValidos(con,forma.getConvenio(),servicios));
				  		/*PresupuestoPaciente mundoPresupuesto=new PresupuestoPaciente();
				  		forma.setMapaServicios(mundoPresupuesto.cargarServiciosPaquetes(con, forma.getPaquete()));
						forma.setMapaArticulos(mundoPresupuesto.cargarArticulosPaquetes(con, forma.getPaquete()));*/
				  	}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
			  else if(estado.equals("empezarContinuarArticulo"))
				{
				  Cargos cargo=new Cargos();
					int indice=(forma.getNumeroFilasArticulos()-1);
					int articulo=Utilidades.convertirAEntero(forma.getMapaArticulos().get("codigoArticulo_"+indice)+"");
					Articulo art=new Articulo();
					art.cargarArticulo(con, articulo);
					int esquemaTarifario=obtenerEsquemaTarifario(art.getClase(), forma, false);
					int tipoTarifario=Utilidades.obtenertipoTarifarioEsquema(con,esquemaTarifario);
					double valorUnitario=cargo.obtenerTarifaBaseArticulo(con, articulo, esquemaTarifario, "" /*fechaVigencia*/);
					
					if(valorUnitario<0)
					{	
						forma.getMapaArticulos().put("error_"+indice, "No existe tarifa válida para el artículo, por tal motivo no puede ser seleccionado.");
						forma.getMapaArticulos().put("fueEliminadoArticulo_"+indice, "true");
					}	
					else
						forma.getMapaArticulos().put("valorUnitarioResultados_"+indice,valorUnitario);
					
					forma.getMapaArticulos().put("esquematarifario_"+indice,esquemaTarifario+"");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
			  else if(estado.equals("imprimir"))
				{
					return this.accionImprimir(mapping, request, con, formaConsulta, usuario, forma);
				}
			//*****************************************************************
				//***************ESTADOS PARA EL FILTRO DE CAMPOS***************************
			  else if (estado.equals("filtroCiudadExpedicion"))
			  {
				  return accionFiltroCiudad(con,forma,mapping);
			  }
			  else if (estado.equals("filtroCiudadNacimiento"))
			  {
				  return accionFiltroCiudad(con, forma, mapping);
			  }
			  else if (estado.equals("filtroCiudadResidencia"))
			  {
				  return accionFiltroCiudad(con, forma, mapping);
			  }
			//*********************************************************************************+
			  else if (estado.equals("cargarContrato"))
			  {
		  			forma.setEsquemasInventario(Contrato.obtenerEsquemasTarifariosInventariosVigentesFecha(con, forma.getContrato()+"", UtilidadFecha.getFechaActual(con)));
		  			forma.setEsquemasProcedimientos(Contrato.obtenerEsquemasTarifariosProcedimientosVigentesFecha(con, forma.getContrato()+"", UtilidadFecha.getFechaActual(con)));
					UtilidadBD.closeConnection(con);
				    return mapping.findForward("paginaPrincipal");
				}
			  else if(estado.equals("continuar"))
				{
				  if(Utilidades.convertirAEntero(forma.getMapaServiciosIntervencion().get("numeroFilasMapaServicios")+"")>0)
				  	{
				  		String servicios="'-1'";
				  		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaServiciosIntervencion().get("numeroFilasMapaServicios")+"");i++)
				  		{
				  			servicios=servicios+",'"+forma.getMapaServiciosIntervencion().get("codigoServicio_"+i)+"'";
				  		}
				  		forma.setPaquetesValidos(PresupuestoPaciente.obtenerPaquetesValidos(con,forma.getConvenio(),servicios));
				  		
				  	}
			  		forma.setContratosMap(mundo.consultarContratos(con, forma.getConvenio()));
			  		if(Utilidades.convertirAEntero(forma.getContratosMap().get("numRegistros")+"")==1)
			  		{
			  			forma.setEsquemasInventario(Contrato.obtenerEsquemasTarifariosInventariosVigentesFecha(con, forma.getContratosMap().get("codigo_0")+"", UtilidadFecha.getFechaActual(con)));
			  			forma.setEsquemasProcedimientos(Contrato.obtenerEsquemasTarifariosProcedimientosVigentesFecha(con, forma.getContratosMap().get("codigo_0")+"", UtilidadFecha.getFechaActual(con)));
			  		}
					UtilidadBD.closeConnection(con);
				    return mapping.findForward("paginaPrincipal");
				}
			  else if(estado.equals("cargarPaquetes"))
				{
				  
				  	this.accionCargarPaquetes(con,forma,mundo);
					UtilidadBD.closeConnection(con);
				    return mapping.findForward("paginaPrincipal");
				}
				//***************************************************************************
			  else if(estado.equals("actualizaIntervenciones"))
				{
					return accionActualizarItervenciones(con,forma,mundo,mapping);
				}
			  else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
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
	 * 
	 * @param grupoClase
	 * @param forma
	 * @param esServicio
	 * @return
	 */
	private int obtenerEsquemaTarifario(String grupoClase, PresupuestoPacienteForm forma, boolean esServicio) 
	{
		int esquemaTarifario=ConstantesBD.codigoNuncaValido;
		if(esServicio)
		{
			for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("numRegistros")+"");i++)
			{
				if(Utilidades.convertirAEntero(grupoClase)==Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("gruposervicio_"+i)+""))
				{
					return Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("esquematarifario_"+i)+"");
				}
				else if(UtilidadTexto.isEmpty(forma.getEsquemasProcedimientos().get("gruposervicio_"+i)+""))//esquema generico
				{
					esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasProcedimientos().get("esquematarifario_"+i)+"");
				}
					
			}
		}
		else
		{
			for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasInventario().get("numRegistros")+"");i++)
			{
				if(Utilidades.convertirAEntero(grupoClase)==Utilidades.convertirAEntero(forma.getEsquemasInventario().get("claseinventario_"+i)+""))
				{
					return Utilidades.convertirAEntero(forma.getEsquemasInventario().get("esquematarifario_"+i)+"");
				}
				else if(UtilidadTexto.isEmpty(forma.getEsquemasInventario().get("claseinventario_"+i)+""))//esquema generico
				{
					esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasInventario().get("esquematarifario_"+i)+"");
				}
			}
		}
		return esquemaTarifario;
	}
 	

	private void accionCargarPaquetes(Connection con, PresupuestoPacienteForm forma, PresupuestoPaciente mundo) 
	{
		for(int i=0;i<Utilidades.convertirAEntero(forma.getNumeroFilasServicios()+"");i++)
		  {
			  if(forma.getMapaServicios().containsKey("esPaquete_"+i)&&UtilidadTexto.getBoolean(forma.getMapaServicios().get("esPaquete_"+i)+""))
			  {
				  forma.setMapaServicios("fueEliminadoServicio_"+i,"true");
			  }
		  }
		  for(int i=0;i<Utilidades.convertirAEntero(forma.getNumeroFilasArticulos()+"");i++)
		  {
			  if(forma.getMapaArticulos().containsKey("esPaquete_"+i)&&UtilidadTexto.getBoolean(forma.getMapaArticulos().get("esPaquete_"+i)+""))
			  {
				  forma.setMapaArticulos("fueEliminadoArticulo_"+i,"true");
			  }
		  }
		  		
		  		PresupuestoPaciente mundoPresupuesto=new PresupuestoPaciente();
		  		/*HashMap mapaServTemporales=mundoPresupuesto.cargarServiciosPaquetes(con, forma.getPaquete(), forma.getConvenio(), forma.getContrato());
		  		int pos=Integer.parseInt(forma.getMapaServicios().get("numeroFilasMapaServicios")+"");
		  		for(int i=0;i<Utilidades.convertirAEntero(mapaServTemporales.get("numRegistros")+"");i++)
		  		{
		  			forma.setMapaServicios("codigoTipoCirugia_"+pos, mapaServTemporales.get("codigoTipoCirugia_"+i));
		  			forma.setMapaServicios("fueEliminadoServicio_"+pos, mapaServTemporales.get("fueEliminadoServicio_"+i));
		  			forma.setMapaServicios("valorTotalServicio_"+pos, mapaServTemporales.get("valorTotalServicio_"+i));
		  			forma.setMapaServicios("observaciones_"+pos, mapaServTemporales.get("observaciones_"+i));
		  			forma.setMapaServicios("codigoCups_"+pos, mapaServTemporales.get("codigoCups_"+i));
		  			forma.setMapaServicios("codigoServicio_"+pos, mapaServTemporales.get("codigoServicio_"+i));
		  			forma.setMapaServicios("codigoAyudante_"+pos, mapaServTemporales.get("codigoAyudante_"+i));
		  			forma.setMapaServicios("cantidadSer_"+pos, mapaServTemporales.get("cantidadSer_"+i));
		  			forma.setMapaServicios("codigoCirujano_"+pos, mapaServTemporales.get("codigoCirujano_"+i));
		  			forma.setMapaServicios("numeroServicio_"+pos, mapaServTemporales.get("numeroServicio_"+i));
		  			forma.setMapaServicios("valor_unitario_"+pos, mapaServTemporales.get("valor_unitario_"+i));
		  			forma.setMapaServicios("esNivelServicioContratado_"+pos, mapaServTemporales.get("esNivelServicioContratado_"+i));
		  			forma.setMapaServicios("descripcionEspecialidad_"+pos, mapaServTemporales.get("descripcionEspecialidad_"+i));
		  			forma.setMapaServicios("codigoEspecialidad_"+pos, mapaServTemporales.get("codigoEspecialidad_"+i));
		  			forma.setMapaServicios("descripcionServicio_"+pos, mapaServTemporales.get("descripcionServicio_"+i));
		  			forma.setMapaServicios("esPos_"+pos, mapaServTemporales.get("esPos_"+i));
		  			forma.setMapaServicios("esPaquete_"+pos, ConstantesBD.acronimoSi);
		  			pos++;
		  		}
		  		forma.setMapaServicios("numRegistros",pos+"");*/
		  		forma.setMapaServicios(mundoPresupuesto.cargarServiciosPaquetes(con, forma.getPaquete(),forma.getConvenio(),forma.getContrato()));
				forma.setMapaArticulos(mundoPresupuesto.cargarArticulosPaquetes(con, forma.getPaquete()));
		  		forma.getMapaServicios().put("numeroFilasMapaServicios",forma.getMapaServicios().get("numRegistros"));
				forma.setNumeroFilasServicios(Integer.parseInt(forma.getMapaServicios().get("numeroFilasMapaServicios")+""));
				forma.setNumeroFilasArticulos(Integer.parseInt(forma.getMapaArticulos().get("numRegistros")+""));
				/*int posart=forma.getNumeroFilasArticulos();
				HashMap mapaArtTemporales=mundoPresupuesto.cargarArticulosPaquetes(con, forma.getPaquete());
				for(int j=0;j<Utilidades.convertirAEntero(mapaArtTemporales.get("numRegistros")+"");j++)
				{
					forma.setMapaArticulos("valorTotalArticulo_"+posart,mapaArtTemporales.get("valorTotalArticulo_"+j));
					forma.setMapaArticulos("codigoArticulo_"+posart,mapaArtTemporales.get("codigoArticulo_"+j));
					forma.setMapaArticulos("valorUnitarioResultados_"+posart,mapaArtTemporales.get("valorUnitarioResultados_"+j));
					forma.setMapaArticulos("cantidad_"+posart,mapaArtTemporales.get("cantidad_"+j));
					forma.setMapaArticulos("descripcionArticulo_"+posart,mapaArtTemporales.get("descripcionArticulo_"+j));
					forma.setMapaArticulos("esPaquete_"+posart, ConstantesBD.acronimoSi);
					posart++;
				}
				forma.setMapaArticulos("numRegistros",posart+"");*/
				
	}



	/**
	 * Método implementado para realizar las validaciones iniciales al entrar a la funcionalidad de presupuesto
	 * @param con
	 * @param mapping
	 * @param request
	 * @param usuario 
	 * @param forma 
	 * @return
	 */
	private ActionForward accionValidacionesIniciales(Connection con, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PresupuestoPacienteForm forma) 
	{
		String consecutivoPresupuesto=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoPresupuesto, usuario.getCodigoInstitucionInt());
		String ocuMedicoEspecialista=ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(usuario.getCodigoInstitucionInt(), true);
		String ocuMedicoGeneral=ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(usuario.getCodigoInstitucionInt(), true);
		String[] ciudad =  ValoresPorDefecto.getCiudadNacimiento(usuario.getCodigoInstitucionInt()).split("-");
		Collection futurosErrores=new ArrayList();
		
		if(consecutivoPresupuesto.equals("-1"))
		{
			ElementoApResource elem=new ElementoApResource("error.manejoPaciente.consecutivoPresupuestoNoDisponible");
			futurosErrores.add(elem);
		}
		if(!UtilidadCadena.noEsVacio(ocuMedicoEspecialista))
		{
			ElementoApResource elem=new ElementoApResource("error.manejoPaciente.faltaParOcupacionMedicoEspecialista");
			futurosErrores.add(elem);
		}
		if(!UtilidadCadena.noEsVacio(ocuMedicoGeneral))
		{
			ElementoApResource elem=new ElementoApResource("error.manejoPaciente.faltaParOcupacionMedicoGeneral");
			futurosErrores.add(elem);
		}
		if(!UtilidadCadena.noEsVacio(ciudad[0])||!UtilidadCadena.noEsVacio(ciudad[1]))
		{
			ElementoApResource elem=new ElementoApResource("error.manejoPaciente.faltaParCiudadNacCiudadRes");
			futurosErrores.add(elem);
		}
		if (!futurosErrores.isEmpty())
		{
			return ComunAction.envioMultiplesErrores(mapping, request, con, "label.manejoPaciente.generacionPresupuesto", futurosErrores, logger);
		}
		else
		{
			try 
			{
				return accionEmpezar(forma, mapping,request, con);
			} 
			catch (SQLException e) 
			{
				logger.error("Error en accinValidacionesGenerales: "+e);
				return null;
			}
		}
	}



	/**
	 * Método que realiza el filtro de las ciudades de expedicion por pais
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionFiltroCiudad(Connection con, PresupuestoPacienteForm forma, ActionMapping mapping) 
	{
		if(forma.getEstado().equals("filtroCiudadExpedicion"))
		{
			forma.setNomCiudadId(Utilidades.obtenerCiudadesXPais(con, forma.getPais()));
		}
		else if (forma.getEstado().equals("filtroCiudadNacimiento"))
		{
			forma.setNomCiudadNac(Utilidades.obtenerCiudadesXPais(con, forma.getPaisNac()));
		}
		else if (forma.getEstado().equals("filtroCiudadResidencia"))
		{
			forma.setNomCiudadResiden(Utilidades.obtenerCiudadesXPais(con, forma.getPaisResiden()));
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método que redirecciona a la página de ingreso tipo y número de Id
	 * @param forma Forma para resetear
	 * @param mapping Mapping para Redirrecion
	 * @param con Conexión con la BD
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(PresupuestoPacienteForm forma, ActionMapping mapping, HttpServletRequest request, Connection con) throws SQLException
	{
		UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
		forma.reset(usuario.getCodigoInstitucionInt());
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("ingresoId");
	}
	
	/**
	 * Método para rediccionar a la página de ingreso del presupuesto del paciente
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param con Conexión con la BD
	 * @param usuario.getCodigoInstitucionInt() Codigo de la institución del usuario
	 * @return ActionForward hacia la página de ingreso
	 */
	private ActionForward accionIngresarPresupuesto(PresupuestoPacienteForm forma, ActionMapping mapping, HttpServletRequest request, Connection con, UsuarioBasico usuario)
	{
		PresupuestoPaciente mundoPresupuesto=new PresupuestoPaciente();
		
		//Consulta los convenios activos y que su contrato está vigente a la fecha actual
		forma.setListadoConveniosVigentes(mundoPresupuesto.consultarConveniosVigentes(con, true));
		forma.setListadoConveniosContratos(mundoPresupuesto.consultarConveniosVigentes(con, false));
		
		Paciente paciente=new Paciente();
		
		String tipoBD = System.getProperty("TIPOBD");
		boolean existe = false;
		
		try
		{
			
			forma.setNomPais(Utilidades.obtenerPaises(con));
			
			int codigoPaciente = UtilidadValidacion.getCodigoPersona(con, forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion());
			
			
			existe=UtilidadValidacion.existeUsuario(con,tipoBD,forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion());
			
			forma.setNomPais(Utilidades.obtenerPaises(con));
			
			if(UtilidadValidacion.existePacientes(con,tipoBD,forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion()))
			{
				codigoPaciente = UtilidadValidacion.getCodigoPersona(con, forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion());

				
				paciente.cargarPaciente(con, codigoPaciente);
				
				forma.setPrimerNombre(paciente.getPrimerNombrePersona(false));
				forma.setSegundoNombre(paciente.getSegundoNombrePersona(false));
				forma.setPrimerApellido(paciente.getPrimerApellidoPersona(false));
				forma.setSegundoApellido(paciente.getSegundoApellidoPersona(false));
				forma.setFechaNacimiento(paciente.getDiaNacimiento()+"/"+paciente.getMesNacimiento()+"/"+paciente.getAnioNacimiento());
				forma.setDireccion(paciente.getDireccion(false));
				forma.setTelefono(paciente.getTelefono());
				forma.setSexo(Integer.parseInt(paciente.getCodigoSexo()));
				forma.setExistePersonaEnSistema(true);
				
				
				//**************************CAPTURO DATOS x CAMBIOS FUNCIONALIDADES X VENEZUELA ***************
				
				forma.setPais(paciente.getCodigoPaisId());
				forma.setPaisNac(paciente.getCodigoPaisIdentificacion());
				forma.setPaisResiden(paciente.getCodigoPais());
				forma.setPaisResidenAnterior(paciente.getCodigoPais());
				
				
				forma.setNomCiudadId(Utilidades.obtenerCiudadesXPais(con, paciente.getCodigoPaisId()));
				forma.setNomCiudadNac(Utilidades.obtenerCiudadesXPais(con, paciente.getCodigoPaisIdentificacion()));
				forma.setNomCiudadResiden(Utilidades.obtenerCiudadesXPais(con, paciente.getCodigoPais()));
				
				
				forma.setCodigoCiudadId(paciente.getCodigoDepartamentoId()+ConstantesBD.separadorSplit+paciente.getCodigoCiudadId());
				forma.setCiudadNac(paciente.getCodigoDepartamentoIdentificacion()+ConstantesBD.separadorSplit+paciente.getCodigoCiudadIdentificacion());
				forma.setCiudadresiden(paciente.getCodigoDepartamento()+ConstantesBD.separadorSplit+paciente.getCodigoCiudad());
				forma.setCiudadResidenAnterior(paciente.getCodigoDepartamento()+ConstantesBD.separadorSplit+paciente.getCodigoCiudad());
				
				//*********************************************************************************************
				
				forma.setCodigoPersona(paciente.getCodigoPersona());
				//String[] datosCiudad = Utilidades.getCodigoPaisDeptoCiudadPersona(con, forma.getTipoIdentificacion().split("-")[1], forma.getNumeroIdentificacion()).split(ConstantesBD.separadorSplit);
				//forma.setCodigoCiudadId( datosCiudad[1]+ConstantesBD.separadorSplit+datosCiudad[2] );
				forma.setGrupoPoblacional(paciente.getCodigoGrupoPoblacional());
				/*Cuenta i=new Cuenta();
				i.cargarCuenta(con, persona.getCodigoCuenta()+"");
				forma.getMapaPeticionEncabezado().put("tipoPaciente", i.getCodigoTipoPaciente());*/
			}
			
			else if(existe)
			{
				this.validacionUsuarioPaciente(con,forma);
			}
				
			
			else
			{
				String[] paisNacVec=ValoresPorDefecto.getPaisNacimiento(usuario.getCodigoInstitucionInt()).split("-");
				String[] paisRecVec=ValoresPorDefecto.getPaisResidencia(usuario.getCodigoInstitucionInt()).split("-");
				String[] ciudadNacVec=ValoresPorDefecto.getCiudadNacimiento(usuario.getCodigoInstitucionInt()).split("-");
				String[] ciudadRecVec=ValoresPorDefecto.getCiudadVivienda(usuario.getCodigoInstitucionInt()).split("-");
				forma.setPaisNac(paisNacVec[0]);
				forma.setPaisResiden(paisRecVec[0]);
				forma.setCiudadNac(ciudadNacVec[0]+ConstantesBD.separadorSplit+ciudadNacVec[1]);
				forma.setCiudadresiden(ciudadRecVec[0]+ConstantesBD.separadorSplit+ciudadRecVec[1]);
				forma.setDireccion(ValoresPorDefecto.getDireccionPaciente(usuario.getCodigoInstitucionInt()));
				forma.setExistePersonaEnSistema(false);
				
				forma.setNomCiudadNac(Utilidades.obtenerCiudadesXPais(con, forma.getPaisNac()));
				forma.setNomCiudadResiden(Utilidades.obtenerCiudadesXPais(con, forma.getPaisResiden()));
				
				//si no existe persona en el sistema se valida el consecutivo de historia clínica
				ActionErrors errores = new ActionErrors();
				
				//*********************VALIDACION DEL CONSECUTIVO DE HISTORIA CLINICA************************************************************************
				String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt());
				
				if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
					errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","la historia clínica"));
				else
				{
					try
					{
						//se verifica que sea un campo numérico
						Integer.parseInt(consecutivo);
					}
					catch(Exception e)
					{
						logger.error("Error en validacionConsecutivoDisponible:  "+e);
						errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de la historia clínica"));
					}
				}
				
				if(!errores.isEmpty())
				{
					UtilidadBD.closeConnection(con);
					saveErrors(request, errores);
					return mapping.findForward("paginaErroresActionErrors");
				}
				//***************************************************************************************************************
			}
		}
		catch (SQLException e)
		{
			logger.error("Error cargando la persona : "+e);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void validacionUsuarioPaciente(Connection con, PresupuestoPacienteForm forma) 
	{
		String tipoBD = System.getProperty("TIPOBD");
		boolean existeUsuario = false;
		try 
		{
			existeUsuario = UtilidadValidacion.existeUsuario(con,tipoBD,forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion());
		} catch (SQLException e) 
		{
			logger.error("Error al verificar si existe usuario para ese paciente: "+e);
		}
		
		if(existeUsuario)
		{
			//se consultan datos de la persona
			HashMap datosPersona = Utilidades.obtenerDatosPersona(
							con,
							forma.getTipoIdentificacion().split("-")[0],
							forma.getNumeroIdentificacion()
							);
			//Se llena la forma con los datos de la persona
			forma.setCiudadNac(datosPersona.get("codigo_depto_nacimiento_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_nacimiento_0").toString());
			forma.setPaisNac(datosPersona.get("codigo_pais_nacimiento_0").toString());
			forma.setFechaNacimiento(datosPersona.get("fecha_nacimiento_0").toString());
			//forma.setEstadoCivil(datosPersona.get("codigo_estado_civil_0").toString());
			forma.setSexo(Utilidades.convertirAEntero(datosPersona.get("codigo_sexo_0").toString()));
			forma.setPrimerNombre(datosPersona.get("primer_nombre_0").toString());
			forma.setSegundoNombre(datosPersona.get("segundo_nombre_0").toString());
			forma.setPrimerApellido(datosPersona.get("primer_apellido_0").toString());
			forma.setSegundoApellido(datosPersona.get("segundo_apellido_0").toString());
			forma.setDireccion(datosPersona.get("direccion_0").toString());
			
			forma.setNomCiudadId(Utilidades.obtenerCiudadesXPais(con, datosPersona.get("codigo_pais_id_0").toString()));
			forma.setNomCiudadNac(Utilidades.obtenerCiudadesXPais(con, datosPersona.get("codigo_pais_nacimiento_0").toString()));
			forma.setNomCiudadResiden(Utilidades.obtenerCiudadesXPais(con, datosPersona.get("codigo_pais_vivienda_0").toString()));
			
			
			forma.setPaisResiden(datosPersona.get("codigo_pais_vivienda_0").toString());
			forma.setCiudadresiden(datosPersona.get("codigo_depto_vivienda_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_vivienda_0").toString());
			forma.setTelefono(datosPersona.get("telefono_0").toString());
			forma.setPais(datosPersona.get("codigo_pais_id_0").toString());
			forma.setCodigoCiudadId(datosPersona.get("codigo_depto_id_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_id_0").toString());
			forma.setUsuarioPaciente(true);
			
			//Se consulta el login del usuario
			Usuario mundoUsuario = new Usuario();
			mundoUsuario.init(tipoBD);
			forma.setLoginUsuarioPaciente(mundoUsuario.buscarLogin(con, forma.getTipoIdentificacion().split("-")[0], forma.getNumeroIdentificacion(), ""));
			
		}
		else
			forma.setUsuarioPaciente(false);
		
	}
	
	
	
	/**
	 * Método para insertar los datos en la BD
	 * @param con Conexión con la BD
	 * @param forma Forma
	 * @param mapping Mapping para redireccionar
	 * @param usuario Usuario del sistema
	 * @param request 
	 * @param pacienteSession 
	 * @return ActionMapping con la Página de resumen
	 */
	private ActionForward accionGuardar(Connection con, PresupuestoPacienteForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		PresupuestoPaciente presupuesto=new PresupuestoPaciente();
		int consecutivoPresupuesto=0;
		
		
		//-Pasar los datos del form al mundo
		llenarMundo(forma, presupuesto);
		
		int codigoPersona=forma.getCodigoPersona();
		if(!forma.isExistePersonaEnSistema())
		{
			try
			{ 
				
				/**Cargamos los formato de impresion existentes**/
				FormatoImpresionPresupuesto mundoAux = new FormatoImpresionPresupuesto();
				forma.setMapaFormatosImpresion(mundoAux.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
				//String[] cuidadNacimiento=ValoresPorDefecto.getCiudadNacimiento(usuario.getCodigoInstitucionInt()).split("-");
				Paciente paciente=new Paciente();
				
				//Se toma el consecutivo de historia clínica
				String consecutivoHC=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt());
				String anioConsecutivoHC=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivoHC);

				paciente.setNumeroHistoriaClinica(consecutivoHC);
				paciente.setAnioHistoriaClinica(anioConsecutivoHC);
				
				
				
				paciente.setNumeroIdentificacion(forma.getNumeroIdentificacion());
				paciente.setCodigoTipoIdentificacion(forma.getTipoIdentificacion().split("-")[0]);
				
				paciente.setPrimerNombrePersona(forma.getPrimerNombre());
				paciente.setSegundoNombrePersona(forma.getSegundoNombre());
				paciente.setPrimerApellidoPersona(forma.getPrimerApellido());
				paciente.setSegundoApellidoPersona(forma.getSegundoApellido());
				
				
				paciente.setDireccion(forma.getDireccion());
				
				String codDepto = forma.getCodigoCiudadId().split(ConstantesBD.separadorSplit)[0];
				String codCiudad = forma.getCodigoCiudadId().split(ConstantesBD.separadorSplit)[1];		
				paciente.setCodigoDepartamentoId(codDepto);
				paciente.setCodigoCiudadId(codCiudad);
				paciente.setCodigoPaisId(forma.getPais());
				
				String codDeptoNac = forma.getCiudadNac().split(ConstantesBD.separadorSplit)[0];
				String codCiudadNac = forma.getCiudadNac().split(ConstantesBD.separadorSplit)[1];
				paciente.setCodigoDepartamentoIdentificacion(codDeptoNac);
				paciente.setCodigoCiudadIdentificacion(codCiudadNac);
				paciente.setCodigoPaisIdentificacion(forma.getPaisNac());
				
				String codDeptoRes = forma.getCiudadresiden().split(ConstantesBD.separadorSplit)[0];
				String codCiudadRes = forma.getCiudadresiden().split(ConstantesBD.separadorSplit)[1];
				paciente.setCodigoDepartamento(codDeptoRes);
				paciente.setCodigoCiudad(codCiudadRes);
				paciente.setCodigoPais(forma.getPaisResiden());
				
				
				//se guarda sin barrio y sin localidad
			    paciente.setCodigoBarrio(""); 
			    paciente.setCodigoLocalidad("");
				
				paciente.setCodigoZonaDomicilio(ValoresPorDefecto.getZonaDomicilio(usuario.getCodigoInstitucionInt()));
				
				paciente.setTelefono(forma.getTelefono());
				
				
				String[] fechas=forma.getFechaNacimiento().split("/");
				paciente.setDiaNacimiento(fechas[0]);
				paciente.setMesNacimiento(fechas[1]);
				paciente.setAnioNacimiento(fechas[2]);
				
				paciente.setCodigoTipoPersona("1");
				paciente.setCodigoEstadoCivil("D");
				paciente.setCodigoSexo(forma.getSexo()+"");
				
				paciente.setCodigoOcupacion(ValoresPorDefecto.getOcupacion(usuario.getCodigoInstitucionInt()));
				paciente.setCodigoTipoSangre("9");
				paciente.setCodigoGrupoPoblacional(forma.getGrupoPoblacional());
				
				UtilidadBD.iniciarTransaccion(con);
				RespuestaInsercionPersona resp=paciente.insertarPaciente(con, usuario.getCodigoInstitucionInt());
				
				if(!resp.isSalioBien())
				{
					logger.error("Error ingresando el paciente ");
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivoHC, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);

					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else
				{					
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivoHC, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					UtilidadBD.finalizarTransaccion(con);
					forma.getResultadoProceso().put("procesoExitoso", ConstantesBD.acronimoSi);
					forma.getResultadoProceso().put("mensaje", "El proceso fue Realizado con Exito");
					codigoPersona=resp.getCodigoPersona();
					UtilidadValidacion.validacionPermisosInstitucionPaciente(con, resp.getCodigoPersona(), usuario.getCodigoInstitucion());
					
					
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				logger.error("Error ingresando el paciente : "+e);
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
		}
		else
		{
			try
			{
				
				Paciente paciente=new Paciente();
				paciente.cargarPaciente(con, codigoPersona);
				/**Cargamos los formato de impresion existentes**/
				FormatoImpresionPresupuesto mundoAux = new FormatoImpresionPresupuesto();
				forma.setMapaFormatosImpresion(mundoAux.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
				paciente.setPrimerNombrePersona(forma.getPrimerNombre());
				paciente.setSegundoNombrePersona(forma.getSegundoNombre());
				paciente.setPrimerApellidoPersona(forma.getPrimerApellido());
				paciente.setSegundoApellidoPersona(forma.getSegundoApellido());
				
				String[] fechas=forma.getFechaNacimiento().split("/");
				paciente.setDiaNacimiento(fechas[0]);
				paciente.setMesNacimiento(fechas[1]);
				paciente.setAnioNacimiento(fechas[2]);

				paciente.setCodigoSexo(forma.getSexo()+"");

				paciente.setDireccion(forma.getDireccion());
				paciente.setTelefono(forma.getTelefono());
				
				String codDepto = forma.getCodigoCiudadId().split(ConstantesBD.separadorSplit)[0];
				String codCiudad = forma.getCodigoCiudadId().split(ConstantesBD.separadorSplit)[1];		
				paciente.setCodigoDepartamentoId(codDepto);
				paciente.setCodigoCiudadId(codCiudad);
				paciente.setCodigoPaisId(forma.getPais());
				
				String codDeptoNac = forma.getCiudadNac().split(ConstantesBD.separadorSplit)[0];
				String codCiudadNac = forma.getCiudadNac().split(ConstantesBD.separadorSplit)[1];
				paciente.setCodigoDepartamentoIdentificacion(codDeptoNac);
				paciente.setCodigoCiudadIdentificacion(codCiudadNac);
				paciente.setCodigoPaisIdentificacion(forma.getPaisNac());
				
				String codDeptoRes = forma.getCiudadresiden().split(ConstantesBD.separadorSplit)[0];
				String codCiudadRes = forma.getCiudadresiden().split(ConstantesBD.separadorSplit)[1];
				paciente.setCodigoDepartamento(codDeptoRes);
				paciente.setCodigoCiudad(codCiudadRes);
				paciente.setCodigoPais(forma.getPaisResiden());
				
				//Si se modificó el pais y/o ciudad de residencia entonces se debe quitar el barrio y la localidad que antes existian
				if(!forma.getPaisResiden().equals(forma.getPaisResidenAnterior())||!forma.getCiudadresiden().equals(forma.getCiudadResidenAnterior()))
				{
					paciente.setCodigoBarrio("");
					paciente.setCodigoLocalidad("");
				}
				
				paciente.setCodigoGrupoPoblacional(forma.getGrupoPoblacional());

				Paciente pacienteAnterior = new Paciente();
				pacienteAnterior.cargarPaciente(con, codigoPersona);
				
				int respuestaModificacion=paciente.modificarPaciente(con, usuario,pacienteAnterior);
				if(respuestaModificacion<1)
				{
					logger.error("Error modificando el paciente ");
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				logger.error("Error modificando el paciente : "+e);
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
		}
		
		//-- Inserciòn de la información básica del presupuesto
		try
		{
				consecutivoPresupuesto=presupuesto.insertarPresupuesto(con, codigoPersona, usuario);
			
				//--Insertar Los Servicios de Intervencion
				presupuesto.insertarServiciosIntervencion(con, consecutivoPresupuesto);
	
				//--Insertar Los Materiales de la Cotizacion
				presupuesto.insertarMateriales(con, consecutivoPresupuesto);
	
				//--Insertar Los Servicios 
				presupuesto.insertarServicios(con, consecutivoPresupuesto);
		}
		catch(SQLException e)
		{
			logger.error("Error en la informaciòn básica del presupuesto : "+e);
		}
		
			presupuesto.cargarInfoPresupuesto(con, consecutivoPresupuesto);
			
			//Se llena la forma con la información consultada
			llenarForm (forma, presupuesto);
			forma.getResultadoProceso().put("procesoExitoso", ConstantesBD.acronimoSi);
			forma.getResultadoProceso().put("mensaje", "El proceso fue Realizado con Exito");
			forma.setEstado("resumen");
			//return this.accionCargarParaModificarPeticion(forma, mapping, con, resultado[1], false, usuario);
			try
			{
				UtilidadBD.cerrarConexion(con);
			}
			catch (SQLException e)
			{
				logger.error("Error cerrando la conexión "+e);
				return null;
			}
			return mapping.findForward("paginaPrincipal");
		//}
	}
	
	/**
	 * @param forma
	 * @param presupuesto
	 */
	private void llenarMundo(PresupuestoPacienteForm forma, PresupuestoPaciente presupuesto)
	{
		presupuesto.setPaquete(forma.getPaquete());
		presupuesto.setContrato(forma.getContrato());
		presupuesto.setConvenio(forma.getConvenio());
		presupuesto.setMedicoTratante(forma.getMedicoTratante());
		presupuesto.setDiagnosticoIntervencion(forma.getDiagnosticoIntervencion());
		presupuesto.setCieDiagnostico(forma.getCieDiagnostico());
		
		
		//-Numero de los materiales 
		presupuesto.setNumeroFilasArticulos( forma.getNumeroFilasArticulos() );
		
		//-Mapa materiales		
		presupuesto.setMapaArticulos(forma.getMapaArticulos());

		//-Numero de los Servicios 
		presupuesto.setNumeroFilasServicios( forma.getNumeroFilasServicios() );
		
		//-Mapa Servicios Intervencion		
		presupuesto.setMapaServiciosIntervencion(forma.getMapaServiciosIntervencion());
		
		//-Mapa Servicios		
		presupuesto.setMapaServicios(forma.getMapaServicios());
		
	}	
	
	/**
	 * Método para cargar el form con los datos del mundo
	 * @param forma
	 * @param mundoPresupuesto
	 */
	private void llenarForm (PresupuestoPacienteForm forma, PresupuestoPaciente mundoPresupuesto)
	{
		forma.setNombrePaquete(mundoPresupuesto.getNombrePaquete());
		forma.setNumeroContrato(mundoPresupuesto.getNumeroContrato());
		forma.setNombreConvenio(mundoPresupuesto.getNombreConvenio());
		forma.setNombreMedicoTratante(mundoPresupuesto.getNombreMedicoTratante());
		forma.setNombreDiagnostico(mundoPresupuesto.getNombreDiagnosticoIntervencion());
	}
	
	/**
	 * Accion para imprimir el presupuesto que se consulto
	 * @param mapping
	 * @param request
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionImprimir(ActionMapping mapping,HttpServletRequest request, Connection con,ConsultaPresupuestoForm forma, UsuarioBasico usuario, PresupuestoPacienteForm formaTemp)throws Exception
	{
		String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		int consecutivoPresupuesto=Integer.parseInt(UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoPresupuesto, usuario.getCodigoInstitucionInt()));
		consecutivoPresupuesto=consecutivoPresupuesto-1;
		forma.setCodigoPresupuesto(consecutivoPresupuesto);
		forma.setCodigoFormato(forma.getCodigoFormato());
		PresupuestoPdf.pdfPresupuesto(ValoresPorDefecto.getFilePath()+ nombreArchivo, con, usuario, formaTemp.getCodigoFormato(), consecutivoPresupuesto,formaTemp.getIndex(),formaTemp.getTiposMonedaTagMap(), request);
		UtilidadBD.cerrarConexion(con);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Consulta Presupuesto");
		return mapping.findForward("abrirPdf");
	}
	
	private ActionForward accionActualizarItervenciones(Connection con,PresupuestoPacienteForm forma,PresupuestoPaciente mundo, ActionMapping mapping)
	{
		logger.info("\n\nMAPA DE SSERVICIOS INTERVENCION-->>"+forma.getMapaServiciosIntervencion());
		String[] indices={"codigoTipoCirugia_", "observaciones_", "codigoCups_", "codigoServicio_", "codigoAyudante_", "codigoCirujano_", "numeroServicio_","valor_unitario_", "esNivelServicioContratado_", "descripcionEspecialidad_", "codigoEspecialidad_", "descripcionServicio_", "esPos_", "fueEliminadoIntervencion_"};
		int numReg=Utilidades.convertirAEntero(forma.getMapaServiciosIntervencion().get("numeroFilasMapaServicios")+"");
		int cont=0;
		boolean esEliminado=false;
		String servicios="'-1'";
		for(int i=0;i<numReg;i++)
		{
			esEliminado=UtilidadTexto.getBoolean(forma.getMapaServiciosIntervencion().get("fueEliminadoIntervencion_"+i)+"");
			if(esEliminado)
			{
				cont=cont+1;
				int ultimaPosMapa=numReg-1;
				for(int a=i;a<numReg;a++)
				{
					for(int j=0;j<indices.length;j++)
					{
						forma.getMapaServiciosIntervencion().put(indices[j]+""+a,forma.getMapaServiciosIntervencion().get(indices[j]+""+(a+1)));
					}
				}
				
				//ahora eliminamos el ultimo registro del mapa.
				for(int j=0;j<indices.length;j++)
				{
					forma.getMapaServiciosIntervencion().remove(indices[j]+""+ultimaPosMapa);
				}
				
				//ahora actualizamo el numero de registros en el mapa.
				forma.getMapaServiciosIntervencion().put("numeroFilasMapaServicios",ultimaPosMapa+"");
			}
			
			//servicios=servicios.replace("null", "-1");
		}
		
		if(Utilidades.convertirAEntero(forma.getMapaServiciosIntervencion().get("numeroFilasMapaServicios")+"")==0)
		{
			servicios="'-1'";
			forma.setPaquetesValidos(new HashMap());
			forma.getPaquetesValidos().put("numRegistros", "0");
		}
		else
		{
			for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaServiciosIntervencion().get("numeroFilasMapaServicios")+"");i++)
			{
				servicios=servicios+",'"+forma.getMapaServiciosIntervencion().get("codigoServicio_"+i)+"'";
			}
			forma.setPaquetesValidos(PresupuestoPaciente.obtenerPaquetesValidos(con,forma.getConvenio(),servicios));
		}
		boolean existePaquete=false;
		for(int i=0;i<Utilidades.convertirAEntero(forma.getPaquetesValidos().get("numRegistros")+"");i++)
		{
			if((forma.getPaquetesValidos().get("codigo_paquete_"+i)+"").equals(forma.getPaquete()))
			{
				existePaquete=true;
				i=Utilidades.convertirAEntero(forma.getPaquetesValidos().get("numRegistros")+"");
				
			}
		}
		if(!existePaquete)
			forma.setPaquete("");
		this.accionCargarPaquetes(con, forma, mundo);
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión "+e);
			return null;
		}
		return mapping.findForward("paginaPrincipal");
	}

}
