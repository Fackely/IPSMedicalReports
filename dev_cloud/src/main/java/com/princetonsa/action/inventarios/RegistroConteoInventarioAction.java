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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.RegistroConteoInventarioForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;
import com.princetonsa.mundo.inventarios.RegistroConteoInventario;
import com.princetonsa.mundo.inventarios.Secciones;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * @author axioma
 *
 */
public class RegistroConteoInventarioAction extends Action {
	
	/**
	 * logger 
	 * */
	static Logger logger = Logger.getLogger(RegistroConteoInventarioAction.class);
	
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
    		if(form instanceof RegistroConteoInventarioForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			RegistroConteoInventarioForm forma = (RegistroConteoInventarioForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO RegistroConteoInventario ---->"+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), usuario.getLoginUsuario());
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
    			else if(estado.equals("cargarSecciones"))
    			{
    				return this.accionCargarSecciones(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("cargarGrupos"))
    			{
    				return this.accionCargarGrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarSubgrupos"))
    			{
    				return this.accionCargarSubgrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("adicionarSeccion"))
    			{
    				return this.accionAdicionarSeccion(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("eliminarSeccionElegida"))
    			{
    				return this.accionEliminarSeccionElegida(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("adicionarArticulo"))
    			{
    				return this.accionAdicionarArticulo(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("eliminarArticulo"))
    			{
    				return this.accionEliminarArticulo(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("anularRegistros"))
    			{
    				return this.accionAnularRegistros(forma,con,mapping, usuario);
    			}
    			else if (estado.equals("filtrarArticulos"))
    			{
    				return this.accionfiltrarArticulos(forma,con,mapping,usuario);
    			}
    			else if(estado.equals("volverFiltro"))
    			{
    				return mapping.findForward("RegistroConteoInventario");
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO ==> ORDENAR
				 -------------------------------*/
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("RegistroConteoInventarioRS");
    				}
    				else if(estado.equals("guardar"))
    				{
    					return this.accionadicionarArticulos(forma,con,mapping,usuario);	
    				}
    				else if(estado.equals("finalizar"))
    				{
    					return this.accionadicionarArticulos(forma,con,mapping,usuario);	
    				}
    				else if(estado.equals("imprimir"))
    				{
    					forma.setEstado("finalizar");
    					accionadicionarArticulos(forma,con,mapping,usuario);
    					this.generarReporte(con, forma, mapping, request, usuario);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("RegistroConteoInventario");
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
    
    
    private ActionForward accionAnularRegistros(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
    	// Obtener los codigos de preparación
    	String codigosPreparacion = "";
    	for(int i=0; i<Integer.parseInt(forma.getArticulosFiltradosMap("numRegistros").toString()); i++){
    		codigosPreparacion+=forma.getArticulosFiltradosMap("codigo_preparacion_"+i)+", ";
    	}
    	codigosPreparacion+=ConstantesBD.codigoNuncaValido;
    		
    	//se prepara el objeto
    	RegistroConteoInventario rci = new RegistroConteoInventario();
    	rci.setCodigosPreparaciones(codigosPreparacion);
		rci.setCentroAtencion(forma.getCentroAtencion());
		rci.setAlmacen(forma.getAlmacen());
		try{
			rci.setSeccion(forma.getSeccionesElegidasMap().get("cadenaCodigos").toString());
		}
		catch (Exception e){
			rci.setSeccion("");
		}
		rci.setSubseccion(forma.getSubseccion());
		rci.setClase(forma.getClase());
		rci.setGrupo(forma.getGrupo());
		rci.setSubgrupo(forma.getSubgrupo());
		rci.setCodigosArticulos(forma.getArticulosMap().get("codigosArticulos").toString());
		rci.setOrdenar(forma.getOrdArticulo().toString());
		rci.setIndArticulo(forma.getIndArticulo().toString());
		rci.setInstitucion(usuario.getCodigoInstitucionInt());
		
		if (forma.getEleccionAnular().equals("Finalizados"))
			forma.setEleccionAnular(ConstantesIntegridadDominio.acronimoEstadoFinalizado);
		else
			forma.setEleccionAnular(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		
		// se eliminan los conteos
		rci.anularConteos(con, rci, forma.getEleccionAnular());
    	
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("RegistroConteoInventarioRS");
	}


	private ActionForward accionCargarAlmacenes(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	
    	Secciones seccion = new Secciones();
		
		seccion.setInstitucion(usuario.getCodigoInstitucionInt());
		seccion.setCentroAtencion(forma.getCentroAtencion());
		seccion.setAlmacen(forma.getAlmacen());
		
		HashMap subsecciones = new HashMap();
		subsecciones.put("numRegistros", "0");
		forma.setSubseccionesMap(subsecciones);
		
		HashMap seccionesElegidas = new HashMap();
		seccionesElegidas.put("numRegistros", "0");
		forma.setSeccionesElegidasMap(seccionesElegidas);
    	
    	
    	forma.setAlmacen(ConstantesBD.codigoNuncaValido);
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		forma.setClase(ConstantesBD.codigoNuncaValido);
		forma.setSeccion("");
		forma.setClasesMap(new HashMap());
		forma.setGruposMap(new HashMap());
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setClasesMap("numRegistros", 0);
		forma.setGruposMap("numRegistros", 0);
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setCentrosAtencionMap(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("RegistroConteoInventario");	
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
    private ActionForward generarReporte(Connection con, RegistroConteoInventarioForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	 
    	String nombreRptDesign = "registroConteo.rptdesign";
    	RegistroConteoInventario pti=new RegistroConteoInventario();
    	pti.setArticulosFiltradosMap(forma.getArticulosFiltradosMap());
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
				comp.insertLabelInGridPpalOfHeader(2,0, "REGISTRO DE CONTEO");
				comp.insertLabelInGridPpalOfHeader(4,0, "Usuario Responsable: "+usuario.getLoginUsuario());
				comp.insertLabelInGridPpalOfHeader(5,0, "Centro de Atención: "+usuario.getCentroAtencion());
				comp.insertLabelInGridPpalOfHeader(5,1, "Almacén: "+forma.getAlmacenesMap().get("nombre_"+x));
			}
		}
        
        String secciones="", subseccion="";
        for(int x=0; x<Integer.parseInt(forma.getSeccionesElegidasMap().get("numRegistros")+"");x++)
        {
        	secciones += forma.getSeccionesElegidasMap().get("descseccion_"+x)+", ";
		}
        for(int x=0; x<Integer.parseInt(forma.getSubseccionesMap().get("numRegistros")+"");x++)
		{
			if (Integer.parseInt(forma.getSubseccionesMap().get("codigo_subseccion_"+x).toString()) == forma.getSubseccion())
			{
				subseccion = forma.getSubseccionesMap().get("descripcion_"+x)+"";
			}    
		}
        if (!secciones.equals("")){
        	if(!subseccion.equals("")){
        		comp.insertLabelInGridPpalOfHeader(6,0, "Sección: "+secciones);
        		comp.insertLabelInGridPpalOfHeader(6,1, "Subsección: "+subseccion);
        	}	
        	else
        		comp.insertLabelInGridPpalOfHeader(6,0, "Sección(es): "+secciones);
        }
        
        
       /* if (forma.getSeccionesElegidasMap().containsKey("cadenaCodigos") && !forma.getSeccionesElegidasMap().get("cadenaCodigos").equals("") && !forma.getSeccionesElegidasMap().get("cadenaCodigos").equals("null"))
        {
        	CondicionSeccion=" AND apa.seccion IN ("+forma.getSeccionesElegidasMap().get("cadenaCodigos")+")";
        	if (forma.getSubseccion()>0){
				CondicionSubseccion=" AND apa.subseccion IN ("+forma.getSubseccion()+")";
			}
		}
      
       String cadenaCodigos=" pti.articulo IN (-1,"+cadenaPreparada+"-1) ";
       String cadenaCodigos1="AND pti.almacen="+forma.getAlmacen()+" "+"AND t.codigo IN (-1,"+cadenaPreparada+"-1) ";
       String order="ORDER BY seccion_subseccion,"+forma.getOrdArticulo()+", t.descripcion ";
       String almacen=" ex.almacen="+forma.getAlmacen();
       
       comp.obtenerComponentesDataSet("registroConteo");
       String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", cadenaCodigos);
       comp.modificarQueryDataSet(newquery);
       String newquery1=comp.obtenerQueryDataSet().replaceAll("2=2", almacen);
       comp.modificarQueryDataSet(newquery1);
       String newquery2=comp.obtenerQueryDataSet().replaceAll("AND 3=3", CondicionSeccion);
       comp.modificarQueryDataSet(newquery2);
       String newquery3=comp.obtenerQueryDataSet().replaceAll("AND 4=4", CondicionSubseccion);
       comp.modificarQueryDataSet(newquery3);
       String newquery4=comp.obtenerQueryDataSet().replaceAll("AND 5=5", cadenaCodigos1);
       comp.modificarQueryDataSet(newquery4);
       String newquery5=comp.obtenerQueryDataSet().replaceAll("ORDER BY t.descripcion", order);*/
       
       String cadenaPreparada=crearCadena(pti.getArticulosFiltradosMap());
       String sql = RegistroConteoInventario.sqlReporteRegistroConteoInventario(forma.getSeccionesElegidasMap(), forma.getSubseccion(), cadenaPreparada, forma.getAlmacen(), forma.getOrdArticulo(), usuario.getCodigoInstitucionInt());
        
       comp.obtenerComponentesDataSet("registroConteo");
       comp.modificarQueryDataSet(sql);
       logger.info("Query >>>"+sql);
       
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
		return mapping.findForward("RegistroConteoInventario");
		
	}
    
    
 	
	/**
	 * 
	 * @param mapa
	 * @return
	 */
	private static String crearCadena(HashMap mapa) {
    	String cadena = "";
    	int x;
   	for(x=0; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++){
    		cadena += mapa.get("codigo_"+x);
    		cadena += ",";
    	}
    	return cadena;
	}
    

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionadicionarArticulos(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	insertarConteo(forma, usuario, con);
    	UtilidadBD.closeConnection(con);		
    	return mapping.findForward("RegistroConteoInventario");
    }

    /**
	 * 
	 * @param forma
	 * @param usuario
	 * @param con
	 * @return
	 */
	private static boolean insertarConteo(RegistroConteoInventarioForm forma, UsuarioBasico usuario, Connection con)
	{
		RegistroConteoInventario pti = new RegistroConteoInventario();
    	pti.setCentroAtencion(forma.getCentroAtencion());
		pti.setAlmacen(forma.getAlmacen());
		pti.setArticulosFiltradosMap(forma.getArticulosFiltradosMap());
		pti.setUsuarioModifica(usuario.getLoginUsuario());
		pti.setUsuario_responsable(forma.getUsuario());
		pti.setInstitucion(usuario.getCodigoInstitucionInt());
		pti.setEstado(forma.getEstado());
		pti.setIndArticulo(forma.getIndArticulo().toString());
		return pti.guardarConteo(con, pti);
	}
    
    

	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	public static void accionOrdenarMapa(RegistroConteoInventarioForm forma)
	{
		String[] indices = {"descripcion_","codigo_","unidad_medida_","codigo_interfaz_","lote_","codigo_lote_","fecha_vencimiento_","existencias_","concentracion_","forma_farmaceutica_","naturaleza_","subgrupocon_", "seccion_subseccion_","seccion_","subseccion_","subgrupo_","grupo_","clase_","codigo_det_pk_","ubicacion_","conteo_","codigo_preparacion_","nomunidadmedida_"};
		
		int numReg = Integer.parseInt(forma.getArticulosFiltradosMap("numRegistros").toString());
		
		forma.setArticulosFiltradosMap(Listado.ordenarMapaRompimiento(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getArticulosFiltradosMap(), forma.getRompimiento()));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setArticulosFiltradosMap("numRegistros",numReg+"");
		forma.setArticulosFiltradosMap("INDICES_MAPA",indices+"");		
	}

    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionfiltrarArticulos(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		RegistroConteoInventario rci = new RegistroConteoInventario();
		rci.setCentroAtencion(forma.getCentroAtencion());
		rci.setAlmacen(forma.getAlmacen());
		try{
			rci.setSeccion(forma.getSeccionesElegidasMap().get("cadenaCodigos").toString());
		}
		catch (Exception e){
			rci.setSeccion("");
		}
		rci.setSubseccion(forma.getSubseccion());
		rci.setClase(forma.getClase());
		rci.setGrupo(forma.getGrupo());
		rci.setSubgrupo(forma.getSubgrupo());
		rci.setCodigosArticulos(forma.getArticulosMap().get("codigosArticulos").toString());
		rci.setOrdenar(forma.getOrdArticulo().toString());
		rci.setIndArticulo(forma.getIndArticulo().toString());
		rci.setInstitucion(usuario.getCodigoInstitucionInt());
		HashMap mapa = rci.filtrarArticulos(con, rci);
		forma.setArticulosFiltradosMap(mapa);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("RegistroConteoInventarioRS");
		
	}


	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), usuario.getLoginUsuario());	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("RegistroConteoInventario");		
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarArticulo(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
			
			HashMap mapa = forma.getArticulosMap();
			String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
			
			codigosArticulos = codigosArticulos.replaceAll(mapa.get("codigo_"+forma.getIndexMap())+",", "");
			
			eliminarRegistroMapaArticulos(mapa, Integer.parseInt(forma.getIndexMap().toString()));
			
			forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
			
			UtilidadBD.closeConnection(con);		
			return mapping.findForward("RegistroConteoInventario");
		}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarArticulo(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
			if(!forma.getDescripcionArticulo().equals("")) {
				
			
				HashMap mapa = forma.getArticulosMap();
				
				logger.info("MAPA ARTICULOS EN ACTION           ->"+forma.getArticulosMap());
				logger.info("MAPA ARTICULOS FILTRADOS EN ACTION ->"+forma.getArticulosFiltradosMap());
				logger.info("ARTICULO Cod->"+forma.getCodigoArticulo());
				logger.info("ARTICULO Des->"+forma.getDescripcionArticulo());
				
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
	        return mapping.findForward("RegistroConteoInventario");
	    	    	
		}

		/**
		 * 
		 * @param forma
		 * @param con
		 * @param mapping
		 * @param usuario
		 * @return
		 */
		private ActionForward accionEliminarSeccionElegida(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	    	
	    	int posSecEle = Integer.parseInt(forma.getSeccion());
	    	int posSec = Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString());
	    	
	    	forma.getSeccionesMap().put("codigo_pk_"+posSec, forma.getSeccionesElegidasMap().get("codigo_pk_"+posSecEle));
	    	forma.getSeccionesMap().put("descseccion_"+posSec, forma.getSeccionesElegidasMap().get("descseccion_"+posSecEle));
	    	forma.getSeccionesMap().put("numRegistros", posSec+1);
	    	
	    	
		    forma.setSeccionesElegidasMap(eliminarRegistroMapaSeccion(forma.getSeccionesElegidasMap(), Integer.parseInt(forma.getSeccion())));
		    
		    forma.setSeccionesElegidasMap(crearCadenaConComas(forma.getSeccionesElegidasMap()));
		    
		    cargarSubsecciones(forma, con, mapping, usuario);
	  
	    	UtilidadBD.closeConnection(con);		
			return mapping.findForward("RegistroConteoInventario");
		}

		/**
		 * 
		 * @param forma
		 * @param con
		 * @param mapping
		 * @param usuario
		 * @return
		 */
		private ActionForward accionAdicionarSeccion(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	    	HashMap seccionesElegidas = forma.getSeccionesElegidasMap();
	    	
	    	if(Integer.parseInt(forma.getSeccion().toString()) != -1){
		    	int aux = 0;
		    	int numRegistros = Integer.parseInt(seccionesElegidas.get("numRegistros").toString());
		    	for(int x=0; x<Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString()); x++){
		    		if (Integer.parseInt(forma.getSeccionesMap().get("codigo_pk_"+x).toString()) == Integer.parseInt(forma.getSeccion().toString())){
		    			
		    			seccionesElegidas.put("codigo_pk_"+numRegistros, forma.getSeccion().toString());
		    			seccionesElegidas.put("descseccion_"+numRegistros, forma.getSeccionesMap().get("descseccion_"+x).toString());
		    			numRegistros++;
		    			seccionesElegidas.put("numRegistros", numRegistros++);
		    			
		    			seccionesElegidas = crearCadenaConComas(seccionesElegidas);
		    			
		    			forma.setSeccionesElegidasMap(seccionesElegidas);
		    			aux = x;
		    		}
		    	}
		    	forma.setSeccionesMap(eliminarRegistroMapaSeccion(forma.getSeccionesMap(), aux));
	    	}
	    	
	    	cargarSubsecciones(forma, con, mapping, usuario);
	   
	    	UtilidadBD.closeConnection(con);		
			return mapping.findForward("RegistroConteoInventario");
		}

		/**
		 * 
		 * @param mapa
		 * @param pos
		 * @return
		 */
	    private HashMap eliminarRegistroMapaSeccion(HashMap mapa, int pos){
	    	int aux=pos+1;
			
	    	for(int x=pos; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++)
			{
	    		
	    		mapa.put("codigo_pk_"+x, mapa.get("codigo_pk_"+aux));
	    		mapa.put("descseccion_"+x, mapa.get("descseccion_"+aux));
	    		aux++;
			}
	    	aux = Integer.parseInt(mapa.get("numRegistros").toString());
	    	mapa.remove("codigo_pk_"+aux);
	    	mapa.remove("descseccion_"+aux);
	    	mapa.put("numRegistros", aux-1);

	    	return mapa;
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
	    /**
	     * 
	     * @param forma
	     * @param con
	     * @param mapping
	     * @param usuario
	     */
	    private void cargarSubsecciones(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	    {
	    	if (forma.getSeccionesElegidasMap().get("numRegistros").toString().equals("1")){

				Secciones seccion= new Secciones();	
				
				forma.setSubseccionesMap(Secciones.consultarSubsecciones(con, Integer.parseInt(forma.getSeccionesElegidasMap().get("codigo_pk_"+0).toString())));
				
	    	}
	    	else
	    	{
	    		HashMap x=new HashMap();
	    		x.put("numRegistros", "0");
				forma.setSubseccionesMap(x);	
	    	}
	    }
		/**
		 * 
		 * @param forma
		 * @param con
		 * @param mapping
		 * @param usuario
		 * @return
		 */
		private ActionForward accionCargarSubgrupos(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
		{
			
			forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
			
			forma.setSubgruposMap(new HashMap());
			forma.setArticulosMap(new HashMap());
			
			forma.setSubgruposMap("numRegistros", 0);
			forma.setArticulosMap("numRegistros", 0);
			forma.setArticulosMap("codigosArticulos", "");
			
			forma.setSubgruposMap(PreparacionTomaInventario.consultarSubgrupos(con, forma.getClase(), forma.getGrupo()));
			UtilidadBD.closeConnection(con);		
			return mapping.findForward("RegistroConteoInventario");	
		}


		/**
	     * 
	     * @param forma
	     * @param con
	     * @param mapping
	     * @param usuario
	     * @return
	     */
		private ActionForward accionCargarGrupos(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
		{
			
			forma.setGrupo(ConstantesBD.codigoNuncaValido);
			forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
			
			forma.setGruposMap(new HashMap());
			forma.setSubgruposMap(new HashMap());
			forma.setArticulosMap(new HashMap());
			
			forma.setGruposMap("numRegistros", 0);
			forma.setSubgruposMap("numRegistros", 0);
			forma.setArticulosMap("numRegistros", 0);
			forma.setArticulosMap("codigosArticulos", "");
			
			forma.setGruposMap(PreparacionTomaInventario.consultarGrupos(con, forma.getClase()));
			UtilidadBD.closeConnection(con);		
			return mapping.findForward("RegistroConteoInventario");	
		}

		/**
	     * 
	     * @param forma
	     * @param con
	     * @param mapping
	     * @param usuario
	     * @return
	     */
		private ActionForward accionCargarSecciones(RegistroConteoInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
		{
			Secciones seccion = new Secciones();
			
			seccion.setInstitucion(usuario.getCodigoInstitucionInt());
			seccion.setCentroAtencion(forma.getCentroAtencion());
			seccion.setAlmacen(forma.getAlmacen());
			
			HashMap subsecciones = new HashMap();
			subsecciones.put("numRegistros", "0");
			forma.setSubseccionesMap(subsecciones);
			
			HashMap seccionesElegidas = new HashMap();
			seccionesElegidas.put("numRegistros", "0");
			forma.setSeccionesElegidasMap(seccionesElegidas);
			
			
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
			
			logger.info("valor del mapa clases >>> "+forma.getClasesMap());
			
			forma.setSeccionesMap(Secciones.consultar(con, seccion));
			
			UtilidadBD.closeConnection(con);		
			return mapping.findForward("RegistroConteoInventario");	
		}

}