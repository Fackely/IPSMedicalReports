package com.servinte.axioma.dao.fabrica.odontologia.contrato;

import com.servinte.axioma.dao.impl.odontologia.contrato.ContratoOdontologicoDAO;
import com.servinte.axioma.dao.impl.odontologia.contrato.FirmaContratoInstitucionDAO;
import com.servinte.axioma.dao.impl.odontologia.contrato.FirmaContratoMultiEmpresaDAO;
import com.servinte.axioma.dao.impl.odontologia.presupuesto.OtrosSiHibernateDAO;
import com.servinte.axioma.dao.interfaz.odontologia.contrato.IContratoOdontologicoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.contrato.IFirmaContratoInstitucionDAO;
import com.servinte.axioma.dao.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IOtrosSiDAO;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public abstract class ContratoOdontologicoFabricaDAO {

	
	
	/**
	 * CREAR CONTRATO 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IContratoOdontologicoDAO crearContratoDAO()
	{
		return new ContratoOdontologicoDAO();
	}
	
	
	
	
	/**
	 * CREAR FIRMA MULTI EMPRESA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IFirmaContratoMultiEmpresaDAO crearFirmasMultiEmpresa()
	{
		return new FirmaContratoMultiEmpresaDAO();
	}
	
	
	
	
	
	/**
	 * CREAR FIRMA INSTITUCION
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IFirmaContratoInstitucionDAO crearFirmasInstitucion()
	{
		return new FirmaContratoInstitucionDAO();
	}
	
	
	
	/**
	 * Retorna una instancia de la implementación de la Interface
	 * @return IOtrosSiDAO
	 */
	public static final IOtrosSiDAO crearOtrosSiDao(){
		return new OtrosSiHibernateDAO();
	}
	
}
