/*
 * Created on 4/04/2005
 *
 */
package com.sies.mundo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.ResultadoBoolean;

import com.sies.dao.CategoriaDao;
import com.sies.dao.SiEsFactory;

/**
 * @author karenth
 * 
 * Juan David Ram�rez L.
 */
public class Categoria {
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(Categoria.class);
	
	/**
	 * Codigo de la categor�a a la cual se asigna ha una enfermera
	 */
	private int codigo;
	
	/**
	 * Nombre de la categor�a
	 */
	private String nombre;
	 
	/**
	 * Descripci�n de la categor�a
	 */
	private String descripcion;
	 
	/**
	 * Indica si la categor�a esta o no activa en el sistema
	 */	 
	private boolean activo;

	/**
	 * Codigo asignado por el sistema a la enfermera 
	 */ 
	private int codigoPersona;
	
	/**
	 * fecha de inicio de la asociaci�n de la categor�a a la enfermera
	 */
	private String fechaInicio;
	   
	/**
	 * fecha de finalizaci�n de la asociaci�n de la enfermera a la categor�a
	 */
	private String fechaFin;

	/**
	 * Variable de Clase para la conexi�n con el DAO
	 */	
	private CategoriaDao categoriaDao;

	/**
	 * Centro de costo asociado a la categor�a
	 */
	private int centroCosto;
	
	/**
	 * Color de la categor�a
	 */
	private String color;
	
	/**
	 * @author mono
	 * Metodo que define la conexion a la BD y que reemplaza al que esta documentado
	 */
	
	public Categoria()
	{
		if(categoriaDao==null)
		{
			categoriaDao=SiEsFactory.getDaoFactory().getCategoriaDao();
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
		centroCosto=0;
		activo=false;
		codigoPersona=0;
		fechaInicio="";
		fechaFin="";
		color="#FFFFFF";
	}
	
		
	/**
	 * Obtener el c�digo de la categor�a
	 * @return retorna el codigo de la categor�a
	 */
	public int getCodigo()
	{
		return codigo;
	}
	
	/**
	 * Set del c�digo de la categoria
	 * @param codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}
	
	/**
	 * get del Nombre de la categoria
	 * @return nombre retorna nombre que es
	 *  el nombre de la categor�a
	 */
	public String getNombre()
	{
		return nombre;
		}
	
	/**
	 * set del Nombre  de la categor�a
	 * @param nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
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
	 * @return Returns the activo.
	 */
	public boolean getActivo ()
	{
		return activo;
	}
	
	
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(boolean activo)
	{
		this.activo=activo;
	}
	
	
	/**
	 * M�todo que inserta una nueva categor�a al sistema 
	 * @param con
	 * @return
	 */
	public int insertar (Connection con)
	{
		return categoriaDao.insertarCategoria(con, nombre, descripcion, activo, centroCosto, color);
		
	}
	
	/**
	 * M�todo que consulta si el nombre que ingresa ya esta siendo utilizado en la BD
	 * @param con
	 * @param nombre
	 * @return
	 */
	public Collection<HashMap<String, Object>> categoriaUtilizada(Connection con, String nombre)
	{
		return categoriaDao.categoriaUtilizada(con,nombre);
	}
	
	/**
	 * M�todo que hace una consulta de todas las categor�as que hay en la base de datos
	 * @param con
	 * @param centroCosto Si este par�metro es diferente de null se utilizar� como filtro
	 * @return Listado de todas las categor�as existentes en el sistema
	 */
	public Collection<HashMap<String, Object>> consultarCategorias(Connection con, Integer centroCosto)
	{
		return categoriaDao.consultarCategorias(con, false, centroCosto);
	}

	/**
	 * M�todo que hace una consulta de todas las categor�as que hay en la base de datos
	 * @param con
	 * @param centroCosto Si este par�metro es diferente de null se utilizar� como filtro
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCategoriasActivas(Connection con, Integer centroCosto)
	{
		return categoriaDao.consultarCategorias(con, true, centroCosto);
	}

	/**
	 * M�todo que consulta la informaci�n de una sola categor�a, cuando esta va a 
	 * ser modificada
	 * @param con
	 * @param codigo
	 */
	public void consultarModificar(Connection con, int codigo) 
	{
		ResultSet rs=null;
		try
		{
			rs=categoriaDao.consultarModificar(con,codigo);
			rs.next();
			this.codigo=rs.getInt("codigo");
			this.nombre=rs.getString("nombre");
			this.descripcion=rs.getString("descripcion");
			this.centroCosto=rs.getInt("centro_costo");
			this.activo=rs.getBoolean("estado");
			this.color=rs.getString("color");
		}
		catch(Exception e)
		{
			logger.warn("Error consultaModificar "+e.toString());    
		}
	}
	
	
	/**
	 * M�todo que actualiza los datos despues de que se hace una modificaci�n
	 * @param con
	 */
	public void modificar(Connection con)
	{
	    categoriaDao.modificar(con,codigo,nombre,descripcion,activo, centroCosto, color);
	}
	
	
	
	/**
	 * @author mono
	 * M�todo que activa/inactiva una categor�a
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarCategoria(Connection con, int codigo)
	{
		 return categoriaDao.eliminarCategoria(con, codigo);	
	}
	
	/**
	 * M�todo que consulta las enfermeras que se encuentran asignadas en el momento 
	 * a determinada categor�a
	 * @param con
	 * @param codigo
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigo, boolean ordenar)
	{
		return categoriaDao.consultarEnfermeraCategoria(con, codigo, ordenar);
	}
	
	/**
	 * M�todo que consulta las categor�as que tiene asociadas una enfermera;
	 */
	public Collection<HashMap<String, Object>> consultarCategoriasEnfermera(Connection con, int codigo_enfermera)
	{
		return categoriaDao.consultarCategoriasEnfermera(con, codigo_enfermera);
	}
	
	/**
	 * M�todo que retorna el nombre de la enfermera
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultarEnfermera(Connection con, int codigo)
	{
	    return categoriaDao.consultarEnfermera(con,codigo);
	}
	
	
	/**
	 * M�todo que inserta una nueva asignaci�n de enfermeras a categor�a
	 * en la tabla categoria_enfermera
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public int insertarEnfermeraCategoria(Connection con, String fechaInicio, String fechaFin)
	{
		return categoriaDao.insertarEnfermeraCategoria(con, codigoPersona, codigo, fechaInicio, fechaFin);
		
	}

	/**
	 * M�todo que actualiza en campo de fecha de finalizaci�n con la fecha actual
	 * de la asignaci�n, cuando una enfermera es asignada a otra categor�a
	 * @param con
	 * @param codigoPersona
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @param eliminarTurnos
	 * @param codigoCategoriaOrigen
	 * @return
	 */
	public int actualizarEnfermeraCategoria(Connection con, int codigoPersona, String fechaInicio, String fechaFin, int codigoCategoria, boolean eliminarTurnos, Integer codigoCategoriaOrigen)
	{
		return categoriaDao.actualizarEnfermeraCategoria(con, codigoPersona, fechaInicio, fechaFin, codigoCategoria, eliminarTurnos, codigoCategoriaOrigen);
	}

    //   Asignar Restricciones a categor�a
    
    /**
	 * M�todo que consulta las restricciones que se encuentran asignadas en el momento 
	 * a determinada categoria
	 * @param con
     * @param codigo
     * @return
	 */
	public Collection<HashMap<String, Object>> consultarRestriccionCategoria(Connection con, int codigo)
	{
		return categoriaDao.consultarRestriccionCategoria(con, codigo);
	}
	
	
	/**
	 * M�todo que actualiza la fecha de finalizaci�n de la asociaci�n a la fecha actual
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoCategoria
	 * @return
	 */
	public int actualizarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoCategoria)
	{
	    return categoriaDao.actualizarRestriccionCategoria(con, codigoRestriccion, codigoCategoria);
	}
	
	/**
	 * M�todo que inserta una nueva Restriccion a la categor�a
	 * en la tabla categoria_restriccion
	 * @param con
	 * @return
	 */
	public int insertarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoCategoria, int valor)
	{
		return categoriaDao.insertarRestriccionCategoria(con, codigoRestriccion, codigoCategoria, valor);
		
	}

	/**
	 * M�todo que elimina los datos que se encuentran en la tabla categoria_restriccion
	 * @param con
	 * @param codigo
	 * @param activo
	 * @return
	 */
	public int eliminarTabla (Connection con)
	{
		 int elimino=0;
		 elimino=categoriaDao.eliminarTabla(con);
		 return elimino;	
	}
    
	
	public ResultadoBoolean estaEnfermeraEnCategoria(Connection con, int codigoProfesional)
	{
		return categoriaDao.estaEnfermeraEnCategoria(con, codigoProfesional);
	}
	
	/**
	 * M�todo que retorna un Collection los datos de las enfermeras que cumplen con las
	 * condificiones de una b�squeda avanzada
	 * @param con
	 * @param codigo
	 * @param nombreProfesional
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermerasAvanzada(Connection con, int codigo, String nombreProfesional, int codigoCategoria)
	{
		return categoriaDao.consultarEnfermerasAvanzada(con, codigo,nombreProfesional, codigoCategoria);
	}

	/**
	 * @return centroCosto
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @param centroCosto Asigna centroCosto
	 */
	public void setCentroCosto(int centroCosto)
	{
		this.centroCosto = centroCosto;
	}

	/**
	 * @return codigoPersona
	 */
	public int getCodigoPersona()
	{
		return codigoPersona;
	}

	/**
	 * @param codigoPersona Asigna codigoPersona
	 */
	public void setCodigoPersona(int codigoPersona)
	{
		this.codigoPersona = codigoPersona;
	}

	/**
	 * @return fechaFin
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * @param fechaFin Asigna fechaFin
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * @return fechaInicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * @param fechaInicio Asigna fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}
    
	/**
	 * Consulta las personas que no se encuentran en la categor�a seleccionada
	 * @param con
	 * @param codigoCategoria
	 * @param codigoCentroAtencion 
	 * @return Listado de personas
	 */
	public Collection<HashMap<String, Object>> consultarPersonas(Connection con, Integer codigoCategoria, Integer codigoCentroAtencion)
	{
		return categoriaDao.consultarPersonas(con, codigoCategoria, codigoCentroAtencion);
	}

	/**
	 * (Juan David)
	 * M�todo que indica si existe un cuadro de turnos destino
	 * para mover los turnos de las personas
	 * @param connection
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public String existeCuadroDestino(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return categoriaDao.existeCuadroDestino(con, codigoCategoria, fechaInicio, fechaFin);
	}

	/**
	 * (Juan David)
	 * M�todo para consultar si existen turnos generados al momento de cancelar
	 * una sociaci�n
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoPersona
	 * @return
	 */
	public boolean existenTurnosGenerados(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int codigoPersona)
	{
		return categoriaDao.existenTurnosGenerados(con, codigoCategoria, fechaInicio, fechaFin, codigoPersona);
	}

	/**
	 * M�todo para actualizar la fecha de finalizaci�n de una asignaci�n de persona
	 * @param con conexi�n con la BD
	 * @param codigoPersona C�digo de la persona que se desea actualizar
	 * @param codigoCategoria C�digo de la categor�a
	 * @param fechaFinAsociacion fecha actual de finalizaci�n
	 * @param limite Fecha a la cual se desea actualizar la fecha fin
	 * @return n�mero de registros actualizados en la BD
	 */
	public int actualizarFechaFin(Connection con, int codigoPersona, int codigoCategoria, String fechaFinAsociacion, String limite)
	{
		return categoriaDao.actualizarFechaFin(con, codigoPersona, codigoCategoria, fechaFinAsociacion, limite);
	}

	/**
	 * @return color
	 */
	public String getColor()
	{
		return color;
	}

	/**
	 * @param color Asigna color
	 */
	public void setColor(String color)
	{
		this.color = color;
	}
	
	/**
	 * M�todo para consultar las categor�as en las cuales se puede hacer un
	 * cubrimiento de turno
	 * @param con
	 * @param fecha
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCategoriasDestino(Connection con, String fecha)
	{
		return categoriaDao.consultarCategoriasDestino(con, fecha);
	}
	
	/**
	 * M�todo para consultar el nombre de una categor�a dado esu c�digo
	 * @param con Conexi�n con la base de datos
	 * @param codigoCategoria C�digo de la categor�a a consultar
	 */
	public void consultar(Connection con, int codigoCategoria)
	{
		Collection<HashMap<String, Object>> categorias=categoriaDao.consultar(con, codigoCategoria);
		Iterator<HashMap<String, Object>> iterador=categorias.iterator();
		if(iterador.hasNext())
		{
			HashMap<String, Object> categoria=(HashMap<String, Object>)iterador.next();
			codigo=Integer.parseInt(categoria.get("cat_identificador")+"");
			nombre=(String)categoria.get("cat_nombre");
			descripcion=(String)categoria.get("cat_descripcion");
			activo=(Boolean)categoria.get("cat_activo");
			centroCosto=Integer.parseInt(categoria.get("centro_costo")+"");
			color=(String)categoria.get("color");
		}
	}
}