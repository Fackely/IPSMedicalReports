package com.princetonsa.action.facturacion;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.IngresarModificarContratosEntidadesSubcontratadasForm;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.IngresarModificarContratosEntidadesSubcontratadas;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntidadesSubServicio;

/**
 * @author jfhernandez@princetonsa.com
 */
public class IngresarModificarContratosEntidadesSubcontratadasAction extends Action 
{
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(SoportesFacturasAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof IngresarModificarContratosEntidadesSubcontratadasForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			IngresarModificarContratosEntidadesSubcontratadasForm forma = (IngresarModificarContratosEntidadesSubcontratadasForm)form;
    			String estado = forma.getEstado();

    			@SuppressWarnings("unused")
    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización de Ingresar Modificar Entidades Subcontratadas (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}

    			if(estado.equals("empezar"))
    			{
    				return accionEmpezar(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("consultarInfoXEntidad"))
    			{
    				return accionConsultarInfoXEntidad(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("guardar"))
    			{
    				return accionGuardar(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("adicionarInv"))
    			{
    				return accionAdicionarInv(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("adicionarServ"))
    			{
    				return accionAdicionarServ(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("eliminarInventario"))
    			{
    				return accionEliminarInventario(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("eliminarServicio"))
    			{
    				return accionEliminarServicio(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("iniciarBusqueda"))
    			{
    				return accionIniciarBusqueda(con, forma, usuario, request, mapping); 
    			}
    			if (estado.equals("buscarCtos"))
    			{	    		
    				return accionBuscarContratos(con, forma, usuario, request, mapping);
    			}
    			if (estado.equals("modificarRegistro"))
    			{
    				return accionModificarRegistro(con, forma, usuario, request, mapping);
    			}	
    			if(estado.equals("adicionarInvMod"))
    			{
    				return accionAdicionarInvMod(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("adicionarServMod"))
    			{
    				return accionAdicionarServMod(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("guardarCambios"))
    			{
    				return accionGuardarCambios(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("eliminarInventarioMod"))
    			{
    				return accionEliminarInventarioMod(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("eliminarServicioMod"))
    			{
    				return accionEliminarServicioMod(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("modificarInventario"))
    			{
    				return accionModificarInventario(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("modificarServicio"))
    			{
    				return accionModificarServicio(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("guardarServicio"))
    			{
    				return accionGuardarServicio(con, forma, usuario, request, mapping);
    			}
    			if(estado.equals("guardarInventario"))
    			{
    				return accionGuardarInventario(con, forma, usuario, request, mapping);
    			}
    			if (estado.equals("MenuPPal"))
    			{
    				return mapping.findForward("menuPpal");
    			}
    			if(estado.equals("mostrarFiltroTarifa")){
    				if(forma.getFiltros("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
    					forma.setMostrarFiltroTarifa(ConstantesBD.acronimoSi);
    				else
    					forma.setMostrarFiltroTarifa(ConstantesBD.acronimoNo);

    				return mapping.findForward("principal");
    			}
    			if(estado.equals("mostrarFiltroTarifaConsulta")){
    				if(forma.getFiltros("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
    					forma.setMostrarFiltroTarifaConsulta(ConstantesBD.acronimoSi);
    				else
    					forma.setMostrarFiltroTarifaConsulta(ConstantesBD.acronimoNo);

    				return mapping.findForward("consulta");
    			}
    			if(estado.equals("mostrarFiltroTarifaModifica")){
    				logger.info("entra al action y verifica el estado: "+ forma.getFiltros("tipoTarifa").toString());
    				if(forma.getFiltros("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
    				{	forma.setMostrarFiltroTarifaConsulta(ConstantesBD.acronimoSi);
    				logger.info("entra al si Mostrar");
    				}
    				else
    				{	forma.setMostrarFiltroTarifaConsulta(ConstantesBD.acronimoNo);
    				logger.info("entra al no Mostrar");
    				}

    				return mapping.findForward("modificar");
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
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezar(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.reset();
		forma.setFiltros("fechaini", UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()));
		forma.setEntidades(IngresarModificarContratosEntidadesSubcontratadas.obtenerEntidades(con));
		forma.setInventarios(IngresarModificarContratosEntidadesSubcontratadas.obtenerClaseInventarios(con));
		forma.setEsquemas(IngresarModificarContratosEntidadesSubcontratadas.obtenerEsquemas(con));
		forma.setGrupoServicio(IngresarModificarContratosEntidadesSubcontratadas.obtenerGruposServicio(con, ""));
		forma.setEsquemasProcedimientos(IngresarModificarContratosEntidadesSubcontratadas.obtenerEsquemasProcedimientos(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
    /**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionConsultarInfoXEntidad(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		logger.info("EL VALOR DE LA ENTIDAD ES--------->"+forma.getFiltros("nrocontrato"));
		forma.setInfoEntidadInv(IngresarModificarContratosEntidadesSubcontratadas.obtenerInfoXEntidadInv(con,forma.getFiltros("nrocontrato").toString()));
		forma.setInfoEntidadInv(IngresarModificarContratosEntidadesSubcontratadas.obtenerInfoXEntidadServ(con,forma.getFiltros("nrocontrato").toString()));
		forma.setFiltros(IngresarModificarContratosEntidadesSubcontratadas.consultaInfoXEntidadEncabezado(con, forma.getFiltros("nrocontrato").toString()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAdicionarInv(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());
		ActionErrors errores = new ActionErrors();
		boolean errorFechaEsquema=false;
		boolean errorFechaEsquemaClase=false;
		boolean errorMismoEsquemaClaseDiferenteTodos=false;
		/* En este mapa se almacenan mayor fecha de la clase de inventario
		 * anterior a la fecha ingresada por registro y la menorfecha de la clase
		 * superior a la fecha ingresada por registro
		 */
		HashMap<String, String> vigenciaEsquemasTarifarios=new HashMap<String, String>();
		if (numRegistros!=0)
		{
			for(int i=0; i<numRegistros; i++)
			{
				if(forma.getAuxEsquemasInv("claseinventario_"+i).toString().equals(forma.getInfoEntidadInv("claseinventario").toString()))
				{
					String fechaMenorDesp=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasInv("claseinventario_"+i)+"menor");
					String fechaMayorAnt=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasInv("claseinventario_"+i)+"mayor");
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getInfoEntidadInv("fechavigclaseinv").toString(), forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString()))
					{
						if(fechaMenorDesp==null)
						{
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"menor", forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_menor", forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
						}
						else if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString(), fechaMenorDesp))
						{
							// asigno la fecha al mapa
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"menor", forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_menor", forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
						}
					}
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString(), forma.getInfoEntidadInv("fechavigclaseinv").toString()))
					{
						if(fechaMayorAnt==null)
						{
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"mayor", forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_mayor", forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
						}
						else if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaMayorAnt, forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString()))
						{
							// asigno la fecha al mapa
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"mayor", forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_mayor", forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
						}
					}
				}
				Utilidades.imprimirMapa(vigenciaEsquemasTarifarios);
			}
			for (int i=0;i<numRegistros;i++)
			{
				//Valida que no s evayan a ingresar registros con la misma fecha de vigencia y esuqema tarifario
				if (
						forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString().equals(forma.getInfoEntidadInv("fechavigclaseinv").toString())
						&&
						forma.getAuxEsquemasInv("esquematarinv_"+i).toString().equals(forma.getInfoEntidadInv("esquematarinv").toString())
					)
				{
					errorFechaEsquema=true;
				}
				//Valida que no s evayan a ingresar registros con el mismo esquema tarifario y clase de inventario
				if	
					((
						forma.getAuxEsquemasInv("esquematarinv_"+i).toString().equals(forma.getInfoEntidadInv("esquematarinv").toString())
							&&forma.getAuxEsquemasInv("claseinventario_"+i).toString().equals(forma.getInfoEntidadInv("claseinventario").toString()))
					)	
				{
					// Ahora valido que sea de una fecha válida para la misma clase de inventario
					String esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_mayor");
					//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
					if(esquema!=null && esquema.equals(forma.getAuxEsquemasInv("esquematarinv_"+i).toString()))
					{
						errorFechaEsquema=false;
						errorFechaEsquemaClase=true;
					}
					esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_menor");
					//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
					if(esquema!=null && esquema.equals(forma.getAuxEsquemasInv("esquematarinv_"+i).toString()))
					{
						errorFechaEsquema=false;
						errorFechaEsquemaClase=true;
					}
				}
				
				// Solamente si la clase de inventarios es todas, debe validar  
				if((
						forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString().equals(forma.getInfoEntidadInv("fechavigclaseinv").toString())
						&&forma.getAuxEsquemasInv("claseinventario_"+i).toString().equals(forma.getInfoEntidadInv("claseinventario").toString())
				))
				{
					errorFechaEsquema=false;
					errorFechaEsquemaClase=false;
					errorMismoEsquemaClaseDiferenteTodos=true;
				}

			}
			//Si pasa la validacion llena el mapa auxiliar, se usa numregistros para recorrer el mapa y sumarle uno 
			//al final para seguir con la secuencia, y la variable i, es para recorrer lo que ya hay existente en el mapa auxiliar
			if (!errorFechaEsquema&&!errorFechaEsquemaClase&&!errorMismoEsquemaClaseDiferenteTodos)
			{
				forma.setAuxEsquemasInv("claseinventario_"+numRegistros,forma.getInfoEntidadInv("claseinventario").toString());
				forma.setAuxEsquemasInv("esquematarinv_"+numRegistros, forma.getInfoEntidadInv("esquematarinv").toString());
				forma.setAuxEsquemasInv("fechavigclaseinv_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadInv("fechavigclaseinv").toString()));
				forma.setAuxEsquemasInv("nombreinventario_"+numRegistros, forma.getNombreInventario(forma.getInfoEntidadInv("claseinventario").toString()));
				forma.setAuxEsquemasInv("nombreesquema_"+numRegistros, forma.getNombreEsquemaInv(forma.getInfoEntidadInv("esquematarinv").toString()));
				forma.setAuxEsquemasInv("nuevo_"+numRegistros, ConstantesBD.acronimoSi);
				forma.setAuxEsquemasInv("numRegistros",numRegistros+1);
			}
			//Se agregan los errores si las banderas estan activas
			if(errorFechaEsquema)
			{
				errores.add("",new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de inventarios con el esquema tarifario y la fecha seleccionados. Favor verificar."));
				saveErrors(request, errores);
			}
			if (errorFechaEsquemaClase)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de inventarios con la clase de inventario y el esquema tarifario. Favor verificar."));
				saveErrors(request, errores);
			}
			if (errorMismoEsquemaClaseDiferenteTodos)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de inventarios con la fecha específica para la misma clase. Favor verificar."));
				saveErrors(request, errores);
			}
		}
		else
		{
			forma.setAuxEsquemasInv("claseinventario_"+numRegistros,forma.getInfoEntidadInv("claseinventario").toString());
			forma.setAuxEsquemasInv("esquematarinv_"+numRegistros, forma.getInfoEntidadInv("esquematarinv").toString());
			forma.setAuxEsquemasInv("fechavigclaseinv_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadInv("fechavigclaseinv").toString()));
			forma.setAuxEsquemasInv("nombreinventario_"+numRegistros, forma.getNombreInventario(forma.getInfoEntidadInv("claseinventario").toString()));
			forma.setAuxEsquemasInv("nombreesquema_"+numRegistros, forma.getNombreEsquemaInv(forma.getInfoEntidadInv("esquematarinv").toString()));
			forma.setAuxEsquemasInv("nuevo_"+numRegistros, ConstantesBD.acronimoSi);
			forma.setAuxEsquemasInv("numRegistros",numRegistros+1);
		}
			
		Utilidades.imprimirMapa(forma.getAuxEsquemasInv());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAdicionarServ(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());
		ActionErrors errores = new ActionErrors();
		boolean errorFechaEsquema=false;
		boolean errorFechaEsquemaGrupo=false;
		boolean errorMismoEsquemaGrupoDiferenteTodos=false;
		
		
		HashMap<String, String> vigenciaEsquemasTarifarios=new HashMap<String, String>();
		if (numRegistros!=0)
		{
			for(int i=0; i<numRegistros; i++)
			{
				if(forma.getAuxEsquemasServ("gruposervicio_"+i).toString().equals(forma.getInfoEntidadServ("gruposervicio").toString()))
				{
					String fechaMenorDesp=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasServ("gruposervicio_"+i)+"menor");
					String fechaMayorAnt=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasServ("gruposervicio_"+i)+"mayor");
					logger.info("fechaMenorDesp "+fechaMenorDesp );
					logger.info("fechaMayorAnt "+fechaMayorAnt );
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getInfoEntidadServ("fechvigproc").toString(), forma.getAuxEsquemasServ("fechvigproc_"+i).toString()))
					{
						if(fechaMenorDesp==null)
						{
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"menor", forma.getAuxEsquemasServ("fechvigproc_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_menor", forma.getAuxEsquemasServ("esquematarser_"+i).toString());
						}
						else if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getAuxEsquemasServ("fechvigproc_"+i).toString(), fechaMenorDesp))
						{
							// asigno la fecha al mapa
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"menor", forma.getAuxEsquemasServ("fechvigproc_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_menor", forma.getAuxEsquemasServ("esquematarser_"+i).toString());
						}
					}
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getAuxEsquemasServ("fechvigproc_"+i).toString(), forma.getInfoEntidadServ("fechvigproc").toString()))
					{
						if(fechaMayorAnt==null)
						{
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"mayor", forma.getAuxEsquemasServ("fechvigproc_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_mayor", forma.getAuxEsquemasServ("esquematarser_"+i).toString());
						}
						else if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaMayorAnt, forma.getAuxEsquemasServ("fechvigproc_"+i).toString()))
						{
							// asigno la fecha al mapa
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"mayor", forma.getAuxEsquemasServ("fechvigproc_"+i).toString());
							vigenciaEsquemasTarifarios.put(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_mayor", forma.getAuxEsquemasServ("esquematarser_"+i).toString());
						}
					}
				}
				Utilidades.imprimirMapa(vigenciaEsquemasTarifarios);
			}
			for (int i=0;i<numRegistros;i++)
			{
				//Valida que no s evayan a ingresar registros con la misma fecha de vigencia y esuqema tarifario
				if (
						forma.getAuxEsquemasServ("fechvigproc_"+i).toString().equals(forma.getInfoEntidadServ("fechvigproc").toString())
						&&
						forma.getAuxEsquemasServ("esquematarser_"+i).toString().equals(forma.getInfoEntidadServ("esquematarser").toString())
					)
				{
					errorFechaEsquema=true;
				}
				//Valida que no s evayan a ingresar registros con el mismo esquema tarifario y clase de inventario
				if	
					((
						forma.getAuxEsquemasServ("esquematarser_"+i).toString().equals(forma.getInfoEntidadServ("esquematarser").toString())
							&&forma.getAuxEsquemasServ("gruposervicio_"+i).toString().equals(forma.getInfoEntidadServ("gruposervicio").toString()))
					)	
				{
					// Ahora valido que sea de una fecha válida para la misma clase de inventario
					String esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_mayor");
					//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasServ("esquematarser_"+i).toString());
					if(esquema!=null && esquema.equals(forma.getAuxEsquemasServ("esquematarser_"+i).toString()))
					{
						errorFechaEsquema=false;
						errorFechaEsquemaGrupo=true;
					}
					esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_menor");
					//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasServ("esquematarser_"+i).toString());
					if(esquema!=null && esquema.equals(forma.getAuxEsquemasServ("esquematarser_"+i).toString()))
					{
						errorFechaEsquema=false;
						errorFechaEsquemaGrupo=true;
					}
				}
				
				// Solamente si la clase de inventarios es todas, debe validar  
				if((
						forma.getAuxEsquemasServ("fechvigproc_"+i).toString().equals(forma.getInfoEntidadServ("fechvigproc").toString())
						&&forma.getAuxEsquemasServ("gruposervicio_"+i).toString().equals(forma.getInfoEntidadServ("gruposervicio").toString())
				))
				{
					errorFechaEsquema=false;
					errorFechaEsquemaGrupo=false;
					errorMismoEsquemaGrupoDiferenteTodos=true;
				}

			}
			//Si pasa la validacion llena el mapa auxiliar, se usa numregistros para recorrer el mapa y sumarle uno 
			//al final para seguir con la secuencia, y la variable i, es para recorrer lo que ya hay existente en el mapa auxiliar
			if (!errorFechaEsquema&&!errorFechaEsquemaGrupo&&!errorMismoEsquemaGrupoDiferenteTodos)
			{
				forma.setAuxEsquemasServ("gruposervicio_"+numRegistros,forma.getInfoEntidadServ("gruposervicio").toString());
				forma.setAuxEsquemasServ("esquematarser_"+numRegistros, forma.getInfoEntidadServ("esquematarser").toString());
				forma.setAuxEsquemasServ("fechvigproc_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadServ("fechvigproc").toString()));
				forma.setAuxEsquemasServ("nombreservicioserv_"+numRegistros, forma.getNombreServicio(forma.getInfoEntidadServ("gruposervicio").toString()));
				forma.setAuxEsquemasServ("nombreesquemaserv_"+numRegistros, forma.getNombreEsquemaServ(forma.getInfoEntidadServ("esquematarser").toString()));
				forma.setAuxEsquemasServ("nuevo_"+numRegistros, ConstantesBD.acronimoSi);
				forma.setAuxEsquemasServ("numRegistros",numRegistros+1);
			}
			//Se agregan los errores si las banderas estan activas
			if(errorFechaEsquema)
			{
				errores.add("",new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con el esquema tarifario y la fecha seleccionados. Favor verificar."));
				saveErrors(request, errores);
			}
			if (errorFechaEsquemaGrupo)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con grupo de servicio y el esquema tarifario. Favor verificar."));
				saveErrors(request, errores);
			}
			if (errorMismoEsquemaGrupoDiferenteTodos)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con la fecha específica para el mismo grupo de servicio. Favor verificar."));
				saveErrors(request, errores);
			}
		}
		else
		{
			forma.setAuxEsquemasServ("gruposervicio_"+numRegistros,forma.getInfoEntidadServ("gruposervicio").toString());
			forma.setAuxEsquemasServ("esquematarser_"+numRegistros, forma.getInfoEntidadServ("esquematarser").toString());
			forma.setAuxEsquemasServ("fechvigproc_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadServ("fechvigproc").toString()));
			forma.setAuxEsquemasServ("nombreservicioserv_"+numRegistros, forma.getNombreServicio(forma.getInfoEntidadServ("gruposervicio").toString()));
			forma.setAuxEsquemasServ("nombreesquemaserv_"+numRegistros, forma.getNombreEsquemaServ(forma.getInfoEntidadServ("esquematarser").toString()));
			forma.setAuxEsquemasServ("nuevo_"+numRegistros, ConstantesBD.acronimoSi);
			forma.setAuxEsquemasServ("numRegistros",numRegistros+1);
		}
			
		Utilidades.imprimirMapa(forma.getAuxEsquemasServ());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		 
		
		 
		 
		/*
		
		if (numRegistros!=0)
		{
			for (int i=0;i<numRegistros;i++)
			{
				//Valida que no se vayan a ingresar registros con la misma fecha de vigencia y esuqema tarifario
				if (
					(forma.getAuxEsquemasServ("fechvigproc_"+i).toString().equals(forma.getInfoEntidadServ("fechvigproc").toString())
						&&forma.getAuxEsquemasServ("esquematarser_"+i).toString().equals(forma.getInfoEntidadServ("esquematarser").toString()))
					)
				{
					errorFechaEsquema=true;
				}
				//Valida que no se vayan a ingresar registros con la misma fecha de vigencia, esuqema tarifario y clase de inventario
				if	
					(
						(forma.getAuxEsquemasServ("fechvigproc_"+i).toString().equals(forma.getInfoEntidadServ("fechvigproc").toString())
								&&forma.getAuxEsquemasServ("esquematarser_"+i).toString().equals(forma.getInfoEntidadServ("esquematarser").toString())
									&&forma.getAuxEsquemasServ("gruposervicio_"+i).toString().equals(forma.getInfoEntidadServ("gruposervicio").toString()))
						)
						
				{
					errorFechaEsquema=false;
					errorFechaEsquemaGrupo=true;
				}
			}
			if (!errorFechaEsquema&&!errorFechaEsquemaGrupo)
			{
				forma.setAuxEsquemasServ("gruposervicio_"+numRegistros,forma.getInfoEntidadServ("gruposervicio").toString());
				forma.setAuxEsquemasServ("esquematarser_"+numRegistros, forma.getInfoEntidadServ("esquematarser").toString());
				forma.setAuxEsquemasServ("fechvigproc_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadServ("fechvigproc").toString()));
				forma.setAuxEsquemasServ("nombreservicioserv_"+numRegistros, forma.getNombreServicio(forma.getInfoEntidadServ("gruposervicio").toString()));
				forma.setAuxEsquemasServ("nombreesquemaserv_"+numRegistros, forma.getNombreEsquemaServ(forma.getInfoEntidadServ("esquematarser").toString()));
				forma.setAuxEsquemasServ("nuevo_"+numRegistros, ConstantesBD.acronimoSi);
				forma.setAuxEsquemasServ("numRegistros",numRegistros+1);
			}
			
			//Se agregan los errores si las banderas estan activas
			if(errorFechaEsquema)
			{
				errores.add("",new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con el esquema tarifario y la fecha seleccionados. Por favor seleccione otro."));
				saveErrors(request, errores);
			}
			if (errorFechaEsquemaGrupo)
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con el grupo de servicio, el esquema tarifario y la fecha seleccionados. Por favor seleccione otro."));
				saveErrors(request, errores);
			}
		}
		else
		{
			forma.setAuxEsquemasServ("gruposervicio_"+numRegistros,forma.getInfoEntidadServ("gruposervicio").toString());
			forma.setAuxEsquemasServ("esquematarser_"+numRegistros, forma.getInfoEntidadServ("esquematarser").toString());
			forma.setAuxEsquemasServ("fechvigproc_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadServ("fechvigproc").toString()));
			forma.setAuxEsquemasServ("nombreservicioserv_"+numRegistros, forma.getNombreServicio(forma.getInfoEntidadServ("gruposervicio").toString()));
			forma.setAuxEsquemasServ("nombreesquemaserv_"+numRegistros, forma.getNombreEsquemaServ(forma.getInfoEntidadServ("esquematarser").toString()));
			forma.setAuxEsquemasServ("nuevo_"+numRegistros, ConstantesBD.acronimoSi);
			forma.setAuxEsquemasServ("numRegistros",numRegistros+1);
		}
	
		Utilidades.imprimirMapa(forma.getAuxEsquemasServ());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");*/
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarInventario(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());
		for (int i=0;i<numRegistros;i++)
		{	
			if (i==forma.getIndice())
			{
				forma.setAuxEsquemasInv("claseinventario_"+i,"eliminado");
				forma.setAuxEsquemasInv("esquematarinv_"+i,"eliminado");
				forma.setAuxEsquemasInv("fechavigclaseinv_"+i,"eliminado");
				forma.setAuxEsquemasInv("nombreinventario_"+i,"eliminado");
				forma.setAuxEsquemasInv("nombreesquema_"+i,"eliminado");
				
			}
		}
		//forma.setAuxEsquemasInv("numRegistros",numRegistros-1);
		Utilidades.imprimirMapa(forma.getAuxEsquemasInv());
		
		forma.setEstado("adicionarInv");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarServicio(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());
		for (int i=0;i<numRegistros;i++)
		{	
			if (i==forma.getIndice())
			{
				forma.setAuxEsquemasServ("gruposervicio_"+i,"eliminado");
				forma.setAuxEsquemasServ("esquematarser_"+i,"eliminado");
				forma.setAuxEsquemasServ("fechvigproc_"+i,"eliminado");
				forma.setAuxEsquemasServ("nombreservicioserv_"+i,"eliminado");
				forma.setAuxEsquemasServ("nombreesquemaserv_"+i,"eliminado");
			}
		}
		Utilidades.imprimirMapa(forma.getAuxEsquemasServ());
		forma.setEstado("adicionarServ");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGuardar(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		long consecutivo=0;
		ActionErrors errores = new ActionErrors();
		forma.setFiltros("institucion", usuario.getCodigoInstitucionInt());
		forma.setFiltros("fechamodifica", UtilidadFecha.getFechaActual());
		forma.setFiltros("horamodifica", UtilidadFecha.getHoraActual());
		forma.setFiltros("usuariomodifica", usuario.getLoginUsuario());
		forma.setFiltros("nombreentidad", forma.getNombreEntidad(forma.getFiltros("codigoentidad").toString()));
		forma.setFiltros("tipoTarifa",forma.getFiltros().get("tipoTarifa"));//////////
		logger.info("el codigo de la entidad es----->"+forma.getFiltros("codigoentidad").toString());
		Utilidades.imprimirMapa(forma.getFiltros());
		boolean ingreseArticulos=false;
		boolean ingreseServicios=false;
		boolean ingresoTarConvenioPaciente=false;
		
		if (IngresarModificarContratosEntidadesSubcontratadas.verificarTraslapeContratos(con,forma.getFiltros()))
		{
			errores.add("fechas", new ActionMessage("errors.contratoConvigenciaYaExistente",forma.getFiltros("fechaini"),forma.getFiltros("fechafin")));
			saveErrors(request, errores);
		}	
		else
		{
			logger.info("Tipo tarifa es :-->"+forma.getFiltros("tipoTarifa").toString());
			//Permite guardar solamente cuando tipoTarifa sea igual a TCP (TarifaConvenioPaciente)
		   if(forma.getFiltros("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaConvenioPaciente))			    		
		   {
			consecutivo=IngresarModificarContratosEntidadesSubcontratadas.guardarEncabezado(con,forma.getFiltros());
			ingresoTarConvenioPaciente=true;
		   } 
		   else
		   {
			
			HashMap auxiliarInv= new HashMap<String, Object>();
			HashMap auxiliarServ= new HashMap<String, Object>();
												
				//Primero se verifica de que se ingrese si quiera un esuqema tarifario
				boolean verificarEsquema=false;
				for(int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());i++)
				{
					if (!forma.getAuxEsquemasInv("claseinventario_"+i).equals("eliminado"))
						verificarEsquema=true;
				}
				for(int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());i++)
				{
					if (!forma.getAuxEsquemasServ("gruposervicio_"+i).equals("eliminado"))			
						verificarEsquema=true;
				}

				//Si pasa la validación de que al menos exista un esquema entonces si puede insertar
				if (verificarEsquema)
				{
					consecutivo=IngresarModificarContratosEntidadesSubcontratadas.guardarEncabezado(con,forma.getFiltros());
					
					for(int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());i++)
					{
						if (!forma.getAuxEsquemasInv("claseinventario_"+i).equals("eliminado"))
						{
							auxiliarInv.put("claseinventario",forma.getAuxEsquemasInv("claseinventario_"+i).toString());
							auxiliarInv.put("esquematarinv",forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
							auxiliarInv.put("fechavigclaseinv",forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString());
							auxiliarInv.put("contratoentidad",consecutivo);
							auxiliarInv.put("fechamodifica", UtilidadFecha.getFechaActual());
							auxiliarInv.put("horamodifica", UtilidadFecha.getHoraActual());
							auxiliarInv.put("usuariomodifica", usuario.getLoginUsuario());
							auxiliarInv.put("activo", ConstantesBD.acronimoSi);
							ingreseArticulos=IngresarModificarContratosEntidadesSubcontratadas.guardarEsquemasInventarios(con,auxiliarInv);
						}
					}
					for(int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());i++)
					{
						if (!forma.getAuxEsquemasServ("gruposervicio_"+i).equals("eliminado"))
						{			
							auxiliarServ.put("gruposervicio",forma.getAuxEsquemasServ("gruposervicio_"+i).toString());
							auxiliarServ.put("esquematarser",forma.getAuxEsquemasServ("esquematarser_"+i).toString());
							auxiliarServ.put("fechvigproc",forma.getAuxEsquemasServ("fechvigproc_"+i).toString());
							auxiliarServ.put("contratoentidad",consecutivo);
							auxiliarServ.put("fechamodifica", UtilidadFecha.getFechaActual());
							auxiliarServ.put("horamodifica", UtilidadFecha.getHoraActual());
							auxiliarServ.put("usuariomodifica", usuario.getLoginUsuario());
							auxiliarServ.put("activo", ConstantesBD.acronimoSi);
							ingreseServicios=IngresarModificarContratosEntidadesSubcontratadas.guardarEsquemasServicios(con,auxiliarServ);
						}
					}
				}
				else
				{
					errores.add("", new ActionMessage("errors.notEspecific","Debe existir al menos un esquema de inventarios ó procedimientos para ingresar el contrato."));
					saveErrors(request, errores);
				}
			}			
		}
		if (ingreseArticulos||ingreseServicios||ingresoTarConvenioPaciente)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumenIngresoContrato");
		}
		else
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionIniciarBusqueda(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.reset();
		forma.setEntidades(IngresarModificarContratosEntidadesSubcontratadas.obtenerEntidades(con));
		forma.setInventarios(IngresarModificarContratosEntidadesSubcontratadas.obtenerClaseInventarios(con));
		forma.setEsquemas(IngresarModificarContratosEntidadesSubcontratadas.obtenerEsquemas(con));
		forma.setGrupoServicio(IngresarModificarContratosEntidadesSubcontratadas.obtenerGruposServicio(con, ""));
		forma.setEsquemasProcedimientos(IngresarModificarContratosEntidadesSubcontratadas.obtenerEsquemasProcedimientos(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consulta");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscarContratos(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		ActionErrors errores = new ActionErrors();
		
		if (!UtilidadTexto.isEmpty(forma.getFiltros("fechaini").toString()))
		{				
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFiltros("fechaini").toString()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial del contrato "+forma.getFiltros("fechaini").toString()));
				saveErrors(request, errores);
			}
		}
		if (!UtilidadTexto.isEmpty(forma.getFiltros("fechafin").toString()))
		{				
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFiltros("fechafin").toString()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final del contrato "+forma.getFiltros("fechafin").toString()));
				saveErrors(request, errores);
			}	
		}
		
		if (UtilidadTexto.isEmpty(forma.getFiltros("fechaini").toString())&&!UtilidadTexto.isEmpty(forma.getFiltros("fechafin").toString()))
		{
			errores.add(forma.getFiltros("fechaini").toString(), new ActionMessage("errors.required","La Fecha Inicial del Contrato "));
			saveErrors(request, errores);
		}
		
		if (!UtilidadTexto.isEmpty(forma.getFiltros("fechaini").toString())&&UtilidadTexto.isEmpty(forma.getFiltros("fechafin").toString()))
		{
			errores.add(forma.getFiltros("fechaini").toString(), new ActionMessage("errors.required","La Fecha Final del Contrato "));
			saveErrors(request, errores);
		}
		
		if (!UtilidadTexto.isEmpty(forma.getFiltros("fechaini").toString())&&!UtilidadTexto.isEmpty(forma.getFiltros("fechafin").toString()))
		{
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltros("fechaini").toString(), forma.getFiltros("fechafin").toString()))
			{
				errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Final del contrato "+forma.getFiltros("fechafin").toString(),"Inicial del contrato "+forma.getFiltros("fechaini").toString()));
				saveErrors(request, errores);
			}
		}
		
		//VALIDA QUE SOLO CONSULTE LAS FECHAS DE LOS ESQUEMAS SOLO CUANDO EL TIPOtARIFA SEA IGUAL A TP=TARIFAPROPIA
		if(forma.getFiltros("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
		{
			if (!UtilidadTexto.isEmpty(forma.getInfoEntidadInv("fechavigclaseinv").toString()))
			{
				if(!UtilidadFecha.esFechaValidaSegunAp(forma.getInfoEntidadInv("fechavigclaseinv").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de inventarios "+forma.getInfoEntidadInv("fechavigclaseinv").toString()));
					saveErrors(request, errores);
				}
			}
		
			if (!UtilidadTexto.isEmpty(forma.getInfoEntidadServ("fechvigproc").toString()))
			{			
				if(!UtilidadFecha.esFechaValidaSegunAp(forma.getInfoEntidadServ("fechvigproc").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de servicios "+forma.getInfoEntidadServ("fechvigproc").toString()));
					saveErrors(request, errores);
				}
			}
		}
			logger.info("\n\n\n -El valor de tipoTarifa en BuscarContratos es:-->"+forma.getFiltros("tipoTarifa").toString()+"\n\n\n");
			if(forma.getFiltros().toString().equals(ConstantesBD.codigoNuncaValido)){
				errores.add("",new ActionMessage("errors.noSeleccionoTipoTarifa"+forma.getFiltros("tipoTarifa").toString()));
				saveErrors(request, errores);
		
	}
		

		if(errores.isEmpty())
		{
			Utilidades.imprimirMapa(forma.getFiltros());			
			HashMap infoContratos = IngresarModificarContratosEntidadesSubcontratadas.consultaContratos(
					con,forma.getFiltros(),forma.getInfoEntidadInv(),forma.getInfoEntidadServ());
			
			int numRegistros= Utilidades.convertirAEntero(infoContratos.get("numRegistros").toString());
			
			if(numRegistros>0){
				
				IAutorizacionesEntidadesSubServicio autorizacionServicio = ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();
				ArrayList<AutorizacionesEntidadesSub> listaAutorizaciones = null;
				DtoContrato dtoContrato = null;				
				int numeroContrato=0;				
				
				/*for(int i=0; i<numRegistros; i++){
					dtoContrato = new DtoContrato();
					
					if (!(forma.getFiltros()).get("nrocontrato_"+i).toString().equals("")){
						if(UtilidadTexto.isNumber((infoContratos.get("nrocontrato_"+i).toString()))){
							numeroContrato = (Integer)(infoContratos.get("nrocontrato_"+i));
						}
					}
					dtoContrato.setCodigo(numeroContrato);
					listaAutorizaciones = autorizacionServicio.obtenerAutorizacionesContratoID(dtoContrato);
					
					if(Utilidades.isEmpty(listaAutorizaciones)){
						infoContratos.put("puedeModificar_" + i, true);
					}*/
					
					forma.setResultadosBusqueda("encabezado",infoContratos);
					forma.setResultadosBusqueda("tarifasinventarios",IngresarModificarContratosEntidadesSubcontratadas.consultarEsquemasInventarios(con));
					forma.setResultadosBusqueda("tarifasprocedimientos",IngresarModificarContratosEntidadesSubcontratadas.consultarEsquemasServicios(con));
										
				//}
				
			}
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resultado");
		}
		return mapping.findForward("consulta");
	}
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionModificarRegistro(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		forma.resetPpal();
		forma.setEntidades(IngresarModificarContratosEntidadesSubcontratadas.obtenerEntidades(con));
		forma.setInventarios(IngresarModificarContratosEntidadesSubcontratadas.obtenerClaseInventarios(con));
		forma.setEsquemas(IngresarModificarContratosEntidadesSubcontratadas.obtenerEsquemas(con));
		forma.setGrupoServicio(IngresarModificarContratosEntidadesSubcontratadas.obtenerGruposServicio(con, ""));
		forma.setEsquemasProcedimientos(IngresarModificarContratosEntidadesSubcontratadas.obtenerEsquemasProcedimientos(con));
		HashMap encabezado = (HashMap)(forma.getResultadosBusqueda("encabezado"));
		HashMap tarifasinventarios = (HashMap)(forma.getResultadosBusqueda("tarifasinventarios"));
		HashMap tarifasprocedimientos = (HashMap)(forma.getResultadosBusqueda("tarifasprocedimientos"));
		int numRegistros=0;
		
		forma.setIndiceMod(forma.getIndice());
		logger.info("VALOR DE INDICEMOD--------->"+forma.getIndiceMod());
		
		forma.setFiltros("consecutivo",encabezado.get("consecutivo_"+forma.getIndice()));
		forma.setFiltros("codigoentidad",encabezado.get("entidad_"+forma.getIndice()));
		forma.setFiltros("nrocontrato",encabezado.get("nrocontrato_"+forma.getIndice()));
		forma.setFiltros("valorcontrato",encabezado.get("valorcontrato_"+forma.getIndice()));
		forma.setFiltros("estadoautoriz",encabezado.get("estadoautoriz_"+forma.getIndice()));///////////////////////
			logger.info("\n\n VALOR DE TARIFA EN MODIFI--------->"+encabezado.get("tipotarifa_"+forma.getIndice()));			
		forma.setFiltros("tipoTarifa",encabezado.get("tipotarifa_"+forma.getIndice()));//
		forma.setFiltros("fechaini",UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechaini_"+forma.getIndice()).toString()));
		forma.setFiltros("fechafin",UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechafin_"+forma.getIndice()).toString()));
		forma.setFiltros("fechafirma",UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechafirma_"+forma.getIndice()).toString()));
		forma.setFiltros("observaciones",encabezado.get("observaciones_"+forma.getIndice()));
		forma.setFiltros("fechamodifica",UtilidadFecha.getFechaActual());
		forma.setFiltros("horamodifica",UtilidadFecha.getHoraActual());
		forma.setFiltros("usuariomodifica",usuario.getLoginUsuario());
		
		forma.setFiltros("codigoentidadantiguo",encabezado.get("entidad_"+forma.getIndice()));
		forma.setFiltros("consecutivoantiguo",encabezado.get("consecutivo_"+forma.getIndice()));
		forma.setFiltros("nrocontratoantiguo",encabezado.get("nrocontrato_"+forma.getIndice()));
		forma.setFiltros("valorcontratoantiguo",encabezado.get("valorcontrato_"+forma.getIndice()));
		forma.setFiltros("tipoTarifaantiguo",encabezado.get("tipotarifa_"+forma.getIndice()));//////nueva linea
		forma.setFiltros("fechafinantiguo",UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechafin_"+forma.getIndice()).toString()));
		forma.setFiltros("fechafirmaantiguo",UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechafirma_"+forma.getIndice()).toString()));
		forma.setFiltros("observacionesantiguo",encabezado.get("observaciones_"+forma.getIndice()));
		forma.setFiltros("fechamodificaantiguo",encabezado.get("fechamodifica_"+forma.getIndice()));
		forma.setFiltros("horamodificaantiguo",encabezado.get("horamodifica_"+forma.getIndice()));
		forma.setFiltros("usuariomodificaantiguo",encabezado.get("usuariomodifica_"+forma.getIndice()));
	
	logger.info("\n\n\nel tipo de tarifa para modificar es : "+ forma.getFiltros("tipoTarifa")+"\n\n");	
	/*if(forma.getFiltros("tipoTarifa").equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
	{*/
		for (int j=0;j<Utilidades.convertirAEntero(tarifasinventarios.get("numRegistros").toString());j++)
		{
			if(encabezado.get("consecutivo_"+forma.getIndice()).toString().equals(tarifasinventarios.get("contrato_"+j).toString()))
			{
				forma.setAuxEsquemasInv("consecutivo_"+numRegistros,tarifasinventarios.get("consecutivo_"+j).toString());
				forma.setAuxEsquemasInv("claseinventario_"+numRegistros,tarifasinventarios.get("claseinv_"+j).toString());
				forma.setAuxEsquemasInv("esquematarinv_"+numRegistros, tarifasinventarios.get("esquemainv_"+j).toString());
				forma.setAuxEsquemasInv("fechavigclaseinv_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(tarifasinventarios.get("fechavigenciainv_"+j).toString()));
				forma.setAuxEsquemasInv("nombreinventario_"+numRegistros, tarifasinventarios.get("nombreclaseinv_"+j).toString());
				forma.setAuxEsquemasInv("nombreesquema_"+numRegistros, tarifasinventarios.get("nombreesquema_"+j).toString());
				forma.setAuxEsquemasInv("activo_"+numRegistros,tarifasinventarios.get("activo_"+j).toString());
				forma.setAuxEsquemasInv("nuevo_"+numRegistros,ConstantesBD.acronimoNo);
				forma.setAuxEsquemasInv("numRegistros",numRegistros+1);
				numRegistros++;
			}
		}
		
		numRegistros=0;
		
		for (int i=0;i<Utilidades.convertirAEntero(tarifasprocedimientos.get("numRegistros").toString());i++)
		{
			if(encabezado.get("consecutivo_"+forma.getIndice()).toString().equals(tarifasprocedimientos.get("contrato_"+i).toString()))
			{
				forma.setAuxEsquemasServ("consecutivo_"+numRegistros,tarifasprocedimientos.get("consecutivo_"+i).toString());
				forma.setAuxEsquemasServ("gruposervicio_"+numRegistros,tarifasprocedimientos.get("gruposerv_"+i).toString());
				forma.setAuxEsquemasServ("esquematarser_"+numRegistros, tarifasprocedimientos.get("esquemaserv_"+i).toString());
				forma.setAuxEsquemasServ("fechvigproc_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(tarifasprocedimientos.get("fechavigenciaserv_"+i).toString()));
				forma.setAuxEsquemasServ("nombreservicioserv_"+numRegistros, tarifasprocedimientos.get("descgruposerv_"+i).toString());
				forma.setAuxEsquemasServ("nombreesquemaserv_"+numRegistros, tarifasprocedimientos.get("nombreesquema_"+i).toString());
				forma.setAuxEsquemasServ("activo_"+numRegistros, tarifasprocedimientos.get("activo_"+i).toString());
				forma.setAuxEsquemasServ("nuevo_"+numRegistros,ConstantesBD.acronimoNo);
				forma.setAuxEsquemasServ("numRegistros",numRegistros+1);
				numRegistros++;
			}
		}
	//}
		Utilidades.imprimirMapa(forma.getAuxEsquemasInv());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAdicionarInvMod(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());
		ActionErrors errores = new ActionErrors();
		boolean errorFechaEsquema=false;
		boolean errorFechaEsquemaClase=false;
		boolean errorMismoEsquemaClaseDiferenteTodos=false;
		boolean centinelaFecha=false;
		HashMap<String, String> vigenciaEsquemasTarifarios=new HashMap<String, String>();
		
		if (UtilidadTexto.isEmpty(forma.getInfoEntidadInv("esquematarinv").toString()))
		{
			errores.add(forma.getInfoEntidadInv("esquematarinv").toString(), new ActionMessage("errors.required","El esquema tarifario de Inventario "));
			centinelaFecha=true;
			saveErrors(request, errores);
		}
		if (UtilidadTexto.isEmpty(forma.getInfoEntidadInv("fechavigclaseinv").toString()))
		{
			errores.add(forma.getInfoEntidadInv("fechavigclaseinv").toString(), new ActionMessage("errors.required","La fecha de vigencia de Inventario "));
			centinelaFecha=true;
			saveErrors(request, errores);
		}
		
		if (!UtilidadTexto.isEmpty(forma.getInfoEntidadInv("fechavigclaseinv").toString()))
		{
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getInfoEntidadInv("fechavigclaseinv").toString()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de inventarios "+forma.getInfoEntidadInv("fechavigclaseinv").toString()));
				saveErrors(request, errores);
				centinelaFecha=true;
			}
			if(!centinelaFecha&&!UtilidadTexto.isEmpty(forma.getFiltros("fechaini").toString()))
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getInfoEntidadInv("fechavigclaseinv").toString(), forma.getFiltros("fechaini").toString()))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Vigencia de Inventarios "+forma.getInfoEntidadInv("fechavigclaseinv").toString(), "de Inicio del Contrato "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltros("fechaini").toString())));
					centinelaFecha=true;
					saveErrors(request, errores);
				}
			}
		}
		
		for (int i=0;i<numRegistros;i++)
		{
			//Valida que no s evayan a ingresar registros con la misma fecha de vigencia y esuqema tarifario
			if (
					forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString().equals(forma.getInfoEntidadInv("fechavigclaseinv").toString())
					&&
					forma.getAuxEsquemasInv("esquematarinv_"+i).toString().equals(forma.getInfoEntidadInv("esquematarinv").toString())
				)
			{
				errorFechaEsquema=true;
			}
			//Valida que no s evayan a ingresar registros con el mismo esquema tarifario y clase de inventario
			if	
				((
					forma.getAuxEsquemasInv("esquematarinv_"+i).toString().equals(forma.getInfoEntidadInv("esquematarinv").toString())
						&&forma.getAuxEsquemasInv("claseinventario_"+i).toString().equals(forma.getInfoEntidadInv("claseinventario").toString()))
				)	
			{
				// Ahora valido que sea de una fecha válida para la misma clase de inventario
				String esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_mayor");
				//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
				if(esquema!=null && esquema.equals(forma.getAuxEsquemasInv("esquematarinv_"+i).toString()))
				{
					errorFechaEsquema=false;
					errorFechaEsquemaClase=true;
				}
				esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasInv("claseinventario_"+i)+"esq_menor");
				//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
				if(esquema!=null && esquema.equals(forma.getAuxEsquemasInv("esquematarinv_"+i).toString()))
				{
					errorFechaEsquema=false;
					errorFechaEsquemaClase=true;
				}
			}
			
			// Solamente si la clase de inventarios es todas, debe validar  
			if((
					forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString().equals(forma.getInfoEntidadInv("fechavigclaseinv").toString())
					&&forma.getAuxEsquemasInv("claseinventario_"+i).toString().equals(forma.getInfoEntidadInv("claseinventario").toString())
			))
			{
				errorFechaEsquema=false;
				errorFechaEsquemaClase=false;
				errorMismoEsquemaClaseDiferenteTodos=true;
			}
		}
		//Se agregan los errores si las banderas estan activas
		if(errorFechaEsquema)
		{
			errores.add("",new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de inventarios con el esquema tarifario y la fecha seleccionados. Favor verificar."));
			saveErrors(request, errores);
		}
		if (errorFechaEsquemaClase)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de inventarios con la clase de inventario y el esquema tarifario. Favor verificar."));
			saveErrors(request, errores);
		}
		if (errorMismoEsquemaClaseDiferenteTodos)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de inventarios con la fecha específica para la misma clase. Favor verificar."));
			saveErrors(request, errores);
		}
		
		
		if (!centinelaFecha&&!errorFechaEsquema&&!errorFechaEsquemaClase&&!errorMismoEsquemaClaseDiferenteTodos)
		{
			forma.setAuxEsquemasInv("claseinventario_"+numRegistros,forma.getInfoEntidadInv("claseinventario").toString());
			forma.setAuxEsquemasInv("esquematarinv_"+numRegistros, forma.getInfoEntidadInv("esquematarinv").toString());
			forma.setAuxEsquemasInv("fechavigclaseinv_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadInv("fechavigclaseinv").toString()));
			forma.setAuxEsquemasInv("nombreinventario_"+numRegistros, forma.getNombreInventario(forma.getInfoEntidadInv("claseinventario").toString()));
			forma.setAuxEsquemasInv("nombreesquema_"+numRegistros, forma.getNombreEsquemaInv(forma.getInfoEntidadInv("esquematarinv").toString()));
			forma.setAuxEsquemasInv("activo_"+numRegistros,ConstantesBD.acronimoSi);
			forma.setAuxEsquemasInv("nuevo_"+numRegistros,ConstantesBD.acronimoSi);
			forma.setAuxEsquemasInv("consecutivo_"+numRegistros,ConstantesBD.codigoNuncaValido);
			forma.setAuxEsquemasInv("numRegistros",numRegistros+1);
			Utilidades.imprimirMapa(forma.getAuxEsquemasInv());
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("modificar");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAdicionarServMod(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());
		ActionErrors errores = new ActionErrors();
		boolean errorFechaEsquema=false;
		boolean errorFechaEsquemaGrupo=false;
		boolean errorMismoEsquemaGrupoDiferenteTodos=false;
		HashMap<String, String> vigenciaEsquemasTarifarios=new HashMap<String, String>();
		boolean centinelaFecha=false;
		
		if (UtilidadTexto.isEmpty(forma.getInfoEntidadServ("esquematarser").toString()))
		{
			errores.add(forma.getInfoEntidadServ("esquematarser").toString(), new ActionMessage("errors.required","El esquema tarifario de Servicios "));
			centinelaFecha=true;
			saveErrors(request, errores);
		}
		if (UtilidadTexto.isEmpty(forma.getInfoEntidadServ("fechvigproc").toString()))
		{
			errores.add(forma.getInfoEntidadServ("fechvigproc").toString(), new ActionMessage("errors.required","La fecha de vigencia de Servicios "));
			centinelaFecha=true;
			saveErrors(request, errores);
		}
		if (!UtilidadTexto.isEmpty(forma.getInfoEntidadServ("fechvigproc").toString()))
		{			
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getInfoEntidadServ("fechvigproc").toString()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de inventarios "+forma.getInfoEntidadServ("fechvigproc").toString()));
				centinelaFecha=true;
				saveErrors(request, errores);
			}
			if (!centinelaFecha&&!UtilidadTexto.isEmpty(forma.getFiltros("fechaini").toString()))
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getInfoEntidadServ("fechvigproc").toString(), forma.getFiltros("fechaini").toString()))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Vigencia de Servicios "+forma.getInfoEntidadServ("fechvigproc").toString(), "de Inicio del Contrato "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltros("fechaini").toString())));
					centinelaFecha=true;
					saveErrors(request, errores);
				}
			}		
		}
		
		for (int i=0;i<numRegistros;i++)
		{
			//Valida que no s evayan a ingresar registros con la misma fecha de vigencia y esuqema tarifario
			if (
					forma.getAuxEsquemasServ("fechvigproc_"+i).toString().equals(forma.getInfoEntidadServ("fechvigproc").toString())
					&&
					forma.getAuxEsquemasServ("esquematarser_"+i).toString().equals(forma.getInfoEntidadServ("esquematarser").toString())
				)
			{
				errorFechaEsquema=true;
			}
			//Valida que no s evayan a ingresar registros con el mismo esquema tarifario y clase de inventario
			if	
				((
					forma.getAuxEsquemasServ("esquematarser_"+i).toString().equals(forma.getInfoEntidadServ("esquematarser").toString())
						&&forma.getAuxEsquemasServ("gruposervicio_"+i).toString().equals(forma.getInfoEntidadServ("gruposervicio").toString()))
				)	
			{
				// Ahora valido que sea de una fecha válida para la misma clase de inventario
				String esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_mayor");
				//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasServ("esquematarser_"+i).toString());
				if(esquema!=null && esquema.equals(forma.getAuxEsquemasServ("esquematarser_"+i).toString()))
				{
					errorFechaEsquema=false;
					errorFechaEsquemaGrupo=true;
				}
				esquema=vigenciaEsquemasTarifarios.get(forma.getAuxEsquemasServ("gruposervicio_"+i)+"esq_menor");
				//logger.info("Esquema mapa "+esquema+"  :  "+forma.getAuxEsquemasServ("esquematarser_"+i).toString());
				if(esquema!=null && esquema.equals(forma.getAuxEsquemasServ("esquematarser_"+i).toString()))
				{
					errorFechaEsquema=false;
					errorFechaEsquemaGrupo=true;
				}
			}
			
			// Solamente si la clase de inventarios es todas, debe validar  
			if((
					forma.getAuxEsquemasServ("fechvigproc_"+i).toString().equals(forma.getInfoEntidadServ("fechvigproc").toString())
					&&forma.getAuxEsquemasServ("gruposervicio_"+i).toString().equals(forma.getInfoEntidadServ("gruposervicio").toString())
			))
			{
				errorFechaEsquema=false;
				errorFechaEsquemaGrupo=false;
				errorMismoEsquemaGrupoDiferenteTodos=true;
			}

		}
		
		//Se agregan los errores si las banderas estan activas
		if(errorFechaEsquema)
		{
			errores.add("",new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con el esquema tarifario y la fecha seleccionados. Favor verificar."));
			saveErrors(request, errores);
		}
		if (errorFechaEsquemaGrupo)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con grupo de servicio y el esquema tarifario. Favor verificar."));
			saveErrors(request, errores);
		}
		if (errorMismoEsquemaGrupoDiferenteTodos)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "Ya seleccionó un Esquema de procedimientos con la fecha específica para el mismo grupo de servicio. Favor verificar."));
			saveErrors(request, errores);
		}
		
		if (!centinelaFecha&&!errorFechaEsquema&&!errorFechaEsquemaGrupo&&!errorMismoEsquemaGrupoDiferenteTodos)
		{
			forma.setAuxEsquemasServ("gruposervicio_"+numRegistros,forma.getInfoEntidadServ("gruposervicio").toString());
			forma.setAuxEsquemasServ("esquematarser_"+numRegistros, forma.getInfoEntidadServ("esquematarser").toString());
			forma.setAuxEsquemasServ("fechvigproc_"+numRegistros, UtilidadFecha.conversionFormatoFechaAAp(forma.getInfoEntidadServ("fechvigproc").toString()));
			forma.setAuxEsquemasServ("nombreservicioserv_"+numRegistros, forma.getNombreServicio(forma.getInfoEntidadServ("gruposervicio").toString()));
			forma.setAuxEsquemasServ("nombreesquemaserv_"+numRegistros, forma.getNombreEsquemaServ(forma.getInfoEntidadServ("esquematarser").toString()));
			forma.setAuxEsquemasServ("activo_"+numRegistros,ConstantesBD.acronimoSi);
			forma.setAuxEsquemasServ("nuevo_"+numRegistros,ConstantesBD.acronimoSi);
			forma.setAuxEsquemasServ("consecutivo_"+numRegistros,ConstantesBD.codigoNuncaValido);
			forma.setAuxEsquemasServ("numRegistros",numRegistros+1);
			Utilidades.imprimirMapa(forma.getAuxEsquemasServ());
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("modificar");
	}
	
	@SuppressWarnings("unused")
	private ActionErrors validateGuardarCambios(IngresarModificarContratosEntidadesSubcontratadasForm forma)
	{
		ActionErrors errores = new ActionErrors();
		
		boolean centinelaFecha=false;
		if (UtilidadTexto.isEmpty(forma.getFiltros("codigoentidad").toString()))
		{
			errores.add(forma.getFiltros("codigoentidad").toString(), new ActionMessage("errors.required","La Entidad Subcontratada "));
		}
		if(forma.getFiltros("tipoTarifa").toString().equals(ConstantesBD.codigoNuncaValido)){
			errores.add(forma.getFiltros("tipoTarifa").toString(), new ActionMessage("errors.required","El tipo tarifa "));
		}
		if (UtilidadTexto.isEmpty(forma.getFiltros("nrocontrato").toString()))
		{
			errores.add(forma.getFiltros("nrocontrato").toString(), new ActionMessage("errors.required","El número del contrato "));
		}
		if (UtilidadTexto.isEmpty(forma.getFiltros("valorcontrato").toString()))
		{
			errores.add(forma.getFiltros("valorcontrato").toString(), new ActionMessage("errors.required","El valor del contrato "));
		}
		if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFiltros("fechafin").toString()))
		{
			errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+forma.getFiltros("fechafin").toString()));
		}
		if (UtilidadTexto.isEmpty(forma.getFiltros("fechafin").toString()))
		{
			errores.add(forma.getFiltros("fechafin").toString(), new ActionMessage("errors.required","La fecha final del contrato "));
		}
		if (UtilidadTexto.isEmpty(forma.getFiltros("fechaini").toString()))
		{
			errores.add(forma.getFiltros("fechaini").toString(), new ActionMessage("errors.required","La fecha inicial del contrato "));
		}
		if (!forma.getFiltros("fechafin").toString().isEmpty())
		{
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFiltros("fechafin").toString(), forma.getFiltros("fechaini").toString()))
			{
				errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Final "+forma.getFiltros("fechafin").toString(), "Inicio "+forma.getFiltros("fechaini").toString()));
			}
		}
		
		boolean sinInv=true;
		boolean sinServ=true;
		if (Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros")+"")>0)
		{
			for (int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());i++)
			{
				if (!forma.getAuxEsquemasInv("claseinventario_"+i).equals("eliminado"))
				{	
					sinInv=false;
				}	
			}
		}
		
		if (Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros")+"")>0)
		{
			for (int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());i++)
			{
				if (!forma.getAuxEsquemasServ("gruposervicio_"+i).equals("eliminado"))
				{
					sinServ=false;
				}	
			}
		}
		/**Cambio tarea 110481
		if (sinInv)
			errores.add("", new ActionMessage("errors.required","Al menos un tipo de esquema tarifario de Inventarios debe ser agregado al contrato "));
		if (sinServ)
			errores.add("", new ActionMessage("errors.required","Al menos un tipo de esquema tarifario de Procedimietos debe ser agregado al contrato "));
		**/
		
		if (!UtilidadTexto.isEmpty(forma.getFiltros("fechafin").toString()))
		{				
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFiltros("fechafin").toString()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final del contrato "+forma.getFiltros("fechafin").toString()));
				centinelaFecha=true;
			}
			
			if(centinelaFecha)
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFiltros("fechafin").toString(), UtilidadFecha.getFechaActual()))
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Final del contrato "+forma.getFiltros("fechafin").toString(), "Inicial del contrato "+UtilidadFecha.getFechaActual()));
			}
		}
    
		return errores;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGuardarCambios(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		
		ActionErrors errores = new ActionErrors();
		errores = validateGuardarCambios(forma);
		
		if (!errores.isEmpty())
			saveErrors(request, errores);
		else
		{
		/*if (IngresarModificarContratosEntidadesSubcontratadas.verificarTraslapeContratos(con,forma.getFiltros()))
		{
			errores.add("fechas", new ActionMessage("errors.contratoConvigenciaYaExistente",forma.getFiltros("fechaini"),forma.getFiltros("fechafin")));
			saveErrors(request, errores);
		}	
		else
		{*/
		
			boolean transaccionExitosa=true;
			UtilidadBD.iniciarTransaccion(con);
			//*************INSERCION ENCABEZADO
			HashMap datosInvEliminar=new HashMap();
			HashMap datosServEliminar=new HashMap();
			logger.info("VALOR DEL CONSECUTIVO DEL CONTRATO------->"+forma.getFiltros("consecutivo"));
			
			//Se carga el mapa de filtros
			forma.setFiltros("codigoentidadantiguo",forma.getFiltros("codigoentidad"));
			forma.setFiltros("consecutivoantiguo",forma.getFiltros("consecutivo"));
			forma.setFiltros("nrocontratoantiguo",forma.getFiltros("nrocontrato"));
			forma.setFiltros("valorcontratoantiguo",forma.getFiltros("valorcontrato"));
			forma.setFiltros("fechafinantiguo",forma.getFiltros("fechafin"));
			forma.setFiltros("fechafirmaantiguo",forma.getFiltros("fechafirma"));
			forma.setFiltros("observacionesantiguo",forma.getFiltros("observaciones"));
			forma.setFiltros("fechamodificaantiguo",forma.getFiltros("fechamodifica"));
			forma.setFiltros("horamodificaantiguo",forma.getFiltros("horamodifica"));
			forma.setFiltros("usuariomodificaantiguo",forma.getFiltros("usuariomodifica"));
			//forma.setFiltros("tipoTarifa",forma.getFiltros("tipoTarifa"));
			forma.setFiltros("tipoTarifaantiguo",forma.getFiltros("tipoTarifa"));
			logger.info("entra a accionGuardarCambios y Tipo Tarifa= "+forma.getFiltros("tipoTarifa"));
			
			if (IngresarModificarContratosEntidadesSubcontratadas.actualizarEncabezado(con, forma.getFiltros()))
			{
				transaccionExitosa=true;
			} else {
				transaccionExitosa = false;
			}
			
		/*if(forma.getFiltros("tipoTarifa").equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
		{*/	
			//*************ELIMINACIONES
			
			for (int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());i++)
			{
				if (forma.getAuxEsquemasInv("nuevo_"+i).toString().equals(ConstantesBD.acronimoNo)
					&& forma.getAuxEsquemasInv("claseinventario_"+i).toString().equals("eliminado"))
				{
					datosInvEliminar.put("consecutivo",forma.getAuxEsquemasInv("consecutivo_"+i).toString());
					datosInvEliminar.put("fechainactiva",UtilidadFecha.getFechaActual());
					datosInvEliminar.put("horainactiva",UtilidadFecha.getHoraActual());
					datosInvEliminar.put("usuarioinactiva",usuario.getLoginUsuario());
					if(!IngresarModificarContratosEntidadesSubcontratadas.eliminarEsquemasInventarios(con,datosInvEliminar))
						transaccionExitosa = false;
				}
			}
			
			for (int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());i++)
			{
				if (forma.getAuxEsquemasServ("nuevo_"+i).toString().equals(ConstantesBD.acronimoNo)
					&& forma.getAuxEsquemasServ("gruposervicio_"+i).toString().equals("eliminado"))
				{
					datosServEliminar.put("consecutivo",forma.getAuxEsquemasServ("consecutivo_"+i).toString());
					datosServEliminar.put("fechainactiva",UtilidadFecha.getFechaActual());
					datosServEliminar.put("horainactiva",UtilidadFecha.getHoraActual());
					datosServEliminar.put("usuarioinactiva",usuario.getLoginUsuario());
					if(!IngresarModificarContratosEntidadesSubcontratadas.eliminarEsquemasServicios(con,datosServEliminar))
						transaccionExitosa = false;
				}
			}
			//***********ACTUALIZACIONES
			HashMap datosActualizar= new HashMap();
			for (int i=0; i<Utilidades.convertirAEntero(forma.getAuxModificacionServicios("numRegistros").toString());i++)
			{
				datosActualizar.put("consecutivo",forma.getAuxEsquemasServ("consecutivo_"+i).toString());
				datosActualizar.put("fechainactiva",UtilidadFecha.getFechaActual());
				datosActualizar.put("horainactiva",UtilidadFecha.getHoraActual());
				datosActualizar.put("usuarioinactiva",usuario.getLoginUsuario());
				datosActualizar.put("consecutivo", forma.getAuxModificacionServicios("consecutivo_"+i).toString());
				if(!IngresarModificarContratosEntidadesSubcontratadas.eliminarEsquemasServicios(con, datosActualizar))
					transaccionExitosa = false;
			}
			
			//************INSERCIONES
			HashMap auxiliarInv= new HashMap<String, Object>();
			HashMap auxiliarServ= new HashMap<String, Object>();
			
			for(int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());i++)
			{
				if (!forma.getAuxEsquemasInv("claseinventario_"+i).equals("eliminado")&&
						forma.getAuxEsquemasInv("nuevo_"+i).toString().equals(ConstantesBD.acronimoSi) )
				{
					auxiliarInv.put("claseinventario",forma.getAuxEsquemasInv("claseinventario_"+i).toString());
					auxiliarInv.put("esquematarinv",forma.getAuxEsquemasInv("esquematarinv_"+i).toString());
					auxiliarInv.put("fechavigclaseinv",forma.getAuxEsquemasInv("fechavigclaseinv_"+i).toString());
					auxiliarInv.put("contratoentidad",forma.getFiltros("consecutivo").toString());
					auxiliarInv.put("fechamodifica", UtilidadFecha.getFechaActual());
					auxiliarInv.put("horamodifica", UtilidadFecha.getHoraActual());
					auxiliarInv.put("usuariomodifica", usuario.getLoginUsuario());
					auxiliarInv.put("activo", ConstantesBD.acronimoSi);
					if(!IngresarModificarContratosEntidadesSubcontratadas.guardarEsquemasInventarios(con,auxiliarInv))
						transaccionExitosa = false;
					forma.setAuxEsquemasInv("nuevo_"+i,ConstantesBD.acronimoNo);
					forma.setAuxEsquemasInv("consecutivo_"+i,UtilidadBD.obtenerUltimoValorSecuencia(con, "facturacion.seq_tar_inv_con_ent_sub"));
					
				}
			}
			for(int i=0;i<Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());i++)
			{
				if (!forma.getAuxEsquemasServ("gruposervicio_"+i).equals("eliminado")&&
						forma.getAuxEsquemasServ("nuevo_"+i).toString().equals(ConstantesBD.acronimoSi))
				{			
					auxiliarServ.put("gruposervicio",forma.getAuxEsquemasServ("gruposervicio_"+i).toString());
					auxiliarServ.put("esquematarser",forma.getAuxEsquemasServ("esquematarser_"+i).toString());
					auxiliarServ.put("fechvigproc",forma.getAuxEsquemasServ("fechvigproc_"+i).toString());
					auxiliarServ.put("contratoentidad",forma.getFiltros("consecutivo").toString());
					auxiliarServ.put("fechamodifica", UtilidadFecha.getFechaActual());
					auxiliarServ.put("horamodifica", UtilidadFecha.getHoraActual());
					auxiliarServ.put("usuariomodifica", usuario.getLoginUsuario());
					auxiliarServ.put("activo", ConstantesBD.acronimoSi);
					if(!IngresarModificarContratosEntidadesSubcontratadas.guardarEsquemasServicios(con,auxiliarServ))
						transaccionExitosa = false;
					forma.setAuxEsquemasServ("nuevo_"+i,ConstantesBD.acronimoNo);
					forma.setAuxEsquemasServ("consecutivo_"+i,UtilidadBD.obtenerUltimoValorSecuencia(con, "facturacion.seq_tar_proc_con_ent_sub"));
				}
			}
		//}	
			if(transaccionExitosa){
				UtilidadBD.finalizarTransaccion(con);
				forma.setMensaje("EL CONTRATO FUE MODIFICADO EXITOSAMENTE");
			} else {
				UtilidadBD.abortarTransaccion(con);
				forma.setMensaje("ERROR AL TRATAR DE MODIFICAR EL CONTRATO");
			}
		//}	
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarInventarioMod(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());
		int numregsServicios=Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());
		int noHayServicio=0;
		int soloUno=0;
		ActionErrors errores = new ActionErrors();
		
		for(int j=0;j<numregsServicios;j++)
		{
			if (forma.getAuxEsquemasServ("gruposervicio_"+j).equals("eliminado"))
				noHayServicio++;
		}
		
		for (int k=0;k<numRegistros;k++)
		{
			if (!forma.getAuxEsquemasInv("claseinventario_"+k).equals("eliminado"))
				soloUno++;
		}
			
		logger.info("noHayServicio-->"+noHayServicio);
		logger.info("numregsServicios-->");
		logger.info("soloUno-->"+soloUno);
		logger.info("numRegistros-->"+numRegistros);
		logger.info(noHayServicio+"=="+numregsServicios+"||"+numregsServicios+"&&"+soloUno);
		
		if (((noHayServicio==numregsServicios)||(numregsServicios==0))&&soloUno==1)
		{
			errores.add("",new ActionMessage("errors.notEspecific", "No se puede eliminar el esquema. El contrato debe poseer como mínimo un esquema tarifario."));
			saveErrors(request, errores);
		}
		else
		{
			for (int i=0;i<numRegistros;i++)
			{	
				if (i==forma.getIndice())
				{
					forma.setAuxEsquemasInv("claseinventario_"+i,"eliminado");
					forma.setAuxEsquemasInv("esquematarinv_"+i,"eliminado");
					forma.setAuxEsquemasInv("fechavigclaseinv_"+i,"eliminado");
					forma.setAuxEsquemasInv("nombreinventario_"+i,"eliminado");
					forma.setAuxEsquemasInv("nombreesquema_"+i,"eliminado");
				}
			}
		}
		Utilidades.imprimirMapa(forma.getAuxEsquemasInv());
		
		forma.setEstado("adicionarInvMod");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarServicioMod(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		int numRegistros= Utilidades.convertirAEntero(forma.getAuxEsquemasServ("numRegistros").toString());
		int numregsInventarios=Utilidades.convertirAEntero(forma.getAuxEsquemasInv("numRegistros").toString());
		int noHayInventario=0;
		int soloUno=0;
		ActionErrors errores = new ActionErrors();
		
		for(int j=0;j<numregsInventarios;j++)
		{
			if (forma.getAuxEsquemasInv("claseinventario_"+j).equals("eliminado"))
				noHayInventario++;
		}
		
		for (int k=0;k<numRegistros;k++)
		{
			if (!forma.getAuxEsquemasServ("gruposervicio_"+k).equals("eliminado"))
				soloUno++;
		}
			
		logger.info("noHayInventario-->"+noHayInventario);
		logger.info("numregsInventarios-->"+(numregsInventarios));
		logger.info("soloUno-->"+soloUno);
		logger.info("numRegistros-->"+numRegistros);
		logger.info(noHayInventario+"=="+numregsInventarios+"||"+numregsInventarios+"&&"+soloUno);
		
		if (((noHayInventario==numregsInventarios)||(numregsInventarios==0))&&soloUno==1)
		{
			errores.add("",new ActionMessage("errors.notEspecific", "No se puede eliminar el esquema. El contrato debe poseer como mínimo un esquema tarifario."));
			saveErrors(request, errores);
		}
		else
		{
			for (int i=0;i<numRegistros;i++)
			{	
				if (i==forma.getIndice())
				{
					forma.setAuxEsquemasServ("gruposervicio_"+i,"eliminado");
					forma.setAuxEsquemasServ("esquematarser_"+i,"eliminado");
					forma.setAuxEsquemasServ("fechvigproc_"+i,"eliminado");
					forma.setAuxEsquemasServ("nombreservicioserv_"+i,"eliminado");
					forma.setAuxEsquemasServ("nombreesquemaserv_"+i,"eliminado");
				}
			}
		}
		
		
		Utilidades.imprimirMapa(forma.getAuxEsquemasServ());
		
		forma.setEstado("adicionarServMod");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificarInventario(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		
		logger.info("EL VALOR DEL INDICE--------->"+forma.getIndice());
		int i= forma.getIndice();
		//Seteo la vista con los valores actuales
		forma.setInfoEntidadInvAux("claseinventario",forma.getAuxEsquemasInv("claseinventario_"+i));
		forma.setInfoEntidadInvAux("esquematarinv",forma.getAuxEsquemasInv("esquematarinv_"+i));
		forma.setInfoEntidadInvAux("fechavigclaseinv",forma.getAuxEsquemasInv("fechavigclaseinv_"+i));
		forma.setInfoEntidadInvAux("consecutivo",forma.getAuxEsquemasInv("consecutivo_"+i));
		
		
		//Guardo de manera auxiliar los consecutivos de los registros que fueron modificados
		//Si el registro Ya se encuentra en BD se guarda registro para modificarlo posteriormente
		/*if (forma.getAuxEsquemasServ("nuevo_"+forma.getIndice()).toString().equals(ConstantesBD.acronimoNo))
		{
			int numRegistros=Utilidades.convertirAEntero(forma.getAuxModificacionInventarios("numRegistros").toString());
			forma.setAuxModificacionInventarios("consecutivo_"+numRegistros,forma.getAuxEsquemasInv("consecutivo_"+forma.getIndice()));
			forma.setAuxModificacionInventarios("numRegistros", numRegistros+1);
		}
		if (forma.getAuxEsquemasServ("nuevo_"+forma.getIndice()).toString().equals(ConstantesBD.acronimoNo))
		{
			
		}*/
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificarServicio(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		
		logger.info("EL VALOR DEL INDICE--------->"+forma.getIndice());
		//Seteo la vista con los valores actuales
		forma.setInfoEntidadServAux("gruposervicio",forma.getAuxEsquemasServ("gruposervicio_"+forma.getIndice()));
		forma.setInfoEntidadServAux("esquematarser",forma.getAuxEsquemasServ("esquematarser_"+forma.getIndice()));
		forma.setInfoEntidadServAux("fechvigproc",forma.getAuxEsquemasServ("fechvigproc_"+forma.getIndice()));
		forma.setInfoEntidadServAux("consecutivo",forma.getAuxEsquemasServ("consecutivo_"+forma.getIndice()));
		
		
		//Guardo de manera auxiliar los consecutivos de los registros que fueron modificados
		//Si el registro Ya se encuentra en BD se guarda registro para modificarlo posteriormente
		if (forma.getAuxEsquemasServ("nuevo_"+forma.getIndice()).toString().equals(ConstantesBD.acronimoNo))
		{
			int numRegistros=Utilidades.convertirAEntero(forma.getAuxModificacionServicios("numRegistros").toString());
			forma.setAuxModificacionServicios("consecutivo_"+numRegistros,forma.getAuxEsquemasServ("consecutivo_"+forma.getIndice()));
			forma.setAuxModificacionServicios("numRegistros", numRegistros+1);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarServicio(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		
		//Asigno los nuevos valores a mostrar el findForward de MODIFICAR en el mapa auxiliar
		int indice=forma.getIndice();
		forma.setIndice(forma.getIndiceMod());
		
		logger.info("EL VALOR DEL INDICE--------->"+indice);
		logger.info("CONS. CONTRATO: "+forma.getFiltros("consecutivo"));

		forma.setAuxEsquemasServ("gruposervicio_"+indice,forma.getInfoEntidadServ("gruposervicio")+"");
		forma.setAuxEsquemasServ("nombreservicioserv_"+indice,forma.getNombreServicio(forma.getInfoEntidadServ("gruposervicio")+""));
		forma.setAuxEsquemasServ("esquematarser_"+indice,forma.getInfoEntidadServ("esquematarser")+"");
		forma.setAuxEsquemasServ("nombreesquemaserv_"+indice,forma.getNombreEsquemaServ(forma.getInfoEntidadServ("esquematarser")+""));
		forma.setAuxEsquemasServ("fechvigproc_"+indice,forma.getInfoEntidadServ("fechvigproc")+"");
		forma.setAuxEsquemasServ("consecutivo_"+indice,forma.getInfoEntidadServ("consecutivo")+"");
		forma.setAuxEsquemasServ("nuevo_"+indice,ConstantesBD.acronimoSi);

		logger.info("\n\nmapa aux esquema serv--------->"+forma.getAuxEsquemasServ());
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarInventario(Connection con, IngresarModificarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		
		//Asigno los nuevos valores a mostrar el findForward de MODIFICAR en el mapa auxiliar
		int indice=forma.getIndice();
		forma.setIndice(forma.getIndiceMod());
		
		logger.info("EL VALOR DEL INDICE--------->"+indice+"\n\nmapa 1:::::::: "+forma.getInfoEntidadInvAux()+"\n\nmapa 2:::::: "+forma.getAuxEsquemasInv());
		
		forma.setAuxEsquemasInv("gruposervicio_"+indice,forma.getInfoEntidadInvAux("claseinventario")+"");
		forma.setAuxEsquemasInv("nombreservicioserv_"+indice,forma.getNombreInventario(forma.getInfoEntidadInvAux("claseinventario")+""));
		forma.setAuxEsquemasInv("esquematarser_"+indice,forma.getInfoEntidadInvAux("esquematarinv")+"");
		forma.setAuxEsquemasInv("nombreesquemaserv_"+indice,forma.getNombreEsquemaInv(forma.getInfoEntidadInvAux("esquematarinv")+""));
		forma.setAuxEsquemasInv("fechvigproc_"+indice,forma.getInfoEntidadInvAux("fechavigclaseinv")+"");
		forma.setAuxEsquemasInv("consecutivo_"+indice,forma.getInfoEntidadInvAux("consecutivo")+"");
		forma.setAuxEsquemasInv("nuevo_"+indice,ConstantesBD.acronimoSi);
		forma.setAuxEsquemasInv("fechavigclaseinv_"+indice, forma.getInfoEntidadInvAux("fechavigclaseinv"));
		forma.setAuxEsquemasInv("esquematarinv_"+indice, forma.getInfoEntidadInvAux("esquematarinv"));
		forma.setAuxEsquemasInv("claseinventario_"+indice, forma.getInfoEntidadInvAux("claseinventario"));
		forma.setAuxEsquemasInv("nombreinventario_"+indice, forma.getNombreInventario(forma.getInfoEntidadInvAux("claseinventario")+""));
		
		logger.info("\n\nmapa modificado:::::: "+forma.getAuxEsquemasInv());
		
		logger.info("CONS. CONTRATO: "+forma.getFiltros("consecutivo"));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}
}