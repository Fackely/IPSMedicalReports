package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.inventarios.ConsultaSaldosCierresInventariosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsultaSaldosCierresInventariosDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jaramillo Henao
 * Fecha: Agosto de 2008
 */

public class ConsultaSaldosCierresInventarios
{

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ConsultaSaldosCierresInventariosDao objetoDao;
	
	/**
	 * Metodo Reset
	 */
	public ConsultaSaldosCierresInventarios()
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
			objetoDao=myFactory.getConsultaSaldosCierresInventariosDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}

	/**
	 * @param con
	 * @param forma
	 * @param usuario 
	 * @return
	 */
	public HashMap consultarSaldosCierresInventarios(Connection con, ConsultaSaldosCierresInventariosForm forma, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("codAlmacen", forma.getAlmacen());
		criterios.put("anio", forma.getAnioCierre());
		criterios.put("mes", forma.getMesCierre());
		criterios.put("clase", forma.getClase());
		criterios.put("grupo", forma.getGrupo());
		criterios.put("subGrupo", forma.getSubGrupo());
		criterios.put("articulo", forma.getCodArticulo());
		criterios.put("institucion", usuario.getCodigoInstitucionInt());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaSaldosCierresInventariosDao().consultarSaldosCierresInventarios(con, criterios);
	}

	/**
	 * Método para armar las condiciones de la
	 * consulta para ser mandada al BIRT y poder
	 * ejecutar el reporte 
	 * @param con
	 * @param forma
	 * @return
	 */
	public String consultarCondicionesSaldosCierresInventarios(Connection con, ConsultaSaldosCierresInventariosForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("codAlmacen", forma.getAlmacen());
		criterios.put("anio", forma.getAnioCierre());
		criterios.put("mes", forma.getMesCierre());
		criterios.put("clase", forma.getClase());
		criterios.put("grupo", forma.getGrupo());
		criterios.put("subGrupo", forma.getSubGrupo());
		criterios.put("articulo", forma.getCodArticulo());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaSaldosCierresInventariosDao().consultarCondicionesSaldosCierresInventarios(con, criterios);
	}
	
}