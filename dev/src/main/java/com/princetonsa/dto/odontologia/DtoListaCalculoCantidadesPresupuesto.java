package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 
 * @author axioma
 *
 */
public class DtoListaCalculoCantidadesPresupuesto implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4199724413374171504L;
	/**
	 * 
	 */
	private ArrayList<DtoAgrupacionHallazgoSuperficie> listaCalculoCantidades;
	
	/**
	 * 
	 * @param hallazgo1
	 * @param programaServicio1
	 * @param numeroSuperficies1
	 */
	public void add(int hallazgo1, BigDecimal programaServicio1, int numeroSuperficies1)
	{
		int pos= this.contains(hallazgo1, programaServicio1);
		if(pos<0)
		{
			this.listaCalculoCantidades.add(new DtoAgrupacionHallazgoSuperficie(hallazgo1, programaServicio1, numeroSuperficies1, 1));
		}
		else
		{
			this.listaCalculoCantidades.get(pos).incremetarContador();
		}
	}

	/**
	 * 
	 * @param hallazgo1
	 * @param programaServicio1
	 * @return
	 */
	public int contains(int hallazgo1, BigDecimal programaServicio1)
	{
		int pos=0, posRetorna=-1;
		for(DtoAgrupacionHallazgoSuperficie dto: this.getListaCalculoCantidades())
		{
			if(dto.getHallazgo()==hallazgo1 && programaServicio1.doubleValue()== dto.getProgramaServicio().doubleValue())
			{
				posRetorna= pos;
				break;
			}
			pos++;
		}
		return posRetorna;
	}
	
	
	public DtoListaCalculoCantidadesPresupuesto() 
	{
		super();
		this.listaCalculoCantidades = new ArrayList<DtoAgrupacionHallazgoSuperficie>();
	}

	/**
	 * @return the listaCalculoCantidades
	 */
	public ArrayList<DtoAgrupacionHallazgoSuperficie> getListaCalculoCantidades() {
		return listaCalculoCantidades;
	}

	/**
	 * @param listaCalculoCantidades the listaCalculoCantidades to set
	 */
	public void setListaCalculoCantidades(
			ArrayList<DtoAgrupacionHallazgoSuperficie> listaCalculoCantidades) {
		this.listaCalculoCantidades = listaCalculoCantidades;
	}
	
	
	
}
