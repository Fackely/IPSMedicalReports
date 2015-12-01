/*
 * @author armando
 * 
 */

package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.struts.upload.FormFile;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ActividadesProgramasPYPDao;


/**
 * 
 * @author armando
 *
 */
public class ActividadesProgramasPYP 
{
	
	
	ActividadesProgramasPYPDao objetoDao;
	

	/**
	 * 
	 */
	private HashMap actProgPYP;
	
/**
 *  * Variable para manejar el programa seleccionado al ingreso del sistema.
	 */
	private String programa;
	
	/**
	 * 
	 */
	private String actividad;
	
	/**
	 * 
	 */
	private boolean embarazo;
	
	/**
	 * 
	 */
	private String finalidadConsulta;
	
	/**
	 * 
	 */
	private String finalidadServicio;
	/**
	 * 
	 */
	private String semanasGestacion;
	
	/***
	 * 
	 */
	private boolean requerido;
	
	/**
	 * Indica si la actividad que se está asociando al programa 
	 * se puede ejecutar varias veces al día (RQF 431) 
	 */
	private boolean permitirEjecutar;
	
	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 */
	private String  descArchivo;
	
    /**
     * 
     */
    private int institucion;

    /**
     * 
     */
    private String codigo;
    
    /**
     * 
     */
    private HashMap viasIngreso;
    
    /**
     * 
     */
    private String regimenGrupoEtareo;
    
    
    /**
     * 
     */
    private HashMap gruposEtareos;
    
    
    /**
     * 
     */
    private HashMap metas;;
    
	/**
	 * 
	 *
	 */
	public ActividadesProgramasPYP()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 */
	private HashMap diagnosticos;
	
	/**
	 * 
	 */
	private String servicioActividad;
	/**
	 * 
	 * @param property
	 */
	private boolean init(String tipoBD) 
	{
		if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getActividadesProgramasPYPDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void reset() 
	{
		this.programa="";
		this.actividad="";
		this.embarazo=false;
		this.semanasGestacion="";
		this.requerido=false;
		this.permitirEjecutar=false;
		this.activo=true;
		this.codigo="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.descArchivo="";
		this.actProgPYP=new HashMap();
		this.actProgPYP.put("numRegistros","0");
		this.viasIngreso=new HashMap();
		this.viasIngreso.put("numRegistros","0");
		this.regimenGrupoEtareo="";
		this.gruposEtareos=new HashMap();
		this.gruposEtareos.put("numRegistros","0");
		this.metas=new HashMap();
		this.metas.put("numRegistros","0");
		this.diagnosticos=new HashMap();
		this.diagnosticos.put("numRegistros","0");
		this.finalidadServicio="";
		this.finalidadConsulta="";
		this.servicioActividad="";
	}

	public HashMap getActProgPYP() {
		return actProgPYP;
	}

	public void setActProgPYP(HashMap actProgPYP) {
		this.actProgPYP = actProgPYP;
	}

	
	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @param institucion 
	 */
	public void consultarActivadesProgramasPYP(Connection con, String programa, String institucion) 
	{
		this.setActProgPYP((HashMap)objetoDao.consultarActivadesProgramasPYP(con,programa,institucion).clone());
		
	}


	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getDescArchivo() {
		return descArchivo;
	}

	public void setDescArchivo(String descArchivo) {
		this.descArchivo = descArchivo;
	}

	public boolean isEmbarazo() {
		return embarazo;
	}

	public void setEmbarazo(boolean embarazo) {
		this.embarazo = embarazo;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public boolean isRequerido() {
		return requerido;
	}

	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}

	public String getSemanasGestacion() {
		return semanasGestacion;
	}

	public void setSemanasGestacion(String semanasGestacion) {
		this.semanasGestacion = semanasGestacion;
	}


	public boolean insertarActividadPrograma(Connection con) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		transaccion=objetoDao.insertarActividadPrograma(con,this.institucion,this.programa.split(ConstantesBD.separadorSplit)[0],this.actividad,this.embarazo,this.semanasGestacion,this.requerido,this.descArchivo,this.activo,this.finalidadConsulta,this.finalidadServicio, this.permitirEjecutar);
		this.codigo=Utilidades.obtenerCodigoActividadPorPrograma(con,this.institucion,this.programa.split(ConstantesBD.separadorSplit)[0],this.actividad);
		if(transaccion)
			this.guardarDiagnosticos(con);
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return false;
		}

	}

	public boolean modifcarActividadPrograma(Connection con) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		transaccion=objetoDao.modifcarActividadPrograma(con,this.codigo,this.actividad,this.embarazo,this.semanasGestacion,this.requerido,this.descArchivo,this.activo,this.finalidadConsulta,this.finalidadServicio, this.permitirEjecutar);
		if(transaccion)
			this.guardarDiagnosticos(con);
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return false;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 */
	public void consultarActivadProgramaPYP(Connection con, String programa, String actividad, int institucion) 
	{
		HashMap mapa=objetoDao.consultarActivadProgramaPYP(con,programa,actividad,institucion);
		this.codigo=mapa.get("codigo")+"";
		this.institucion=institucion;
		this.programa=mapa.get("programa")+"";
		this.actividad=mapa.get("actividad")+"";
		this.embarazo=UtilidadTexto.getBoolean(mapa.get("embarazo")+"");
		this.semanasGestacion=mapa.get("semgestacion")==null||(mapa.get("semgestacion")+"").trim().equalsIgnoreCase("null")?"":mapa.get("semgestacion")+"";
		this.requerido=UtilidadTexto.getBoolean(mapa.get("requerido")+"");
		this.descArchivo=mapa.get("descarchivo")+"";
		this.activo=UtilidadTexto.getBoolean(mapa.get("activo")+"");
		this.permitirEjecutar=UtilidadTexto.getBoolean(mapa.get("permitirejecutar"));
		this.finalidadConsulta=mapa.get("finalidadconsulta")+"";
		this.finalidadServicio=mapa.get("finalidadservicio")+"";
		this.diagnosticos=objetoDao.consultarDiagnosticosActProPYP(con,this.codigo);
		this.servicioActividad=mapa.get("servicio")+"";
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	
	/**
	 * @param con 
	 * 
	 *
	 */
	private boolean guardarDiagnosticos(Connection con) 
	{
		boolean transaccion=true;
		for(int i=0;i<Integer.parseInt(this.diagnosticos.get("numRegistros")+"");i++)
		{
			if(!UtilidadTexto.getBoolean(this.diagnosticos.get("eliminado_"+i)+""))
			{
				if(!(this.diagnosticos.get("tiporegistro_"+i)+"").equalsIgnoreCase("BD"))
				{
					if(!objetoDao.guardarDiagnostico(con,this.codigo,(this.diagnosticos.get("acronimo_"+i)+""),(this.diagnosticos.get("cie_"+i)+"")))
					{
						transaccion=false;
						i=Integer.parseInt(this.diagnosticos.get("numRegistros")+"");
					}
				}
			}
			else if((this.diagnosticos.get("tiporegistro_"+i)+"").equalsIgnoreCase("BD"))
			{
				if(!objetoDao.eliminarDiagnostico(con,this.codigo,(this.diagnosticos.get("acronimo_"+i)+""),(this.diagnosticos.get("cie_"+i)+"")))
				{
					transaccion=false;
					i=Integer.parseInt(this.diagnosticos.get("numRegistros")+"");
				}
			}
		}
		return transaccion;
	}


	
	/**
	 * 
	 * @param con
	 * @param codigo2
	 */
	public boolean eliminarActividad(Connection con, String codigo) 
	{
		return objetoDao.eliminarActividad(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo2
	 */
	public void cargarViasIngreso(Connection con, String codigo) 
	{
		this.setViasIngreso(objetoDao.cargarViasIngresoActividadPrograma(con,codigo));
	}

	public HashMap getViasIngreso() {
		return viasIngreso;
	}

	public void setViasIngreso(HashMap viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param string2
	 * @param string3
	 * @return
	 */
	public boolean eliminarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion) 
	{
		return objetoDao.eliminarRegistroViaIngreso(con,codigoAP,viaIngreso,ocupacion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public boolean existeModificacionViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar) 
	{
		return objetoDao.existeModificacionViaIngreso(con,codigoAP,viaIngreso,ocupacion,solicitar,programar,ejecutar);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @return
	 */
	public boolean modificarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar) 
	{
		return objetoDao.modificarRegistroViaIngreso(con,codigoAP,viaIngreso,ocupacion,solicitar,programar,ejecutar);
	}

	
	/**
	 * 
	 * @param con
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @return
	 */
	public boolean insertarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar) 
	{
		return objetoDao.insertarRegistroViaIngreso(con,codigoAP,viaIngreso,ocupacion,solicitar,programar,ejecutar);
	}

	public String getRegimenGrupoEtareo() {
		return regimenGrupoEtareo;
	}

	public void setRegimenGrupoEtareo(String regimenGrupoEtareo) {
		this.regimenGrupoEtareo = regimenGrupoEtareo;
	}

	public HashMap getGruposEtareos() {
		return gruposEtareos;
	}

	public void setGruposEtareos(HashMap gruposEtareos) {
		this.gruposEtareos = gruposEtareos;
	}

	/**
	 * 
	 * @param con
	 * @param regimen 
	 * @param codigo2
	 */
	public void cargarGruposEtareos(Connection con, String codigo, String regimen) 
	{
		this.setGruposEtareos(objetoDao.cargarGruposEtareos(con,codigo,regimen));
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param regimenGrupoEtareo2
	 * @param string2
	 * @return
	 */
	public boolean eliminarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo) 
	{
		return objetoDao.eliminarRegistroGrupoEtareo(con,codigo,regimen,grupoEtareo);
	}

	/**
	 * 
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia 
	 * @return
	 */
	public boolean existeModificacionGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia) 
	{
		return objetoDao.existeModificacionGrupoEtareo(con,codigo,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}

	/**
	 * 
	 * @param con
	 * @param tipoFrecuencia 
	 * @param string
	 * @param regimenGrupoEtareo2
	 * @param string2
	 * @param string3
	 * @return
	 */
	public boolean modificarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia) 
	{
		return objetoDao.modificarRegistroGrupoEtareo(con,codigo,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @return
	 */
	public boolean insertarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia) 
	{
		return objetoDao.insertarRegistroGrupoEtareo(con,codigo,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}

	public HashMap getMetas() {
		return metas;
	}

	public void setMetas(HashMap metas) {
		this.metas = metas;
	}

	/**
	 * 
	 * @param con
	 * @param codigo2
	 */
	public void cargarMetas(Connection con, String codigo) 
	{
		this.setMetas(objetoDao.cargarMetas(con,codigo));
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @return
	 */
	public boolean eliminarRegistroMeta(Connection con, String codigo, String regimen) 
	{
		return objetoDao.eliminarRegistroMeta(con,codigo,regimen);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public boolean existeModificacionMetas(Connection con, String codigo, String regimen, String meta) 
	{
		return objetoDao.existeModificacionMetas(con,codigo,regimen,meta);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public boolean modificarRegistroMetas(Connection con, String codigo, String regimen, String meta) 
	{
		return objetoDao.modificarRegistroMetas(con,codigo,regimen,meta);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param string2
	 * @param string3
	 * @return
	 */
	public boolean insertarRegistroMeta(Connection con, String codigo, String regimen, String meta) 
	{
		return objetoDao.insertarRegistroMeta(con,codigo,regimen,meta);
	}

	public HashMap getDiagnosticos() {
		return diagnosticos;
	}

	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	public String getFinalidadConsulta() {
		return finalidadConsulta;
	}

	public void setFinalidadConsulta(String finalidadConsulta) {
		this.finalidadConsulta = finalidadConsulta;
	}

	public String getFinalidadServicio() {
		return finalidadServicio;
	}

	public void setFinalidadServicio(String finalidadServicio) {
		this.finalidadServicio = finalidadServicio;
	}

	public String getServicioActividad()
	{
		return servicioActividad;
	}

	public void setServicioActividad(String servicioActividad)
	{
		this.servicioActividad = servicioActividad;
	}
	
	public void setPermitirEjecutar(boolean permitirEjecutar) {
		this.permitirEjecutar = permitirEjecutar;
	}

	public boolean isPermitirEjecutar() {
		return permitirEjecutar;
	}


}
