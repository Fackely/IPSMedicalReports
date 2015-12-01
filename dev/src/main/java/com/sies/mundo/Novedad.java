/*
 * Created on 13/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.mundo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.sies.dao.NovedadDao;
import com.sies.dao.SiEsFactory;

/**
 * @author karenth
 *
 */
public class Novedad {

	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(Novedad.class);

	/**
	 * Código de la Novedad
	 */
	private int codigo;
	
	/**
	 * Nombre de la Novedad
	 */
	private String nombre;
	 
	/**
	 * Descripción de la Novedad
	 */
	private String descripcion;
	
	/**
	 * Indica si la novedad cuenta para novedad
	 */
	private boolean nomina;
	
	/**
	 * Estado de la Novedad
	 */
	private boolean activo;
	 
	
	/**
     * Codigo de la enfermera a que se le va a hacer la asignación
     */
    private int codigoEnfermera;
    
	
    
    /**
     * prioridad, indica si la novedad asignada, puede ser o no obviada
     */
    private boolean prioridad;
 
    /**
     * fecha en cual se registra la novedad
     */
    private String fechaRegistro;
    
    
    /**
     * Fecha para la cual se programa la novedad
     */
    private String fechaProgramacion;
    
  /**
   * Indica si la asociación de la novedad está activa aún 
   */
    private boolean activoAsociacion;
    
    /**
     * La explicación de la asociación de la novedad 
     */
    private String observacion;
    
    /**
     * codigo de la asociacion de la novedad a la enfermera
     */
    private int codigoAsociacion;
    
    /**
	 * Variable de Clase para la conexión con el DAO
	 */	
	private static NovedadDao novedadDao;
	
	/**
	 * @author mono
	 * Metodo que define la conexion a la BD y que reemplaza al que esta documentado
	 */
	public Novedad()
	{
		if(novedadDao==null)
		{
			novedadDao=SiEsFactory.getDaoFactory().getNovedadDao();
		}
	}
		
	/**
	 * Limpiar e inicializar atributos 
	 */
	public void clean()
	{
		codigo=0;
		nombre="";
		descripcion="";
		activo=false;
		nomina=false;
		codigoEnfermera=0;
        prioridad=false;
        fechaRegistro="";
        fechaProgramacion="";
        activoAsociacion=true;
        observacion="";
	}
	
	
	
	/**
	 * Permite la inserción de una Nueva novedad al sistema
	 * @param con
	 * @return la implementación del Dao correspondiente a este método
	 */
	public int insertar (Connection con)
	{
		return Novedad.novedadDao.insertarNovedad(con, nombre, descripcion, nomina, activo);
		
	}
	
	/**
	 * Me permite consultar en la base de datos si ya existe una novedad con ese mismo nombre
	 * @param con
	 * @param nombre
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEsta(Connection con, String nombre)
	{
		return Novedad.novedadDao.consultarEsta(con,nombre);
	}
	
	/**
	 * Permite hacer la consulta de todos los datos que estan la tabla
	 * @param con
	 * @return El dao para que devuelva la implementación que es
	 */
	public Collection<HashMap<String, Object>> consultarNovedades(Connection con)
	{
		return novedadDao.consultarNovedades(con);
	}
	
	
	public void consultarModificar(Connection con, int codigo) 
	{
		ResultSet rs=null;
	    try
	    {
	        rs=Novedad.novedadDao.consultarModificar(con,codigo);
	        rs.next();
	        this.codigo=rs.getInt("codigo");
	        this.nombre=rs.getString("nombre");
	        this.descripcion=rs.getString("descripcion");
	        this.nomina=rs.getBoolean("nomina");
	        this.activo=rs.getBoolean("estado");
	     }
	    catch(Exception e)
	    {
	        logger.warn("Error consultaModificar"+e.toString());    
	    }
	}
	
	/**
	 * Realiza la modificación ingresada
	 * @param con
	 */
	public void modificar(Connection con)
	{
	    Novedad.novedadDao.modificar(con,codigo,nombre,descripcion,nomina,activo);
	}
	
	/**
	 * @author mono
	 * Método que elimina una novedad
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarNovedad(Connection con, int codigo)
	{
		 return novedadDao.eliminarNovedad(con, codigo);	
	}
	
	/**
	 * activa o inactiva una Novedad específica
	 * @param con
	 * @param codigo
	 * @param activo
	 * @return
	 */
	/*public int eliminar (Connection con, int codigo, boolean activo)
	{
		 int elimino=0;
		 elimino=Novedad.novedadDao.eliminar(con, codigo, activo);
		 return elimino;	
	}*/
	
	
	/**
	 * Consulta los registros que concuerdan con la fecha, la novedad
	 * la enfermera 
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarNovedadEnfermera(Connection con, int codigoEnfermera, String fechaProgramacion)
	{
	    return novedadDao.consultarNovedadEnfermera(con, codigoEnfermera, fechaProgramacion);
	}
	
	
	/**
	 * Metodo que permite insertar una nueva asociacion de una enfermera a una novedad
	 * @param con
	 * @return
	 */
	public int insertarNovedadEnfermera(Connection con, boolean pasarFechaABD)
	{
		if(pasarFechaABD)
		{
			fechaProgramacion=UtilidadFecha.conversionFormatoFechaABD(fechaProgramacion);
		}
		//System.out.println("Este es el código de la enfermera"+codigoEnfermera);
		return Novedad.novedadDao.insertarNovedadEnfermera(con, codigoEnfermera, codigo, prioridad, fechaProgramacion, observacion);
	}
	
	
	/**
	 * Metodo que pasa una asociacion de novedad de un estado activo a inactivo
	 * @param con
	 * @param codigoAsociacion
	 * @return
	 */
	public int inactivarAsociacion(Connection con, int codigoAsociacion, boolean activo)
	{
	    return Novedad.novedadDao.inactivarAsociacion(con, codigoAsociacion, activo);
	}

	/**
	 * Inactiva una asociación entre el rango de fechas específico
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoProfesional
	 * @param activo
	 * @return
	 */
	public int cambiarEstadoAsociacion(Connection con, String fechaInicio, String fechaFin, int codigoProfesional, boolean activo)
	{
	    return Novedad.novedadDao.cambiarEstadoAsociacion(con, fechaInicio, fechaFin, codigoProfesional, activo);
	}

	/**
	 * Consulta las novedades que se encuentran asociadas a una enfermera, que aun est activas
	 * y que su fecha de programación es mayor o igual al día de hoy, usada en el cuadro de turnos
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoMedico
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarNovEnfermera(Connection con, int codigoEnfermera, String fechaInicio, String fechaFin)
	{
	   return Novedad.novedadDao.consultarNovEnfermera(con, codigoEnfermera, fechaInicio, fechaFin);
	}
	
	
	/**
	 * Consulta todas las novedades asociadas a una enfermera sin incluir las inactivas
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTodasNovedadesEnfermera(Connection con, int codigoEnfermera)
	{
	   Collection<HashMap<String, Object>> cole =	Novedad.novedadDao.consultarTodasNovedadesEnfermera(con, codigoEnfermera);
	   //System.out.println("Cole:"+cole.size());
	   return cole;
	}
	
	/**
	 * Metodo para consultar una asociacion específicamente
	 * @param con
	 * @param codigoAsociacion
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEstaNovEnf(Connection con, int codigoAsociacion)
	{
	    return Novedad.novedadDao.consultarEstaNovEnf(con, codigoAsociacion);
	}
	
	
	/**
	 * Metodo para modificar los datos de la tabla novedad_enfermera
	 * @param con
	 * @return
	 */
	public int modificarNovedadEnfermera(Connection con)
	{
	    return Novedad.novedadDao.modificarNovedadEnfermera(con, codigoEnfermera, codigo, fechaProgramacion, prioridad, observacion);
	}
	
	/**
	 * Metodo para consultar las novedades que tiene asociada una enfermera en un periodo determinado
	 * incluida la opcion de todas las enfermeras
	 * @param con
	 * @param codigoProfesional
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoInstitucion
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteNov(Connection con, int codigoProfesional, String fechaInicio, String fechaFin, String fechaProgramacion,int codigoNovedad)
	{
		return Novedad.novedadDao.consultarTurnosReporteNov(con, codigoProfesional, fechaInicio, fechaFin, fechaProgramacion, codigoNovedad);
	}
	
	
	/**
	 * @return Returns the activo.
	 */
	public boolean getActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the nomina.
	 */
	public boolean getNomina() {
		return nomina;
	}
	/**
	 * @param nomina The nomina to set.
	 */
	public void setNomina(boolean nomina) {
		this.nomina = nomina;
	}
	
	
	
	
	
	
	
/**
 * @return Returns the activoAsociacion.
 */
public boolean getActivoAsociacion() {
    return activoAsociacion;
}
/**
 * @param activoAsociacion The activoAsociacion to set.
 */
public void setActivoAsociacion(boolean activoAsociacion) {
    this.activoAsociacion = activoAsociacion;
}
    /**
     * @return Returns the codigoEnfermera.
     */
    public int getCodigoEnfermera() {
        return codigoEnfermera;
    }
    /**
     * @param codigoEnfermera The codigoEnfermera to set.
     */
    public void setCodigoEnfermera(int codigoEnfermera) {
        this.codigoEnfermera = codigoEnfermera;
    }
    /**
     * @return Returns the fechaProgramacion.
     */
    public String getFechaProgramacion() {
        return fechaProgramacion;
    }
    /**
     * @param fechaProgramacion The fechaProgramacion to set.
     */
    public void setFechaProgramacion(String fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }
    /**
     * @return Returns the fechaRegistro.
     */
    public String getFechaRegistro() {
        return fechaRegistro;
    }
    /**
     * @param fechaRegistro The fechaRegistro to set.
     */
    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    /**
     * @return Returns the observacion.
     */
    public String getObservacion() {
        return observacion;
    }
    /**
     * @param observacion The observacion to set.
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    /**
     * @return Returns the prioridad.
     */
    public boolean getPrioridad() {
        return prioridad;
    }
    /**
     * @param prioridad The prioridad to set.
     */
    public void setPrioridad(boolean prioridad) {
        this.prioridad = prioridad;
    }
    /**
     * @return Returns the codigoAsociacion.
     */
    public int getCodigoAsociacion() {
        return codigoAsociacion;
    }
    /**
     * @param codigoAsociacion The codigoAsociacion to set.
     */
    public void setCodigoAsociacion(int codigoAsociacion) {
        this.codigoAsociacion = codigoAsociacion;
    }
    
    public boolean consultarNovedadEnfermeraTieneTurno(Connection con, int codigoRegistro) throws Exception
	{
		return novedadDao.consultarNovedadEnfermeraTieneTurno(con, codigoRegistro);
	}

}
