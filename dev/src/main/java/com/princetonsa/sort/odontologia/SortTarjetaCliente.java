package com.princetonsa.sort.odontologia;

import java.util.Comparator;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;

/**
 * 
 * @author axioma
 *
 */
public class SortTarjetaCliente implements Comparator<DtoTarjetaCliente>  
{
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	public SortTarjetaCliente (String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}
	
	/**
	 * 
	 */
	public int compare(DtoTarjetaCliente one, DtoTarjetaCliente two) 
	{
		if(this.getPatronOrdenar().equals("codigo"))
		{	
			return  (one.getCodigoTipoTarj().toLowerCase()).compareTo(two.getCodigoTipoTarj().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("nombre"))
		{
			return  (one.getNombre().toLowerCase()).compareTo(two.getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("aliado"))
		{
			return  (one.getAliado().toLowerCase()).compareTo(two.getAliado().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("convenio"))
		{
			return  (one.getConvenio().getNombre().toLowerCase()).compareTo(two.getConvenio().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("servicioPersona"))
		{
			return  (one.getServicioPersonal().getNombre().toLowerCase()).compareTo(two.getServicioPersonal().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("servicioEmpresarial"))
		{
			return  (one.getServicioEmpresarial().getNombre().toLowerCase()).compareTo(two.getServicioEmpresarial().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("servicioFamiliar"))
		{
			return  (one.getServicioFamiliar().getNombre().toLowerCase()).compareTo(two.getServicioFamiliar().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("numeroBeneficiarios"))
		{
			return  new Integer(one.getNumBeneficiariosFam()).compareTo( new Integer(two.getNumBeneficiariosFam()));
		}
		else if(this.getPatronOrdenar().equals("codigo_descendente"))
		{
			return  (two.getCodigoTipoTarj().toLowerCase()).compareTo(one.getCodigoTipoTarj().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("nombre_descendente"))
		{
			return  (two.getNombre().toLowerCase()).compareTo(one.getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("aliado_descendente"))
		{
			return  (two.getAliado().toLowerCase()).compareTo(one.getAliado().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("convenio_descendente"))
		{
			return  (two.getConvenio().getNombre().toLowerCase()).compareTo(one.getConvenio().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("servicioPersona_descendente"))
		{
			return  (two.getServicioPersonal().getNombre().toLowerCase()).compareTo(one.getServicioPersonal().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("servicioEmpresarial_descendente"))
		{
			return  (two.getServicioEmpresarial().getNombre().toLowerCase()).compareTo(one.getServicioEmpresarial().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("servicioFamiliar_descendente"))
		{
			return  (two.getServicioFamiliar().getNombre().toLowerCase()).compareTo(one.getServicioFamiliar().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("numeroBeneficiarios_descendente"))
		{
			return  new Integer(two.getNumBeneficiariosFam()).compareTo( new Integer(one.getNumBeneficiariosFam()));
		}
		else
		{
			return  (one.getCodigoTipoTarj().toLowerCase()).compareTo(two.getCodigoTipoTarj().toLowerCase());
		}
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
}
