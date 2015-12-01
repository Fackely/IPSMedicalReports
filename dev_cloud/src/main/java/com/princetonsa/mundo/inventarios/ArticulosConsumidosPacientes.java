package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.actionform.inventarios.ArticulosConsumidosPacientesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ArticulosConsumidosPacientesDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ArticulosConsumidosPacientes;

public class ArticulosConsumidosPacientes 
{
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ArticulosConsumidosPacientesDao objetoDao;
	
	/**
	 *
	 */
	public ArticulosConsumidosPacientes()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getArticulosConsumidosPacientesDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}
	
    /**
     * @param con
     * @param forma
     * @param usuarioActual 
     * @return
     */
    public static  HashMap<String, Object> consultarCondicionesArticulosConsumidos(Connection con, ArticulosConsumidosPacientesForm forma, UsuarioBasico usuarioActual)
    {
    	HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("centroAtencion", forma.getCentroAtencion());
		criterios.put("almacen", forma.getAlmacen());
		criterios.put("articulo", forma.getArticulo());
		criterios.put("desArticulo", forma.getDescripcionArticulo());
		criterios.put("institucion", usuarioActual.getCodigoInstitucion());
		criterios.put("clase", forma.getClase());
		criterios.put("desClase", forma.getDescripcionClase());
		criterios.put("grupo", forma.getGrupo());
		criterios.put("desGrupo", forma.getDescripcionGrupo());
		criterios.put("subGrupo", forma.getSubGrupo());
		criterios.put("desSubGrupo", forma.getDescripcionSubGrupo());
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("tipoCodigoArticulo", forma.getTipoCodigoArticulo());
		criterios.put("tipoInforme", forma.getTipoInforme());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosConsumidosPacientesDao().consultarCondicionesArticulosConsumidos(con, criterios);
	}
    
    /**
     * 
     * @param con
     * @param criterios
     * @return
     */
    public static  HashMap<String, Object> consultarCondicionesArticulosConsumidos(Connection con, HashMap criterios)
    {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosConsumidosPacientesDao().consultarCondicionesArticulosConsumidos(con, criterios);
	}
	
    /**
     * Método que devuelve un mapa con todo los resultados de la consulta
     * para ser ingresados en el archivo plano
     * @param con
     * @param forma
     * @return
     */
    public HashMap consultarArticulosConsumidos(Connection con, ArticulosConsumidosPacientesForm forma, UsuarioBasico usuarioActual)
	{
    	HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("centroAtencion", forma.getCentroAtencion());
		criterios.put("almacen", forma.getAlmacen());
		criterios.put("articulo", forma.getArticulo());
		criterios.put("institucion", usuarioActual.getCodigoInstitucion());
		criterios.put("clase", forma.getClase());
		criterios.put("grupo", forma.getGrupo());
		criterios.put("subGrupo", forma.getSubGrupo());
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("tipoCodigoArticulo", forma.getTipoCodigoArticulo());
		criterios.put("tipoInforme", forma.getTipoInforme());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosConsumidosPacientesDao().consultarArticulosConsumidos(con, criterios);
	}
    
    /**
	 * Método que organiza los datos de la consulta de Articulos Consumidos
	 * para el tipo de reporte Consumo Paciente por Artículo e Ingreso
	 * exportados a un archivo plano con extensión .CSV
     * @param articulosConsumidos
     * @param nombreReporte
     * @param encabezado
     * @param loginUsuario
     * @return
     */
    public StringBuffer cargarMapaConsumoPacienteArticulo(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario)
    {
    	StringBuffer datos = new StringBuffer();
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			datos.append(mapa.get("fecha_"+i)+", "+mapa.get("codigo_"+i)+", "+mapa.get("des_"+i)+", "+mapa.get("unidad_"+i)+", "+mapa.get("ingreso_"+i)+", "+mapa.get("tipo_id_"+i)+", "+mapa.get("paciente_"+i)+", "+mapa.get("id_ent_"+i)+", "+mapa.get("entidad_"+i)+", "+mapa.get("cantidad_"+i)+"  \n");
		}
		
		return datos;
	}
}