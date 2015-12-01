package util.FacturasVarias;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.UtilidadesFacturasVariasDao;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

/**
 * @author Víctor Hugo Gómez L.
 */
public class UtilidadesFacturasVarias {
	
	/**
	 * Constructor
	 */
	public UtilidadesFacturasVarias(){}
	
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesFacturasVariasDao getUtilidadesFacturasVariasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesFacturasVariasDao();
	}
	
	/***
	 * ObtenerConceptosFraVarias
	 * @param con
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(Connection con, int institucion, boolean activo)
	{
		return getUtilidadesFacturasVariasDao().obtenerConceptosFraVarias(con, institucion, activo);
	}
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param activo
	 * @param tipoConcepto
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(Connection con, int institucion, boolean activo, String tipoConcepto )
	{
		return getUtilidadesFacturasVariasDao().obtenerConceptosFraVarias(con, institucion, activo, tipoConcepto);
	}
	
	
	/**
	 * 
	 * @param consecutivoFacturaVaria
	 * @param institucion
	 * @return
	 */
	public static BigDecimal obtenerPkFacturaVaria( BigDecimal consecutivoFacturaVaria , int institucion)
	{
		return getUtilidadesFacturasVariasDao().obtenerPkFacturaVaria(consecutivoFacturaVaria, institucion);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @param codigoPkFacturasVarias
	 * @return
	 */
	public static String aplicaReciboCaja(Connection con, DtoRecibosCaja dto)
	{
		return getUtilidadesFacturasVariasDao().aplicaReciboCaja(con, dto);
	}
	
		
}
