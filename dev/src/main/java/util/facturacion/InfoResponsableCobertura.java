package util.facturacion;

import java.io.Serializable;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;

/**
 * Objeto que encapsula la informacion del responsable de la cobertura
 * @author wilson
 *
 */
public class InfoResponsableCobertura implements Serializable
{
	/**
	 * 
	 */
	private DtoSubCuentas dtoSubCuenta;
	
	/**
	 * 
	 */
	private InfoCobertura infoCobertura;

	/**
	 * 
	 * @param dtoSubCuenta
	 * @param infoCobertura
	 */
	public InfoResponsableCobertura(DtoSubCuentas dtoSubCuenta, InfoCobertura infoCobertura) {
		super();
		this.dtoSubCuenta = dtoSubCuenta;
		this.infoCobertura = infoCobertura;
	}

	/**
	 * 
	 *
	 */
	public InfoResponsableCobertura() 
	{
		this.dtoSubCuenta= new DtoSubCuentas();
		this.infoCobertura=new InfoCobertura();
	}

	/**
	 * @return the dtoSubCuenta
	 */
	public DtoSubCuentas getDtoSubCuenta() {
		return dtoSubCuenta;
	}

	/**
	 * @param dtoSubCuenta the dtoSubCuenta to set
	 */
	public void setDtoSubCuenta(DtoSubCuentas dtoSubCuenta) {
		this.dtoSubCuenta = dtoSubCuenta;
	}

	/**
	 * @return the infoCobertura
	 */
	public InfoCobertura getInfoCobertura() {
		return infoCobertura;
	}

	/**
	 * @param infoCobertura the infoCobertura to set
	 */
	public void setInfoCobertura(InfoCobertura infoCobertura) {
		this.infoCobertura = infoCobertura;
	}
	
}