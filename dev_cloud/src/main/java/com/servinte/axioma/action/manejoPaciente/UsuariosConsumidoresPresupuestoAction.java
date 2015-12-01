package com.servinte.axioma.action.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.manejoPaciente.PacientesPoliconsultadoresForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.actionForm.manejoPaciente.UsuariosConsumidoresPresupuestoForm;
import com.servinte.axioma.bl.facturacion.facade.FacturacionFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.manejoPaciente.EncabezadoRepUsuConDto;
import com.servinte.axioma.dto.manejoPaciente.InfoUsuariosConsumidoresArchivoPlanoDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresPresupuestoDto;
import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.manejoPaciente.usuariosConsumidoresPresupuesto.GeneradorReporteUsuariosConsumidoresPresupuesto;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.TiposContrato;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;


/**
 * Fecha Julio - 2012
 * @author David Ricardo Gómez M.
 *
 */

public class UsuariosConsumidoresPresupuestoAction extends DispatchAction 
{

	/**
	 * 
	 * Para manejar los logger de la clase UsuariosConsumidoresPresupuesto
	 */
	Logger logger = Logger.getLogger(UsuariosConsumidoresPresupuestoAction.class);
	

	public ActionForward empezar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		ActionMessages errores = new ActionMessages();
		try{			
			//optenemos el valor de la forma.	
			UsuariosConsumidoresPresupuestoForm forma = (UsuariosConsumidoresPresupuestoForm) form;
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			InstitucionBasica institucionActual = (InstitucionBasica) request
					.getSession().getAttribute("institucionBasica");
			forma.reset();
			forma.setNombreArchivoGenerado("");
			forma.setListaMeses(UtilidadFecha.obtenerListaDtoMes());
			List<String> listaa= new ArrayList<String>();
			int agnoActual=UtilidadFecha.getAnioActual();
			for (int i=2000; i<=(agnoActual + 2); i++)
			{
				listaa.add(Integer.toString(i));
			}
			forma.setListaAgnos(listaa);
						//valor por defecto de autorizaciones
			forma.setautorizaciones("1");
			//Cargamos el select con todos los convenios
			FacturacionFacade listaConvenios = new FacturacionFacade();
			forma.setListaConvenios(listaConvenios.listarConvenios());
			ArrayList<Convenios> j=forma.getListaConvenios();
			
			   //Cargamos el select con los tipos de identificación
			ManejoPacienteFacade manejoPacienteFachada = new ManejoPacienteFacade();
			ArrayList<TiposIdentificacion> idT= manejoPacienteFachada.listaTiposIdentificacion();
			ArrayList<TiposIdentificacion> idTi= new ArrayList<TiposIdentificacion>();
			for (TiposIdentificacion i: idT)
			{
				try {if (!i.getTipo().equals(null)|| !i.getTipo().equals(""))
				{
					idTi.add(i);
				}}
				catch(Exception e){}
			}
			forma.setListaIdentificacion(idTi);
			//Cargamos el select con todas las vias ingreso
			forma.setViasIngresos(manejoPacienteFachada.consultarViasIngreso());
			
					
			//Cargamos la lista de grupos de servicio y la clase de inventario
			FacturacionFacade gruposServicio= new FacturacionFacade();
		    forma.setListaGruposServicios(gruposServicio.gruposServicioTodo());
		    
   		    FacturacionFacade listaClaseInventario= new FacturacionFacade();
   		    ArrayList<ClaseInventario> listarClaseInventario=listaClaseInventario.listarClaseInventario();
   		    ArrayList<ClaseInventario> listarClaseInventario1= new ArrayList<ClaseInventario>();
   		    for (ClaseInventario list: listarClaseInventario)
   		    {
   		    	if (list.getCuentasContablesByCuentaInventario() != null)
   		    	{
   		    		listarClaseInventario1.add(list);
   		    	}
   		    }
   		    forma.setListaClaseInventario(listarClaseInventario1);
 			//forma.setListaClaseInventario();	
   		  
			
   		    forma.setEstado("generarReporte");
//   		    if(!forma.getValidado().equals(""))
//   		    {
//   		    	determinarValidacionCampo(mapping, forma, request, response);
//   		    }
  		    if (forma.getValidado().equals(""))
   		    {forma.setValidado("1");}
			forma.setEstado("generarReporte");
	
			
					
          }catch (ClassCastException e) {
			
			//Mostrar error al usuario
			errores.add("notSpecific", new ActionMessage("errors.notEspecific", "Form incorrecto"));
			saveErrors(request, errores);
			
		}catch (IPSException e) {
			
			//Mostrar error al usuario
			errores.add("notSpecific", new ActionMessage("errors.notEspecific", "Form incorrecto"));
			saveErrors(request, errores);
			
		}
		catch (Exception e) {
			
			//Mostrar error al usuario
			errores.add("notSpecific", new ActionMessage("errors.notEspecific", e.getMessage()));
			saveErrors(request, errores);
		}
		
		
		return mapping.findForward("empezar");
	}
	
	/**
	 * 
	 * Método que se encarga de determinar como se debe validar el campo de
	 * ingreso de número de identificación del paciente
	 * 
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 */
	public ActionForward determinarValidacionCampo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
				.getSession());
		InstitucionBasica institucionActual = (InstitucionBasica) request
				.getSession().getAttribute("institucionBasica");
		String codigoInstitucion= usuario.getCodigoInstitucion();

		UsuariosConsumidoresPresupuestoForm forma = (UsuariosConsumidoresPresupuestoForm) form;
		UtilidadTransaccion.getTransaccion().begin();

        forma.setNumeroIdentificacion("");
		ITiposIdentificacionServicio tiposIdentificacionServicio = AdministracionFabricaServicio
				.crearTiposIdentificacionServicio();

		String numDigCaptNumIdPac = ValoresPorDefecto
				.getNumDigCaptNumIdPac(Integer.valueOf(codigoInstitucion));

		if (UtilidadTexto.isNumber(numDigCaptNumIdPac)) {

			if (numDigCaptNumIdPac.equals("0"))
			{
				numDigCaptNumIdPac="20";
			}
			forma.setNumDigCaptNumId(Integer.parseInt(numDigCaptNumIdPac));
			

		} else {

			forma.setNumDigCaptNumId(20);
		}

		if (!"".equals(forma.getTipoIdentificacion())) {

			TiposIdentificacion tipoIdentificacion = tiposIdentificacionServicio
					.obtenerTipoIdentificacionPorAcronimo(forma
							.getTipoIdentificacion());

			if (tipoIdentificacion != null) {

				if (tipoIdentificacion.getSoloNumeros() != null
						&& tipoIdentificacion.getSoloNumeros().equals(
								ConstantesBD.acronimoSi.charAt(0))) {

					request.setAttribute("validacionCampo", "soloNumero");
				forma.setNumero(true);
				forma.setAlfanum(false);

				} else {

					request.setAttribute("validacionCampo", "alfanumerico");
					forma.setNumero(false);
					forma.setAlfanum(true);
				}
			}
		}

		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("numIdentificacionPaciente");
	}

	/**
	 * 
	 * Método que se encarga de realizar la busqueda de lus usuarios consumidores, segun los parametros
	 * especificados; luego genera el reporte en el formato elegido.
	 * 
	 */
	
		public ActionForward exportarReporte (ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response){
			ActionMessages errores = new ActionMessages();
			try {
				UsuariosConsumidoresPresupuestoForm forma = (UsuariosConsumidoresPresupuestoForm) form;
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				UsuarioBasico usuario=  Utilidades.getUsuarioBasicoSesion(request.getSession());
				EncabezadoRepUsuConDto list = new EncabezadoRepUsuConDto();
				 list.setTelefono(institucion.getTelefono());
					list.setRazonSocial(institucion.getRazonSocial());
					list.setCentroAtencion(institucion.getRazonSocialInstitucionBasica());
					list.setNit(institucion.getNit());
					list.setActividadEconomica(institucion.getActividadEconomica());
					list.setDireccion(institucion.getDireccion());
					list.setUbicacionLogo(institucion.getUbicacionLogo());
					list.setRutaLogo(institucion.getLogoJsp());
					String nombreUsu=usuario.getNombreUsuario();
					String delim=" ";
					String[] nombreSep = nombreUsu.split(delim);
					String nombreUsuario="";
					int size=nombreSep.length;
					nombreUsuario=nombreUsuario+nombreSep[0]+" "+nombreSep[size-2];
					list.setUsuario(nombreUsuario+"("+ usuario.getLoginUsuario() +")");
					list.setTipoImpresion("Impresión");
			   	int j= Integer.parseInt(forma.getMesInicial());
				j=j+1;
				String mesI="";
				if (j<10)
				{
					mesI= "0"+String.valueOf(j);
				}
				else
				{mesI= String.valueOf(j);}
				j= Integer.parseInt(forma.getMesFinal());
				j=j+1;
				 String mesF="";
					if (j<10)
					{
						mesF= "0"+String.valueOf(j);
					}
					else
					{mesF= String.valueOf(j);}
				
				InfoUsuariosConsumidoresArchivoPlanoDto infoUsuariosConsumidoresArchivoPlanoDto = new InfoUsuariosConsumidoresArchivoPlanoDto();
				
				infoUsuariosConsumidoresArchivoPlanoDto.setFechaInicial(UtilidadFecha.obtenerNombreMes(Integer.parseInt(mesI))+"/"+forma.getAgnoInicial());
				infoUsuariosConsumidoresArchivoPlanoDto.setFechaFinal(UtilidadFecha.obtenerNombreMes(Integer.parseInt(mesF))+"/"+forma.getAgnoFinal());
					
				String criteriosBusqueda="Rango Meses: "+UtilidadFecha.obtenerNombreMes(Integer.parseInt(mesI))+"/"+forma.getAgnoInicial()+" - "+UtilidadFecha.obtenerNombreMes(Integer.parseInt(mesF))+"/"+forma.getAgnoFinal()+", Estado:";
				if (forma.getAutorizaciones().equals("1"))
				{
					criteriosBusqueda=criteriosBusqueda+" Autorizados";
					infoUsuariosConsumidoresArchivoPlanoDto.setAutorizaciones("Autorizadas");
				}
				else
				{
					criteriosBusqueda=criteriosBusqueda+" Facturados";
					infoUsuariosConsumidoresArchivoPlanoDto.setAutorizaciones("Facturadas");
				}
				if (!forma.getConvenioSeleccionado().equals("-1"))
				{   ArrayList<Convenios> listaConvenios=new ArrayList<Convenios>();
				    listaConvenios=forma.getListaConvenios();
				    for (Convenios convenio: listaConvenios)
					{  
				    	if (convenio.getCodigo()==Integer.parseInt(forma.getConvenioSeleccionado())) {
							criteriosBusqueda = criteriosBusqueda + ", Convenio: "+ convenio.getNombre();
							infoUsuariosConsumidoresArchivoPlanoDto.setConvenio(convenio.getNombre());
					   }
					}}
				if (!forma.getViaIngreso().equals("-1"))
				{  List<ViaIngresoDto>viasIngresos= new ArrayList<ViaIngresoDto>();
				   viasIngresos= forma.getViasIngresos();
				   for (ViaIngresoDto via: viasIngresos)
				   {
					   if (via.getCodigo()== Integer.parseInt(forma.getViaIngreso())) {
						   criteriosBusqueda = criteriosBusqueda + ", Via Ingreso: "+ via.getNombre();
						   infoUsuariosConsumidoresArchivoPlanoDto.setViaIngreso(via.getNombre());
					   }
				   }}
				if (!forma.getGrupoSeleccionado().equals("-1"))
				{ArrayList<GruposServicios> listaGruposServicios= new ArrayList<GruposServicios>();
				listaGruposServicios= forma.getListaGruposServicios();
				for(GruposServicios grupo: listaGruposServicios)
				{   
					if (grupo.getCodigo()== Integer.parseInt(forma.getGrupoSeleccionado())) {
						criteriosBusqueda = criteriosBusqueda + ", Grupo Seleccionado: "+ grupo.getDescripcion();
						infoUsuariosConsumidoresArchivoPlanoDto.setGrupoServicios(grupo.getDescripcion());
					}
				}}
				if (!forma.getInventarioSeleccionado().equals("-1"))
				{   ArrayList<ClaseInventario> listaClaseInventario= new ArrayList<ClaseInventario>();
				    listaClaseInventario= forma.getListaClaseInventario();
				    for (ClaseInventario clase: listaClaseInventario)
				    { 
				    	if (clase.getCodigo()== Integer.parseInt(forma.getInventarioSeleccionado())) {
				    		criteriosBusqueda = criteriosBusqueda + ", Clase Inventario: "+ clase.getNombre();
				    		infoUsuariosConsumidoresArchivoPlanoDto.setClaseInventarios(clase.getNombre());
				    	}
				    }}
				if (!forma.getDiagnosticoIntervencion().equals(""))
				{
					criteriosBusqueda = criteriosBusqueda + ", Diagnostico: "+ forma.getDiagnosticoIntervencion();
				}
				
				if(!forma.getDiagnosticosDefinitivos().get("principal").toString().isEmpty()) {
					String diagnostico[]= forma.getDiagnosticosDefinitivos().get("principal").toString().split("@@@@@");
					infoUsuariosConsumidoresArchivoPlanoDto.setDiagnostico(diagnostico[2]);
				}
				
				if (!forma.getValorInicial().equals("") && !forma.getValorFinal().equals(""))
				{
					criteriosBusqueda = criteriosBusqueda + ", Valor Inicial: "+ forma.getValorInicial()+ ", Valor Final: "+ forma.getValorFinal();
					infoUsuariosConsumidoresArchivoPlanoDto.setValorInicial(forma.getValorInicial());
					infoUsuariosConsumidoresArchivoPlanoDto.setValorFinal(forma.getValorFinal());
				}
				if (!forma.getTipoIdentificacion().equals("-1")) {
					criteriosBusqueda= criteriosBusqueda + ", Paciente con Identificación: "+forma.getTipoIdentificacion() + " " +forma.getNumeroIdentificacion();
					infoUsuariosConsumidoresArchivoPlanoDto.setTipoIdentificacion(forma.getTipoIdentificacion());
					infoUsuariosConsumidoresArchivoPlanoDto.setNumeroIdentificacion(forma.getNumeroIdentificacion());
				}
				
				String rango= UtilidadFecha.obtenerNombreMes(Integer.parseInt(mesI))+"/"+forma.getAgnoInicial()+" - "+UtilidadFecha.obtenerNombreMes(Integer.parseInt(mesF))+"/"+forma.getAgnoFinal();
				
				int mess= Integer.parseInt(mesF);
				if (mess!=12)
				{
					mess=mess+1;
				}
				else {mess=1;}
				if (mess<10)
				{
					mesF= "0"+String.valueOf(mess);
				}
				else
				{mesF= String.valueOf(mess);}
				int agnos= Integer.parseInt(forma.getAgnoFinal());
				if (mess!=1)
				{
					agnos=agnos++;
				}
				
				String fechaIni="01/"+ mesI+"/"+forma.getAgnoInicial();
				String fechaFin= "01/"+ mesF+"/"+String.valueOf(agnos);
//				if ((Integer.parseInt(mesF) == UtilidadFecha.getMesAnioDiaActual("mes")) && (Integer.parseInt(forma.getAgnoFinal())==UtilidadFecha.getAnioActual()))
//						{
//					fechaFin= UtilidadFecha.getFechaActual();
//						}
				// Realiza la consulta segun los parametros recibidos
				ManejoPacienteFacade buscarFacade=new ManejoPacienteFacade();
				String diag=""; 
				diag= (String) forma.getDiagnosticosDefinitivos("principal");
				String diag1="";
				if (!diag.equals(""))
				{
				diag1= diag.substring(0, 4);
				} 
				List<UsuariosConsumidoresPresupuestoDto> busquedaUsuarios = buscarFacade.listaUsuariosConsumidoresPresupuesto(fechaIni, fechaFin, forma.getAutorizaciones(), forma.getConvenioSeleccionado(), forma.getViaIngreso(), forma.getGrupoSeleccionado(), forma.getInventarioSeleccionado(), diag1, forma.getValorInicial(), forma.getValorFinal(), forma.getTipoIdentificacion(), forma.getNumeroIdentificacion());
				forma.setListadoUsuarios(busquedaUsuarios);
			
				//Obtine el tipo de reporte
				String tipoSalida=forma.getTipoSalida();
			
				//Crea el encabezado del reporte
				ManejoPacienteFacade facencabezado= new ManejoPacienteFacade();
				String autorizado= forma.getAutorizaciones();
				String nombreArchivoOriginal="";
			
				//Genera Reporte
				GeneradorReporteUsuariosConsumidoresPresupuesto generadorReporte= null;
				List<UsuariosConsumidoresPresupuestoDto> listadoUsuarios = forma.getListadoUsuarios();
			
				// Si el reporte esta vacio
				if (listadoUsuarios.isEmpty())
					{
						logger.warn("VACIO" );
						forma.setEstado("empezar");
						tipoSalida="0";
						forma.setNoReporte("1");
										
					}
				else {
					forma.setNoReporte("2");
				}
			try{
				// Si el tipo de archivo en PDF
				if (tipoSalida.equals("1"))
					{			
						generadorReporte= new GeneradorReporteUsuariosConsumidoresPresupuesto(listadoUsuarios,list, autorizado, tipoSalida, criteriosBusqueda);
						JasperPrint reporteOriginal=null;
						reporteOriginal = generadorReporte.generarReporte();
						nombreArchivoOriginal = generadorReporte.exportarReportePDF(reporteOriginal, "UsuariosConsumidoresPresupuestoPDF");
						forma.setNombreArchivoGenerado(nombreArchivoOriginal);
						forma.setEstado("empezar");
						
						
					}
			
				//Si el tipo de archivo es Hoja de Calculo Excel
				if (tipoSalida.equals("3"))
					{			
					    generadorReporte= new GeneradorReporteUsuariosConsumidoresPresupuesto(listadoUsuarios,list, autorizado, tipoSalida, criteriosBusqueda);
					    JasperPrint reporteOriginal=null;
						reporteOriginal = generadorReporte.generarReporte();
						nombreArchivoOriginal= generadorReporte.exportarReporteExcel(reporteOriginal, "UsuariosConsumidoresPresupuestoXLS");
						forma.setNombreArchivoGenerado(nombreArchivoOriginal);
						forma.setEstado("empezar");
						
					}
			
				//Si el tipo de archivo es Archivo Plano
				if (tipoSalida.equals("2"))
					{			
						generadorReporte= new GeneradorReporteUsuariosConsumidoresPresupuesto(listadoUsuarios, autorizado, tipoSalida, rango, infoUsuariosConsumidoresArchivoPlanoDto);
						JasperPrint reporteOriginal=null;
						reporteOriginal = generadorReporte.generarReporte();
						nombreArchivoOriginal= generadorReporte.exportarReporteTextoPlano(reporteOriginal, "UsuariosConsumidoresPresupuestoPlano");
						forma.setNombreArchivoGenerado(nombreArchivoOriginal);
						forma.setEstado("empezar");
						
					}
				}	catch (ClassCastException e) {
				
						//Mostrar error al usuario
						errores.add("notSpecific", new ActionMessage("errors.notEspecific", "Creacion del reporte incorrecto"));
						saveErrors(request, errores);
				
					} catch (Exception e) {
						errores.add("notSpecific", new ActionMessage("errors.notEspecific", "No se pudo crear el reporte"));
						saveErrors(request, errores);
					//	logger.warn("No se pudo crear el reporte " + e.toString());
					}
			
				forma.setTipoSalida("");
	
			}catch (ClassCastException e) {
				
				//Mostrar error al usuario
				errores.add("notSpecific", new ActionMessage("errors.notEspecific", "Generacion del reporte incorrecto"));
				saveErrors(request, errores);
				
			}catch (IPSException e) {
				
				//Mostrar error al usuario
				errores.add("notSpecific", new ActionMessage("errors.notEspecific", "Generacion del reporte incorrecto"));
				saveErrors(request, errores);
		
			}catch (Exception e) {
				
				//Mostrar error al usuario
				errores.add("notSpecific", new ActionMessage("errors.notEspecific", e.getMessage()));
				saveErrors(request, errores);
			}
		
		return mapping.findForward("buscar");
		}
}
