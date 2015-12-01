package com.servinte.axioma.orm.delegate.manejoPaciente;

import com.servinte.axioma.orm.AutoEntsubPeticiones;
import com.servinte.axioma.orm.AutoEntsubPeticionesHome;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.PeticionQx;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene lógica del negocio sobre el modelo 
 */
public class AutoEntsubPeticionesDelegate extends AutoEntsubPeticionesHome
{
	public static void main(String[] args) 
	{
		AutorizacionesEntidadesSub autorizacionesEntidadesSub; autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
		autorizacionesEntidadesSub.setConsecutivo(1);
		
		PeticionQx peticionQx; peticionQx = new PeticionQx();
		peticionQx.setCodigo(1);
		
		AutoEntsubPeticiones autoEntsubPeticiones; autoEntsubPeticiones = new AutoEntsubPeticiones();
		autoEntsubPeticiones.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
		autoEntsubPeticiones.setPeticionQx(peticionQx);
		
		AutoEntsubPeticionesDelegate d = new AutoEntsubPeticionesDelegate();
		d.persist(autoEntsubPeticiones);
	}
}
