package com.princetonsa.mundo.glosas;

/*
 * @author: Juan Alejandro Cardona
 * date: octubre 2008
 * codigo que estaba en Conceptos Cartera 
 */


import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.Utilidades;
import com.princetonsa.actionform.glosas.ConceptosRespuestasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ConceptosRespuestasDao;
import com.princetonsa.dao.glosas.RegistrarModificarGlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConceptosRespuestasDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConceptosRespuestas;


public class ConceptosRespuestas {

	private static ConceptosRespuestasDao getConceptosRespuestasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConceptosRespuestasDao();
	}
	
	
    private String codigo;			// codigo del concepto
    private String descripcion;		// descripcion del concepto

    private String tipoConcepto;		// tipo concepto
    private String conceptoGlosa;		// concepto Glosa
    private String conceptoAjuste;		// concepto ajuste
    
    
    //	Manejador de logs de la clase    
	private Logger logger = Logger.getLogger(ConceptosRespuestas.class);	
	
	 // DAO de este objeto, para trabajar con ConceptosRespuestas en la fuente de datos
    private static ConceptosRespuestasDao conceptosDao;
	

    // para almacenar todos los datos correspondientes al formulario. estados de los datos.    
    private HashMap mapConceptos;

    
	private HashMap mapConceptoGlosa;	// Mapa para cargar los datos de conceptos glosas
	private HashMap mapConceptoAjuste;	// Mapa para cargar los datos de conceptos ajustes

    
    
    
    /** limpiar e inicializar atributos de esta clase   */
	public void reset () {
		this.codigo = "";
		this.descripcion = "";
		this.conceptoAjuste = "";
		this.conceptoGlosa = "";
		this.tipoConcepto = "";
		this.mapConceptos = new HashMap ();
    
	 	this.mapConceptoGlosa = new HashMap ();
	 	this.mapConceptoAjuste = new HashMap ();
    }
    

    
	// tipoConcepto
	public String getTipoConcepto() {	return tipoConcepto;	}
	public void setTipoConcepto(String tipoConcepto) {	this.tipoConcepto = tipoConcepto;	}


	// conceptoGlosa
	public String getConceptoGlosa() {	return conceptoGlosa;	}
	public void setConceptoGlosa(String conceptoGlosa) {	this.conceptoGlosa = conceptoGlosa;	}


	// conceptoAjuste
	public String getConceptoAjuste() {	return conceptoAjuste;	}
	public void setConceptoAjuste(String conceptoAjuste) {	this.conceptoAjuste = conceptoAjuste;	}


    //mapConceptoGlosa
    public HashMap getMapConceptoGlosa() {  return mapConceptoGlosa;   }
    public void setMapConceptoGlosa(HashMap mapConceptoGlosa) {   this.mapConceptoGlosa = mapConceptoGlosa;    }
    public Object getMapConceptoGlosa(String key) {     return mapConceptoGlosa.get(key);   }
    public void setMapConceptoGlosa(String key,Object value) {   this.mapConceptoGlosa.put(key,value);   }
	
	
     //mapConceptoAjuste
    public HashMap getMapConceptoAjuste() {  return mapConceptoAjuste;   }
    public void setMapConceptoAjuste(HashMap mapConceptoAjuste) {   this.mapConceptoAjuste = mapConceptoAjuste;    }
    public Object getMapConceptoAjuste(String key) {     return mapConceptoAjuste.get(key);   }
    public void setMapConceptoajuste(String key,Object value) {   this.mapConceptoAjuste.put(key,value);   }
	
	
	// mapConceptos
    public HashMap getMapConceptos() {	return mapConceptos;    }
    public void setMapConceptos(HashMap mapConceptos) {	this.mapConceptos = mapConceptos;    }
    public Object getMapConceptos(String key) {	return mapConceptos.get(key);   }
    public void setMapConceptos(String key,Object value) {	this.mapConceptos.put(key,value);    }
    

     // codigo
    public String getCodigo() {		return codigo;    }
    public void setCodigo(String codigo) {	this.codigo = codigo;    }

    
     // descripcion
    public String getDescripcion() {	return descripcion;    }
    public void setDescripcion(String descripcion) {	this.descripcion = descripcion;    }
    
    
    
    
    
    /** Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD) {
		if ( conceptosDao== null ) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			conceptosDao= myFactory.getConceptosRespuestasDao();
			if( conceptosDao!= null )
				return true;
		}
		return false;
	}
	
	/** * Constructor  */
	public ConceptosRespuestas() {
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
    
    
	
	public HashMap consultarConceptosRespuestasGlosas(Connection con, boolean esAvanzada, int codigoInstitucionInt) {
		int k =0,relaciones=0;
		ResultSetDecorator rs1 = null;
		ResultSetDecorator rs =null;

		try {
			if(esAvanzada)
	            rs = conceptosDao.busquedaAvanzadaRespuestasGlosas(con, this.codigo, codigoInstitucionInt, this.descripcion);
	        else
	            rs = conceptosDao.consultaConceptosRespuestasGlosas(con, codigoInstitucionInt);

			while(rs.next()) {
			    this.setMapConceptos("codigoRespuestaConcepto_"+k,rs.getString("codigoRespuestaConcepto")+"");
			    this.setMapConceptos("codigoRespuestaConceptoAntiguo_"+k,rs.getString("codigoRespuestaConcepto")+"");
			    this.setMapConceptos("descripcion_"+k,rs.getString("descripcion")+"");

			    this.setMapConceptos("tipoConcepto_"+k,rs.getString("tipoConcepto")+"");

			    this.setMapConceptos("conceptoGlosa_"+k,rs.getString("conceptoGlosa")+"");   
			    this.setMapConceptos("descripGlosa_"+k,rs.getString("descripGlosa")+"");   //values cambiados
			    this.setMapConceptos("conceptoAjuste_"+k,rs.getString("conceptoAjuste")+""); 
			    this.setMapConceptos("descripAjuste_"+k,rs.getString("descripAjuste")+""); //values cambiados
			    if((rs.getString("tipoCartera")+"").equals(null) || (rs.getString("tipoCartera")+"").equals(""))
			    {	
			    	this.setMapConceptos("tipoCartera_"+k,"");
			    	this.setMapConceptos("nombreTipoCartera_"+k, "");
			    }	
			    else
			    {	
			    	this.setMapConceptos("tipoCartera_"+k,rs.getString("tipoCartera")+"");
			    	this.setMapConceptos("nombreTipoCartera_"+k, rs.getString("nombreTipoCartera")+"");
			    }	
			    
			    this.setMapConceptos("tipo_"+k,"BD");
			    
			    
			    if(ConceptosRespuestas.puedoEliminar(rs.getString("codigoRespuestaConcepto"), codigoInstitucionInt))
			        this.setMapConceptos("existeRelacion_"+k,"false");  
	            else
	                this.setMapConceptos("existeRelacion_"+k,"true");
			    k ++;
			}
		}	

		catch(SQLException e) {
	        logger.error(e+"Error sql en consultarConceptosGlosas"+e.toString());	        
	    }

		this.setMapConceptos("numRegistros",k+"");
	    this.setMapConceptos("registrosEliminados","0");
	    logger.info("NUMERO DE REGISTROS ---> "+k);
	    return mapConceptos;
	}
	

	/**
	 * Método encargado de cargar los conceptos glosa
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void cargarConceptosGlosa(Connection con, ConceptosRespuestasForm forma, UsuarioBasico usuario)
	{
		logger.info("===> Cargar los conceptos Glosa !!! ");
		forma.setMapConceptoGlosa(Utilidades.obtenerConceptosGlosa(con, usuario.getCodigoInstitucionInt()));
	}
	

	/**
	 * Método encargado de cargar los conceptos glosa
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void cargarConceptosAjuste(Connection con, ConceptosRespuestasForm forma, UsuarioBasico usuario)
	{
		logger.info("===> Cargar los conceptos Ajuste !!! ");
		forma.setMapConceptoAjuste(Utilidades.obtenerConceptosAjuste(con, usuario.getCodigoInstitucionInt()));
	}
	

	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoInstitucionInt
	 * @return	 */
	public boolean eliminarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt) {
		int temp=conceptosDao.eliminarConceptoRespuestaGlosas(con,codigoConceptoAntiguo,codigoInstitucionInt);
		if(temp>0)
			return true;
		else
			return false;
	}
	

	
	
	/**
	 * @param (con, codigoConcepto, codigoInstitucionInt, descripcion, criterios
	 * @return	 */
	public boolean insertarConceptoRespuestaGlosas(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion, HashMap criterios) 
	{
		int temp=conceptosDao.insertarConceptoRespuestaGlosas(con,codigoConcepto,codigoInstitucionInt,descripcion, criterios);
		if(temp > 0)
			return true;
		else
			return false;
	}
	
	
	public boolean modificarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt, String codigoConcepto, String descripcion, HashMap criterios) {
		int temp=conceptosDao.modificarConceptoRespuestaGlosas(con,codigoConceptoAntiguo,codigoInstitucionInt,codigoConcepto,descripcion, criterios);
		if(temp>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Metodo que consulta si un concepto tipo glosa ha sido utilizado o no para validar si se puede o no eliminar
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public boolean consultarConceptosRespuesta (Connection con, String codConcepto)
	{
		return getConceptosRespuestasDao().consultarConceptosRespuesta(con, codConcepto);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean puedoEliminar(String codigo, int institucion)
	{
		return getConceptosRespuestasDao().puedoEliminar(codigo, institucion);
	}
}