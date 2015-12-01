package util.Cargos;

import java.io.Serializable;

import com.princetonsa.dto.cargos.DtoTercero;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
/**
 * 
 * @author axioma
 *
 */
public class InfoDeudorTerceroDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5106291509338176905L;
	
	
	/**
	 * 
	 */
	private DtoTercero  dtoTercero;
	private DtoDeudor dtoDeudor;

	
	
	
	
	public InfoDeudorTerceroDto()
	{
		this.setDtoTercero(new DtoTercero());
		this.setDtoDeudor(new DtoDeudor());
	}
	
	
	

	public void setDtoTercero(DtoTercero dtoTercero) {
		this.dtoTercero = dtoTercero;
	}

	public DtoTercero getDtoTercero() {
		return dtoTercero;
	}

	public void setDtoDeudor(DtoDeudor dtoDeudor) {
		this.dtoDeudor = dtoDeudor;
	}

	public DtoDeudor getDtoDeudor() {
		return dtoDeudor;
	}
	

}
