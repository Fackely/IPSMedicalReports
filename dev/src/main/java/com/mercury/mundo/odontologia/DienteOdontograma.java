package com.mercury.mundo.odontologia;

import java.util.ArrayList;

public class DienteOdontograma
{
    private int numDiente;
    private int codEstadoInst;
    private int codEstado;
    private int numFotograma;
    private String nomEstado;
    private ArrayList sectores;
    
    public final static int AUSENTE=4;
    public final static int ANODONCIAVERDADERA=3;
    public final static int SINERUPCIONAR=8;
    
    public DienteOdontograma()
    {
        this.numDiente=-1;
        this.codEstadoInst=1;
        this.codEstado=1;
        this.numFotograma=1;
        this.nomEstado="";
        this.sectores=new ArrayList();
        for(int i=0; i<7; i++)
        {
            SectorDiente tempoSector = new SectorDiente();
            tempoSector.setNumero(i);
            this.sectores.add(tempoSector);
        }
    }
    
    public int getNumDiente()
    {
        return this.numDiente;
    }
    
    public int getCodEstadoInst()
    {
        return this.codEstadoInst;
    }
    
    public String getNomEstado()
    {
        return this.nomEstado;
    }
    
    public int getNumFotograma()
    {
        return this.numFotograma;
    }
    
    public void setNumDiente(int numDiente)
    {
        this.numDiente=numDiente;
    }
    
    public void setCodEstadoInst(int codEstadoInst)
    {
        this.codEstadoInst=codEstadoInst;
    }
    
    public void setNomEstado(String nomEstado)
    {
        this.nomEstado=nomEstado;
    }
    
    public void setNumFotograma(int numFotograma)
    {
        this.numFotograma=numFotograma;
    }
    
    public void setSector(SectorDiente sector)
    {
        this.sectores.set(sector.getNumero(), sector);
    }
    
    public SectorDiente getSector(int numero)
    {
        return (SectorDiente)this.sectores.get(numero);
    }
    
    public int getNumSectores()
    {
        return this.sectores.size();
    }

	/**
	 * @return Returns the codEstado.
	 */
	public int getCodEstado() 
	{
		return codEstado;
	}

	/**
	 * @param codEstado The codEstado to set.
	 */
	public void setCodEstado(int codEstado) 
	{
		this.codEstado = codEstado;
	}
}
