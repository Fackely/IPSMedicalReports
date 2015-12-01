package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.inventarios.ConsultaInventarioFisicoArticulosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsultaInventarioFisicoArticulos;
import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


public class ConsultaInventarioFisicoArticulosAction extends Action{
	
	
	/**
	 * logger 
	 * */
	static Logger logger = Logger.getLogger(ConsultaInventarioFisicoArticulosAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{

    	Connection con = null;
    	try
    	{

    		if(response==null);
    		if(form instanceof ConsultaInventarioFisicoArticulosForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			ConsultaInventarioFisicoArticulosForm forma = (ConsultaInventarioFisicoArticulosForm)form;
    			String estado = forma.getEstado();
    			String filtro=forma.getFiltro();
    			logger.info("\n\n ESTADO RegistroConteoInventario ---->"+estado+"\n\n"+filtro+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
    				return this.accionEmpezar(forma, con, mapping, usuario);    			
    			}
    			else if(estado.equals("cargarAlmacenes"))
    			{    			
    				return this.accionCargarAlmacenes(forma, con, mapping, usuario);    			
    			}
    			else if(estado.equals("cargarClases"))
    			{    			
    				return this.accionCargarClases(forma, con, mapping, usuario);    			
    			}
    			else if(estado.equals("cargarGrupos"))
    			{
    				return this.accionCargarGrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarSubgrupos"))
    			{
    				return this.accionCargarSubgrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("adicionarArticulo"))
    			{
    				return this.accionAdicionarArticulo(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("eliminarArticulo"))
    			{
    				return this.accionEliminarArticulo(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("filtrarArticulos"))
    			{
    				return this.accionFiltrarArticulos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("volverFiltro"))
    			{
    				return mapping.findForward("ConsultaInventarioFisicoArticulos");
    			}
    			else if(estado.equals("mostrarPopup"))
    			{
    				return this.accionConsultarConteos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("mostrarPopup1"))
    			{
    				return this.accionConsultarPreparaciones(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("mostrarPopup2"))
    			{
    				return this.accionConsultarUbicacion(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("mostrarPopup3"))
    			{
    				return this.accionConsultarAjuste(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("filtrarArticulosSin"))
    			{
    				return this.accionFiltrarArticulos(forma,con,mapping, usuario);
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO ==> ORDENAR
				 -------------------------------*/
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("articulosFiltrados");
    				}
    				else if (estado.equals("redireccion"))
    				{
    					UtilidadBD.closeConnection(con);
    					response.sendRedirect(forma.getLinkSiguiente());
    					return null;
    				}
    				else if(estado.equals("imprimirfiltrarArticulosSin"))
    				{
    					return this.generarReporte(con, forma, mapping, request, usuario);
    				}
    				else if(estado.equals("imprimirfiltrarArticulos"))
    				{
    					return this.generarReporte1(con, forma, mapping, request, usuario);
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


	//-----------METODOS----------
    
   
	/**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward generarReporte1(Connection con, ConsultaInventarioFisicoArticulosForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	   	
    	String nombreRptDesign = "ConsultaInventarioFisicoArticulos1.rptdesign";
		
    	ConsultaInventarioFisicoArticulos cuc=new ConsultaInventarioFisicoArticulos();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        for(int x=0; x<Integer.parseInt(forma.getAlmacenesMap().get("numRegistros")+"");x++)
		{
			if (Integer.parseInt(forma.getAlmacenesMap().get("codigo_"+x).toString()) == forma.getAlmacen())
			{
				comp.insertLabelInGridPpalOfHeader(2,0, "CONSULTA INVENTARIO FISICO \n  ARTICULOS CON INVENTARIO FISICO \n "+usuario.getCentroAtencion()+" - "+forma.getAlmacenesMap().get("nombre_"+x));
			}
		}
       
        
        //Parametros generacion listado
        
        String parame="";
        
        if (forma.getClase()!=ConstantesBD.codigoNuncaValido)
        {
        	for(int x=0; x<Integer.parseInt(forma.getClasesMap().get("numRegistros")+"");x++)
    		{
    			if (Integer.parseInt(forma.getClasesMap().get("codigoclaseinventario_"+x).toString()) == forma.getClase())
    				parame+="Parámetros de generación del listado \n [ Clase: "+forma.getClasesMap().get("nombreclaseinventario_"+x)+" ] ";
    		}
        	
        	if(forma.getGrupo()!=ConstantesBD.codigoNuncaValido)
        	{
        		for(int x=0; x<Integer.parseInt(forma.getGruposMap().get("numRegistros")+"");x++)
        		{
        			if (Integer.parseInt(forma.getGruposMap().get("codigo_"+x).toString()) == forma.getGrupo())
        				parame+="- [ Grupo: "+forma.getGruposMap().get("nombre_"+x)+" ] ";
        		}
        		
        		
        		if(forma.getSubgrupo()!=ConstantesBD.codigoNuncaValido)
        		{
            		for(int x=0; x<Integer.parseInt(forma.getSubgruposMap().get("numRegistros")+"");x++)
            		{
            			if (Integer.parseInt(forma.getSubgruposMap().get("codigo_"+x).toString()) == forma.getSubgrupo())
            				parame+="- [ Subgrupo: "+forma.getSubgruposMap().get("nombre_"+x)+" ] ";
            		}
        		}
        	}
        }
       
      comp.insertLabelInGridPpalOfHeader(3,0, parame);
       
      //Reemplazar consulta del reporte
      comp.obtenerComponentesDataSet("consultaInvFisArt");
      String newquery = ConsultasBirt.articulosConInventarioFisico(con, forma.getArticulosFiltradosMap().get("codigos_preparados").toString()+"-1", forma.getAlmacen(), forma.getPatronOrdenar());
      comp.modificarQueryDataSet(newquery);
       
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
      return mapping.findForward("ConsultaInventarioFisicoArticulos");
		
	}
    
    
    /**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward generarReporte(Connection con, ConsultaInventarioFisicoArticulosForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	   	
    	String nombreRptDesign = "ConsultaInventarioFisicoArticulos.rptdesign";
		
    	ConsultaInventarioFisicoArticulos cuc=new ConsultaInventarioFisicoArticulos();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        for(int x=0; x<Integer.parseInt(forma.getAlmacenesMap().get("numRegistros")+"");x++)
		{
			if (Integer.parseInt(forma.getAlmacenesMap().get("codigo_"+x).toString()) == forma.getAlmacen())
			{
				comp.insertLabelInGridPpalOfHeader(2,0, "CONSULTA INVENTARIO FISICO \n  ARTICULOS SIN INVENTARIO FISICO \n "+usuario.getCentroAtencion()+" - "+forma.getAlmacenesMap().get("nombre_"+x));
			}
		}
       
       //Parametros generacion listado
       
         String parame="";
         
         if (forma.getClase()!=ConstantesBD.codigoNuncaValido)
         {
         
         	for(int x=0; x<Integer.parseInt(forma.getClasesMap().get("numRegistros")+"");x++)
     		{
     			if (Integer.parseInt(forma.getClasesMap().get("codigoclaseinventario_"+x).toString()) == forma.getClase())
     			{
     				parame+="Parámetros de generación del listado \n [ Clase: "+forma.getClasesMap().get("nombreclaseinventario_"+x)+" ] ";
     			}
     		}
         	
         	if(forma.getGrupo()!=ConstantesBD.codigoNuncaValido)
         	{
         		for(int x=0; x<Integer.parseInt(forma.getGruposMap().get("numRegistros")+"");x++)
         		{
         			if (Integer.parseInt(forma.getGruposMap().get("codigo_"+x).toString()) == forma.getGrupo())
         			{
         				parame+="- [ Grupo: "+forma.getGruposMap().get("nombre_"+x)+" ] ";
         			}
         		}
         		
         		
         		if(forma.getSubgrupo()!=ConstantesBD.codigoNuncaValido)
         		{
             		for(int x=0; x<Integer.parseInt(forma.getSubgruposMap().get("numRegistros")+"");x++)
             		{
             			if (Integer.parseInt(forma.getSubgruposMap().get("codigo_"+x).toString()) == forma.getSubgrupo())
             			{
             				parame+="- [ Subgrupo: "+forma.getSubgruposMap().get("nombre_"+x)+" ] ";
             			}
             		}
         			
         		}
         	}
         }
         
         
       comp.insertLabelInGridPpalOfHeader(3,0, parame);
       
       //Reemplazar consulta del reporte
       comp.obtenerComponentesDataSet("consultaInvFisArt");
       String newquery = ConsultasBirt.articulosSinInventarioFisico(con, forma.getArticulosFiltradosMap().get("codigos_preparados").toString()+"-1", forma.getAlmacen(), forma.getPatronOrdenar());
       comp.modificarQueryDataSet(newquery);
        
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
		return mapping.findForward("ConsultaInventarioFisicoArticulos");
		
	}

    
    
    
    
    
    
    
    
    
    /**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */

	public static void accionOrdenarMapa(ConsultaInventarioFisicoArticulosForm forma)
	{
		String[] indices = {"descripcion_","ubicacion_","codigo_","nomunidadmedida_","unidad_medida_","codigo_interfaz_","lote_","fecha_vencimiento_","codigo_lote_","fecha_toma_","existencia_","usuario_modifica_","cant_conteo_","codajuste_","codregistroconteo_"};
		String codigosPreparados=forma.getArticulosFiltradosMap().get("codigos_preparados").toString();
		String codigosFiltrados=forma.getArticulosFiltradosMap().get("codigos_filtrados").toString();
		int numReg = Integer.parseInt(forma.getArticulosFiltradosMap().get("numRegistros").toString());
		forma.setArticulosFiltradosMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getArticulosFiltradosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.getArticulosFiltradosMap().put("numRegistros",numReg+"");
		forma.getArticulosFiltradosMap().put("INDICES_MAPA",indices+"");
		forma.getArticulosFiltradosMap().put("codigos_preparados", codigosPreparados+"");
		forma.getArticulosFiltradosMap().put("codigos_filtrados", codigosFiltrados+"");
	}
    
   
	  
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionConsultarAjuste(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	ConsultaInventarioFisicoArticulos cuc = new ConsultaInventarioFisicoArticulos();
       	cuc.setCodigoLote(forma.getCodigoLote());
		cuc.setAlmacen(forma.getAlmacen());
		cuc.setCodigoAjuste(forma.getCodigoAjuste());
		forma.setConteosArticulo(cuc.consultarAjusteArticulo(con, cuc));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popupConteos3");
	}
	
	
	
	
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionConsultarUbicacion(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	ConsultaInventarioFisicoArticulos cuc = new ConsultaInventarioFisicoArticulos();
    	cuc.setCodigoLote(forma.getCodigoLote());
		cuc.setAlmacen(forma.getAlmacen());
		cuc.setCodigoArticulo(forma.getCodigoArticulo());
		forma.setConteosArticulo(cuc.consultarUbicacionArticulo(con, cuc));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popupConteos2");
	}
	
	
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionConsultarPreparaciones(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	ConsultaInventarioFisicoArticulos cuc = new ConsultaInventarioFisicoArticulos();
    	cuc.setCodigoLote(forma.getCodigoLote());
		cuc.setAlmacen(forma.getAlmacen());
		cuc.setCodigoArticulo(forma.getCodigoArticulo());
		forma.setConteosArticulo(cuc.consultarPreparacionesArticulo(con, cuc));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popupConteos1");
	}



    
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionFiltrarArticulos(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {

		
    	logger.info("Articulos Map \n");
    	Utilidades.imprimirMapa(forma.getArticulosMap());
    	
		ConsultaInventarioFisicoArticulos cifa = new ConsultaInventarioFisicoArticulos();
		cifa.setCentroAtencion(forma.getCentroAtencion());
		cifa.setAlmacen(forma.getAlmacen());
		cifa.setClase(forma.getClase());
		cifa.setGrupo(forma.getGrupo());
		cifa.setSubgrupo(forma.getSubgrupo());
		cifa.setCodigosArticulos(forma.getArticulosMap().get("codigosArticulos").toString());
		cifa.setPatronOrdenar(forma.getPatronOrdenar());
		cifa.setEstado(forma.getEstado());
		
		
		HashMap mapa = cifa.filtrarArticulos(con, cifa);
		
		forma.setArticulosFiltradosMap(mapa);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("articulosFiltrados");
    	
    	
    	
	}




    /**
     * Metodo para consultar la información de los conteos realizados a un articulo
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionConsultarConteos(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	
    	ConsultaInventarioFisicoArticulos cuc = new ConsultaInventarioFisicoArticulos();
    	cuc.setCodigoLote(forma.getCodigoLote());
		cuc.setAlmacen(forma.getAlmacen());
		cuc.setCodigoArticulo(forma.getCodigoArticulo());
		cuc.setCodigoPreparacion(forma.getCodigoPreparacion());
		
		logger.info("codigos registro conteos >>>"+forma.getArticulosFiltradosMap().get("codregistroconteo_"+forma.getIndex())+"");
		cuc.setCodigosRegConteo(forma.getArticulosFiltradosMap().get("codregistroconteo_"+forma.getIndex())+"");
		forma.setConteosArticulo(cuc.consultarConteosArticulo(con, cuc));
		forma.setCodigoPreparacion(ConstantesBD.codigoNuncaValido);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popupConteos");
	}




	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarArticulo(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		HashMap mapa = forma.getArticulosMap();
		String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
		
		codigosArticulos = codigosArticulos.replaceAll(mapa.get("codigo_"+forma.getIndexMap())+",", "");
		
		eliminarRegistroMapaArticulos(mapa, Integer.parseInt(forma.getIndexMap().toString()));
		
		forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ConsultaInventarioFisicoArticulos");
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarArticulo(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	
		if(!forma.getDescripcionArticulo().equals("")) {
			HashMap mapa = forma.getArticulosMap();
	    	String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
	    	int pos=Integer.parseInt(forma.getArticulosMap().get("numRegistros")+"");
	    	
	     	mapa.put("codigo_"+pos,forma.getCodigoArticulo());
	     	mapa.put("descripcion_"+pos,forma.getDescripcionArticulo());
	    	mapa.put("numRegistros", (pos+1)+"");
	    	
			codigosArticulos += forma.getArticulosMap().get("codigo_"+pos).toString() + ",";
			forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
			
			forma.setDescripcionArticulo("");
		}
		UtilidadBD.closeConnection(con);		
        return mapping.findForward("ConsultaInventarioFisicoArticulos");
    	    	
	}
    
    /**
     * 
     * @param mapa
     * @param pos
     * @return
     */
	private HashMap eliminarRegistroMapaArticulos(HashMap mapa, int pos){
    	int aux=pos+1;
		
    	for(int x=pos; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++)
		{
    		
    		mapa.put("codigo_"+x, mapa.get("codigo_"+aux));
    		mapa.put("descripcion_"+x, mapa.get("descripcion_"+aux));
    		aux++;
		}
    	aux = Integer.parseInt(mapa.get("numRegistros").toString());
    	mapa.remove("codigo_"+aux);
    	mapa.remove("descripcion_"+aux);
    	mapa.put("numRegistros", aux-1);
    	
    	return mapa;
    }
    
    /**
     * 
     * @param mapa
     * @return
     */
	private HashMap crearCadenaConComas(HashMap mapa){
    	String cadena = "";
    	int x;
    	for(x=0; x<Integer.parseInt(mapa.get("numRegistros").toString())-1; x++){
    		cadena += mapa.get("codigo_pk_"+x);
    		cadena += ",";
    	}
    	cadena += mapa.get("codigo_pk_"+x);
    	mapa.put("cadenaCodigos", cadena);
    	return mapa;
    }

	private ActionForward accionCargarSubgrupos(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
		pti.setClase(forma.getClase());
		pti.setSubgrupo(forma.getSubgrupo());
		
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setSubgruposMap(new HashMap());
		forma.setSubgruposMap("numRegistros", 0);
		
		forma.setArticulosMap(new HashMap());
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setSubgruposMap(PreparacionTomaInventario.consultarSubgrupos(con, forma.getClase(), forma.getGrupo()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ConsultaInventarioFisicoArticulos");	
	}


	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarGrupos(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
		pti.setClase(forma.getClase());
		
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		
		forma.setGruposMap(new HashMap());
		forma.setSubgruposMap(new HashMap());
		forma.setGruposMap("numRegistros", 0);
		forma.setSubgruposMap("numRegistros", 0);
		
		forma.setArticulosMap(new HashMap());
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
				
		forma.setGruposMap(PreparacionTomaInventario.consultarGrupos(con, forma.getClase()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ConsultaInventarioFisicoArticulos");	
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarAlmacenes(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setAlmacen(ConstantesBD.codigoNuncaValido);
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		forma.setClase(ConstantesBD.codigoNuncaValido);
		forma.setClasesMap(new HashMap());
		forma.setGruposMap(new HashMap());
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setClasesMap("numRegistros", 0);
		forma.setGruposMap("numRegistros", 0);
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setClasesMap(UtilidadInventarios.cargarInfoClasesInventario(usuario.getCodigoInstitucionInt(), forma.getAlmacen(), ""));
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ConsultaInventarioFisicoArticulos");
	}
	
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarClases(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{	
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		forma.setClase(ConstantesBD.codigoNuncaValido);
		forma.setClasesMap(new HashMap());
		forma.setGruposMap(new HashMap());
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setClasesMap("numRegistros", 0);
		forma.setGruposMap("numRegistros", 0);
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setClasesMap(UtilidadInventarios.cargarInfoClasesInventario(usuario.getCodigoInstitucionInt(), forma.getAlmacen(), ""));
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ConsultaInventarioFisicoArticulos");	
	}

	
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(ConsultaInventarioFisicoArticulosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaInventarioFisicoArticulos");		
	}
	
	
	
	
}