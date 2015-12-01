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
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.PreparacionTomaInventarioForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;
import com.princetonsa.mundo.inventarios.Secciones;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * Clase para el manejo del workflow de la preparacion de la toma del inventario
 * Date: 2008-01-29
 * @author garias@princetonsa.com
 */
public class PreparacionTomaInventarioAction extends Action {
	
	/**
	 * logger 
	 * */
	static Logger logger = Logger.getLogger(SeccionesAction.class);
	
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
    		if(form instanceof PreparacionTomaInventarioForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			PreparacionTomaInventarioForm forma = (PreparacionTomaInventarioForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO SECCIONES ---->"+estado+"\n\n");

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
    			else if(estado.equals("filtrarArticulos"))
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

    				else if(estado.equals("volverFiltro"))
    				{
    					return mapping.findForward("preparacionTomaInventario");
    				}
    				else if(estado.equals("confirmar"))
    				{
    					return this.accionConfirmar(forma,con,mapping, usuario);
    				}
    				else if(estado.equals("imprimir"))
    				{
    					insertarPreparacion(forma, usuario, con);
    					this.generarReporte(con, forma, mapping, request, usuario);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("preparacionTomaInventario");
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

	private ActionForward generarReporte(Connection con, PreparacionTomaInventarioForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	   	
    	String nombreRptDesign = "ListadoConteo.rptdesign";
		
    	PreparacionTomaInventario pti=new PreparacionTomaInventario();
    	
    	int codPreMax=pti.CodigoPreparacionMax(con,pti)-1;
    	String CondicionSeccion="";
    	String CondicionSubseccion="";
    	
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
				comp.insertLabelInGridPpalOfHeader(2,0, "LISTA DE CONTEO \n "+usuario.getCentroAtencion()+" - "+forma.getAlmacenesMap().get("nombre_"+x));
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
        
//      Parametros Generacion reporte
        
        String parame="";
           
        
        if (!secciones.equals("")){
       	parame+="Parámetros de generación del listado \n";
        	
       	if(!subseccion.equals(""))
        		parame+="[ Sección: "+secciones + "] - [ Subsección: "+subseccion+" ]";
        	else
        		parame+="[ Secciones: "+secciones+" ]";
        }
           if (forma.getClase()!=ConstantesBD.codigoNuncaValido)
           {
           if(parame.equals(""))
           	parame+="Parámetros de generación del listado \n ";
           else
           	parame+=" - ";
           
           	for(int x=0; x<Integer.parseInt(forma.getClasesMap().get("numRegistros")+"");x++)
       		{
       			if (Integer.parseInt(forma.getClasesMap().get("codigoclaseinventario_"+x).toString()) == forma.getClase())
       			{
       				parame+="[ Clase: "+forma.getClasesMap().get("nombreclaseinventario_"+x)+" ] ";
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
          
          String sql = pti.sqlListadoConteo(codPreMax, forma.getAlmacen(), forma.getSeccionesElegidasMap(), forma.getSubseccion());
          comp.obtenerComponentesDataSet("ListadoConteo");
          comp.modificarQueryDataSet(sql);
          logger.info("QUERY >>>>>>>>>>>> \n "+sql);
        
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
		return mapping.findForward("preparacionTomaInventario");
		
	}
    
    
	private ActionForward accionConfirmar(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	insertarPreparacion(forma, usuario, con);
    	UtilidadBD.closeConnection(con);		
    	return mapping.findForward("preparacionTomaInventario");
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param con
	 * @return
	 */
	private static boolean insertarPreparacion(PreparacionTomaInventarioForm forma, UsuarioBasico usuario, Connection con)
	{
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
    	pti.setCentroAtencion(forma.getCentroAtencion());
		pti.setAlmacen(forma.getAlmacen());
		pti.setArticulosFiltradosMap(forma.getArticulosFiltradosMap());
		pti.setUsuarioModifica(usuario.getLoginUsuario());
		pti.setFecha_toma(forma.getFecha());
		pti.setHora_toma(forma.getHora());
		pti.setInstitucion(usuario.getCodigoInstitucionInt());
		logger.info("Pasó por aqui ");
		return pti.confirmarPreparacion(con, pti);
	}
	

	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	public static void accionOrdenarMapa(PreparacionTomaInventarioForm forma)
	{
		
		
		String[] indices = {"descripcion_","codigo_","unidad_medida_","codigo_interfaz_","lote_","codigo_lote_","fecha_vencimiento_","existencias_","concentracion_","forma_farmaceutica_","naturaleza_","subgrupocon_", "seccion_subseccion_","seccion_","subseccion_","subgrupo_","grupo_","clase_","codigo_det_pk_"};
		
		int numReg = Integer.parseInt(forma.getArticulosFiltradosMap("numRegistros").toString());
		
		forma.setArticulosFiltradosMap(Listado.ordenarMapaRompimiento(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getArticulosFiltradosMap(), forma.getRompimiento()));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setArticulosFiltradosMap("numRegistros",numReg+"");
		forma.setArticulosFiltradosMap("INDICES_MAPA",indices+"");		
		logger.info("############ "+forma.getArticulosFiltradosMap());
		
		logger.info(">>>>>>>>>>><"+forma.getRompimiento()+">>>>>>>>>>>>"+forma.getPatronOrdenar());
	}

   
private ActionForward accionFiltrarArticulos(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
		pti.setCentroAtencion(forma.getCentroAtencion());
		pti.setAlmacen(forma.getAlmacen());
		try{
			pti.setSeccion(forma.getSeccionesElegidasMap().get("cadenaCodigos").toString());
		}
		catch (Exception e){
			pti.setSeccion("");
		}
		pti.setSubseccion(forma.getSubseccion());
		pti.setClase(forma.getClase());
		pti.setGrupo(forma.getGrupo());
		pti.setSubgrupo(forma.getSubgrupo());
		pti.setCodigosArticulos(forma.getArticulosMap().get("codigosArticulos").toString());
		
		HashMap mapa = pti.filtrarArticulos(con, pti);
		
		forma.setArticulosFiltradosMap(mapa);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("articulosFiltrados");
	}


private ActionForward accionEliminarArticulo(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		HashMap mapa = forma.getArticulosMap();
		String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
		
		codigosArticulos = codigosArticulos.replaceAll(mapa.get("codigo_"+forma.getIndexMap())+",", "");
		
		eliminarRegistroMapaArticulos(mapa, Integer.parseInt(forma.getIndexMap().toString()));
		
		forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("preparacionTomaInventario");
	}


private ActionForward accionAdicionarArticulo(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	
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
        return mapping.findForward("preparacionTomaInventario");
	}


	private ActionForward accionEliminarSeccionElegida(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	
    	int posSecEle = Integer.parseInt(forma.getSeccion());
    	int posSec = Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString());
    	
    	forma.getSeccionesMap().put("codigo_pk_"+posSec, forma.getSeccionesElegidasMap().get("codigo_pk_"+posSecEle));
    	forma.getSeccionesMap().put("descseccion_"+posSec, forma.getSeccionesElegidasMap().get("descseccion_"+posSecEle));
    	forma.getSeccionesMap().put("numRegistros", posSec+1);
    	
    	
	    forma.setSeccionesElegidasMap(eliminarRegistroMapaSeccion(forma.getSeccionesElegidasMap(), Integer.parseInt(forma.getSeccion())));
	    
	    forma.setSeccionesElegidasMap(crearCadenaConComas(forma.getSeccionesElegidasMap()));
	    
	    cargarSubsecciones(forma, con, mapping, usuario);
  
    	UtilidadBD.closeConnection(con);		
		return mapping.findForward("preparacionTomaInventario");
	}


	private ActionForward accionAdicionarSeccion(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
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
		return mapping.findForward("preparacionTomaInventario");
	}
    
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
    
    private void cargarSubsecciones(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
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

	private ActionForward accionCargarSubgrupos(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
		pti.setClase(forma.getClase());
		pti.setSubgrupo(forma.getSubgrupo());
		
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setSubgruposMap(PreparacionTomaInventario.consultarSubgrupos(con, forma.getClase(), forma.getGrupo()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("preparacionTomaInventario");	
	}


	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarGrupos(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
		pti.setClase(forma.getClase());
		
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
		return mapping.findForward("preparacionTomaInventario");	
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarSecciones(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
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
		return mapping.findForward("preparacionTomaInventario");	
	}

	
	private ActionForward accionCargarAlmacenes(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
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
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("preparacionTomaInventario");
	}
	
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(PreparacionTomaInventarioForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("preparacionTomaInventario");		
	}
}