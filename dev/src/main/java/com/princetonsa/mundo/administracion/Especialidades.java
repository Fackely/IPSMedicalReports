package com.princetonsa.mundo.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.EspecialidadesDao;
import com.princetonsa.dao.manejoPaciente.AutorizacionesDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseEspecialidadesDao;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Víctor Hugo Gómez L.
 */

public class Especialidades 
{
	/**
	 * Manejador de mensajes
	 */
	private static Logger logger = Logger.getLogger(Especialidades.class);
	
	/**
	 * Listado Especialidades 
	 */
	private ArrayList<DtoEspecialidades> especialidades = new ArrayList<DtoEspecialidades>();
	
	/**
	 * instancia del DAO
	 * @return
	 */
	public static EspecialidadesDao getEspecialidadessDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEspecialidadesDao();
	}
	
	/**
	 * Constructor
	 */
	public Especialidades()
	{
		this.especialidades = new ArrayList<DtoEspecialidades>();
	}
	
	/**
	 * Guarda y/o Modifica Especilidades
	 * @param con
	 * @param usuario
	 * @return
	 */
	public ArrayList<DtoEspecialidades> guardarEspecilidades(Connection con, UsuarioBasico usuario)
	{
		for(int i=0;i<this.getEspecialidades().size();i++)
		{
			if(this.getEspecialidades().get(i).getModificar().equals(ConstantesBD.acronimoSi) 
					&& this.getEspecialidades().get(i).getEliminar().equals(ConstantesBD.acronimoNo))
			{
				if(this.getEspecialidades().get(i).getIngresar().equals(ConstantesBD.acronimoSi))// se ingresa una nueva Especialdad
				{
					DtoEspecialidades especilidadIns = (DtoEspecialidades) this.getEspecialidades().get(i);
					especilidadIns.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
					especilidadIns.setUsuarioGrabacion(usuario.getLoginUsuario());
					this.insertarEspecialidad(con, especilidadIns);
				}else{ // se Modifica una Nueva Especialidad
					DtoEspecialidades especilidadMod = (DtoEspecialidades) this.getEspecialidades().get(i);
					especilidadMod.setUsuarioGrabacion(usuario.getLoginUsuario());
					this.updateEspecialidad(con, especilidadMod);
				}	
			}
		}
		return this.cargarListadoEspecialidades(con, usuario.getCodigoInstitucion());
	}
	
	/**
	 * Insertar una Autorizacion
	 * @param Connection con
	 * @param DtoEspecialidades dtoEspecialidades
	 */
	public static int insertarEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades){
		return getEspecialidadessDao().insertarEspecialidad(con, dtoEspecialidades);
	}
	
	/**
	 * Modificacion Especialidad
	 * @param Connection con
	 * @param DtoEspecialidades dtoEpecialidades
	 * 
	 */
	public static int updateEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades){
		return getEspecialidadessDao().updateEspecialidad(con, dtoEspecialidades);
	}
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoEspecialidades> cargarListadoEspecialidades(Connection con, String institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion", institucion);
		return getEspecialidadessDao().cargarListadoEspecialidades(con, parametros);
	}

	/**
	 * Metodo que Elimina  una Especialida 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static int deleteEspecialidades(Connection con, int codigo_especialidad)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigo_especialidad", codigo_especialidad);
		return getEspecialidadessDao().deleteEspecialidades(con, parametros);
	}
	
	/**
	 * @return the especialidades
	 */
	public ArrayList<DtoEspecialidades> getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList<DtoEspecialidades> especialidades) {
		this.especialidades = especialidades;
	}
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoEspecialidades> busquedaAvanzadaEspecialidades(Connection con, String institucion, String consecutivo, String descripcion, int centro_costo)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion", institucion);
		if(!consecutivo.equals(""))
			parametros.put("consecutivo", consecutivo);
		if(!descripcion.equals(""))
			parametros.put("descripcion", descripcion);
		if(centro_costo!=ConstantesBD.codigoNuncaValido)
			parametros.put("centro_costo", centro_costo);
		return getEspecialidadessDao().busquedaAvanzadaEspecialidades(con, parametros);
	}
	
	/**
	 * Consulta listado de Especialidades y Verifica que especialidad esta Siendo Usada 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoEspecialidades> verificarEspecialidadesUsadas(Connection con, ArrayList<DtoEspecialidades> list)
	{
		return getEspecialidadessDao().verificarEspecialidadesUsadas(con, list);
	}

	public static ArrayList<DtoEspecialidades> cargarEspecialidadesTipo(int institucion,String tipoEspecialidad) 
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoEspecialidades> retornar=new ArrayList<DtoEspecialidades>();
		HashMap parametros = new HashMap();
		parametros.put("institucion", institucion);
		parametros.put("tipoEspecialidad", tipoEspecialidad);
		retornar=getEspecialidadessDao().busquedaAvanzadaEspecialidades(con, parametros);
		UtilidadBD.closeConnection(con);
		return retornar;
	}
}
