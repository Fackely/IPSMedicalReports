/*
 * Creado  17/08/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RegUnidadesConsultaDao;
import com.princetonsa.dao.sqlbase.SqlBaseRegUnidadesConsultaDao;


/**
 * Clase para manejar
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class RegistroUnidadesConsulta
{

/*
 * manejo de errores
 */
    
    private static Logger logger=Logger.getLogger(RegistroUnidadesConsulta.class);    
    
    private int codigoT;
    
/*
* 
*/
    private RegUnidadesConsultaDao registrosUnidades;
/*
* Campo para capturar la descripcion de la unidad de 
* consulta.
*/
    private String Descripcion;
/*
* Campo para capturar el codServicio asociado a la
* unidad de consulta.
*/
    private int codServicio;
/*
 *Campo que permite Activar/Inactivar las unidades de 
 *consulta.  
*/
    private boolean Activo;
/*
* Atributo para manejar los estados del workFlow
*/
    private String estado;
         
/*
*Atributo para capturar el codigo de la especialidad
*de la tabla Servicios del campo especialidad. 
*/
    private int codigoEspecialidad;    
	
    /**
     * 
     */
    private String especialidadUniAgen;
    private String nomEspecialidadUniAgen;
    
	/**
	 * HashMap para almacenar los servicios de una Unidad de Consulta.
	 */
	private HashMap servicios;  
    /**
     * 
     */
	
	/*
	 * Atributo adicionado por documento 869 
	 */
	private String tiposAtencion;
	
	/*
	 * Atributo para almacenar el color seleccionado en la paleta de colores documento 869
	 */
	private String colorFondo;
	
    public RegistroUnidadesConsulta()
    {
        clean();
        init();
    }
public String getTiposAtencion() {
		return tiposAtencion;
	}
	public void setTiposAtencion(String tiposAtencion) {
		this.tiposAtencion = tiposAtencion;
	}
	public String getColorFondo() {
		return colorFondo;
	}
	public void setColorFondo(String colorFondo) {
		this.colorFondo = colorFondo;
	}
/**
 * @return Retorna  estado.
 */
public String getEstado()
{
    return estado;
}
/**
 * @param estado asigna estado.
 */
public void setEstado(String estado)
{
    this.estado = estado;
}

/**
 * @return Retorna  descripcion.
 */
public String getDescripcion()
{
    return Descripcion;
}
/**
 * @param descripcion asigna descripcion.
 */
public void setDescripcion(String descripcion)
{
    this.Descripcion = descripcion;
}
/**
 * @return Retorna  estado.
 */
public boolean getActivo()
{
    return Activo;
}
/**
 * @param estado asigna estado.
 */
public void setActivo(boolean activo)
{
    this.Activo = activo;
}
/**
 * @return Retorna  codServicio.
 */
public int getCodServicio()
{
    return codServicio;
}
/**
 * @param codServicio asigna codServicio.
 */
public void setCodServicio(int codServicio)
{
    this.codServicio = codServicio;
}

/**
 * @return Retorna  codigoEspecialidad.
 */
public int getCodigoEspecialidad()
{
    return codigoEspecialidad;
}
/**
 * @param codigoEspecialidad asigna codigoEspecialidad.
 */
public void setCodigoEspecialidad(int codigoEspecialidad)
{
    this.codigoEspecialidad = codigoEspecialidad;
}

/**
 * @return Retorna  codigoT.
 */
public int getCodigoT()
{
    return codigoT;
}
/**
 * @param codigoT asigna codigoT.
 */
public void setCodigoT(int codigoT)
{
    this.codigoT = codigoT;
}


/**
 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
 * @param tipoBD el tipo de base de datos que va a usar este objeto
 * (e.g., Oracle, PostgreSQL, etc.). 
 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
 */
	public void /*boolean*/ init()
	{
	   //boolean wasInited = false;
	    String tipoBD = System.getProperty("TIPOBD" );
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
   	   
   	    //if (myFactory != null) 
   	    //{
   	        registrosUnidades=(RegUnidadesConsultaDao)myFactory.getRegUnidadesConsultaDao();
			//wasInited = (registrosUnidades != null);
		//}

		//return wasInited;
	}
/**
 * Metodo para limpiar los atributos.
 */
	public void clean ()
	{
	    this.Descripcion="";
	    this.codServicio=0;
	    this.codigoEspecialidad=0;
	    this.Activo=false;
	    this.estado="";
	    this.codigoT=-1;
	    this.servicios=new HashMap();
	    this.servicios.put("numeroServicios","0");
	    this.especialidadUniAgen = "";
	    this.nomEspecialidadUniAgen = "" ;
	    this.tiposAtencion="";
	    this.colorFondo="";
	}
	
	
	/**
	 * Metodo para insertar los datos de este objeto.
	 */
	public boolean insertar (Connection con)
	{
		//metodo que Inserta el cabezote de la unidad de consulta.
		boolean enTransaccion=true;
		try 
		{
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			int codigoUC=registrosUnidades.insertarRegUniCon (con,Descripcion,Activo,Utilidades.convertirAEntero(especialidadUniAgen),tiposAtencion,colorFondo);			
			enTransaccion=codigoUC>0;
			this.codigoT = codigoUC;
			int numReg=Integer.parseInt(this.servicios.get("numeroServicios")+"");
			for(int i=0;i<numReg;i++)
			{
				if((tiposAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral)) || (tiposAtencion.equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica)  /* && (servicios.get("atencionOdon_"+i)+"").equals(ConstantesBD.acronimoSi) */))
				{
					if(enTransaccion)
					{
						Log4JManager.info(this.servicios.get("codigo_"+i));
						
						
						String tmp[] = this.servicios.get("codigo_"+i).toString().split("-");
						int codServicio = 0;
						if (tmp.length >1) {
							codServicio=Integer.parseInt(tmp[1]);
						}else{
							codServicio=Integer.parseInt(tmp[0]);
						}
						
						
						int especialidad = ConstantesBD.codigoNuncaValido;
						if(!this.servicios.get("especialidad_"+i).toString().equals(""))
							 especialidad = Integer.parseInt(this.servicios.get("especialidad_"+i)+"");					
						
						enTransaccion=registrosUnidades.insertarDetalle(con,codigoUC,codServicio,especialidad);
					}				
					else
					{
						i=numReg;
					}
				}
			}
			if(!enTransaccion)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return false;
			}
			else
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		catch (Exception e){
			Log4JManager.error(e);
			Log4JManager.info(e);
			return false;
		}
	}
	
	/**
	 * Metodo para insertar el detalle
	 */
	public boolean insertarDetalle (Connection con,int codigoUC,int codServicio,int especialidad)
	{
		return registrosUnidades.insertarDetalle(con,codigoUC,codServicio,especialidad);
	}
	
	
	/**
	    * Metodo para realizar la busqueda avanzada segun por los 
	    * campos que se desee consultar.
	    * 
	     * @param con Connection, Conexion a la base de datos.
	     * @param codigo Codigo de la unidad de consulta.
	     * @param descripcion Descripcion de la unidad de consulta.
	     * @param codServicio Codigo de la especialidad.
	 * @param especialidad 
	     * @param activo, Estado true o false.
	     * @return ResultSetDecorator con toda la consulta.
	     */
	public Collection consultaAvanzada(Connection con,int Codigo, String Descripcion, int codServicio,boolean Activo, int especialidad, String temp)
    {
        ResultSetDecorator rs=null;
        Collection coleccion=null;
        try
        {
            rs=registrosUnidades.consultaAvanzada(con, Codigo, Descripcion, codServicio, Activo, especialidad,temp);
            coleccion=UtilidadBD.resultSet2Collection(rs);
            	                        
		}
		catch(Exception e)
		{

            logger.warn("Error consulta avanzada Registro Unidades Consulta(Mundo)"+e.toString());  
			
		}
        
      return coleccion;  
    }
	
	/**
	 * Metodo empleado para realizar una consulta general, de todos
	 * los registros presentes en la BD, para esta tabla.
	 * @param con, Connection con la BD.
	 * @return,Collection
	 */
	public Collection consultarTodo(Connection con)
	{
        ResultSetDecorator rs=null;
        Collection coleccion=null;
        try
        {
          rs=registrosUnidades.consultarTodo(con);
          coleccion=UtilidadBD.resultSet2Collection(rs);
        }
        catch (Exception e)
        {
            logger.warn(e+"Error consultaTodo->Registro Unidades Consulta(Mundo)"+e.toString());
        }
	    return coleccion;  
	}
	
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public Collection consultarCUPS(Connection con)
	{
	    ResultSetDecorator rs=null;
        Collection coleccion=null;
        try
        {
          rs=registrosUnidades.consultarCUPS(con);
          coleccion=UtilidadBD.resultSet2Collection(rs);
        }
        catch (Exception e)
        {
            logger.warn(e+"Error consultaCUPS"+e.toString());
        }
        return coleccion;  
        
	}
	
	
	/**
	 * Metodo para realizar la busqueda del registro que se modificara,
	 * segun el codigo correspondiente.
	 * @param con, Connection con la BD.
	 * @param codigo, Codigo por el cual se buscara.
	 * 
	 */
	public void consultaModificar(Connection con, int codigo)
	{
	    ResultSetDecorator rs=null;
	    ResultSetDecorator rsServicios=null;
	    try
	    {
	        rs=registrosUnidades.consultaModificar(con,codigo);
	        rs.next();
	        codigoT=rs.getInt("codigo");
	        Descripcion=rs.getString("descripcion");
	        Activo=rs.getBoolean("activa");
	        especialidadUniAgen=rs.getString("especialidad");
	        colorFondo=rs.getString("color");
	        tiposAtencion=rs.getString("tipo_atencion");
	        rsServicios=registrosUnidades.consultaServiciosModificacion(con,codigo);
	        int numServicios=0;
	        while (rsServicios.next())
	        {
	        	this.servicios.put("codigo_"+numServicios,rsServicios.getString("codigo_servicio"));
	        	
	        	
	        	//this.servicios.put("codigo_"+numServicios,rsServicios.getString(5)+"-"+rsServicios.getString(1));
	        	this.servicios.put("codigocups_"+numServicios,rsServicios.getString("codigocups"));
	        	//this.servicios.put("especialidad_"+numServicios,rsServicios.getString(2));
	        	this.servicios.put("descripcionespecialidad_"+numServicios,rsServicios.getString(3));	        	
	        	this.servicios.put("tiposervicio_"+numServicios,rsServicios.getString(4));
	        	this.servicios.put("descripcion_"+numServicios,rsServicios.getString(5));	        
	        	
	        	this.servicios.put("tipo_"+numServicios,"BD");
	        	numServicios++;
	        }
	        this.servicios.put("numeroServicios",numServicios+"");
	        this.servicios.put("registrosEliminados",0+"");
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error consultaModificar"+e.toString());    
	    }
        
        
	}
	
	
	/**
	 * Metodo empleado para modificar todos los campos de un registro, 
	 * segun el codigo.
	 * @param especialidad 
	 * @param color 
	 * @param con, Connection con la BD.
	 * @param codigo, Codigo por el cual se modificara el registro.
	 * @param descripcion, Campo Descripcion.
	 * @param activa, Campo de Estado.
	 */
	public int modificar(Connection con, int codigo,String descripcion,boolean activa, int especialidad, String color)
	{
	    int resp = registrosUnidades.modificar(con,codigo,descripcion,activa, especialidad, color);
	    return resp;
	}
	
	
	/**
	 * Metodo para eliminar un Registro.
	 * @param con, Connection con la BD.
	 * @param codigo, Codigo por el cual se eliminara el registro.
	 * @return, int 1 exitoso, 0 de lo contrario.
	 */
	
	public int  eliminar(Connection con, int codigo)
	{
	    int elimino=0;
	    registrosUnidades.eliminarDetalles(con,codigo);
	    elimino=registrosUnidades.eliminar(con,codigo);
	    return elimino;
	}

    
	/**
	 * @return Returns the servicios.
	 */
	public HashMap getServicios() {
		return servicios;
	}
	/**
	 * @param servicios The servicios to set.
	 */
	public void setServicios(HashMap servicios) {
		this.servicios = servicios;
	}
	/**
	 * @return Returns the servicios.
	 */
	public Object getServicios(String key) {
		return servicios.get(key);
	}
	/**
	 * @param servicios The servicios to set.
	 */
	public void setServicios(String key,Object value) {
		this.servicios.put(key,value);
	}
	/**
	 * @param con 
	 * @param codigoT
	 * @param codigoServicio
	 */
	public void eliminarServicio(Connection con, int codigoT, int codigoServicio) 
	{
		registrosUnidades.eliminarServicio(con,codigoT,codigoServicio);
	}
	/**
	 * @param con
	 * @param codigoT
	 * @param servicioNuevo
	 * @param servicioAntiguo
	 */
	public void modificarServico(Connection con, int codigoT, int servicioNuevo, int servicioAntiguo,int especialidad) 
	{
		registrosUnidades.modificarServico(con,codigoT,servicioNuevo,servicioAntiguo,especialidad);
	}
	
	/**
	 * M�todo que realiza la consulta de los servicios de la unuidad de consulta
	 * @param con
	 * @param codigoUnidadConsulta
	 * @param codigoServicio
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> obtenerServiciosUnidadConsulta(
			Connection con,
			String codigoUnidadConsulta,
			String codigoServicio,
			int institucion,
			boolean verificarSexoPaciente,
			int codigoPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoUnidadConsulta",codigoUnidadConsulta);
		campos.put("codigoServicio",codigoServicio);
		campos.put("institucion",institucion);

		campos.put("validarSexoPaciente",verificarSexoPaciente);
		campos.put("codigoPaciente",codigoPaciente);

		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegUnidadesConsultaDao().obtenerServiciosUnidadConsulta(con, campos);
	}
	/**
	 * @return the especialidadUniAgen
	 */
	public String getEspecialidadUniAgen() {
		return especialidadUniAgen;
	}
	/**
	 * @param especialidadUniAgen the especialidadUniAgen to set
	 */
	public void setEspecialidadUniAgen(String especialidadUniAgen) {
		this.especialidadUniAgen = especialidadUniAgen;
	}
	
	public String obtenerEspecialidad(Connection con,int codigoEpecialidad)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigo", codigoEpecialidad);
		return SqlBaseRegUnidadesConsultaDao.obtenerEspecialidad(con, parametros);
	}
	/**
	 * @return the nomEspecialidadUniAgen
	 */
	public String getNomEspecialidadUniAgen() {
		return nomEspecialidadUniAgen;
	}
	/**
	 * @param nomEspecialidadUniAgen the nomEspecialidadUniAgen to set
	 */
	public void setNomEspecialidadUniAgen(String nomEspecialidadUniAgen) {
		this.nomEspecialidadUniAgen = nomEspecialidadUniAgen;
	}
	
	/**
	 * Metodo que cuenta cuanta veces esta asociada una unidad de agenda con la tabla horario de atencion
	 * @param con
	 * @param unidad_consulta
	 * @return
	 */
	public static int verificarUniAgenAsoHorarioAten(Connection con, int unidad_consulta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegUnidadesConsultaDao().verificarUniAgenAsoHorarioAten(con, unidad_consulta);
	}
	
}