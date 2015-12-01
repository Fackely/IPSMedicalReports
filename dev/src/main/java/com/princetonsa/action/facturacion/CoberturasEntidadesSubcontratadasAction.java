package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

import util.ConstantesBD;
import util.InfoDatosString;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.CoberturasEntidadesSubcontratadasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.CoberturasEntidadesSubcontratadas;


public class CoberturasEntidadesSubcontratadasAction extends Action{
	
	private static final String String = null;
	private Logger logger = Logger.getLogger(CoberturasEntidadesSubcontratadasAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{
			if(form instanceof CoberturasEntidadesSubcontratadasForm)
			{


				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				CoberturasEntidadesSubcontratadasForm forma = (CoberturasEntidadesSubcontratadasForm)form;		

				//se instancia el mundo
				CoberturasEntidadesSubcontratadas mundo = new CoberturasEntidadesSubcontratadas();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.


				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE COBERTURAS ENTIDADES SUBCONTRATADAS ES ====>> "+estado);
				logger.info("\n***************************************************************************");


				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");

					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(con, forma, mundo, mapping, usuario,request);					   			
				}	
				else if(estado.equals("empezarConsulta"))
				{
					return this.accionEmpezarConsulta(con, forma, mundo, mapping, usuario,request);					   			
				}
				else if(estado.equals("filtroContratos"))
				{
					return accionFiltrarContratos(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("filtroContratosCon"))
				{
					return accionFiltrarContratosCon(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("buscarRegistros"))
				{
					return this.accionBuscarRegistros(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("buscarRegistrosCon"))
				{
					return this.accionBuscarRegistrosCon(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("nuevoRegistroEx"))
				{
					forma.setViaIngresoSel("");
					forma.setTipoPacienteSel("");
					forma.setNaturalezaSel("-1");
					forma.setConsecutivoExCoberSel("");
					return mapping.findForward("principal");
				}
				else if(estado.equals("filtroTipoPaciente"))
				{
					return accionFiltrarTipoPaciente(con, forma, mapping,response);
				}
				else if(estado.equals("guardarNuevo"))
				{
					return this.accionGuardarNuevo(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("nuevoRegistroCobertura"))
				{				
					forma.setNuevaCoberPriMap("cobertura", "");
					forma.setNuevaCoberPriMap("prioridad", "");
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarNuevoRegCobertura"))
				{
					return this.accionGuardarRegCobertura(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarRegistroCobertura"))
				{
					return this.accionElimRegCobertura(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarRegistroExCobertura"))
				{
					return this.accionElimRegExCobertura(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("detalleExCobertura"))
				{
					return this.accionDetalleExCobertura(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("detalleExCoberturaCon"))
				{
					return this.accionDetalleExCoberturaCon(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("filtroGrupoInventario"))
				{
					return this.accionFiltrarGrupoInventario(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("filtroSubGrupoInventario"))
				{
					return accionFiltrarSubGrupoInventario(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("asignarPrioridad"))
				{
					return accionAsignarPrioridad(con, forma, mundo, mapping, usuario,request);
				}
				else if (estado.equals("buscarServicio")) 
				{			
					return this.accionBuscarServicio(con, forma, mundo, mapping, usuario,request);
				}
				else if (estado.equals("buscarArticulo")) 
				{			
					return this.accionBuscarArticulo(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("nuevoArticulo"))
				{
					forma.setClaseInventarioSel("");
					forma.setGrupoInventarioSel("");
					forma.setSubGrupoInventarioSel("");
					forma.setNaturalezaArtiSel("");
					forma.setConsecutivoArtiAgruSel("");
					forma.setCheckAA("S");
					return mapping.findForward("detalleExcepciones");
				}
				else if(estado.equals("nuevoServicio"))
				{
					forma.setTipoPos("");
					forma.setPosSubsidiado("");
					forma.setGrupoServicioSel("");
					forma.setTipoServicioSel("");
					forma.setEspecialidadSel("");
					forma.setConsecutivoServAgruSel("");
					forma.setCheckAS("S");
					return mapping.findForward("detalleExcepciones");
				}
				else if(estado.equals("guardarArticulo"))
				{
					return this.accionGuardarArticulo(con, forma, mundo, mapping, usuario,request);
				}		
				else if(estado.equals("guardarServicio"))
				{
					return this.accionGuardarServicio(con, forma, mundo, mapping, usuario,request);
				}	
				else if(estado.equals("modificarRegCoberturas"))
				{
					int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
					forma.setCoberturaSel(forma.getCoberturasEntiSubMap("cobertura_"+n)+"");
					forma.setPrioridadSel(forma.getCoberturasEntiSubMap("prioridad_"+n)+"");
					forma.setConsecutivoCoberSel(forma.getCoberturasEntiSubMap("consecutivo_"+n)+"");

					return mapping.findForward("principal");
				}
				else if(estado.equals("modificarRegistroExCobertura"))
				{
					return this.accionModificarRegistroExCobertura(con, forma, mundo, mapping, usuario,request);				
				}
				else if(estado.equals("guardarModificacionCobertura"))
				{
					return this.accionGuardarModificacionCobertura(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("guardarModiExCober"))
				{
					return this.accionGuardarModiExCober(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarArtiAgru"))
				{
					return this.accionEliminarArtiAgru(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarServAgru"))
				{
					return this.accionEliminarServAgru(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("modificarArtiAgru"))
				{
					return this.accionModificarArtiAgru(con, forma, mundo, mapping, usuario,request);			
				}
				else if(estado.equals("guardarModiArtiAgru"))
				{
					return this.accionGuardarModiArtiAgru(con, forma, mundo, mapping, usuario,request);		
				}
				else if(estado.equals("modificarServAgru"))
				{
					return this.accionModificarServAgru(con, forma, mundo, mapping, usuario,request);	
				}
				else if(estado.equals("guardarModiServAgru"))
				{
					return this.accionGuardarModiServAgru(con, forma, mundo, mapping, usuario,request);
				}
				else if (estado.equals("modificarServEsp"))
				{
					return this.accionModificarServEsp(con, forma, mundo, mapping, usuario,request);	
				}
				else if(estado.equals("guardarServEsp"))
				{
					return this.accionGuardarServEsp(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("guardarModiServEsp"))
				{
					return this.accionGuardarModiServEsp(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarServEsp"))
				{
					return this.accionEliminarServEsp(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("guardarArtEsp"))
				{
					return this.accionGuardarArtEsp(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("modificarArtiEsp"))
				{
					return this.accionModificarArtiEsp(con, forma, mundo, mapping, usuario,request);	
				}
				else if(estado.equals("guardarModiArtEsp"))
				{
					return this.accionGuardarModiArtiEsp(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarArtiEsp"))
				{
					return this.accionEliminarArtiEsp(con, forma, mundo, mapping, usuario,request);
				}
				else if (estado.equals("ordenar"))
				{
					accionOrdenarMapa(forma, mundo);
					return mapping.findForward("principal");
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
     * Metodo para modificar una excepcion de cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionModificarRegistroExCobertura(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		if((forma.getExcepCobertura("viaIngreso_"+n)+"").equals(""))
			forma.setViaIngresoSel("-1");
		else
			forma.setViaIngresoSel(forma.getExcepCobertura("viaIngreso_"+n)+"");
		
		ArrayList<HashMap<String, Object>> tiposPaciente= new ArrayList<HashMap<String, Object>>();
		con = UtilidadBD.abrirConexion();		
		
		tiposPaciente= (Utilidades.obtenerTiposPacientePorViaIngreso(con, forma.getViaIngresoSel()+""));
		
		for(int i=0; i<tiposPaciente.size();i++)
		{	
		   HashMap mapaAux = (HashMap)tiposPaciente.get(i);
		   forma.setTipoPaciente("descripcion_"+i, mapaAux.get("nomtipopaciente"));		   
		   forma.setTipoPaciente("codigo_"+i, mapaAux.get("tipopaciente"));
		}		

		forma.setTipoPaciente("numRegistros", tiposPaciente.size());
		
		UtilidadBD.cerrarConexion(con);
					
		if((forma.getExcepCobertura("tipoPaciente_"+n)+"").equals(""))
			forma.setTipoPacienteSel("-1");
		else
			forma.setTipoPacienteSel(forma.getExcepCobertura("tipoPaciente_"+n)+"");
		if((forma.getExcepCobertura("naturaleza_"+n)+"").equals(""))
			forma.setNaturalezaSel("-1");
		else
			forma.setNaturalezaSel(forma.getExcepCobertura("naturaleza_"+n)+"");
		forma.setConsecutivoExCoberSel(forma.getExcepCobertura("consecutivo_"+n)+"");
					
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que ordena las columnas
	 * @return
	 */
	private void accionOrdenarMapa(CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo)
	{
		String[] indices = mundo.indicesCoberturasEntiSub;

		int numReg = Utilidades.convertirAEntero(forma.getExcepCobertura("numRegistros")+"");
		if (!forma.getExcepCobertura().get("numRegistros").equals("0"))
		{
			logger.info("\n\n\n********IMPRIMO EL MAPA*******"+forma.getExcepCobertura()+"\n\nPATRON ORDENAR----->"+forma.getPatronOrdenar());
			forma.setExcepCobertura(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getExcepCobertura(), numReg));
			forma.setUltimoPatron(forma.getPatronOrdenar());
			forma.setExcepCobertura("numRegistros",numReg+"");
			logger.info("\n\n\n********IMPRIMO EL MAPA*******"+forma.getExcepCobertura());
		}
		else
		{
			forma.setEstado("buscarRegistros");
		}
	}
	
	/**
	 * Método para asignar la prioridad segun la cobertura seleccionada
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionAsignarPrioridad(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		con = UtilidadBD.abrirConexion();
		HashMap coberturas = new HashMap();
		ActionErrors errores = new ActionErrors();	
		int prioridadAsignar=1;
		int l=0;
		
		forma.setCoberturaSel(forma.getNuevaCoberPriMap("cobertura")+"");		
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getCoberturasEntiSubMap("numRegistros")+""));i++)
		{			
			if((forma.getCoberturasEntiSubMap("activo_"+i)+"").equals(ConstantesBD.acronimoSi))
			{			
				if(Utilidades.convertirAEntero(forma.getCoberturasEntiSubMap("cobertura_"+i)+"") == Utilidades.convertirAEntero(forma.getCoberturaSel()))
					errores.add("descripcion",new ActionMessage("prompt.generico","La cobertura "+forma.getCoberturasEntiSubMap("desccobertura_"+i)+" ya fue seleccionada."));
				else
				{
					coberturas.put("prioridad_"+l, forma.getCoberturasEntiSubMap("prioridad_"+i));
					l++;
				}
			}
		}							
		
		for(int j=0;j<l;j++)
		{
			for(int k=j;k<l;k++)
			{
				int aux=0;
				if(Utilidades.convertirAEntero(coberturas.get("prioridad_"+j)+"") > Utilidades.convertirAEntero(coberturas.get("prioridad_"+k)+""))
				{
					aux=Utilidades.convertirAEntero(coberturas.get("prioridad_"+k)+"");
					coberturas.put("prioridad_"+k,Utilidades.convertirAEntero(coberturas.get("prioridad_"+j)+""));
					coberturas.put("prioridad_"+j,aux);
				}
			}
		}	
				
		if(!errores.isEmpty())
		{
			prioridadAsignar=0;
			forma.setNuevaCoberPriMap("cobertura", -1);
			saveErrors(request,errores);
		}
		else
		{
			if(l>0)
				prioridadAsignar=Utilidades.convertirAEntero(coberturas.get("prioridad_"+(l-1))+"")+1;
					
			forma.setPrioridadSel(prioridadAsignar+"");
			
			forma.setNuevaCoberPriMap("prioridad",prioridadAsignar+"");
		}
		
		forma.setEstado("nuevoRegistroCobertura");
		
		return mapping.findForward("principal");	
	}	

	
	/**
     * Metodo para eliminar un articulo especifico
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEliminarArtiEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroEliminar());
		if(Utilidades.convertirAEntero(forma.getDatosBusquedaArticulos("consecutivo_"+n)+"") > 0)
		{
			if(mundo.eliminarArtiEsp(Utilidades.convertirAEntero(forma.getDatosBusquedaArticulos("consecutivo_"+n)+""), usuario.getLoginUsuario()))
				forma.setDatosBusquedaArticulos("activo_"+n, ConstantesBD.acronimoNo);
		}
		else
			forma.setDatosBusquedaArticulos("activo_"+n, ConstantesBD.acronimoNo);
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un articulo especifico
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModiArtiEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		HashMap criterios= new HashMap();
				
		if(mundo.eliminarArtiEsp(Utilidades.convertirAEntero(forma.getDatosBusquedaArticulos("consecutivo_"+n)+""), usuario.getLoginUsuario()))
		{
			forma.setDatosBusquedaArticulos("activo_"+n, ConstantesBD.acronimoNo);
			criterios.put("CoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
			if(forma.getCheckAE().equals("Si") || forma.getCheckAE().equals("S"))
				criterios.put("incluye",ConstantesBD.acronimoSi);
			else
				criterios.put("incluye",ConstantesBD.acronimoNo);
			criterios.put("articulo", forma.getDatosBusquedaArticulos("codigoArticulo_"+n)+"");
			criterios.put("usuario", usuario.getLoginUsuario());
			criterios.put("activo", ConstantesBD.acronimoSi);
			if(mundo.guardarArtiEsp(criterios) > 0)
			{
				if(forma.getCheckAE().equals(ConstantesBD.acronimoSi))
					forma.setDatosBusquedaServicios("checkAE_"+n, "Si");
				else
					forma.setDatosBusquedaServicios("checkAE_"+n, "No");
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
				
		forma.setDatosBusquedaArticulos(mundo.consultaArtiEsp(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));		
		
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un articulo especifico agrupado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionModificarArtiEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
				
		forma.setCodigoArtiEsp(forma.getDatosBusquedaArticulos("codigoArticulo_"+n)+"");
		forma.setDescArtiEsp(forma.getDatosBusquedaArticulos("descripcionArticulo_"+n)+"");
		forma.setCheckAE(forma.getDatosBusquedaArticulos("checkAE_"+n)+"");
						
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un articulo especifico 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarArtEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		HashMap criterios= new HashMap();
		boolean temp=false;
		int conse=0;
		
		logger.info("\n\nmapa articulos:: "+forma.getDatosBusquedaArticulos());
		
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getDatosBusquedaArticulos("numRegistros")+""));i++)
		{
			if((forma.getDatosBusquedaArticulos("fueInsertado_"+i)+"").equals(ConstantesBD.acronimoNo)){
				criterios.put("CoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
				if((forma.getDatosBusquedaServicios("checkAE_"+i)+"").equals("Si"))
					criterios.put("incluye",ConstantesBD.acronimoSi);
				else
					criterios.put("incluye",ConstantesBD.acronimoNo);
				criterios.put("articulo", forma.getDatosBusquedaArticulos("codigoArticulo_"+i)+"");
				criterios.put("usuario", usuario.getLoginUsuario());
				criterios.put("activo", ConstantesBD.acronimoSi);
				conse= mundo.guardarArtiEsp(criterios);
				if(conse > 0)
				{
					temp=true;
					forma.setDatosBusquedaArticulos("fueInsertado_"+i,ConstantesBD.acronimoSi);
				}
				else
					temp=false;
				forma.setDatosBusquedaArticulos("consecutivo_"+i,conse);
			}			
		}
		
		if(temp)
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para eliminar un servicio especifico
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEliminarServEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroEliminar());
		if(Utilidades.convertirAEntero(forma.getDatosBusquedaServicios("consecutivo_"+n)+"") > 0)
		{
			if(mundo.eliminarServEsp(Utilidades.convertirAEntero(forma.getDatosBusquedaServicios("consecutivo_"+n)+""), usuario.getLoginUsuario()))
				forma.setDatosBusquedaServicios("activo_"+n, ConstantesBD.acronimoNo);
		}
		else
			forma.setDatosBusquedaServicios("activo_"+n,ConstantesBD.acronimoNo);
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un servicio especifico
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModiServEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		HashMap criterios= new HashMap();
					
		if(mundo.eliminarServEsp(Utilidades.convertirAEntero(forma.getDatosBusquedaServicios("consecutivo_"+n)+""), usuario.getLoginUsuario()))
		{			
			forma.setDatosBusquedaServicios("activo_"+n, ConstantesBD.acronimoNo);
			criterios.put("CoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
			if(forma.getCheckSE().equals("Si") || forma.getCheckSE().equals("S"))
				criterios.put("incluye",ConstantesBD.acronimoSi);
			else
				criterios.put("incluye",ConstantesBD.acronimoNo);
			criterios.put("servicio", forma.getDatosBusquedaServicios("codServicio_"+n)+"");
			criterios.put("usuario", usuario.getLoginUsuario());
			criterios.put("activo", ConstantesBD.acronimoSi);
			if(mundo.guardarServEsp(criterios) > 0)
			{
				if(forma.getCheckSE().equals(ConstantesBD.acronimoSi))
					forma.setDatosBusquedaServicios("checkSE_"+n, "Si");
				else
					forma.setDatosBusquedaServicios("checkSE_"+n, "No");
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
				
		forma.setDatosBusquedaServicios(mundo.consultaServEsp(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un servicio especifico
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionModificarServEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
				
		logger.info("\n\nN--------->"+n+"\n\nMAPA--------->"+forma.getDatosBusquedaServicios());
		
		forma.setCodigoServEsp(forma.getDatosBusquedaServicios("codServicio_"+n)+"");
		forma.setCodigoCupsServEsp(forma.getDatosBusquedaServicios("codigoCups_"+n)+"");
		forma.setServicioDescServEsp(forma.getDatosBusquedaServicios("servicioDesc_"+n)+"");
		forma.setCheckSE(forma.getDatosBusquedaServicios("checkSE_"+n)+"");
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un servicio especifico
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarServEsp(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		HashMap criterios= new HashMap();
		boolean temp=false;
		int conse=0;
		
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getDatosBusquedaServicios("numRegistros")+""));i++)
		{
			if((forma.getDatosBusquedaServicios("fueInsertado_"+i)+"").equals(ConstantesBD.acronimoNo)){
				criterios.put("CoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
				if((forma.getDatosBusquedaServicios("checkSE_"+i)+"").equals("Si"))
					criterios.put("incluye",ConstantesBD.acronimoSi);
				else
					criterios.put("incluye",ConstantesBD.acronimoNo);
				criterios.put("servicio", forma.getDatosBusquedaServicios("codServicio_"+i)+"");
				criterios.put("usuario", usuario.getLoginUsuario());
				criterios.put("activo", ConstantesBD.acronimoSi);
				conse= mundo.guardarServEsp(criterios);
				if(conse > 0)
				{
					temp=true;
					forma.setDatosBusquedaServicios("fueInsertado_"+i,ConstantesBD.acronimoSi);
					forma.setDatosBusquedaServicios("consecutivo_"+i,conse);
				}
				else
					temp=false;
			}			
		}
				
		if(temp)
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un servicio agrupado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModiServAgru(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		HashMap criterios= new HashMap();
		
		if(mundo.eliminarServAgru(Utilidades.convertirAEntero(forma.getServAgruMap("consecutivo_"+n)+""), usuario.getLoginUsuario()))
		{		
			criterios.put("exCoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
			criterios.put("pos", forma.getTipoPos());
			criterios.put("posSubsidiado", forma.getPosSubsidiado());
			criterios.put("grupo", forma.getGrupoServicioSel());
			criterios.put("tipoServicio", forma.getTipoServicioSel());
			criterios.put("especialidad", forma.getEspecialidadSel());
			criterios.put("incluye", forma.getCheckAS());
			criterios.put("usuario", usuario.getLoginUsuario());
			criterios.put("activo", ConstantesBD.acronimoSi);
											
			if(mundo.guardarAgruServExCober(criterios) > 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		forma.setServAgruMap(mundo.consultaAgruServEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un servicio agrupado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionModificarServAgru(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
				
		forma.setConsecutivoServAgruSel(forma.getServAgruMap("consecutivo_"+n)+"");
		forma.setTipoPos(forma.getServAgruMap("posAcronimo_"+n)+"");
		forma.setPosSubsidiado(forma.getServAgruMap("posSubsidiadoAcronimo_"+n)+"");
		forma.setGrupoServicioSel(forma.getServAgruMap("grupo_"+n)+"");
		forma.setTipoServicioSel(forma.getServAgruMap("tipoServicio_"+n)+"");
		forma.setEspecialidadSel(forma.getServAgruMap("especialidad_"+n)+"");
		forma.setCheckAS(forma.getServAgruMap("incluyeAcronimo_"+n)+"");
				
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un articulo agrupado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModiArtiAgru(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		HashMap criterios= new HashMap();
		
		if(mundo.eliminarArtiAgru(Utilidades.convertirAEntero(forma.getArtiAgruMap("consecutivo_"+n)+""), usuario.getLoginUsuario()))
		{
			criterios.put("exCoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
			criterios.put("claseInv", forma.getClaseInventarioSel());
			criterios.put("grupoInv", forma.getGrupoInventarioSel());
			criterios.put("naturaleza", forma.getNaturalezaArtiSel());
			criterios.put("subGrupoInv", forma.getSubGrupoInventarioSel());
			criterios.put("institucion", usuario.getCodigoInstitucionInt());
			criterios.put("incluye", forma.getCheckAA());
			criterios.put("usuario", usuario.getLoginUsuario());
			criterios.put("activo", ConstantesBD.acronimoSi);
											
			if(mundo.guardarAgruArtiExCober(criterios) > 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		forma.setArtiAgruMap(mundo.consultaAgruArtiEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		logger.info("\n\nMAPA ARTI AGRU------------------------>"+forma.getArtiAgruMap());
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar un articulo agrupado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionModificarArtiAgru(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		
		forma.setGrupoInventario(mundo.consultaGrupoInventario(forma.getArtiAgruMap("claseInventario_"+n)+""));
		forma.setSubGrupoInventario(mundo.consultaSubGrupoInventario(forma.getArtiAgruMap("grupoInventario_"+n)+"", forma.getArtiAgruMap("claseInventario_"+n)+""));
		
		forma.setClaseInventarioSel(forma.getArtiAgruMap("claseInventario_"+n)+"");
		forma.setGrupoInventarioSel(forma.getArtiAgruMap("grupoInventario_"+n)+"");
		forma.setSubGrupoInventarioSel(forma.getArtiAgruMap("subgrupoInventario_"+n)+"");
		forma.setNaturalezaArtiSel(forma.getArtiAgruMap("naturaleza_"+n)+"");
		forma.setCheckAA(forma.getArtiAgruMap("incluye_"+n)+"");
		forma.setConsecutivoArtiAgruSel(forma.getArtiAgruMap("consecutivo_"+n)+"");		
							
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para eliminar un servicio agrupado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEliminarServAgru(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroEliminar());
		mundo.eliminarServAgru(Utilidades.convertirAEntero(forma.getServAgruMap("consecutivo_"+n)+""), usuario.getLoginUsuario());
				
		forma.setServAgruMap(mundo.consultaAgruServEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para eliminar un articulo agrupado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEliminarArtiAgru(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getRegistroEliminar());
		mundo.eliminarArtiAgru(Utilidades.convertirAEntero(forma.getArtiAgruMap("consecutivo_"+n)+""), usuario.getLoginUsuario());
				
		forma.setArtiAgruMap(mundo.consultaAgruArtiEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para modificar una excepcion cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModiExCober(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		HashMap criterios= new HashMap();
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		int error=0;
		ActionErrors errores = new ActionErrors();	
		
		criterios.put("viaIngreso", Utilidades.convertirAEntero(forma.getViaIngresoSel()+""));
		criterios.put("tipoPaciente",forma.getTipoPacienteSel()+"");
		criterios.put("naturaleza", Utilidades.convertirAEntero(forma.getNaturalezaSel()+""));
		criterios.put("usuario", usuario.getLoginUsuario());		
		criterios.put("consecutivo", Utilidades.convertirAEntero(forma.getConsecutivoExCoberSel()+""));
				
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getExcepCobertura("numRegistros")+""));i++)
		{
			if((forma.getExcepCobertura("activo_"+i)+"").equals(ConstantesBD.acronimoSi))
			{
				if(Utilidades.convertirAEntero(forma.getExcepCobertura("viaIngreso_"+i)+"") == Utilidades.convertirAEntero(criterios.get("viaIngreso")+""))
				{
					if((forma.getExcepCobertura("tipoPaciente_"+i)+"").equals(criterios.get("tipoPaciente")+""))
					{
						if(Utilidades.convertirAEntero(forma.getExcepCobertura("naturaleza_"+i)+"") == Utilidades.convertirAEntero(criterios.get("naturaleza")+""))
							error=1;
					}
				}
			}
		}
		
		if(error != 0)
		{
			errores.add("descripcion",new ActionMessage("prompt.generico","La via Ingreso, el tipo Paciente y La naturaleza seleccionada ya fue asignada."));
			saveErrors(request,errores);
		}
		else
		{
								
			int conse= mundo.modificarRegExCober(criterios);
						
			criterios.put("CoberEnti",forma.getExcepCobertura("consecutivo_"+n)+"");
			if((forma.getExcepCobertura("viaIngreso_"+n)+"").equals(""))
				criterios.put("viaIngreso",-1);
			else
				criterios.put("viaIngreso",forma.getExcepCobertura("viaIngreso_"+n)+"");
			if((forma.getExcepCobertura("tipoPaciente_"+n)+"").equals(""))
				criterios.put("tipoPaciente",-1);
			else
				criterios.put("tipoPaciente",forma.getExcepCobertura("tipoPaciente_"+n)+"");
			if((forma.getExcepCobertura("naturaleza_"+n)+"").equals(""))
				criterios.put("naturaleza",-1);
			else
				criterios.put("naturaleza",forma.getExcepCobertura("naturaleza_"+n)+"");
			criterios.put("usuario",usuario.getLoginUsuario());
			
			if(conse > 0 && mundo.guardarRegExCoberturaLog(criterios))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
			criterios.put("numContrato", forma.getContratoXentSubSel());

			forma.setExcepCobertura(mundo.consultaExcCoberturas(criterios));	
		}
		
		return mapping.findForward("principal");
	}	
	
	/**
     * Metodo para modificar una cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModificacionCobertura(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		HashMap criterios= new HashMap();
		HashMap temp= new HashMap();
		int n= Utilidades.convertirAEntero(forma.getRegistroModificar());
		ActionErrors errores = new ActionErrors();		
		
		criterios.put("contratoEntidadSub", forma.getContratoXentSubSel());
		criterios.put("cobertura", forma.getCoberturaSel());
		criterios.put("institucion", usuario.getCodigoInstitucionInt());
		criterios.put("nroPrioridad", forma.getPrioridadSel());
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("activo", ConstantesBD.acronimoSi);
		criterios.put("consecutivo", Utilidades.convertirAEntero(forma.getConsecutivoCoberSel()));
		criterios.put("nuevo", "N");
		
		for(int j=0;j<(Utilidades.convertirAEntero(forma.getCoberturasEntiSubMap("numRegistros")+""));j++)
		{
			if((forma.getCoberturasEntiSubMap("activo_"+j)+"").equals(ConstantesBD.acronimoSi))
			{
					if(forma.getPrioridadSel().equals(forma.getCoberturasEntiSubMap("prioridad_"+j)+""))
					{
						
							if(j != n)
								errores.add("descripcion",new ActionMessage("prompt.generico","La prioridad "+forma.getPrioridadSel()+" ya fue asignada, para "+forma.getCoberturasEntiSubMap("desccobertura_"+j)));
					}	
			}
		}	
		
		if(!errores.isEmpty())
			saveErrors(request,errores);
		else
		{
			int conse= mundo.guardarRegistroCobertura(criterios);
			
			criterios.put("CoberEnti", forma.getCoberturasEntiSubMap("consecutivo_"+n)+"");
			criterios.put("cobertura", forma.getCoberturasEntiSubMap("cobertura_"+n)+"");
			criterios.put("institucion", usuario.getCodigoInstitucionInt());
			criterios.put("nroPrioridad", forma.getCoberturasEntiSubMap("prioridad_"+n)+"");
			criterios.put("usuario", usuario.getLoginUsuario());
						
			if(conse > 0 && mundo.guardarRegCoberturaLog(criterios))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		}
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
		
		return mapping.findForward("principal");
	}			
	
	/**
     * Metodo para ingresar al detalle de una excepcion de cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarServicio(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getServAgruMap("numRegistros")+"");
		HashMap criterios= new HashMap();
		boolean guardo=true;
		ActionErrors errores = new ActionErrors();
		String tipoPos="",posSub="";
		
		if(forma.getTipoPos().equals(""))
			forma.setServAgruMap("tipoPos_"+n, "todos");
		else
			forma.setServAgruMap("tipoPos_"+n, forma.getTipoPos());
		if(forma.getPosSubsidiado().equals(""))
			forma.setServAgruMap("posSubsidiado_"+n,"todos");
		else
			forma.setServAgruMap("posSubsidiado_"+n, forma.getPosSubsidiado());
		if(forma.getGrupoServicioSel().equals("-1"))
			forma.setServAgruMap("grupo_"+n,"");
		else
			forma.setServAgruMap("grupo_"+n, forma.getGrupoServicioSel());
		if(forma.getTipoServicioSel().equals("-1"))
			forma.setServAgruMap("tipo_"+n,"");
		else
			forma.setServAgruMap("tipo_"+n, forma.getTipoServicioSel());
		if(forma.getEspecialidadSel().equals("-1"))
			forma.setServAgruMap("especialidad_"+n,"");
		else
			forma.setServAgruMap("especialidad_"+n, forma.getEspecialidadSel());
		forma.setServAgruMap("incluye_"+n, forma.getCheckAS());	
		
		logger.info("\n\nmapa:: "+forma.getServAgruMap());
		
		for(int i=0; i<(Utilidades.convertirAEntero(forma.getServAgruMap("numRegistros")+""));i++)
		{			
			if((forma.getServAgruMap("pos_"+i)+"").equals("Si"))
				tipoPos="S";
			else if((forma.getServAgruMap("pos_"+i)+"").equals("No"))
				tipoPos="N";
			else if((forma.getServAgruMap("pos_"+i)+"").equals(""))
				tipoPos="todos";
	
			if((forma.getServAgruMap("posSubsidiado_"+i)+"").equals("Si"))
				posSub="S";
			else if((forma.getServAgruMap("posSubsidiado_"+i)+"").equals("No"))
				posSub="N";
			else if((forma.getServAgruMap("posSubsidiado_"+i)+"").equals(""))
				posSub="todos";
			
			String especialidad= Utilidades.convertirAEntero(forma.getEspecialidadSel())>0?forma.getEspecialidadSel():""; 
			
			if((forma.getServAgruMap("activo_"+i)+"").equals("Si"))
			{
				logger.info("i------------------------->"+i);
				logger.info("tipoPos->"+tipoPos+"   "+forma.getServAgruMap("tipoPos_"+n)+"");
				logger.info("posSubsidiado->"+posSub+"  "+forma.getServAgruMap("posSubsidiado_"+n)+"");
				logger.info("grupo->"+(forma.getServAgruMap("grupo_"+i)+"")+"  "+forma.getServAgruMap("grupo_"+n)+"");
				logger.info("tipoServicio->"+(forma.getServAgruMap("tipoServicio_"+i)+"")+"  "+forma.getServAgruMap("tipo_"+n)+"");
				logger.info("Especialdiad->"+(forma.getServAgruMap("especialidad_"+i)+"")+"  "+(especialidad));
				
				if(tipoPos.equals(forma.getServAgruMap("tipoPos_"+n)+"") && posSub.equals(forma.getServAgruMap("posSubsidiado_"+n)+"") && (forma.getServAgruMap("grupo_"+i)+"").equals(forma.getServAgruMap("grupo_"+n)+"") && (forma.getServAgruMap("tipoServicio_"+i)+"").equals(forma.getServAgruMap("tipo_"+n)+"") && (forma.getServAgruMap("especialidad_"+i)+"").equals(especialidad))				
					guardo= false;
			}
		}
				
		if (!guardo)
		{
			errores.add("descripcion",new ActionMessage("prompt.generico","El registro ya fue parametrizado."));
			saveErrors(request,errores);
		}
		else
		{
			criterios.put("exCoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
			criterios.put("pos", forma.getServAgruMap("tipoPos_"+n));
			criterios.put("posSubsidiado", forma.getServAgruMap("posSubsidiado_"+n));
			criterios.put("grupo", forma.getServAgruMap("grupo_"+n));
			criterios.put("tipoServicio", forma.getServAgruMap("tipo_"+n));
			criterios.put("especialidad", forma.getServAgruMap("especialidad_"+n));
			criterios.put("incluye", forma.getServAgruMap("incluye_"+n));
			criterios.put("usuario", usuario.getLoginUsuario());
			criterios.put("activo", ConstantesBD.acronimoSi);					
			
			
			forma.setServAgruMap("numRegistros", n+1);
			
			if(mundo.guardarAgruServExCober(criterios) > 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			forma.setServAgruMap(mundo.consultaAgruServEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		}
			
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para ingresar al detalle de una excepcion de cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarArticulo (Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n= Utilidades.convertirAEntero(forma.getArtiAgruMap("numRegistros")+"");
		HashMap criterios= new HashMap();
		boolean guardo=true;
		ActionErrors errores = new ActionErrors();
				
		forma.setArtiAgruMap("claseInv_"+n, forma.getClaseInventarioSel());
		forma.setArtiAgruMap("grupoInv_"+n, forma.getGrupoInventarioSel());
		forma.setArtiAgruMap("subGrupoInv_"+n, forma.getSubGrupoInventarioSel());
		forma.setArtiAgruMap("naturaleza_"+n, forma.getNaturalezaArtiSel());
		forma.setArtiAgruMap("incluye_"+n, forma.getCheckAA());	
		
		logger.info("\n\nmapa:: "+forma.getArtiAgruMap());
		
		for(int i=0; i<(Utilidades.convertirAEntero(forma.getArtiAgruMap("numRegistros")+""));i++)
		{		
			if((forma.getArtiAgruMap("activo_"+i)+"").equals("Si"))
			{				
				if(Utilidades.convertirAEntero(forma.getArtiAgruMap("claseInventario_"+i)+"")  == Utilidades.convertirAEntero(forma.getArtiAgruMap("claseInv_"+n)+"") 
					&& Utilidades.convertirAEntero(forma.getArtiAgruMap("grupoInventario_"+i)+"")  == Utilidades.convertirAEntero(forma.getArtiAgruMap("grupoInv_"+n)+"") 
					&& Utilidades.convertirAEntero(forma.getArtiAgruMap("subgrupoInventario_"+i)+"")  == Utilidades.convertirAEntero(forma.getArtiAgruMap("subGrupoInv_"+n)+"") 
					&& Utilidades.convertirAEntero(forma.getArtiAgruMap("naturaleza_"+i)+"")  == Utilidades.convertirAEntero(forma.getArtiAgruMap("naturaleza_"+n)+""))
					guardo= false;
			}
		}
		
		if(!guardo)
		{
			errores.add("descripcion",new ActionMessage("prompt.generico","El registro ya fue parametrizado."));
			saveErrors(request,errores);
		}
		else
		{
			
			criterios.put("exCoberEnti", forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura()));
			criterios.put("claseInv", forma.getArtiAgruMap("claseInv_"+n));
			criterios.put("grupoInv", forma.getArtiAgruMap("grupoInv_"+n));
			criterios.put("naturaleza", forma.getArtiAgruMap("naturaleza_"+n));
			criterios.put("subGrupoInv", forma.getArtiAgruMap("subGrupoInv_"+n));
			criterios.put("institucion", usuario.getCodigoInstitucionInt());
			criterios.put("incluye", forma.getArtiAgruMap("incluye_"+n));
			criterios.put("usuario", usuario.getLoginUsuario());
			criterios.put("activo", ConstantesBD.acronimoSi);
										
			forma.setArtiAgruMap("numRegistros", n+1);
			
			if(mundo.guardarAgruArtiExCober(criterios) > 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
					
			forma.setArtiAgruMap(mundo.consultaAgruArtiEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));			
		}		
				
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para ingresar al detalle de una excepcion de cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscarArticulo(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		ActionErrors errores = new ActionErrors();
		int n= Utilidades.convertirAEntero(forma.getDatosBusquedaArticulos("numRegistros")+"");
								
		logger.info("\n\narticulo::"+forma.getArticulo()+"   descripcion::"+forma.getNombreArticulo());
		
		for(int l=0;l<n;l++)
		{			
			if((forma.getDatosBusquedaArticulos("activo_"+l)+"").equals(ConstantesBD.acronimoSi))
			{
				if(Utilidades.convertirAEntero(forma.getDatosBusquedaArticulos("codigoArticulo_"+l)+"")== Utilidades.convertirAEntero(forma.getArticulo()))
					errores.add("descripcion",new ActionMessage("prompt.generico","El Articulo ya fue Seleccionado."));					
			}
		}
		
		if(!errores.isEmpty())		
			saveErrors(request,errores);
		else
		{
			forma.setDatosBusquedaArticulos("codigoArticulo_"+n, forma.getArticulo());
			forma.setDatosBusquedaArticulos("descripcionArticulo_"+n, forma.getNombreArticulo());
			forma.setDatosBusquedaArticulos("fueInsertado_"+n, ConstantesBD.acronimoNo);
			forma.setDatosBusquedaArticulos("checkAE_"+n, "No");
			forma.setDatosBusquedaArticulos("activo_"+n, "S");
			forma.setDatosBusquedaArticulos("numRegistros", n+1);
		}
		
		return mapping.findForward("detalleExcepciones");
	}
	
	
	/**
     * Metodo para ingresar al detalle de una excepcion de cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscarServicio(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{	
		ActionErrors errores = new ActionErrors();
		int n= Utilidades.convertirAEntero(forma.getDatosBusquedaServicios("numRegistros")+"");
						
		for(int l=0;l<n;l++)
		{
			if((forma.getDatosBusquedaServicios("activo_"+l)+"").equals(ConstantesBD.acronimoSi))
			{
				if(Utilidades.convertirAEntero(forma.getDatosBusquedaServicios("codServicio_"+l)+"")== Utilidades.convertirAEntero(forma.getDatosBusquedaServicios("codServicio")+""))				
					errores.add("descripcion",new ActionMessage("prompt.generico","El Servicio ya fue Seleccionado."));
			}
		}
		
		if(!errores.isEmpty())		
			saveErrors(request,errores);
		else
		{
			forma.setDatosBusquedaServicios("codServicio_"+n, forma.getDatosBusquedaServicios("codServicio")+"");
			forma.setDatosBusquedaServicios("servicioDesc_"+n, forma.getDatosBusquedaServicios("servicioDesc")+"");
			forma.setDatosBusquedaServicios("codigoCups_"+n, forma.getDatosBusquedaServicios("codigoCups")+"");
			forma.setDatosBusquedaServicios("checkSE_"+n, "No");
			forma.setDatosBusquedaServicios("fueInsertado_"+n, ConstantesBD.acronimoNo);
			forma.setDatosBusquedaServicios("consecutivo_"+n, "0");
			forma.setDatosBusquedaServicios("activo_"+n, "S");
			forma.setDatosBusquedaServicios("numRegistros", n+1);
			
		}
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para ingresar al detalle de una excepcion de cobertura
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionDetalleExCobertura(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		HashMap criterios = new HashMap();
		ArrayList<HashMap<String, Object>> naturalezaArticulo= new ArrayList<HashMap<String, Object>>();
		con=UtilidadBD.abrirConexion();
		
		logger.info("\n\nENTIDAD SUB------->"+forma.getEntidadSubcontratadaSel()+"\n\ncontrato ent sub"+forma.getContratoXentSubSel());
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
		forma.setExcepCobertura(mundo.consultaExcCoberturas(criterios));
		
		forma.setClaseInventario(mundo.consultaClasesInventarios());
		
		naturalezaArticulo= (Utilidades.obtenerNaturalezasArticulo(con, usuario.getCodigoInstitucionInt()));
			
		for(int i=0; i<naturalezaArticulo.size();i++)
		{	
		   HashMap mapaAux = (HashMap)naturalezaArticulo.get(i);
		   forma.setNaturalezasArticuloMap("descripcion_"+i, mapaAux.get("nombre"));		   
		   forma.setNaturalezasArticuloMap("codigo_"+i, mapaAux.get("acronimo"));
		}		

		forma.setNaturalezasArticuloMap("numRegistros", naturalezaArticulo.size());
		
		forma.setGrupoServicioMap(mundo.consultaGruposServicios());
				
		forma.setTiposServicioMap(mundo.consultaTiposServicio());
		
		forma.setEspecialidadesMap(mundo.consultaEspecialidades());
		
		forma.setArtiAgruMap(mundo.consultaAgruArtiEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		forma.setServAgruMap(mundo.consultaAgruServEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		forma.setDatosBusquedaArticulos(mundo.consultaArtiEsp(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
				
		forma.setDatosBusquedaServicios(mundo.consultaServEsp(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("detalleExcepciones");
	}
	
	/**
     * Metodo para ingresar al detalle de una excepcion de cobertura para consulta
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionDetalleExCoberturaCon(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		HashMap nuevo = new HashMap();
		HashMap criterios = new HashMap();
		ArrayList<HashMap<String, Object>> naturalezaArticulo= new ArrayList<HashMap<String, Object>>();
		con=UtilidadBD.abrirConexion();
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));		
		forma.setExcepCobertura(mundo.consultaExcCoberturas(criterios));
					
		forma.setDatosBusquedaArticulos(nuevo);
		forma.setDatosBusquedaServicios(nuevo);
		
		forma.setArtiAgruMap(mundo.consultaAgruArtiEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		forma.setServAgruMap(mundo.consultaAgruServEntiSub(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		forma.setDatosBusquedaArticulos(mundo.consultaArtiEsp(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		forma.setDatosBusquedaServicios(mundo.consultaServEsp(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getDetalleExCobertura())+"")));
		
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("detalleExcepcionesCon");
	}
	
	/**
     * Metodo para eliminar un registro de Excepcion Coberturas Entidades Subcontratadas 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionElimRegExCobertura(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		ActionErrors errores = new ActionErrors();
		HashMap criterios= new HashMap();
					
		if(mundo.eliminarRegistroExCobertura(Utilidades.convertirAEntero(forma.getExcepCobertura("consecutivo_"+forma.getRegistroEliminar())+""), usuario.getLoginUsuario()))
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setExcepCobertura(mundo.consultaExcCoberturas(criterios));
		
		return mapping.findForward("principal");
	}
	
	/**
     * Metodo para eliminar un registro de Coberturas Entidades Subcontratadas
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionElimRegCobertura(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		ActionErrors errores = new ActionErrors();
		HashMap criterios= new HashMap();
					
		if(mundo.eliminarRegistroCobertura(Utilidades.convertirAEntero(forma.getCoberturasEntiSubMap("consecutivo_"+forma.getRegistroEliminar())+""), usuario.getLoginUsuario()))
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));		
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
		
		return mapping.findForward("principal");
	}
	
	/**
     * Metodo para guardar registro de Coberturas Entidades Subcontratadas 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarRegCobertura(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int n=0;
		ActionErrors errores = new ActionErrors();
		HashMap criterios= new HashMap();
		boolean guardo=false;
		
		n= Utilidades.convertirAEntero(forma.getCoberturasEntiSubMap("numRegistros")+"");
		
		forma.setCoberturasEntiSubMap("cobertura_"+n, forma.getNuevaCoberPriMap("cobertura"));
		forma.setCoberturasEntiSubMap("prioridad_"+n, forma.getNuevaCoberPriMap("prioridad"));
		forma.setCoberturasEntiSubMap("nuevo_"+n, ConstantesBD.acronimoSi);
		forma.setCoberturasEntiSubMap("activo_"+n, ConstantesBD.acronimoSi);
				
		criterios.put("contratoEntidadSub", forma.getContratoXentSubSel());
		criterios.put("cobertura", forma.getCoberturasEntiSubMap("cobertura_"+n)+"");
		criterios.put("institucion", usuario.getCodigoInstitucionInt());
		criterios.put("nroPrioridad", forma.getCoberturasEntiSubMap("prioridad_"+n)+"");
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("activo", ConstantesBD.acronimoSi);
		criterios.put("consecutivo", "");
		criterios.put("nuevo", ConstantesBD.acronimoSi);
				
		int conse= mundo.guardarRegistroCobertura(criterios);
		
		if(conse > 0)
		{
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			forma.setCoberturasEntiSubMap("consecutivo_"+n, conse);
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
			
		return mapping.findForward("principal");
	}
	
	/**
     * Metodo para guardar nuevo registro de excepcion de Coberturas Entidades Subcontratadas 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarNuevo(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{	
		int conse=0;
		ActionErrors errores = new ActionErrors();
		HashMap criterios = new HashMap();
		int error=0;
		
		criterios.put("contratoEntidadSub", forma.getContratoXentSubSel());
		criterios.put("viaIngreso", forma.getViaIngresoSel());
		criterios.put("TipoPaciente", forma.getTipoPacienteSel());
		criterios.put("naturaleza", forma.getNaturalezaSel());
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("institucion", usuario.getCodigoInstitucionInt());
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
				
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getExcepCobertura("numRegistros")+""));i++)
		{
			if((forma.getExcepCobertura("activo_"+i)+"").equals(ConstantesBD.acronimoSi))
			{
				if(Utilidades.convertirAEntero(forma.getExcepCobertura("viaIngreso_"+i)+"") == Utilidades.convertirAEntero(criterios.get("viaIngreso")+""))
				{
					if(Utilidades.convertirAEntero(forma.getExcepCobertura("tipoPaciente_"+i)+"") == Utilidades.convertirAEntero(criterios.get("TipoPaciente")+""))
					{
						if(Utilidades.convertirAEntero(forma.getExcepCobertura("naturaleza_"+i)+"") == Utilidades.convertirAEntero(criterios.get("naturaleza")+""))
							error=1;
					}
				}
			}
		}
		
		if(error != 0)
		{
			errores.add("descripcion",new ActionMessage("prompt.generico","La via Ingreso, el tipo Paciente y La naturaleza seleccionada ya fue asignada."));
			saveErrors(request,errores);
		}
		else
		{
			conse= mundo.guardarNuevoRegistroExCobertura(criterios);
			
			if(conse > 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
			criterios.put("numContrato", forma.getContratoXentSubSel());
			
			forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
					
			forma.setExcepCobertura(mundo.consultaExcCoberturas(criterios));
			
			logger.info("\n\nMAPA EXCEPCIONES--------->"+forma.getExcepCobertura());
		}
		
		return mapping.findForward("principal");
	}	
	
	/**
     * Metodo Para buscar registros Coberturas por Entidades Subcontratadas y contrato
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscarRegistros(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{		
		con = UtilidadBD.abrirConexion();
		HashMap coberturaConsultaMap= new HashMap();
		HashMap criterios= new HashMap();
		ArrayList<HashMap<String, Object>> viasIngreso= new ArrayList<HashMap<String, Object>>();
		Vector<InfoDatosString> naturaleza;
		
		coberturaConsultaMap.put("flagInicio","true");
		coberturaConsultaMap.put("institucionC",usuario.getCodigoInstitucionInt());
	
		forma.setCoberturasMap(Cobertura.consultaCoberturaAvanzada(con,coberturaConsultaMap));
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());		
		if(Utilidades.convertirAEntero(forma.getContratosXEntidadSubMap("numRegistros")+"") == 1)
			criterios.put("numContrato",forma.getContratosXEntidadSubMap("consecutivo_0"));
		else
			criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
										
		viasIngreso= (Utilidades.obtenerViasIngresoTipoPaciente(con));
		
		for(int i=0; i<viasIngreso.size();i++)
		{	
		   HashMap mapaAux = (HashMap)viasIngreso.get(i);
		   forma.setViasIngreso("descripcion_"+i, mapaAux.get("viaingresotipopac"));		   
		   forma.setViasIngreso("codigo_"+i, mapaAux.get("codvia"));
		}		

		forma.setViasIngreso("numRegistros", viasIngreso.size());
			
		naturaleza= (Utilidades.obtenerNaturalezasPaciente(con,"",ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
		
		for(int i=0; i<naturaleza.size();i++)
		{
			forma.setNaturalezaMap("codigo_"+i, naturaleza.get(i).getCodigo());
			forma.setNaturalezaMap("descripcion_"+i, naturaleza.get(i).getNombre());
		}
		
		forma.setNaturalezaMap("numRegistros", naturaleza.size());
		
		forma.setExcepCobertura(mundo.consultaExcCoberturas(criterios));
				
		logger.info("\n\nEXCEPCIONEEES------>"+forma.getExcepCobertura());
				
		UtilidadBD.cerrarConexion(con);
				
		return mapping.findForward("principal");
	}
	
	/**
     * Metodo Para buscar registros Coberturas por Entidades Subcontratadas y contrato para consulta
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscarRegistrosCon(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{		
		con = UtilidadBD.abrirConexion();
		HashMap coberturaConsultaMap= new HashMap();
		HashMap criterios= new HashMap();
		ArrayList<HashMap<String, Object>> viasIngreso= new ArrayList<HashMap<String, Object>>();
		Vector<InfoDatosString> naturaleza;
		
		coberturaConsultaMap.put("flagInicio","true");
		coberturaConsultaMap.put("institucionC",usuario.getCodigoInstitucionInt());
	
		forma.setCoberturasMap(Cobertura.consultaCoberturaAvanzada(con,coberturaConsultaMap));
		
		criterios.put("entidadSub", forma.getEntidadSubcontratadaSel());
		criterios.put("numContrato", forma.getContratoXentSubSel());
		
		forma.setCoberturasEntiSubMap(mundo.consultaCoberturasEntiSub(criterios));
								
		viasIngreso= (Utilidades.obtenerViasIngresoTipoPaciente(con));
		
		for(int i=0; i<viasIngreso.size();i++)
		{	
		   HashMap mapaAux = (HashMap)viasIngreso.get(i);
		   forma.setViasIngreso("descripcion_"+i, mapaAux.get("viaingresotipopac"));		   
		   forma.setViasIngreso("codigo_"+i, mapaAux.get("codvia"));
		}		

		forma.setViasIngreso("numRegistros", viasIngreso.size());
			
		naturaleza= (Utilidades.obtenerNaturalezasPaciente(con,"",ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
		
		for(int i=0; i<naturaleza.size();i++)
		{
			forma.setNaturalezaMap("codigo_"+i, naturaleza.get(i).getCodigo());
			forma.setNaturalezaMap("descripcion_"+i, naturaleza.get(i).getNombre());
		}
		
		forma.setNaturalezaMap("numRegistros", naturaleza.size());
		
		forma.setExcepCobertura(mundo.consultaExcCoberturas(criterios));
				
		logger.info("\n\nNATURALEZA------>"+forma.getNaturalezaMap());
				
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("principalconsulta");
	}
	
	/**
	 * Método para filtrar los subGrupos al cambiarel grupo de inventario
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionFiltrarSubGrupoInventario(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setSubGrupoInventarioSel("-1");
			
		forma.setSubGrupoInventario(mundo.consultaSubGrupoInventario(forma.getGrupoInventarioSel(), forma.getClaseInventarioSel()));
						
		logger.info("\n\nSUB GRUPO INVENTARIO MAPA----->"+forma.getSubGrupoInventario());
				
		return mapping.findForward("detalleExcepciones");	
	}
	
	/**
	 * Método para filtrar los grupos  al cambiar la clase de inventario
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionFiltrarGrupoInventario(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{		
		forma.setGrupoInventarioSel("-1");
		forma.setSubGrupoInventarioSel("-1");
		HashMap nuevo= new HashMap();
		forma.setSubGrupoInventario(nuevo);
				
		forma.setGrupoInventario(mundo.consultaGrupoInventario(forma.getClaseInventarioSel()));
						
		logger.info("\n\nGRUPO INVENTARIO MAPA----->"+forma.getGrupoInventario());
					
		if(Utilidades.convertirAEntero(forma.getGrupoInventario("numRegistros")+"") == 1)
			this.accionFiltrarSubGrupoInventario(con, forma, mundo, mapping, usuario,request);
		
		return mapping.findForward("detalleExcepciones");		
	}
	
	/**
	 * Método para filtrar los tipo pacientes al cambiar la via de ingreso
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionFiltrarTipoPaciente(Connection con,CoberturasEntidadesSubcontratadasForm forma, ActionMapping mapping,HttpServletResponse response) throws SQLException 
	{
		con = UtilidadBD.abrirConexion();		
		ArrayList<HashMap<String, Object>> tiposPaciente= new ArrayList<HashMap<String, Object>>();
				
		tiposPaciente= (Utilidades.obtenerTiposPacientePorViaIngreso(con, forma.getViaIngresoSel()+""));
				
		for(int i=0; i<tiposPaciente.size();i++)
		{	
		   HashMap mapaAux = (HashMap)tiposPaciente.get(i);
		   forma.setTipoPaciente("descripcion_"+i, mapaAux.get("nomtipopaciente"));		   
		   forma.setTipoPaciente("codigo_"+i, mapaAux.get("tipopaciente"));
		}		

		forma.setTipoPaciente("numRegistros", tiposPaciente.size());
		
		logger.info("\n\nTIPO PACIENTE MAPA----->"+forma.getTipoPaciente());
		
		forma.setEstado("nuevoRegistroEx");
		
		return mapping.findForward("principal");
	}
	
	/**
	 * Método para filtrar los contratos al cambiar la entidad subcontratada
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	private ActionForward accionFiltrarContratos(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{				
		forma.resetBusqueda(usuario.getCodigoInstitucionInt());
		forma.setContratosXEntidadSubMap(UtilidadesFacturacion.obtenerContratosPorEntidadSubcontratada(forma.getEntidadSubcontratadaSel()+""));
				
		logger.info("\n\nENTIDAD SUB--->"+forma.getEntidadSubcontratadaSel()+"\n\nCONTRATOS POR ENTIDAD SUBCONTRATADA------------>"+forma.getContratosXEntidadSubMap());
				
		if(Utilidades.convertirAEntero(forma.getContratosXEntidadSubMap("numRegistros")+"") == 1)
			this.accionBuscarRegistros(con, forma, mundo, mapping, usuario,request);			
			
		return mapping.findForward("principal");
	}
	
	/**
	 * Método para filtrar los contratos al cambiar la entidad subcontratada
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	private ActionForward accionFiltrarContratosCon(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{				
		forma.setContratosXEntidadSubMap(UtilidadesFacturacion.obtenerContratosPorEntidadSubcontratada(forma.getEntidadSubcontratadaSel()+""));
				
		logger.info("\n\nENTIDAD SUB--->"+forma.getEntidadSubcontratadaSel()+"\n\nCONTRATOS POR ENTIDAD SUBCONTRATADA------------>"+forma.getContratosXEntidadSubMap());
				
		if(Utilidades.convertirAEntero(forma.getContratosXEntidadSubMap("numRegistros")+"") == 1)
			this.accionBuscarRegistros(con, forma, mundo, mapping, usuario,request);			
			
		return mapping.findForward("principalconsulta");
	}
	
	/**
     * Inicia en el forward de Coberturas Entidades Subcontratadas
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		ArrayList<HashMap<String, Object>> entidades= new ArrayList<HashMap<String, Object>>();
		con = UtilidadBD.abrirConexion();
		int j=0;
		entidades= (UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt()));
				
		for(int i=0; i<entidades.size();i++)
		{		
		   HashMap mapaAux = (HashMap)entidades.get(i);
		   if((mapaAux.get("activo")+"").equals(ConstantesBD.acronimoSi))
		   {
			   forma.setEntidadesSubcontratadasMap("codigo_"+j, mapaAux.get("consecutivo"));
			   forma.setEntidadesSubcontratadasMap("descripcion_"+j, mapaAux.get("descripcion"));
			   j++;
		   }
		}		
		 
		forma.setEntidadesSubcontratadasMap("numRegistros", j);
				
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("principal");
	}	
	
	/**
     * Inicia en el forward de Coberturas Entidades Subcontratadas para Consulta
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezarConsulta(Connection con, CoberturasEntidadesSubcontratadasForm forma, CoberturasEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		ArrayList<HashMap<String, Object>> entidades= new ArrayList<HashMap<String, Object>>();
		con = UtilidadBD.abrirConexion();
		int j=0;
		entidades= (UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt()));
		
		for(int i=0; i<entidades.size();i++)
		{	
		   HashMap mapaAux = (HashMap)entidades.get(i);
		   if((mapaAux.get("activo")+"").equals(ConstantesBD.acronimoSi))
		   {
			   forma.setEntidadesSubcontratadasMap("codigo_"+j, mapaAux.get("consecutivo"));
			   forma.setEntidadesSubcontratadasMap("descripcion_"+j, mapaAux.get("descripcion"));
			   j++;
		   }
		}		
		 
		forma.setEntidadesSubcontratadasMap("numRegistros", j);
		
		logger.info("\n\nentidades sub-------->"+forma.getEntidadesSubcontratadasMap());
				
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principalconsulta");
	}
}