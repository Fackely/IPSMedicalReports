package com.servinte.axioma.dao.fabrica.odontologia.ventastarjeta;



import com.servinte.axioma.dao.impl.odontologia.tipoTarjeta.TipoTarjetaDAO;
import com.servinte.axioma.dao.impl.odontologia.ventaTarjeta.BeneficiarioTcPacienteDAO;
import com.servinte.axioma.dao.impl.odontologia.ventasTarjeta.BeneficiarioDAO;
import com.servinte.axioma.dao.impl.odontologia.ventasTarjeta.VentaTarjetaEmpresarialDAO;
import com.servinte.axioma.dao.impl.odontologia.ventasTarjeta.VentasTarjetaClienteDAO;
import com.servinte.axioma.dao.interfaz.odontologia.tipoTarjeta.ITipoTarjetaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IBeneficiarioDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IBeneficiarioTcPacienteDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IVentaTarjetaEmpresarialDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IVentasTarjetaClienteDAO;


/**
 * Fábrica de Ventas de Tarjeta Cliente
 * @author Edgar Carvajal Ruiz
 *
 */
public abstract class VentaTarjetaFabricaDAO {
	
	/**
	 * Constructor privado para no dejar instancia la clase
	 */
	private VentaTarjetaFabricaDAO(){
		
	}
	
	/**
	 * Interfaz Ventas DAO
	 * Aplica para ventas de tarjetas
	 * esta interfaz solo se instancia una vez, utiliza el patron Singleton.
	 */
	private static IVentasTarjetaClienteDAO interfazVentaDAO;
	
	/**
	 * Crear venta de Tarjeta DAO
	 * Método que crea un instancia de IVentasTarjetaDao
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IVentasTarjetaClienteDAO crearVentaTarjetaDAO(){
		
		if(interfazVentaDAO==null)
		{
			return new VentasTarjetaClienteDAO();
		}
		
		return interfazVentaDAO;
	}
	
	/**
	 * Interfaz Beneficiario DAO
	 */
	private static IBeneficiarioDAO interfazBeneficiarioDAO;
	
	/**
	 * Metodo para crear una instancia de Beneficiario DAO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IBeneficiarioDAO crearBeneficiarioDAO(){
		
		if(interfazBeneficiarioDAO==null)
		{
			return new BeneficiarioDAO();
		}
		return interfazBeneficiarioDAO;
	}
	
	/**
	 * Interfaz Tarjeta Empresarial DAO
	 * 
	 */
	private static IVentaTarjetaEmpresarialDAO intefazTarjetaEmpresarialDAO;
	
	/**
	 * Método que crea la instancia de la interfaz empresarial DAO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IVentaTarjetaEmpresarialDAO crearVentaEmpresarialDAO(){
		
		if(intefazTarjetaEmpresarialDAO==null)
		{
			return new VentaTarjetaEmpresarialDAO();
		}
		
		return intefazTarjetaEmpresarialDAO;
		
	}
	
	/**
	 * Método que Crear una instancia de TipoTarjetaDAO
	 * @return
	 */
	public static final ITipoTarjetaDAO crearTipoTarjetaDAO(){
		return new TipoTarjetaDAO();
	}

	/**
	 * Creación de la instancia concreta para manejar
	 * la relación de beneficiarios con pacientes
	 */
	public static final IBeneficiarioTcPacienteDAO crearBeneficiarioTcPacienteDAO() {
		return new BeneficiarioTcPacienteDAO();
	}

}
