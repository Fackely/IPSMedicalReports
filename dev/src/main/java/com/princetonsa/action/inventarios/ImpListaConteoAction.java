package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.UtilidadBD;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.ImpListaConteoForm;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ImpListaConteo;
import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;
import com.princetonsa.mundo.inventarios.Secciones;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;



/**
 * Clase para el manejo del workflow de la ubicacion de articulos por almacen
 * Date: 2008-01-22
 * @author lgchavez@princetonsa.com
 */
public class ImpListaConteoAction extends Action {
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ImpListaConteoAction.class);
	
	
	
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
    		if(form instanceof ImpListaConteoForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			ImpListaConteoForm forma = (ImpListaConteoForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO >>>>>>>>> ---->"+estado+"\n\n");

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
    				return this.accionCargarAlmacenes(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarSecciones"))
    			{
    				return this.accionCargarSecciones(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarSubsecciones"))
    			{
    				return this.accionCargarSubsecciones(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarGrupos"))
    			{
    				return this.accionCargarGrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarSubgrupos"))
    			{
    				return this.accionCargarSubgrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("adicionarseccion"))
    			{
    				return this.accionAdicionarSeccion(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("eliminarRegistroSeccion"))
    			{
    				return this.accioneliminarRegistroSeccion(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("agregarNuevo"))
    			{
    				return this.accionagregarNuevo(forma,con,mapping, usuario);
    			} 
    			else if(estado.equals("eliminarArticulo"))
    			{
    				return this.accioneliminarArticulo(forma,con,mapping, usuario);
    			}

    			else if(estado.equals("Buscar"))
    			{
    				this.generarReporte(con, forma, mapping, request, usuario);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("ImpListaConteo");
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
    
    
    private ActionForward generarReporte(Connection con, ImpListaConteoForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
       	
    	String nombreRptDesign = "ListadoConteo.rptdesign";
		
    	PreparacionTomaInventario pti=new PreparacionTomaInventario();
    	
    	pti.setCentroAtencion(forma.getCentroAtencion());
		pti.setAlmacen(forma.getAlmacen());
		try{
			pti.setSeccion(forma.getSeccionestemp().get("cadenaCodigos").toString());
		}
		catch (Exception e){
			pti.setSeccion("");
		}
		pti.setSubseccion(forma.getSubseccion());
		pti.setClase(forma.getClase());
		pti.setGrupo(forma.getGrupo());
		pti.setSubgrupo(forma.getSubgrupo());
		pti.setCodigosArticulos(forma.getCodigosArticulosInsertados());
		
		HashMap mapa = pti.filtrarArticulos(con, pti);
		
		pti.setArticulosFiltradosMap(mapa);
    	
    	
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
        
        
        for(int x=0; x<Integer.parseInt(forma.getSeccionestemp().get("numRegistros")+"");x++)
        {
        	if(x!=0)
        		secciones+=", ";
        	
        	secciones += forma.getSeccionestemp().get("descseccion_"+x);
        	  
		}
        
        for(int x=0; x<Integer.parseInt(forma.getSubseccionesMap().get("numRegistros")+"");x++)
		{
			if (Integer.parseInt(forma.getSubseccionesMap().get("codigo_subseccion_"+x).toString()) == forma.getSubseccion())
			{
				subseccion = forma.getSubseccionesMap().get("descripcion_"+x)+"";
			}    
		}
        
        
     

        
        //Parametros Generacion reporte
        
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
        
        
        
        
        
        HashMap codigos=new HashMap();
    	codigos.put("numRegistros", "0");
    	String  conteoMinimo=ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(usuario.getCodigoInstitucionInt());
    	int p=0;
    	logger.info("VALORES POR DEFECTO CONTEO MINIMO >>>>>"+conteoMinimo);
    	try{
    			for (int x=0; x<Integer.parseInt(pti.getArticulosFiltradosMap().get("numRegistros").toString()); x++)
    			{
    				String str="SELECT articulo as codigo from registro_conteo_inventario where articulo="+pti.getArticulosFiltradosMap().get("codigo_"+x)+" and ind_diferencia_conteo='N' and numero_conteo>="+conteoMinimo+" and estado='FIN'";
    				PreparedStatementDecorator ps7 =  new PreparedStatementDecorator(con.prepareStatement(str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    				ResultSetDecorator rs = new ResultSetDecorator(ps7.executeQuery());
    				logger.info("CONSULTA DE DATOS PARA S O N>>>>>>	"+str);
    				if (rs.next()){
    					logger.info("entro al if >>>>>>>"+rs.getInt("codigo"));
    					codigos.put("codigo_"+p, rs.getInt("codigo")+"");
    					p++;
    				}
    			}
    	}
    	
    	catch (SQLException e)
    	{
    		e.printStackTrace();	
    	}
    		
    	String cadenaPreparada=crearCadena(pti.getArticulosFiltradosMap());
    	codigos.put("numRegistros", p);
    	
    	logger.info("Cedena Prepa>>><"+cadenaPreparada);
    	logger.info("Cedena Prepa>>><"+codigos);
    	logger.info("numRegistros>>>>"+codigos.get("numRegistros"));
        
        
        
        
        
        
        
        
        
        
        
        ImpListaConteo imp = new ImpListaConteo();
        
        String sql = imp.sqlReporteListadoConteo(con, forma.getSeccionestemp(), forma.getSubseccion(), cadenaPreparada, codigos, forma.getAlmacen(), forma.getIndArticulo(), forma.getOrdArticulo(), usuario.getCodigoInstitucionInt());
        	
        
     
       
       
        /*
        if (forma.getSeccionestemp().containsKey("cadenaCodigos") && !forma.getSeccionestemp().get("cadenaCodigos").equals("") && !forma.getSeccionestemp().get("cadenaCodigos").equals("null"))
        {
        	CondicionSeccion=" AND apa.seccion IN ("+forma.getSeccionestemp().get("cadenaCodigos")+")";
		
        	if (forma.getSubseccion()>0){
				CondicionSubseccion=" AND apa.subseccion IN ("+forma.getSubseccion()+")";
			}
		}*/
    	
    	/*for (int x=0; x<Integer.parseInt(codigos.get("numRegistros").toString()); x++)
    	{
    		logger.info("remplazo el: "+codigos.get("codigo_"+x).toString());
    		if(x>0)
    			cadenaPreparada=cadenaPreparada.replaceFirst(","+codigos.get("codigo_"+x).toString()+",", ",-1,");
    		else
    			cadenaPreparada=cadenaPreparada.replaceFirst(""+codigos.get("codigo_"+x).toString()+",", "-1,");
    	}*/
    	
    	
        /*
    	imp.setIndArticulo(forma.getIndArticulo());
        String almacenapa=" and  apa.almacen="+forma.getAlmacen();
    	String diferencia=" WHERE 9=9 "+almacenapa;
    	
		if (imp.getIndArticulo().toString().equals("S"))
		{
			diferencia = " INNER JOIN " +
								"preparacion_toma_inventario pti on (t.codigo=pti.articulo and pti.codigo_dapa=dapa.codigo_det_pk and pti.seccion=se.codigo_pk AND pti.subseccion = subs.codigo_subseccion) "+ 
						" LEFT OUTER JOIN " +
								"registro_conteo_inventario rci ON (pti.articulo=rci.articulo  AND rci.codigo_preparacion=pti.codigo AND rci.seccion=pti.seccion AND rci.subseccion = pti.subseccion) " +
						" WHERE " +
								"1=1 " +
								" AND (rci.ind_diferencia_conteo='S' OR (rci.ind_diferencia_conteo IS NULL AND rci.numero_conteo<"+ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(usuario.getCodigoInstitucionInt())+")) "+
								" AND  t.codigo in ("+cadenaPreparada+"-1) " +
								//" and pti.estado='PEN' " +
								" AND (rci.numero_conteo=1 OR rci.numero_conteo IS NULL) ";
		}											
	    
       String codprepa=" pti.articulo IN ("+cadenaPreparada+"-1)";
       String almacen=" pti.estado='PEN' and pti.almacen="+forma.getAlmacen()+" and ex.almacen="+forma.getAlmacen();

       String order="ORDER BY seccion_desc, subseccion_desc, "+forma.getOrdArticulo()+" ";
       
       
       String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", codprepa);
       comp.modificarQueryDataSet(newquery);
       String newquery1=comp.obtenerQueryDataSet().replaceAll("2=2", almacen);
       comp.modificarQueryDataSet(newquery1);
       String newquery2=comp.obtenerQueryDataSet().replaceAll("AND 3=3", CondicionSeccion);
       comp.modificarQueryDataSet(newquery2);
       String newquery3=comp.obtenerQueryDataSet().replaceAll("AND 4=4", CondicionSubseccion);
                
        comp.modificarQueryDataSet(newquery3);
        String newquery4=comp.obtenerQueryDataSet().replaceAll("AND 6=6", order);
        
        comp.modificarQueryDataSet(newquery4);
        String newquery5=comp.obtenerQueryDataSet().replaceAll("WHERE 9=9", diferencia);
        
        
        logger.info("query>>>"+newquery5);
    
    	*/
        
        
        comp.obtenerComponentesDataSet("ListadoConteo");
        logger.info("SQL / "+sql);
        comp.modificarQueryDataSet(sql);
        
        
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
		return mapping.findForward("ImpListaConteo");
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
    private ActionForward accionBuscar(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
      	
    	String codigosArticulosInsertados=crearCadenaConComas1(forma.getArticulosMap());
    	forma.setCodigosArticulosInsertados(codigosArticulosInsertados);
    	
    	ImpListaConteo a = new ImpListaConteo();
    	a.setInstitucion(usuario.getCodigoInstitucionInt());
    	a.setCentroAtencion(forma.getCentroAtencion());
    	a.setAlmacen(forma.getAlmacen());
    	a.setSeccion(forma.getSeccion());
    	a.setSubseccion(forma.getSubseccion());
    	a.setClase(forma.getClase());
    	a.setGrupo(forma.getGrupo());
    	a.setSubgrupo(forma.getSubgrupo());
    	a.setIndArticulo(forma.getIndArticulo());
    	a.setOrdArticulo(forma.getOrdArticulo());
    	a.setArticulosMap(forma.getArticulosMap());
    	a.setCodigosArticulosInsertados(forma.getCodigosArticulosInsertados());
    	a.setSeccionestemp(forma.getSeccionestemp());
    	
    	forma.setArticulosPreparacion(ImpListaConteo.consultar(con, a));
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("ImpListaConteo");
    	
    	
	}

/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @return
 */
	private ActionForward accioneliminarArticulo(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
    	
    	HashMap articulosMap = forma.getArticulosMap();
    	
    	int pos=forma.getCodarticulo();
    	
    	int aux=pos+1;
		String codigosArticulosInsertados = forma.getCodigosArticulosInsertados();
		String codigoArticulo = forma.getArticulosMap("codigo_"+pos).toString();
		//se quita del listado de servicios insertados
		codigosArticulosInsertados = codigosArticulosInsertados.replaceAll(codigoArticulo+",", "");
		forma.setCodigosArticulosInsertados(codigosArticulosInsertados);
        for(int x=pos;x<Integer.parseInt(articulosMap.get("numRegistros").toString()); x++)
                {
                articulosMap.put("codigo_"+x, articulosMap.get("codigo_"+aux));
                articulosMap.put("descripcion_"+x, articulosMap.get("descripcion_"+aux));
                aux++;
                }
        
        aux = Integer.parseInt(articulosMap.get("numRegistros").toString());
        articulosMap.remove("codigo_"+aux);
        articulosMap.remove("descripcion_"+aux);
        articulosMap.put("numRegistros", aux-1);
        
        return mapping.findForward("ImpListaConteo");

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
    		cadena += ", ";
    	}
    	cadena += mapa.get("codigo_pk_"+x);
    	mapa.put("cadenaCodigos", cadena);
    	return mapa;
    }
   
/**
	 * 
	 * @param mapa
	 * @return
	 */
	private String crearCadenaConComas1(HashMap mapa){
    	String cadena = "";
    	int x;
    	for(x=0; x<Integer.parseInt(mapa.get("numRegistros").toString())-1; x++){
    		cadena += mapa.get("codigo_"+x);
    		cadena += ", ";
    	}
    	cadena += mapa.get("codigo_"+x);
    	
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
	private ActionForward accionagregarNuevo(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
    	HashMap articulosmap = forma.getArticulosMap();
    	String codigosArticulosInsertados = forma.getCodigosArticulosInsertados();
    	int pos=Integer.parseInt(forma.getArticulosMap().get("numRegistros")+"");
     	articulosmap.put("codigo_"+pos,forma.getCodigoArticulo());
     	articulosmap.put("descripcion_"+pos,forma.getDescripcionArticulo());
    	articulosmap.put("numRegistros", (pos+1)+"");
    	
		//se registra el articulo como insertado
		codigosArticulosInsertados += forma.getArticulosMap("codigo_"+pos).toString()+", ";
    	forma.setCodigosArticulosInsertados(codigosArticulosInsertados);
	    return mapping.findForward("ImpListaConteo");
    	}

/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @return
 */
	private ActionForward accioneliminarRegistroSeccion(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
	    	HashMap seccionestemp = forma.getSeccionestemp();
	    	HashMap secciones = forma.getSeccionesMap();
	    	
	    	int pos=forma.getCodseccionestemp();
	    	
	    	int aux=pos+1;
            
			int ind=Integer.parseInt(forma.getSeccionesMap().get("numRegistros")+"");
		    
		    secciones.put("codigo_pk_"+ind,forma.getSeccionestemp().get("codigo_pk_"+pos).toString());
			secciones.put("descseccion_"+ind,forma.getSeccionestemp().get("descseccion_"+pos).toString());
			secciones.put("numRegistros",(ind+1)+"");
			
			int ind1=Integer.parseInt(forma.getSeccionesMap().get("numRegistros")+"");
	    	forma.setSeccionesMap(secciones);
	    	
	        for(int x=pos;x<Integer.parseInt(seccionestemp.get("numRegistros").toString()); x++)
	                {
	                
	        		seccionestemp.put("codigo_pk_"+x, seccionestemp.get("codigo_pk_"+aux));
	                seccionestemp.put("descseccion_"+x, seccionestemp.get("descseccion_"+aux));
	                aux++;
	                }
	        
	        aux = Integer.parseInt(seccionestemp.get("numRegistros").toString());
	        seccionestemp.remove("codigo_pk_"+aux);
	        seccionestemp.remove("descseccion_"+aux);
	        seccionestemp.put("numRegistros", aux-1);
	        seccionestemp = crearCadenaConComas(seccionestemp);
	        
	        if (Integer.parseInt(forma.getSeccionestemp().get("numRegistros")+"")==1)
	        {
	        	this.accionCargarSubsecciones(forma,con,mapping, usuario);
	        }
	        else if (Integer.parseInt(forma.getSeccionestemp().get("numRegistros")+"")==0)
	        {
	        	this.accionCargarSubsecciones(forma,con,mapping, usuario);
	        }
	        
	        return mapping.findForward("ImpListaConteo");
	}


	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionAdicionarSeccion(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		     HashMap seccionestemp = forma.getSeccionestemp();
		     seccionestemp.put("numRegistros", seccionestemp.get("numRegistros"));
		     int numRegistros= Integer.parseInt(seccionestemp.get("numRegistros")+"");
		     int g=ConstantesBD.codigoNuncaValido;   
				     
		     
            for(int x=0;x<Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString()); x++){

            	if(Integer.parseInt(forma.getSeccionesMap().get("codigo_pk_"+x).toString()) == Integer.parseInt(forma.getSeccion().toString())){
		                           
               						seccionestemp.put("codigo_pk_"+numRegistros,forma.getSeccion().toString());
		                            seccionestemp.put("descseccion_"+numRegistros,forma.getSeccionesMap().get("descseccion_"+x).toString());
		                            numRegistros++;
		                            seccionestemp.put("numRegistros",numRegistros);
		                            forma.setSeccionestemp(seccionestemp);
		                            seccionestemp = crearCadenaConComas(seccionestemp);
		                            logger.info("CADENA DE CODIGOS >>>>>>"+seccionestemp.get("cadenaCodigos"));
		                            g=x;
		                    }
		            }
		    eliminarRegistroMapaSeccion(forma.getSeccionesMap(),g);
        	this.accionCargarSubsecciones(forma,con,mapping, usuario);
		    
		UtilidadBD.closeConnection(con); 		
		return mapping.findForward("ImpListaConteo");	
	}


/**
 * 
 * @param mapa
 * @param pos
 * @return
 */
    private HashMap eliminarRegistroMapaSeccion(HashMap mapa, int pos){
        int aux=pos+1;
                
        for(int x=pos;x<Integer.parseInt(mapa.get("numRegistros").toString()); x++)
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
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ImpListaConteo");		
	}	
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarAlmacenes(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		HashMap x=new HashMap ();
		x.put("numRegistros",0);
		forma.setSeccionestemp(x);
		
		Secciones seccion= new Secciones();
		seccion.setAlmacen(forma.getAlmacen());
		seccion.setCentroAtencion(forma.getCentroAtencion());
		seccion.setInstitucion(usuario.getCodigoInstitucionInt());
		
		HashMap subsecciones=new HashMap();
		subsecciones.put("numRegistros", "0");
		forma.setSubseccionesMap(subsecciones);
		
		forma.setSeccionesMap(Secciones.consultar(con, seccion));
		
		forma.setSeccion("");
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
		
		forma.setClasesMap(UtilidadInventarios.cargarInfoClasesInventario(usuario.getCodigoInstitucionInt(), forma.getAlmacen(),""));
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ImpListaConteo");
	}
	
		
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarSecciones(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		HashMap x=new HashMap ();
		x.put("numRegistros",0);
		forma.setSeccionestemp(x);
		
		Secciones seccion= new Secciones();
		seccion.setAlmacen(forma.getAlmacen());
		seccion.setCentroAtencion(forma.getCentroAtencion());
		seccion.setInstitucion(usuario.getCodigoInstitucionInt());
		
		HashMap subsecciones=new HashMap();
		subsecciones.put("numRegistros", "0");
		forma.setSubseccionesMap(subsecciones);
		
		forma.setSeccionesMap(Secciones.consultar(con, seccion));
		
		forma.setSeccion("");
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
		
		forma.setClasesMap(UtilidadInventarios.cargarInfoClasesInventario(usuario.getCodigoInstitucionInt(), forma.getAlmacen(),""));
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ImpListaConteo");	
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionCargarSubsecciones(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
        if (Integer.parseInt(forma.getSeccionestemp().get("numRegistros")+"")==1)
        {
		Secciones seccion= new Secciones();
		seccion.setAlmacen(forma.getAlmacen());
		seccion.setCentroAtencion(forma.getCentroAtencion());
		seccion.setCodigoPk(Integer.parseInt(forma.getSeccion()));
		seccion.setInstitucion(usuario.getCodigoInstitucionInt());
		
		
		forma.setSubseccionesMap(Secciones.consultarSubsecciones(con, Integer.parseInt(forma.getSeccionestemp().get("codigo_pk_"+0)+"")));	
		}
 
        else 
        {
    		Secciones seccion= new Secciones();
    		forma.setSubseccionesMap(Secciones.consultarSubsecciones(con, ConstantesBD.codigoNuncaValido));	
        }
        UtilidadBD.closeConnection(con);		
		return mapping.findForward("ImpListaConteo");
    }
	
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionCargarSubgrupos(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		
		forma.setSubgruposMap(PreparacionTomaInventario.consultarSubgrupos(con, forma.getClase(), forma.getGrupo()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ImpListaConteo");	
	}


	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarGrupos(ImpListaConteoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		
		forma.setGruposMap(new HashMap());
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setGruposMap("numRegistros", 0);
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		
		
		forma.setGruposMap(PreparacionTomaInventario.consultarGrupos(con, forma.getClase()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ImpListaConteo");	
	}

    
    
	
}