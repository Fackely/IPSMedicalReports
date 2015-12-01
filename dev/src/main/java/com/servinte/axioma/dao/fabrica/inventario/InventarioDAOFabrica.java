package com.servinte.axioma.dao.fabrica.inventario;

import com.servinte.axioma.dao.impl.inventario.ArticuloHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.ClaseInventarioHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.DespachoHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.DespachoPedidoHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.DetalleDespachoPedidoHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.DetalleDespachosHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.DetallePedidosHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.EsqTarInventariosContratoHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.EsqTarProcedimientoContratoHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.GrupoInventarioHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.NaturalezaArticuloHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.PedidoHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.SubgrupoInventarioHibernateDAO;
import com.servinte.axioma.dao.impl.inventario.UnidosisXArticuloHibernateDAO;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDespachoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDespachoPedidoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachoPedidoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachosDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetallePedidosDAO;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarInventariosContratoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarProcedimientoContratoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IGrupoInventarioDAO;
import com.servinte.axioma.dao.interfaz.inventario.INaturalezaArticuloDAO;
import com.servinte.axioma.dao.interfaz.inventario.IPedidoDAO;
import com.servinte.axioma.dao.interfaz.inventario.ISubgrupoInventarioDAO;
import com.servinte.axioma.dao.interfaz.inventario.IUnidosisXArticuloDAO;


/**
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo de facturación
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class InventarioDAOFabrica {
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	private InventarioDAOFabrica(){
		
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IMontosCobroDAO
	 * 
	 * @return IMontosCobroDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IClaseInventarioDAO crearClaseInventarioDAO(){
		return new ClaseInventarioHibernateDAO();
	}
	

	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IGrupoInventarioDAO
	 * 
	 * @return IGrupoInventarioDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IGrupoInventarioDAO crearGrupoInventarioDAO(){
		return new GrupoInventarioHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ISubgrupoInventarioDAO
	 * 
	 * @return ISubgrupoInventarioDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ISubgrupoInventarioDAO crearSubgrupoInventarioDAO(){
		return new SubgrupoInventarioHibernateDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INaturalezaArticuloDAO
	 * 
	 * @return INaturalezaArticuloDAO
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INaturalezaArticuloDAO crearNaturalezaArticuloDAO(){
		return new NaturalezaArticuloHibernateDAO();
	}
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public static IArticuloDAO crearArticuloDAO(){
		return new ArticuloHibernateDAO();
	}
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public static IDespachoDAO crearDespachoDAO(){
		return new DespachoHibernateDAO();
	}
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public static IDetalleDespachosDAO crearDetalleDespachosDAO(){
		return new DetalleDespachosHibernateDAO();
	}
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public static IPedidoDAO crearPedidoDAO(){
		return new PedidoHibernateDAO();
	}
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public static IDetallePedidosDAO crearDetallePedidosDAO(){
		return new DetallePedidosHibernateDAO();
	}
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public static IDespachoPedidoDAO crearDespachoPedidoDAO(){
		return new DespachoPedidoHibernateDAO();
	}
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public static IDetalleDespachoPedidoDAO crearDetalleDespachoPedidoDAO(){
		return new DetalleDespachoPedidoHibernateDAO();
	}
	
	/**
	 * Esta método se encarga de devolver una instancia de la interfaz IDetalleDespachoPedidoDAO
	 * @return IDetalleDespachoPedidoDAO	 
	 * @author angela Aguirre
	 */
	public static IUnidosisXArticuloDAO crearUnidosisXArticulo(){
		return new UnidosisXArticuloHibernateDAO();
	}
	
	/**
	 * Esta método se encarga de devolver una instancia de 
	 * la interfaz IEsqTarInventariosContratoDAO
	 * @return IEsqTarInventariosContratoDAO	 
	 * @author angela Aguirre
	 */	
	public static IEsqTarInventariosContratoDAO crearEsqTarInventariosContrato(){
		return new EsqTarInventariosContratoHibernateDAO();
	}
	
	
	/**
	 * Esta método se encarga de devolver una instancia de 
	 * la interfaz IEsqTarProcedimientoContratoDAO
	 * @return IEsqTarProcedimientoContratoDAO	 
	 * @author angela Aguirre
	 */	
	public static IEsqTarProcedimientoContratoDAO crearEsqTarProcedimientoContrato(){
		return new EsqTarProcedimientoContratoHibernateDAO();
	}
	
	
	
	
}
