/*
 * Creado el 3/01/2006
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.inventarios.ExistenciasInventariosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ExistenciasInventarios;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


public class ExistenciasInventariosAction extends Action
{

	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */ 
	private Logger logger = Logger.getLogger(ExistenciasInventariosAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
							        {

		Connection con = null;
		try{
			if(form instanceof ExistenciasInventariosForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ExistenciasInventariosForm forma=(ExistenciasInventariosForm)form;
				ExistenciasInventarios mundo= new ExistenciasInventarios();

				String estado = forma.getEstado();
				logger.warn("estado->"+estado);
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					ValoresPorDefecto.cargarValoresIniciales(con);
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					forma.setAlmacenes(UtilidadInventarios.listadoAlmacensActivos(usuario.getCodigoInstitucionInt(),false));
					if(Integer.parseInt(forma.getAlmacenes().get("numRegistros").toString())==1)
					{
						forma.setIndex(0);
						this.accionCargarAlmacen(con,forma,mundo, usuario.getCodigoInstitucionInt());
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalleAlmacen");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("detalleAlmacen"))
				{
					this.accionCargarAlmacen(con,forma,mundo, usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAlmacen");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAlmacen");
				}
				else if(estado.equals("BuscarClaseGrupoSubGrupo"))
				{
					forma.setUltimoPatron("");
					forma.setPatronOrdenar("");
					forma.setMapaArticulos(mundo.consultarArticulosAlmacenClaseGrupoSubgrupo(
							con,
							forma.getCodAlmacen(),
							forma.getClase(),
							forma.getGrupo(),
							forma.getSubgrupo(),
							forma.getMostrarExt(),
							usuario.getCodigoInstitucionInt()
					));
					forma.setArticuloBusdqueda("");
					forma.setTipoBusqueda("claseGrupoSubgrupo");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAlmacen");
				}
				else if(estado.equals("buscarXCodigo"))
				{
					forma.setTipoBusqueda("codigo");
					if(forma.getArticuloBusdqueda().trim().equals(""))
					{
						forma.setMapaArticulos(mundo.consultarArticulosAlmacen(con,forma.getCodAlmacen(),forma.getMostrarExt(),usuario.getCodigoInstitucionInt()));                    
					}
					else
					{
						forma.setMapaArticulos(mundo.consultarArticulosAlmacenCodArticulo(con,forma.getCodAlmacen(),forma.getArticuloBusdqueda(),forma.getMostrarExt(),usuario.getCodigoInstitucionInt()));                    
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAlmacen");
				}
				else if(estado.equals("buscarXDescripcion"))
				{
					forma.setTipoBusqueda("descripcion");
					if(forma.getArticuloBusdqueda().trim().equals(""))
					{
						forma.setMapaArticulos(mundo.consultarArticulosAlmacen(con,forma.getCodAlmacen(),forma.getMostrarExt(),usuario.getCodigoInstitucionInt()));
					}
					else
					{
						forma.setMapaArticulos(mundo.consultarArticulosAlmacenDescArticulo(con,forma.getCodAlmacen(),forma.getArticuloBusdqueda(),forma.getMostrarExt(),usuario.getCodigoInstitucionInt()));
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAlmacen");
				}
				else if(estado.equals("generarReporte"))
				{   
					return accionGenerarReporte(con, mapping, forma, usuario, request);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}


			}
			else
			{
				logger.error("El form no es compatible con el form de ConceptoTesoreriaForm");
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

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGenerarReporte(Connection con,ActionMapping mapping, ExistenciasInventariosForm forma,UsuarioBasico usuario, HttpServletRequest request) {
		String nombreRptDesign = "ExistenciasInventario.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("EXISTENCIAS DE INVENTARIO "+forma.getNombreAlmacen()+" - "+forma.getNombreCentroAtencion());
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
	    String filtroo="";
	    
    	// Clase
	    if(Integer.parseInt(forma.getClase())>0)
	    	filtroo="[Clase: "+forma.getClase()+"]  ";
    	
	    // Grupo
	    if(Integer.parseInt(forma.getClase())>0)
	    	filtroo+="[Grupo: "+forma.getGrupo()+"]  ";
	    
	    // Subgrupo
	    if(Integer.parseInt(forma.getClase())>0)
	    	filtroo+="[Subgrupo: "+forma.getSubgrupo()+"]  ";
	    
    	v.add(filtroo);
	    
    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,1,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
	    //******************
	  
	   comp.obtenerComponentesDataSet("DataSet");
	   
	   //****************** MODIFICAR CONSULTA
	   String newquery = ConsultasBirt.existenciasDeInventario(forma.getTipoBusqueda(), forma.getCodAlmacen(), Utilidades.convertirAEntero(forma.getClase().toString()), Utilidades.convertirAEntero(forma.getGrupo().toString()), Utilidades.convertirAEntero(forma.getSubgrupo().toString()), forma.getMostrarExt(), forma.getArticuloBusdqueda(), usuario.getCodigoInstitucionInt());
	   comp.modificarQueryDataSet(newquery);
	   
	   logger.info("Query >>>"+newquery);
	   
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   comp.updateJDBCParameters(newPathReport);        
	   
	   if(!newPathReport.equals(""))
	    {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
	    }
	   
	   UtilidadBD.closeConnection(con);
	   return mapping.findForward("detalleAlmacen");
	}
	
	
	/**
	 * Método Acción Ordenar
	 * @param forma
	 */
	private void accionOrdenar(ExistenciasInventariosForm forma)
	{
		String[] indices={
        		"codigo_",
        		"codigoint_",
        		"descripcion_",
        		"unidadmedida_",
        		"estado_",
        		"ubicacion_",
        		"costo_",
        		"existencias_",
        		"valtotal_"      		
        		};
        int numReg=Integer.parseInt(forma.getMapaArticulos().get("numRegistros")+"");
		forma.setMapaArticulos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaArticulos(),numReg));
		forma.getMapaArticulos().put("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}

	/**
	 * Método Acción Cargar
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionCargarAlmacen(Connection con, ExistenciasInventariosForm forma, ExistenciasInventarios mundo, int institucion)
	{
		forma.setNombreAlmacen(forma.getAlmacenes().get("nombre_"+forma.getIndex()).toString());
		forma.setCodAlmacen(Integer.parseInt(forma.getAlmacenes().get("codigo_"+forma.getIndex()).toString()));
		forma.setMapaArticulos(mundo.consultarArticulosAlmacen(con,forma.getCodAlmacen(),forma.getMostrarExt(), institucion));
		forma.setNombreCentroAtencion(forma.getAlmacenes().get("nombrecentroatencion_"+forma.getIndex()).toString());
	}
}
